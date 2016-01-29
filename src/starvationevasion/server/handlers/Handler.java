package starvationevasion.server.handlers;


import starvationevasion.server.ClientWorker;
import starvationevasion.server.Request;
import starvationevasion.server.Response;
import starvationevasion.server.ServerMaster;
import starvationevasion.sim.Simulator;

public class Handler
{
  private AbstractHandler handler;
  private ClientWorker worker;

  public Handler (ServerMaster server, ClientWorker clientWorker)
  {
    worker = clientWorker;
    handler = new UserHandler(server, clientWorker).setSuccessor(
            new VoteHandler(server, clientWorker).setSuccessor(
                    new ChatHandler(server, clientWorker).setSuccessor(
                            new DiscardHandler(server, clientWorker).setSuccessor(
                                    new LoginHandler(server, clientWorker)))));
  }

  public void handle (Request request)
  {
    request.setFrom(worker.getName());
    handler.handleRequest(request);
    Response s = handler.getResponse();
    if (s != null)
    {
      worker.send(s);
    }
  }
}
