package starvationevasion.ai.commands;


import starvationevasion.ai.AI;
import starvationevasion.common.PolicyCard;
import starvationevasion.common.Util;
import starvationevasion.common.policies.InternationalFoodReliefProgramPolicy;
import starvationevasion.server.model.*;

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
        System.out.println("Ballot null");
        tries--;
        return true;
      }


      if (getClient().getBallot().size() > 0)
      {
        System.out.println("Ballot got it. With size: " + getClient().getBallot().size());

        for (PolicyCard card : getClient().getBallot())
        {
          if (card.getOwner() != getClient().getUser().getRegion())
          {
            Endpoint endpoint;
            if (Util.likeliness(.20f))
            {
              endpoint = Endpoint.VOTE_UP;
            }
            else
            {
              endpoint = Endpoint.VOTE_DOWN;
            }


            Request request = RequestFactory.build(getClient().getStartNanoSec(), card, endpoint);

            getClient().send(request);
          }
        }
      }


      return false;
    }
    return false;
  }
}
