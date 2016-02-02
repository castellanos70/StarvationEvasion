package starvationevasion.server.handlers;


import starvationevasion.server.Worker;
import starvationevasion.server.Request;
import starvationevasion.server.Response;
import starvationevasion.server.Server;

public class Handler
{
  private AbstractHandler handler;
  private Worker worker;

  public Handler (Server server, Worker worker)
  {
    this.worker = worker;
    handler = new UserHandler(server, worker).setSuccessor(
            new VoteHandler(server, worker).setSuccessor(
                    new ChatHandler(server, worker).setSuccessor(
                            new DiscardHandler(server, worker).setSuccessor(
                                    new LoginHandler(server, worker)))));
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
