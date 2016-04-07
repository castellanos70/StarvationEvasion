package starvationevasion.server.handlers;

import starvationevasion.common.Tuple;
import starvationevasion.server.Worker;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.User;

import java.util.concurrent.BlockingQueue;


public class QueueHandler extends AbstractHandler
{
  public QueueHandler (BlockingQueue<Tuple<User, Request>> server, Worker worker)
  {
    super(server, worker);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {
    request.setWorker(getClient());
    server.add(new Tuple<>(getClient().getUser(), request));

    return true;
  }

}
