package starvationevasion.ai.commands;


import starvationevasion.ai.AI;
import starvationevasion.common.PolicyCard;
import starvationevasion.common.Util;
import starvationevasion.common.policies.InternationalFoodReliefProgramPolicy;
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



      if (getClient().getBallot().size() >0)
      {

        for (PolicyCard card : getClient().getBallot())
        {

          Endpoint endpoint;
          if (Util.rand.nextBoolean())
          {
            endpoint = Endpoint.VOTE_UP;
          }
          else
          {
            endpoint = Endpoint.VOTE_DOWN;
          }

          Request request = new Request(getClient().getStartNanoSec(), endpoint);

          Payload payload = new Payload();
          payload.putData(card);
          request.setData(payload);
          getClient().send(request);
        }
      }


      return false;
    }
    return false;
  }
}
