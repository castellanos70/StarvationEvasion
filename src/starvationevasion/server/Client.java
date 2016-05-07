package starvationevasion.server;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 *
 * THIS VERSION OF CLIENT IS NOT CURRENTLY BEING USED.
 * - George Boujaoude (gboujaoude@unm.edu)
 */


import starvationevasion.common.Constant;
import starvationevasion.common.Util;
import starvationevasion.server.model.DataType;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.util.Scanner;

/**
 * Client
 *
 * UX/UI. This is the interface that handles user input
 */
class Client
{
  private Socket clientSocket;

  // write to socket
  private PrintWriter write;
  // read the socket
  private BufferedReader reader;

  private Scanner keyboard;
  // writes to user
  private ClientListener listener;

  private SecretKey serverKey;
  private Cipher aesCipher;

  private volatile boolean isRunning = true;
  private KeyPair rsaKey;


  public Client(String host, int portNumber)
  {
    setupSecurity();

    keyboard = new Scanner(System.in);

    if (!openConnection(host, portNumber))
    {
      System.out.println("Could not connect. Exiting...");
      System.exit(-1);
    }

    listener = new ClientListener();
    System.out.println("Client(): Starting listener = : " + listener);
    listener.start();

    listenToUserRequests();

    closeAll();

  }

  private boolean openConnection(String host, int portNumber)
  {

    try
    {
      clientSocket = new Socket(host, portNumber);
    }
    catch (UnknownHostException e)
    {
      System.err.println("Client Error: Unknown Host " + host);
      e.printStackTrace();
      isRunning = false;
      return false;
    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open connection to " + host
                                 + " on port " + portNumber);
      e.printStackTrace();
      isRunning = false;
      return false;
    }

    try
    {
      write = new PrintWriter(clientSocket.getOutputStream(), true);
    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open output stream");
      e.printStackTrace();
      return false;
    }
    try
    {
      reader = new BufferedReader(new InputStreamReader(
              clientSocket.getInputStream()));
    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open input stream");
      e.printStackTrace();
      return false;
    }
    isRunning = true;
    // write.println("client");

    Util.startServerHandshake(clientSocket, rsaKey, DataType.JSON);
    return true;

  }

  private void listenToUserRequests()
  {


    while (isRunning)
    {

      String cmd = keyboard.nextLine();


      if (cmd == null || cmd.length() < 1)
      {
        continue;
      }

      if (cmd.charAt(0) == 'q')
      {
        isRunning = false;
      }
      try
      {
        String s = String.valueOf(System.nanoTime()) + " " + cmd;

        aesCipher.init(Cipher.ENCRYPT_MODE, serverKey);
        String data = DatatypeConverter.printBase64Binary(aesCipher.doFinal(s.getBytes()));
        write.print(data + Constant.TERMINATION);
        write.flush();
      }
      catch(InvalidKeyException e)
      {
        e.printStackTrace();
      }
      catch(BadPaddingException e)
      {
        e.printStackTrace();
      }
      catch(IllegalBlockSizeException e)
      {
        e.printStackTrace();
      }

    }
  }

  public void closeAll()
  {
    System.out.println("Closing client");

    if (write != null)
    {
      write.close();
    }

    if (reader != null)
    {
      try
      {
        reader.close();
        clientSocket.close();
      }
      catch (IOException e)
      {
        System.err.println("Client Error: Could not close");
        e.printStackTrace();
      }
    }
  }


  public static void main(String[] args)
  {

    String host = null;
    int port = 0;

    try
    {
      host = args[0];
      port = Integer.parseInt(args[1]);
      if (port < 1)
      {
        throw new Exception();
      }
    }
    catch (Exception e)
    {
      System.out.println("Usage: Client hostname portNumber");
      System.exit(0);
    }
    new Client(host, port);

  }



  /**
   * ClientListener
   *
   * Handles reading stream from socket. The data is then outputted
   * to the console for user.
   */
  class ClientListener extends Thread
  {
    public void run()
    {
      serverKey = Util.endServerHandshake(clientSocket, rsaKey);

      while (isRunning)
      {
        read();
      }
    }

    private void read()
    {
      try
      {
        int i  = reader.read();
        if (i == -1)
        {
          System.out.println("Lost server, press enter to shutdown.");
          isRunning = false;
          return;
        }

        StringBuilder _sb = new StringBuilder();
        while(true)
        {
          if (i == -1 || i == 10)
          {
            if (i >= 0)
            {
               _sb.append((char) i);
              break;
            }
            break;
          }
          _sb.append((char) i);
          i = reader.read();
        }

        aesCipher.init(Cipher.DECRYPT_MODE, serverKey);
        String msg =  new String(aesCipher.doFinal(DatatypeConverter.parseBase64Binary(_sb.toString().trim())));

        System.out.println(msg);

      }
      catch (IOException e)
      {
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
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
