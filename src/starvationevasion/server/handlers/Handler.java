package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.Tuple;
import starvationevasion.server.Worker;
import starvationevasion.server.Server;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.User;

import java.util.concurrent.BlockingQueue;

/**
 * Base Handler that will be instantiated.
 */
public class Handler
{
  private AbstractHandler handler;
  private Worker worker;

  /**
   * Constructor that sets the chain of command succession.
   * @param server server
   * @param worker worker that this handler belongs to.
   */
  public Handler (BlockingQueue<Tuple<User, Request>> server, Worker worker)
  {
    this.worker = worker;
    // building the chain of responsibility
    handler = new UserHandler(server, worker).setSuccessor(
                    new ChatHandler(server, worker).setSuccessor(
                                    new LoginHandler(server, worker).setSuccessor(
                                            new AdminTaskHandler(server, worker).setSuccessor(
                                                    new QueueHandler(server, worker)
                                            )
                                    )));
  }

  /**
   * Send the request through the chain. When a request comes in, it falls though the chain of responsibility.
   * @param request Received request.
   */
  public void handle (Request request)
  {
    // request.setFrom(worker.getName());
    request.setWorker(worker);
    handler.handleRequest(request);
  }
}
