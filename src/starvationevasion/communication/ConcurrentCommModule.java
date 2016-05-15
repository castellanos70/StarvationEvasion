package starvationevasion.communication;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.Util;
import starvationevasion.common.gamecards.GameCard;
import starvationevasion.server.model.*;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A ConcurrentCommModule allows for a client to send and receive information from a server.
 *
 * This class can be used with multiple threads, and aside from some blocking, there should
 * be no issues. This version of the module is capable of receiving messages from the server
 * and polling previously-sent messages at the same time to increase performance by a small
 * amount over the previous version.
 *
 * @author Justin Hall, George Boujaoude, Javier Chavez (javierc@cs.unm.edu)
 */
public class ConcurrentCommModule implements Communication
{
  // Note :: When the different comm module instances start, they will check to see
  //         if they are being asked to connect to the localhost. If this is the case, they
  //         will attempt to secure this port by opening a ServerSocket - whichever instance
  //         binds to this port first gains exclusive rights to spawning the server.
  //
  //         This avoids the situation where every ConcurrentCommModule instance tries to
  //         spawn its own server process, which would be a mess.
  private static final int PROCESS_LOCK_PORT = 5050;

  // Final variables
  private final ReentrantLock LOCK = new ReentrantLock(); // Used for synchronization
  private final AtomicBoolean IS_CONNECTED = new AtomicBoolean(false);
  private final ConcurrentLinkedQueue<Response> RESPONSE_EVENTS = new ConcurrentLinkedQueue<>();
  private final ArrayList<StreamRedirect> STREAM_REDIRECT_THREADS = new ArrayList<>(2);
  private final String HOST;
  private final int PORT;

  // Non-final variables
  private Process localServer;
  private Socket clientSocket;
  private DataInputStream reader;
  private DataOutputStream writer;
  private double startNanoSec = 0;
  private double elapsedTime = 0;

  private SecretKey serverKey; // Obtained at the end of the handshake
  private Cipher aesCipher;
  private KeyPair rsaKey;

  // These are only used when spawning a local server
  private boolean connectInfinitely = false; // Used in a special localhost case
  private ServerSocket processSocket = null;

  /**
   * This class enables the comm module to listen for responses from the server. When
   * a new response is received, it is pushed to the end of the response queue to be
   * dealt with at a later time when pollMessages() is called.
   *
   * @author Justin Hall, George Boujaoude
   */
  private class StreamListener extends Thread
  {
    private boolean encounteredError = false;
    @Override
    public void run()
    {
      while (IS_CONNECTED.get() && !encounteredError) read();
    }

    private void read()
    {
      try
      {
        Response response = readObject();
        // Result of readObject should not be null - assume fatal error if it is (Ex: socket was closed)
        if (response == null)
        {
          encounteredError = true;
          dispose();
          return;
        }
        pushResponse(response);

        if (response.getType().equals(Type.AUTH_SUCCESS)) commPrint("Login successful");
        else if (response.getType().equals(Type.AUTH_ERROR)) commError("Failed to login");
        else if (response.getType().equals(Type.CREATE_SUCCESS)) commPrint("Created user successfully");
        else if (response.getType().equals(Type.CREATE_ERROR)) commError("Failed to create user");
        else if (response.getType().equals(Type.TIME)) setStartTime((Double)response.getPayload().getData());
      }
      catch (Exception e)
      {
        commError("Error reading response from server");
        encounteredError = true;
        dispose();
      }
    }

    private Response readObject () throws IOException, ClassNotFoundException, InvalidKeyException, NoSuchAlgorithmException
    {
      Response response = null;
      try
      {
        int ch1 = reader.read();
        int ch2 = reader.read();
        int ch3 = reader.read();
        int ch4 = reader.read();

        if ((ch1 | ch2 | ch3 | ch4) < 0) throw new EOFException();
        int size = ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));

        byte[] encObject = new byte[size];

        reader.readFully(encObject);
        ByteArrayInputStream in = new ByteArrayInputStream(encObject);
        ObjectInputStream is = new ObjectInputStream(in);
        SealedObject sealedObject = (SealedObject) is.readObject();
        response = (Response) sealedObject.getObject(serverKey);
        is.close();
        in.close();
      }
      catch (Exception e)
      {
        // Ignore
      }
      return response;
    }
  }

  /**
   * STDOUT just means that System.out will be used, while STDERR means that
   * System.err will be used.
   *
   * @author Justin Hall, George Boujaoude
   */
  private enum StreamType
  {
    STDOUT,
    STDERR
  }

  /**
   * StreamRedirect allows this class to intercept the output of a separate spawned process
   * (in this case, a local server process). These can either be ignored or pushed to a separate
   * stream. Right now this class only uses System.out and System.err, but it could be configured
   * to do something like write all output to a file or something.
   *
   * @author Justin Hall, George Boujaoude
   */
  private class StreamRedirect extends Thread
  {
    private final StreamType TYPE;
    private final BufferedReader READ;
    private final AtomicBoolean RUN = new AtomicBoolean(true);

    public StreamRedirect(StreamType type, InputStream stream)
    {
      TYPE = type;
      READ = new BufferedReader(new InputStreamReader(stream));
    }

    @Override
    public void run()
    {
      try
      {
        String line;
        while (RUN.get())
        {
          if (!READ.ready()) continue;
          else if ((line = READ.readLine()) == null) break;
          if (TYPE == StreamType.STDOUT) commPrint(line);
          else if (TYPE == StreamType.STDERR) commError(line);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }

    public void close()
    {
      RUN.set(false);
    }
  }

  /**
   * Constructs a new ConcurrentCommModule. If the given host starts with "local", it will
   * assume that single player mode is enabled. When connect() is called it will attempt
   * to connect exactly once (if local) and if it fails, it will move into the stage of
   * spawning its own local server to connect to.
   *
   * In the event that a local server is spawned, the ConcurrentCommModule sets itself up
   * as a server by opening a ServerSocket bound to port PROCESS_LOCK_PORT. Whichever instance
   * succeeds in securing this port first gains exclusive rights to spawning the process, while
   * other clients will yield and wait for the server to start up. This prevents the issue
   * of multiple clients spawning a local server on the same machine, which would quickly
   * eat up gigabytes of memory.
   *
   * @param host host to connect to
   * @param port port the host is listening to
   */
  public ConcurrentCommModule(String host, int port)
  {
    HOST = host;
    PORT = port;
    // This will hopefully prevent most cases where a local server was spawned but not shutdown
    // because the client's program had to close itself unexpectedly
    Runtime.getRuntime().addShutdownHook(new Thread(() -> dispose()));
  }

  /**
   * This gets the starting nano time that was received from the server. Note that server responses
   * of type TIME include this data, but this means that no listener needs to be bound to this type
   * as the Communication module will manage this itself.
   *
   * @return starting nano time from the server
   */
  @Override
  public double getStartNanoTime()
  {
    return startNanoSec;
  }

  /**
   * The CommModule will stall whatever thread this is called on while it attempts to connect to
   * the server. A single Communication module should be bound to a host/port combination that does not
   * change after instantiation.
   * <p>
   * When called, this should first check to see if it is already connected. If not, the full
   * connection routine should be performed.
   * <p>
   * The primary reason for this function's existence is to
   * 1) allow for an initial connect attempt, and
   * 2) to allow future connect attempts should a disconnect event happen unexpectedly
   */
  @Override
  public void connect()
  {
    try
    {
      LOCK.lock();
      if (isConnected()) return; // connect() was already called
      // Set up the key
      rsaKey = generateRSAKey();

      // If the host starts with "local", try to connect and if it fails, assume
      // that the game is starting in single player mode and spawn the local server
      if (HOST.toLowerCase().startsWith("local"))
      {
        IS_CONNECTED.set(openConnection(HOST, PORT));
        if (!IS_CONNECTED.get()) localServer = spawnLocalServer(PORT);
      }

      final long millisecondTimeStamp = System.currentTimeMillis();
      final double MAX_SECONDS = connectInfinitely ? Double.MAX_VALUE : 10.0;
      double deltaSeconds = 0.0;

      // Try to establish a connection and timeout after MAX_SECONDS if not
      commPrint("Attempting to connect to host " + HOST + ", port " + PORT);
      while (deltaSeconds < MAX_SECONDS)
      {
        IS_CONNECTED.set(openConnection(HOST, PORT));
        if (IS_CONNECTED.get())
        {
          // This solves an issue I don't understand and probably never will
          openConnection(HOST, PORT);
          break;
        } else if (localServer != null && !localServer.isAlive())
        {
          commError("Failed to start the local server");
          dispose();
          break;
        } else if (HOST.toLowerCase().startsWith("local") && secureProcessPort() != null)
        {
          commError("Another client tried and failed to start a local server");
          dispose();
          break;
        }
        deltaSeconds = (System.currentTimeMillis() - millisecondTimeStamp) / 1000.0;
      }

      //closeProcessSocket();

      // See if the connect attempt went badly
      if (!IS_CONNECTED.get())
      {
        commError("Failed to establish connection at host " + HOST + ", port " + PORT + " (connection timed out)");
        return;
      }

      commPrint("Connection successful");

      // Generate and initialize the aes cipher
      try
      {
        aesCipher = generateAESCipher();
        aesCipher.init(Cipher.ENCRYPT_MODE, serverKey);
      } catch (Exception e)
      {
        e.printStackTrace();
      }

      // Start the listener
      new StreamListener().start();
    }
    finally
    {
      LOCK.unlock();
    }
  }

  /**
   * Disposes of this module. All existing threads should be shut down and data cleared out. No
   * existing connection should remain.
   * <p>
   * Calling connect() undoes this.
   */
  @Override
  public void dispose()
  {
    try
    {
      LOCK.lock();
      //if (!isConnected()) return; // Already disposed/the connection never succeeded
      IS_CONNECTED.set(false);
      commError("Shutting down communication module");
      clearResponses();
      closeProcessSocket();
      if (localServer != null) localServer.destroy();
      localServer = null;
      for (StreamRedirect redirect : STREAM_REDIRECT_THREADS) redirect.close();

      if (writer != null) writer.close();
      if (reader != null) reader.close();
      if (clientSocket != null) clientSocket.close();
      writer = null;
      reader = null;
      clientSocket = null;
    }
    catch (IOException e)
    {
      commError("Could not dispose properly");
      e.printStackTrace();
      System.exit(-1);
    }
    finally
    {
      LOCK.unlock();
    }
  }

  /**
   * Returns the current connection state of the module. If at any point the module loses connection
   * with the server, this will return false.
   *
   * @return true if connected to the server and false if not
   */
  @Override
  public boolean isConnected()
  {
    return IS_CONNECTED.get();
  }

  /**
   * Attempts to send a new login request to the server with the given information.
   *
   * @param username username as a string
   * @param password password as a string
   * @param region   region associated with the logging-in user
   * @return true if the request succeeded in sending and false if not (NOTE :: This *does not* reflect
   * the status of the login - only whether the request was sent or not)
   */
  @Override
  public boolean login(String username, String password, EnumRegion region)
  {
    updateCurrentTime();
    Request request = new RequestFactory().login(elapsedTime, username, password, region);
    // Check to see if anything went wrong
    if (request == null) return false;
    send(request);
    return true;
  }

  /**
   * Note :: Check the Endpoint that you are using before you set data to null - some are safe
   * to use without extra data associate with them while some are not (If they say "No Payload"
   * with their comment block, it should be fine to not include extra data). See
   * starvationevasion.server.model.Endpoint for more information.
   * <p>
   * Attempts to send a new request to the server tagged with the given endpoint. The "data" field
   * is potentially optional, while the "message" field is entirely optional. Put null for both of
   * these if you do not wish to use them.
   *
   * @param endpoint endpoint to tag the request with
   * @param data     Sendable object to attach to the request - the most common type for this is Payload
   *                 (see starvationevasion.server.model.Payload for more information)
   * @param message  optional message to attach - this *must* be null if data is also null
   * @return true if the request was sent successfully and false if anything went wrong
   */
  @Override
  public boolean send(Endpoint endpoint, Sendable data, String message)
  {
    updateCurrentTime();
    Request request;
    if (endpoint == null) return false;

    // Figure out how to build the request and then build it using RequestFactory
    if (data == null) request = new RequestFactory().build(elapsedTime, endpoint);
    else
    {
      if (message == null) request = new RequestFactory().build(elapsedTime, data, endpoint);
      else request = new RequestFactory().build(elapsedTime, data, message, endpoint);
    }

    // Check to see if anything went wrong
    if (request == null) return false;

    // Send the built request
    send(request);

    System.out.println("ConcurrentCommModule.send(): ["+request+"]");
    return true;
  }

  /**
   * This function attempts to send a new chat request to the server. Note that for destination,
   * the only two types that can be placed here (at the moment) are String and EnumRegion.
   * <p>
   * The "data" field is for attaching extra data to the chat request. At the moment the only
   * supported type is PolicyCard.
   *
   * @param destination where to send the chat
   * @param text        chat text
   * @param data        extra data (optional - can be null)
   * @return true if the request succeeded and false if anything went wrong
   */
  @Override
  public <T, E> boolean sendChat(T destination, String text, E data)
  {
    updateCurrentTime();
    // TODO find a way to let any type of data be passed into chat
    Request request = new RequestFactory().chat(elapsedTime, destination, text, (GameCard)data);

    // Check to see if anything went wrong
    if (request == null) return false;

    // Send the request
    send(request);
    return true;
  }

  /**
   * All messages sent from the server since the last time this function was called will be packaged
   * up into an array list and returned, and all existing messages within the Communication module's
   * internal storage queue will be cleared out.
   * <p>
   * The returned ArrayList will contain an ordered list of responses, where the first (index 0) reflects
   * the *oldest* in the list and the last (index size-1) represents the most recent response from
   * the server.
   *
   * @return ordered list of server messages since the last time this was called
   */
  @Override
  public ArrayList<Response> pollMessages()
  {
    try
    {
      LOCK.lock();
      int size = RESPONSE_EVENTS.size(); // Take a snapshot of the size so we know how many events to process
      ArrayList<Response> list = new ArrayList<>(size);
      for (int i = 0; i < size; i++) list.add(RESPONSE_EVENTS.poll());
      return list;
    }
    finally
    {
      LOCK.unlock();
    }
  }

  /**
   * Tries to spawn a local server.
   *
   * @param port port to tell the server to bind to
   * @return a Process instance or null if it failed
   */
  private Process spawnLocalServer(int port)
  {
    commPrint("Single player detected - attempting to spawn a new local server (This will test your patience)");
    connectInfinitely = true;
    processSocket = secureProcessPort();
    if (processSocket == null)
    {
      commPrint("Another client has already started the spawning process - continuing");
      return null;
    }
    Process process = null;
    try
    {
      ProcessBuilder builder = new ProcessBuilder();
      builder.directory(new File(System.getProperty("user.dir")));
      builder.command("java",
                      "-Xms4g",               // Try to allocate up to 4gb of heap space
                      "-jar",
                      "Server.jar",
                      Integer.toString(port));//,
                      //"true"); // start up ai automatically.
      process = builder.start();
      STREAM_REDIRECT_THREADS.clear();
      STREAM_REDIRECT_THREADS.add(new StreamRedirect(StreamType.STDOUT, process.getInputStream()));
      STREAM_REDIRECT_THREADS.add(new StreamRedirect(StreamType.STDERR, process.getErrorStream()));
      for (StreamRedirect redirect : STREAM_REDIRECT_THREADS) redirect.start();
    }
    catch (Exception e)
    {
      commError("Failed to start the server");
      closeProcessSocket();
      e.printStackTrace();
      System.exit(-1);
    }
    return process;
  }

  /**
   * Pushes a response to the end of the response queue.
   *
   * @param response response to push
   * @return true if it was added (this should almost always be true with a ConcurrentLinkedQueue)
   */
  private boolean pushResponse(Response response)
  {
    return RESPONSE_EVENTS.add(response);
  }

  private void clearResponses()
  {
    RESPONSE_EVENTS.clear();
  }

  /**
   * Non-blocking time update. The update is based on the nano time for the USER'S system, rather
   * than the server's system, subtracted by the startNanoSec that was sent by the server.
   *
   * If the lock can not be secured immediately, this returns without completing the update.
   */
  private void updateCurrentTime()
  {
    if (!LOCK.tryLock()) return; // Lock is busy - updating the time isn't super important, so don't block
    try
    {
      elapsedTime = System.nanoTime() - startNanoSec;
    }
    finally
    {
      LOCK.unlock();
    }
  }

  /**
   * Sets the starting time (in nano seconds) that was sent by the server over the network.
   *
   * @param time time to set startNanoSec to
   */
  private void setStartTime(double time)
  {
    if (!isConnected()) return;
    try
    {
      LOCK.lock();
      startNanoSec = time;
    }
    finally
    {
      LOCK.unlock();
    }
  }

  private void commPrint(String message)
  {
    System.out.println("Client: " + message);
  }

  private void commError(String message)
  {
    System.err.println("Client: " + message);
  }

  /**
   * Tries to close the server socket obtained from secureProcessPort.
   */
  private void closeProcessSocket()
  {
    try
    {
      processSocket.close();
      processSocket = null;
    }
    catch (Exception e)
    {
      // Ignore
    }
  }

  /**
   * This is used to secure PROCESS_LOCK_PORT when a client is trying to spawn a local server.
   *
   * @return ServerSocket (or null if it failed)
   */
  private ServerSocket secureProcessPort()
  {
    try
    {
      return new ServerSocket(PROCESS_LOCK_PORT);
    }
    catch (Exception e)
    {
      return null;
    }
  }

  /**
   * This makes a *single* attempt to connect given the host/port combination.
   * If it succeeds, clientSocket, writer and reader should contain valid references and
   * the server handshake will have been both started and completed.
   *
   * @param host host to connect to
   * @param port port that the host is listening to
   * @return true if the connection/handshake was successful, false if not
   */
  private boolean openConnection(String host, int port)
  {
    try
    {
      Socket tempSocket = clientSocket;
      DataInputStream input = reader;
      DataOutputStream output = writer;
      clientSocket = new Socket(host, port);
      writer = new DataOutputStream(clientSocket.getOutputStream());
      reader = new DataInputStream(clientSocket.getInputStream());
      if (tempSocket != null && !tempSocket.isClosed())
      {
        tempSocket.close();
        input.close();
        output.close();
      }
    }
    catch (IOException e)
    {
      return false; // Problem connecting
    }
    // POJO = Plain Old Java Object
    Util.startServerHandshake(clientSocket, rsaKey, DataType.POJO);
    serverKey = Util.endServerHandshake(clientSocket, rsaKey);
    return true;
  }

  private KeyPair generateRSAKey()
  {
    try
    {
      final KeyPairGenerator KEY_GEN = KeyPairGenerator.getInstance("RSA");
      return KEY_GEN.generateKeyPair();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }

  private Cipher generateAESCipher()
  {
    try
    {
      return Cipher.getInstance(Constant.DATA_ALGORITHM);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * This is the method that login, send, and sendChat use to actually send the
   * data over a network to the server.
   *
   * @param request request packed with data to send to the server
   */
  private void send (Request request)
  {
    try
    {
      SealedObject sealedObject = new SealedObject(request, aesCipher);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(sealedObject);
      oos.close();

      LOCK.lock(); // Make sure this is done since the stream is not necessarily thread safe
      writer.flush();
      byte[] bytes = baos.toByteArray();
      writer.writeInt(bytes.length);
      writer.write(bytes);
      writer.flush();
      baos.close();
    }
    catch (Exception e)
    {
      commError("An error occurred while trying to send data to the server");
      dispose(); // Assume something went very wrong (Ex: server shut down)
    }
    finally
    {
      LOCK.unlock();
    }
  }
}
