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
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class serves as the new method of communication between the server and the client.
 * It is thread-safe and provides the ability to connect, check connection status, disconnect
 * and send/receive information from a server.
 *
 * @author Javier Chavez (javierc@cs.unm.edu), Justin Hall, George Boujaoude
 */
public class ConcurrentCommModule implements Communication
{
  private final ReentrantLock LOCK = new ReentrantLock(); // Used for synchronization
  private final AtomicBoolean IS_CONNECTED = new AtomicBoolean(false);
  private final ArrayList<Response> RESPONSE_EVENTS = new ArrayList<>(1_000);
  private final String HOST;
  private final int PORT;

  // Non-final variables
  private Socket clientSocket;
  private DataInputStream reader;
  private DataOutputStream writer;
  private double startNanoSec = 0;
  private double elapsedTime = 0;

  private SecretKey serverKey; // Obtained at the end of the handshake
  private Cipher aesCipher;
  private KeyPair rsaKey;

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
          return;
        }
        add(response);

        if (response.getType().equals(Type.AUTH_SUCCESS)) commPrint("Login successful");
        else if (response.getType().equals(Type.AUTH_ERROR)) commError("Failed to login");
        else if (response.getType().equals(Type.CREATE_SUCCESS)) commPrint("Created user successfully");
        else if (response.getType().equals(Type.CREATE_ERROR)) commError("Failed to create user");
        else if (response.getType().equals(Type.TIME)) setStartTime((Double)response.getPayload().getData());
      }
      catch (Exception e)
      {
        commError("Error reading response from server");
        e.printStackTrace();
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

  public ConcurrentCommModule(String host, int port)
  {
    HOST = host;
    PORT = port;
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
    if (IS_CONNECTED.get()) return; // Already connected
    try
    {
      LOCK.lock();
      final long millisecondTimeStamp = System.currentTimeMillis();
      final double MAX_SECONDS = 10.0;
      double deltaSeconds = 0.0;

      // Set up the key
      rsaKey = generateRSAKey();

      // Try to establish a connection and timeout after MAX_SECONDS if not
      commPrint("Attempting to connect to host " + HOST + ", port " + PORT);
      while (deltaSeconds < MAX_SECONDS)
      {
        IS_CONNECTED.set(openConnection(HOST, PORT));
        if (IS_CONNECTED.get()) break;
        deltaSeconds = (System.currentTimeMillis() - millisecondTimeStamp) / 1000.0;
      }

      // Generate the aes cipher
      aesCipher = generateAESCipher();

      // See if the connect attempt went badly
      if (!IS_CONNECTED.get())
      {
        commError("Failed to establish connection at host " + HOST + ", port " + PORT + " within " + MAX_SECONDS + " " +
                  "seconds.");
        return;
      }
      commPrint("Connection successful");

      // Start the listener
      new StreamListener().start();
    }
    finally
    {
      LOCK.unlock();
    }
  }

  /**
   * Disposes of this module. All existing threads should be shut down and data cleared out.
   * <p>
   * The object is not meant to be used in any way after this is called.
   */
  @Override
  public void dispose()
  {
    if (!IS_CONNECTED.get()) return; // Already disposed/the connection never succeeded
    IS_CONNECTED.set(false);
    try
    {
      LOCK.lock();
      RESPONSE_EVENTS.clear();

      writer.close();
      reader.close();
      clientSocket.close();
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
      ArrayList<Response> messages = new ArrayList<>(RESPONSE_EVENTS);
      RESPONSE_EVENTS.clear();
      return messages;
    }
    finally
    {
      LOCK.unlock();
    }
  }

  private void add(Response response)
  {
    try
    {
      LOCK.lock();
      RESPONSE_EVENTS.add(response);
    }
    finally
    {
      LOCK.unlock();
    }
  }

  private boolean openConnection(String host, int port)
  {
    try
    {
      clientSocket = new Socket(host, port);
      writer = new DataOutputStream(clientSocket.getOutputStream());
      reader = new DataInputStream(clientSocket.getInputStream());
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

  private void commPrint(String message)
  {
    System.out.println("Client: " + message);
  }

  private void commError(String message)
  {
    System.err.println("Client: " + message);
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

  private void setStartTime(double time)
  {
    if (!IS_CONNECTED.get()) return;
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

  private void updateCurrentTime()
  {
    try
    {
      if (!LOCK.tryLock()) return; // Lock is busy - updating the time isn't super important, so don't block
      elapsedTime = System.nanoTime() - startNanoSec;
    }
    finally
    {
      LOCK.unlock();
    }
  }

  private void send (Request request)
  {
    try
    {
      aesCipher.init(Cipher.ENCRYPT_MODE, serverKey);
      SealedObject sealedObject = new SealedObject(request, aesCipher);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(sealedObject);
      oos.close();

      writer.flush();
      byte[] bytes = baos.toByteArray();
      writer.writeInt(bytes.length);
      writer.write(bytes);
      writer.flush();
      baos.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
