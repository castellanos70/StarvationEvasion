package starvationevasion.server.io;


import java.io.*;
import java.net.Socket;

public class SocketReadStrategy implements ReadStrategy
{
  private BufferedReader reader;

  public SocketReadStrategy(Socket socket)
  {
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
}
