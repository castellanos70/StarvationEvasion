package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.PolicyCard;
import starvationevasion.common.Tuple;
import starvationevasion.server.Worker;
import starvationevasion.server.Server;
import starvationevasion.server.model.*;

public class VoteHandler extends AbstractHandler
{
  public VoteHandler (Server server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {
    if (!server.getGameState().equals(State.VOTING))
    {
      return false;
    }

    PolicyCard card = (PolicyCard) request.getPayload().getData();
    boolean isPresent = server.getDraftedPolicyCards().contains(card);
    if (!isPresent)
    {
      getClient().send(ResponseFactory.build(server.uptime(), card, "Not present", Type.VOTE_ERROR));
      return false;
    }

    if (request.getDestination().equals(Endpoint.VOTE_UP))
    {
      if (!card.isEligibleToVote(getClient().getUser().getRegion()))
      {
        // send to client.
        getClient().send(ResponseFactory.build(server.uptime(), card, "Not Eligible", Type.VOTE_ERROR));
        return true;
      }

      for (PolicyCard policyCard : server.getDraftedPolicyCards())
      {
        if (policyCard.equals(card))
        {
          policyCard.addEnactingRegion(getClient().getUser().getRegion());
          getClient().send(ResponseFactory.build(server.uptime(), card, Type.VOTE_SUCCESS));
          return true;
        }
      }
    }
    else if (request.getDestination().equals(Endpoint.VOTE_DOWN))
    {
      if (!card.isEligibleToVote(getClient().getUser().getRegion()))
      {
        getClient().send(ResponseFactory.build(server.uptime(), card, "Not Eligible", Type.VOTE_ERROR));
        return true;
      }
      getClient().send(ResponseFactory.build(server.uptime(), card, Type.VOTE_SUCCESS));
    }

    return true;
  }
}
