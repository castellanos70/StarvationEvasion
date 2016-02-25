package starvationevasion.server.io;


import starvationevasion.server.Worker;

import java.io.IOException;
import java.net.Socket;

public class SocketReadStrategy implements ReadStrategy
{

  private final Worker worker;

  public SocketReadStrategy(Worker worker)
  {
    this.worker = worker;
  }


  @Override
  public String read () throws IOException
  {
    return  worker.getClientReader().readLine();
  }
}
