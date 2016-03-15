package starvationevasion.server.io;


import java.io.*;
import java.net.Socket;

public class SocketReadStrategy implements ReadStrategy<String>
{
  private BufferedReader reader;
  private final Socket socket;

  public SocketReadStrategy(Socket socket)
  {
    this.socket = socket;
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
}
