package starvationevasion.server.io;


import starvationevasion.server.model.Encryptable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SecureSocketReadStrategy implements ReadStrategy, Encryptable
{
  private final String key;
  private BufferedReader reader;
  private final Socket socket;

  public SecureSocketReadStrategy (Socket socket, String key)
  {
    this.socket = socket;
    this.key = key;
    try
    {
      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    catch(IOException e)
    {
      System.err.println("Server Worker: Could not open input stream");
      e.printStackTrace();
    }
  }


  @Override
  public String read () throws IOException
  {
    return reader.readLine();
  }

  @Override
  public void close () throws IOException
  {
    socket.shutdownInput();
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