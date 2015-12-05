package starvationevasion.server;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.messages.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * Shea Polansky
 * Worker thread for a server
 */
public class ServerWorker extends Thread
{
  private final Server server;
  private final ObjectInputStream receiveStream;
  private final ObjectOutputStream sendStream;
  private String loginNonce, userName;
  private EnumRegion region;

  public ServerWorker(Server server, Socket socket)
  {
    ObjectInputStream tempReceiveStream;
    ObjectOutputStream tempSendStream;
    try
    {
      tempReceiveStream = new ObjectInputStream(socket.getInputStream());
      tempSendStream = new ObjectOutputStream(socket.getOutputStream());
    }
    catch (IOException e)
    {
      tempReceiveStream = null;
      tempSendStream = null;
      e.printStackTrace();
    }
    this.server = server;
    receiveStream = tempReceiveStream;
    sendStream = tempSendStream;
  }

  @Override
  public void run()
  {
    while (!Thread.interrupted())
    {
      try
      {
        server.acceptMessage((Serializable)receiveStream.readObject(), this);
      }
      catch (IOException | ClassNotFoundException | ClassCastException e)
      {
        send(Response.BAD_MESSAGE);
      }
    }
  }

  public synchronized void send(Serializable message)
  {
    try
    {
      sendStream.writeObject(message);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      server.disconnectClient(this, null);
    }
  }

  public String getLoginNonce()
  {
    return loginNonce;
  }

  public void setLoginNonce(String loginNonce)
  {
    this.loginNonce = loginNonce;
  }

  public EnumRegion getRegion()
  {
    return region;
  }

  public void setRegion(EnumRegion region)
  {
    this.region = region;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName(String userName)
  {
    this.userName = userName;
  }

  public void closeSocket()
  {
    try
    {
      receiveStream.close();
    }
    catch (IOException e)
    {
      System.out.println("Error closing receiveStream in client: " + this);
    }
    try
    {
      sendStream.close();
    }
    catch (IOException e)
    {
      System.out.print("Error closing sendStream in client: " + this);
    }
  }
}
