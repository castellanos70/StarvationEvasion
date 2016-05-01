package starvationevasion.communication.AITest.commands;

import starvationevasion.common.Util;
import starvationevasion.common.gamecards.GameCard;
import starvationevasion.communication.AITest.AI;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Payload;
import starvationevasion.server.model.State;

public class Vote extends AbstractCommand
{
  private int tries = 2;

  public Vote(AI client)
  {
    super(client);
  }

  @Override
  public boolean run()
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
        System.out.println(
            "Ballot got it. With size: " + getClient().getBallot().size());

        for (GameCard card : getClient().getBallot())
        {

          // if (card.getOwner() != getClient().getUser().getRegion()) //
          // Testing
          if (card.isEligibleToVote(getClient().getUser().getRegion())) // Game
                                                                        // play
          {
            Endpoint endpoint;
            if (Util.likeliness(.45f))
            {
              endpoint = Endpoint.VOTE_UP;
            } else
            {
              endpoint = Endpoint.VOTE_DOWN;
            }

            // Request request = new
            // RequestFactory().build(getClient().getStartNanoSec(), card,
            // endpoint);

            // getClient().send(request);
            getClient().getCommModule().send(endpoint, card, null);

          }
        }
        getClient().getCommModule().send(Endpoint.DONE, new Payload(), null);
        // getClient().send(new
        // RequestFactory().build(getClient().getStartNanoSec(), new Payload(),
        // Endpoint.DONE));
      }

      return false;
    }
    return false;
  }

  @Override
  public String commandString()
  {
    // TODO Auto-generated method stub
    return "Vote";
  }
}
