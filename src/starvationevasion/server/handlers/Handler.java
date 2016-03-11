package starvationevasion.server.handlers;


import starvationevasion.server.Worker;
import starvationevasion.server.Server;
import starvationevasion.server.model.Request;

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
                            new CardHandler(server, worker).setSuccessor(
                                    new LoginHandler(server, worker).setSuccessor(
                                            new AdminTaskHandler(server, worker)
                                    )))));
  }

  public void handle (Request request)
  {
    // request.setFrom(worker.getName());
    handler.handleRequest(request);
  }
}
