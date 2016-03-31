package starvationevasion.client;
/**
 * @author Javier Chavez
 */

import com.oracle.javafx.jmx.json.JSONReader;
import starvationevasion.client.Logic.ChatManager;
import starvationevasion.common.EnumRegion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Client
 *
 * UX/UI. This is the interface that handles user input
 */
public class Client
{
  private Socket clientSocket;

  // write to socket
  private PrintWriter write;
  // read the socket
  private BufferedReader reader;

  // time of server start
  private long startNanoSec;
  private volatile long lastInventoryUpdate;
  private Scanner keyboard;
  // writes to user
  private ClientListener listener;

  private ChatManager chatManager;
  private EnumRegion region;
  private JSONReader jsonReader;


  private String currentOutput="";

  private volatile boolean isRunning = true;


  public Client(String host, int portNumber)
  {
    chatManager=new ChatManager(this);
    keyboard = new Scanner(System.in);
    region=EnumRegion.USA_CALIFORNIA;
    while (!openConnection(host, portNumber))
    {
    }
    //jsonReader=new JSONStreamReaderImpl(reader);
    listener = new ClientListener();
    System.out.println("Client(): Starting listener = : " + listener);
    listener.start();

    //listenToUserRequests();

    //closeAll();

  }
  public ChatManager getChatManager(){return chatManager;}
  public boolean writeToServer(String message){
    currentOutput="";
    write.println(System.nanoTime() + " " + message);

    System.out.println((System.nanoTime() + " " + message));
    //System.out.println(currentOutput);
    while(message.contains("login"))
    {
      if (currentOutput.contains("SUCCESS"))return true;
      if(currentOutput.contains("FAIL"))return false;
    }

      return false;
  }
  private void output(String msg){
    currentOutput+=msg;
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
      if (true) throw new RuntimeException("");

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
    //ToDo JavaClient
    write.println("client");
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
      write.println(System.nanoTime() + " " + cmd);
    }
  }

  public void closeAll()
  {
    System.out.println("Closing client");
    isRunning=false;
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

  public EnumRegion getRegion(){return region;}

  private String timeDiff()
  {
    long nanoSecDiff = System.nanoTime() - startNanoSec;
    double secDiff = (double) nanoSecDiff / 1000000000.0;
    return String.format("%.3f", secDiff);
  }
  private void setRegion(String regionString)
  {
    region=EnumRegion.valueOf(regionString);
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
      while (isRunning)
      {
        read();
      }
    }
    private void read()
    {
      try
      {
        String msg = reader.readLine();

        if (msg == null)
        {
          System.out.println("Lost server, press enter to shutdown.");
          isRunning = false;
          return;
        }
        output(msg);


        //TODO Implement JSON Parser    // instanceOf
        //
//        StringReader stringReader=new StringReader(msg);
//        JSONStreamReaderImpl jsonStreamReader=new JSONStreamReaderImpl(stringReader);
//        JSONDocument json=jsonStreamReader.build();
//        String data=json.getString("time");
////
//        if(data.contains("chat"))
//        {
//         chatManager.sendChatToClient(json.getString("data"));
//        }
        if(msg.contains("text"))
        {
          String message=msg.substring(msg.indexOf("text") + 3);
          message=message.substring(msg.indexOf('"'));
          chatManager.sendChatToClient(message);
        }
        if(region==null&&msg.contains("region"))
        {
          String halfMsg=msg.substring((msg.indexOf("region") +9 ));
          setRegion(halfMsg.substring(0, halfMsg.indexOf("\",")));
        }
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
}