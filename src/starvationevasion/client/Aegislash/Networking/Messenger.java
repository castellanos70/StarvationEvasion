package starvationevasion.client.Aegislash.Networking;

import java.io.*;
import java.lang.ClassNotFoundException;
import java.lang.System;
import java.net.Socket;

/**
 * Sends and receives messages to the server
 */
public class Messenger
{
  private Socket clientSide;
  private ObjectOutputStream messageSender;
  private ObjectInputStream messageReceiver;

  /**
   * The messenger class is used to communicate with
   * the server in the form of both receiving and
   * sending Serializable message objects through
   * the specified socket.
   * @param clientSide The specified socket.
   */
  public Messenger(Socket clientSide)
  {
    this.clientSide = clientSide;
    try
    {
      messageSender = new ObjectOutputStream(clientSide.getOutputStream());
      messageReceiver = new ObjectInputStream(clientSide.getInputStream());
    }
    catch (IOException io)
    {
      io.printStackTrace();
    }
  }

  /**
   * Used for sending a Message object to the server in the
   * form of the contents of a MessageContainer.
   * @param container The container wit the desired message
   *                  contents.
   */
  public void send(MessageContainer container)
  {
    try
    {
      messageSender.writeObject(container.getContents());
      messageSender.flush();
    }
    catch (IOException io)
    {
      io.printStackTrace();
    }
  }

  /**
   * Listens for server messages on the socket indefinitely.
   * @return A newly received message.
   * @throws InterruptedException In case stream is broken.
   */
  public Serializable receive() throws InterruptedException
  {
    try
    {
      return (Serializable) messageReceiver.readObject();
    } catch (IOException e)
    {
      e.printStackTrace();
      throw new InterruptedException();
    }
    catch (ClassNotFoundException e)
    {
      System.out.println("Class not found");
      e.printStackTrace();
    }
    return null;
  }
}
