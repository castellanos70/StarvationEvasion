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
  private int tries = 2;

  public Vote (AI client)
  {
    super(client);
  }

  @Override
  public boolean run ()
  {
    if (getClient().getState().equals(State.VOTING))
    {

      if (tries <= 0)
      {
        return false;
      }

      if (getClient().getBallot() == null)
      {
        System.out.println(String.valueOf(getClient().getBallot()));
        tries--;
        return true;
      }


      if (getClient().getBallot().size() > 0)
      {

        for (PolicyCard card : getClient().getBallot())
        {
          if (card.getOwner() != getClient().getUser().getRegion())
          {
            Endpoint endpoint;
            if (Util.randFloat() <= .20f)
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
      }


      return false;
    }
    return false;
  }
}
