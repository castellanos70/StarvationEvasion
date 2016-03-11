package starvationevasion.server.io;


import starvationevasion.server.model.Encryptable;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SecureSocketWriteStrategy implements WriteStrategy, Encryptable
{
  private PrintWriter writer;
  private final Socket socket;
  private final String key;

  public SecureSocketWriteStrategy (Socket socket, String key)
  {
    this.socket = socket;
    this.key = key;

    try
    {
      writer = new PrintWriter(socket.getOutputStream(), true);
    }
    catch(IOException e)
    {
      System.err.println("Server Worker: Could not open output stream");
      e.printStackTrace();
    }
  }


  @Override
  public void write (String s) throws IOException
  {
    writer.println(s);
  }

  @Override
  public void close () throws IOException
  {
    socket.shutdownOutput();
  }

  @Override
  public void encrypt (String msg, String key)
  {

  }

  @Override
  public <T> T decrypt (String msg, String key)
  {
    return null;
  }
}