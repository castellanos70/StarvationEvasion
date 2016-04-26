package starvationevasion.communication;

import starvationevasion.common.Constant;
import starvationevasion.common.Util;
import starvationevasion.server.model.Request;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract class for a series of communication modules that are to be used by
 * both the user's client and the ai's client when communicating with this server.
 *
 * By separating the server-client communication code, it not only reduces code duplication
 * but allows for a standard between both the user and ai clients to be established.
 *
 * @authorJavier Chavez (javierc@cs.unm.edu), George Boujaoude, Justin Hall
 */
@Deprecated
public class CommModule
{
  private final CommClient CLIENT;
  private final AtomicBoolean BRIDGE_ESTABLISHED = new AtomicBoolean();
  private Socket clientSocket;
  private DataInputStream reader;
  private DataOutputStream writer;
  private KeyPair rsaKey;
  private Cipher aesCipher;
  private SecretKey serverKey;

  public CommModule(CommClient client, String host, int port)
  {
    CLIENT = client;
    long timeStamp = System.currentTimeMillis();
    double deltaSeconds = 0.0;
    double maxSeconds = 10.0;

    // Set up the key and cipher
    rsaKey = generateRSAKey();
    aesCipher = generateAESCipher();

    // Attempt to make a connection, but timeout after the given number of seconds
    while (deltaSeconds < maxSeconds)
    {
      if (openConnection(host, port)) break; // break if a connection was made
      deltaSeconds += (System.currentTimeMillis() - timeStamp) / 1000.0; // convert to seconds
      timeStamp = System.currentTimeMillis();
    }

    // Make sure that everything went alright
    if (!BRIDGE_ESTABLISHED.get())
    {
      commError("Connection timeout - failed to establish a connection within " +
                maxSeconds + " seconds");
      System.exit(-1);
    }
  }

  public void send (Request request)
  {
    try
    {
      aesCipher.init(Cipher.ENCRYPT_MODE, serverKey);
      SealedObject sealedObject = new SealedObject(request, aesCipher);
      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
      objectOut.writeObject(sealedObject);
      objectOut.close();

      writer.flush();
      byte[] bytes = byteOut.toByteArray();
      writer.writeInt(bytes.length);
      writer.write(bytes);
      writer.flush();
      byteOut.close();
    }
    catch (IOException | InvalidKeyException | IllegalBlockSizeException e)
    {
      e.printStackTrace();
    }
  }

  private boolean openConnection(String host, int port)
  {
    BRIDGE_ESTABLISHED.set(true); // let's hope for the best
    try
    {
      clientSocket = new Socket(host, port);
      reader = new DataInputStream(clientSocket.getInputStream());
      writer = new DataOutputStream(clientSocket.getOutputStream());
    }
    catch (IOException e)
    {
      commError("Could now open a connection to " + host + " on port " + port);
      e.printStackTrace();
      BRIDGE_ESTABLISHED.set(false);
    }

    // Start the handshake and let the server know what type of client we are
    Util.startServerHandshake(clientSocket, rsaKey, "JavaClient");

    return BRIDGE_ESTABLISHED.get();
  }

  private void commError(String error)
  {
    System.err.println("Client: " + error);
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
}
