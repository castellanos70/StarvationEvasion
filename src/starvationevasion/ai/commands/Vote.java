package starvationevasion.ai.commands;


import starvationevasion.ai.AI;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Payload;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.State;

public class Vote extends AbstractCommand
{

  public Vote (AI client)
  {
    super(client);
  }

  @Override
  public boolean run ()
  {
    if (getClient().getState().equals(State.VOTING))
    {
      System.out.println("vote");
      Request request = new Request(getClient().getStartNanoSec(), Endpoint.VOTE_UP);
      Payload payload = new Payload();
      if (getClient().getBallot().size() >0)
      {
        getClient().getBallot().get(0);

        payload.putData(getClient().getBallot().get(0));
        request.setData(payload);

        getClient().send(request);
      }
      return false;
    }

    return false;
  }
}
