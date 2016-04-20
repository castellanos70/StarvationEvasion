package starvationevasion.util;

/**
 * @author Javier Chavez
 */


import starvationevasion.common.Constant;
import starvationevasion.common.Util;
import starvationevasion.server.model.*;
import starvationevasion.util.events.AuthenticationEvent;
import starvationevasion.util.events.GameStateEvent;
import starvationevasion.util.events.SocketConnectionEvent;
import starvationevasion.util.listeners.SocketEventListener;

import javax.crypto.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * Client
 *
 * UX/UI. This is the interface that handles user input
 */
public class SocketConnection
{
  private final SocketEventListener mListener;

  private Socket clientSocket;

  private DataInputStream reader;
  private DataOutputStream writer;


  // writes to user
  private StreamListener streamListener;

  private SecretKey serverKey;
  private Cipher aesCipher;

  private volatile boolean isRunning = true;
  private KeyPair rsaKey;


  public SocketConnection (String host, int portNumber, SocketEventListener eventListener)
  {
    setupSecurity();

    mListener = eventListener;

    while(!openConnection(host, portNumber))
    {
    }

    streamListener = new StreamListener();
    System.out.println("Client(): Starting listener = : " + streamListener);
    streamListener.start();
  }

  private boolean openConnection (String host, int portNumber)
  {

    try
    {
      clientSocket = new Socket(host, portNumber);
    }
    catch(Exception e)
    {
      System.err.println("Client Error: Could not open connection to " + host
                                 + " on port " + portNumber);
      e.printStackTrace();
      isRunning = false;
      return false;
    }

    try
    {
      writer = new DataOutputStream(clientSocket.getOutputStream());
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
    }


    try
    {
      reader = new DataInputStream(clientSocket.getInputStream());
    }
    catch(IOException e)
    {
      e.printStackTrace();
      return false;
    }
    isRunning = true;

    Util.startServerHandshake(clientSocket, rsaKey, "JavaClient");

    return true;
  }


  public void closeAll ()
  {
    System.out.println("Closing client");


    if (reader != null)
    {
      try
      {
        reader.close();
        clientSocket.close();
      }
      catch(IOException e)
      {
        System.err.println("Client Error: Could not close");
        e.printStackTrace();
      }
    }
  }


  /**
   * StreamListener
   *
   * Handles reading stream from socket. The data is then outputted
   * to the console for user.
   */
  private class StreamListener extends Thread
  {

    public void run ()
    {
      serverKey = Util.endServerHandshake(clientSocket, rsaKey);
      mListener.onReceive(new SocketConnectionEvent.Success() {

        @Override
        public Void getData ()
        {
          return null;
        }
      });


      while(isRunning)
      {
        read();
      }
    }

    private void read ()
    {
      try
      {
        Response response = readObject();

        if (response.getType().equals(Type.AUTH_SUCCESS))
        {
            mListener.onReceive(new AuthenticationEvent.Success()
            {
              @Override
              public User getData ()
              {
                return (User) response.getPayload().getData();
              }
            });
        }
        else if (response.getType().equals(Type.AUTH_ERROR))
        {
          mListener.onReceive(new AuthenticationEvent.Fail()
          {
            @Override
            public Void getData ()
            {
              return null;
            }
          });
        }
        else if (response.getType().equals(Type.USER))
        {
          //mListener.onReceive(new UserEvent????);

        }
        else if (response.getType().equals(Type.TIME))
        {

        }
        else if (response.getType().equals(Type.WORLD_DATA_LIST))
        {
          // System.out.println("Getting a list of WorldData's");
//          worldData = (ArrayList<WorldData>) response.getPayload().getData();
        }
        else if (response.getType().equals(Type.USERS_LOGGED_IN_LIST))
        {
          // System.out.println("Getting a list of ready users");
//          users = (ArrayList<User>) response.getPayload().getData();
        }
        else if (response.getType().equals(Type.WORLD_DATA))
        {
          // System.out.println("Getting a list of WorldData's");
//          worldData.add((WorldData) response.getPayload().getData());
        }
        else if (response.getType().equals(Type.GAME_STATE))
        {

          mListener.onReceive(new GameStateEvent()
          {
            @Override
            public starvationevasion.server.model.State getData ()
            {
              return (starvationevasion.server.model.State) response.getPayload().getData();
            }
          });

        }
        else if (response.getType().equals(Type.DRAFTED) || response.getType().equals(Type.DRAFTED_INTO_VOTE))
        {
          String msg = response.getPayload().getMessage();
          if (msg == null)
          {
            // PolicyCard card = (PolicyCard) response.getPayload().getData();
            // getUser().getHand().remove(card.getCardType());
          }
          else
          {
            System.out.println(msg);
          }
        }
        else if (response.getType().equals(Type.VOTE_BALLOT))
        {
//          ballot = (List<PolicyCard>) response.getPayload().getData();
        }

      }
      catch(EOFException e)
      {
        isRunning = false;
        System.out.println("Lost server, press enter to shutdown.");

      }
      catch(SocketException e)
      {
        isRunning = false;
        System.out.println("Lost server");
        System.out.println("Shutting down...");

      }
      catch(NoSuchAlgorithmException e)
      {
        isRunning = false;
        System.out.println("Will not be able to decrypt");
        System.exit(1);
      }
      catch(IOException e)
      {
        isRunning = false;
        System.out.println("Error reading");
      }
      catch(InvalidKeyException e)
      {
        System.out.println("Key is not valid");
      }
      catch(ClassNotFoundException e)
      {
        isRunning = false;
        System.out.println("Class not found!");
      }
    }
  }

  private Response readObject () throws IOException, ClassNotFoundException, InvalidKeyException, NoSuchAlgorithmException
  {
    int ch1 = reader.read();
    int ch2 = reader.read();
    int ch3 = reader.read();
    int ch4 = reader.read();

    if ((ch1 | ch2 | ch3 | ch4) < 0)
    {
      throw new EOFException();
    }
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
    catch(IOException e)
    {
      e.printStackTrace();
    }
    catch(InvalidKeyException e)
    {
      e.printStackTrace();
    }
    catch(IllegalBlockSizeException e)
    {
      e.printStackTrace();
    }
  }


  private void setupSecurity ()
  {
    try
    {
      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(Constant.ASYM_ALGORITHM);

      keyGen.initialize(1024);
      rsaKey = keyGen.generateKeyPair();
      aesCipher = Cipher.getInstance(Constant.DATA_ALGORITHM);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}