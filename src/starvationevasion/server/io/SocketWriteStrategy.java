package starvationevasion.server.io;



import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketWriteStrategy implements WriteStrategy
{
  private PrintWriter writer;

  public SocketWriteStrategy(Socket socket)
  {
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
}
