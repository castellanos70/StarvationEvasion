package starvationevasion.communication;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.GameCard;
import starvationevasion.common.Util;
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
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implements Communication interface.
 *
 * @author Javier Chavez (javierc@cs.unm.edu), Justin Hall, George Boujaoude
 */
public class CommModule implements Communication
{
  // Final variables
  private final ReentrantLock LOCK = new ReentrantLock(); // Used for synchronization
  private final AtomicBoolean IS_RUNNING = new AtomicBoolean(false);
  private final HashMap<Type, ResponseListener> RESPONSE_MAP = new HashMap<>(10);
  // Responses that don't have a listener go here until a listener is made available
  private final HashMap<Type, ConcurrentLinkedQueue<Response>> SHELVED_RESPONSES = new HashMap<>(10);
  private final ConcurrentLinkedQueue<Response> RESPONSE_EVENTS = new ConcurrentLinkedQueue<>();

  // Non-final variables
  private Socket clientSocket;
  private DataInputStream reader;
  private DataOutputStream writer;
  private double startNanoSec = 0;
  private double elapsedTime = 0;

  private SecretKey serverKey;
  private Cipher aesCipher;
  private KeyPair rsaKey;

  private class StreamListener extends Thread
  {
    @Override
    public void run()
    {
      serverKey = Util.endServerHandshake(clientSocket, rsaKey);
      while (IS_RUNNING.get()) read();
    }

    private void read()
    {
      try
      {
        Response response = readObject();
        RESPONSE_EVENTS.add(response);

        if (response.getType().equals(Type.AUTH_ERROR))
        {
          commError("Failed to login");
          dispose();
        }
      }
      catch (Exception e)
      {
        commError("Error reading response from server - shutting down");
        e.printStackTrace();
        dispose();
      }
    }

    private Response readObject () throws IOException, ClassNotFoundException, InvalidKeyException, NoSuchAlgorithmException
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
      Response response = (Response) sealedObject.getObject(serverKey);
      is.close();
      in.close();

      return response;
    }
  }

  /**
   * Constructs a new CommModule with the given host/port combo. It will attempt
   * to make a connection and if it does not succeed within 10 seconds, the CommModule
   * will timeout and close the program with an commError code.
   *
   * @param host host to connect to
   * @param port port to connect through
   */
  public CommModule(String host, int port)
  {
    final long millisecondTimeStamp = System.currentTimeMillis();
    final double MAX_SECONDS = 10.0;
    double deltaSeconds = 0.0;

    // Set up the key and cipher
    rsaKey = generateRSAKey();
    aesCipher = generateAESCipher();

    // Try to establish a connection and timeout after MAX_SECONDS if not
    commPrint("Attempting to connect to host " + host + ", port " + port);
    while (deltaSeconds < MAX_SECONDS)
    {
      IS_RUNNING.set(openConnection(host, port));
      if (IS_RUNNING.get()) break;
      deltaSeconds = (System.currentTimeMillis() - millisecondTimeStamp) / 1000.0;
    }

    // See if the connect attempt went badly
    if (!IS_RUNNING.get())
    {
      commError("Failed to establish connection at host " + host + ", port " + port + " within " + MAX_SECONDS + " " +
                "seconds.");
      return;
    }
    commPrint("Connection successful");

    // This handles setting the startNanoTime variable
    setResponseListener(Type.TIME, (type, data) -> setStartTime((Double)data));

    // Start the listener
    new StreamListener().start();
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
   * Returns the current connection state of the module. If at any point the module loses connection
   * with the server, this will return false.
   *
   * @return true if connected to the server and false if not
   */
  @Override
  public boolean isConnected()
  {
    return IS_RUNNING.get();
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
   * Note :: No responses should be pushed to their respective listeners until an external source
   * (Ex: AI/UI client) requests that the Communication module do so. This avoids the issue
   * of requiring these listeners to be thread-safe. See pushResponseEvents().
   * <p>
   * This binds a response listener to a type. The type represents one of the possible responses that
   * can be generated by the server and pushed to a Communication module. These responses often
   * contain data from the server, so the listener itself will receive this data so that it can
   * push it to whoever needs it. For example, if the type was USER, it means that the server has just
   * sent an entire serialized User over the network (it can be cast directly to a User object).
   *
   * @param type     type to listen for
   * @param listener listener to bind to - see starvationevasion.communication.ResponseListener
   */
  @Override
  public void setResponseListener(Type type, ResponseListener listener)
  {
    if (!IS_RUNNING.get()) return;
    try
    {
      LOCK.lock();
      RESPONSE_MAP.put(type, listener);
    }
    finally
    {
      LOCK.unlock();
    }
  }

  /**
   * When this is called, the entire list of response events that were sent from the server since the last
   * time this was called will be pushed to their listeners (if no listener was bound, these events
   * should be shelved until a listener is bound).
   * <p>
   * A good data structure to consider about for this is a FIFO list so that older events are processed
   * first, and newer commands are processed last in case they need to override/alter the outcome
   * of the older events.
   */
  @Override
  public void pushResponseEvents()
  {
    try
    {
      LOCK.lock();
      int size = RESPONSE_EVENTS.size(); // Get a snapshot of the size (prevents sync issues)
      for (int i = 0; i < size; i++)
      {
        Response response = RESPONSE_EVENTS.poll();
        Type type = response.getType();
        ResponseListener listener = RESPONSE_MAP.get(type);
        // If no listener is present, shelve the response for when (if) one is made available later
        if (listener == null)
        {
          shelveResponse(response);
          continue;
        }
        // Check to see if there are existing shelved responses that need attention
        if (SHELVED_RESPONSES.containsKey(type))
        {
          ConcurrentLinkedQueue<Response> shelvedResponses = SHELVED_RESPONSES.get(type);
          for (Response shelvedResponse : shelvedResponses) listener.processResponse(type,
                                                                                     shelvedResponse.getPayload().getData());
          shelvedResponses.clear();
          SHELVED_RESPONSES.remove(type);
        }
        // Now process the current response
        listener.processResponse(type, response.getPayload().getData());
      }
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
    if (!IS_RUNNING.get()) return; // Already disposed/the connection never succeeded
    IS_RUNNING.set(false);
    while(RESPONSE_EVENTS.size() > 0) pushResponseEvents(); // Clear out the events
    RESPONSE_MAP.clear();
    try
    {
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
    return true;
  }

  private void setStartTime(double time)
  {
    if (!IS_RUNNING.get()) return;
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

  private void shelveResponse(Response response)
  {
    if (!IS_RUNNING.get()) return;
    try
    {
      LOCK.lock();
      if (!SHELVED_RESPONSES.containsKey(response.getType())) SHELVED_RESPONSES.put(response.getType(),
                                                                                    new ConcurrentLinkedQueue<>());
      SHELVED_RESPONSES.get(response.getType()).add(response);
    }
    finally
    {
      LOCK.unlock();
    }
  }

  public void commPrint(String message)
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

  public void send (Request request)
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
