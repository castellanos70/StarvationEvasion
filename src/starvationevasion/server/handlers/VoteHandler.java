package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.PolicyCard;
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
    boolean isPresent = false;
    int i;
    PolicyCard card = (PolicyCard) request.getPayload().getData();
//    PolicyCard[] cards = server.getDraftedPolicyCards()[card.getOwner().ordinal()];
//    for (i = 0; i < cards.length; i++)
//    {
//      if (cards[i].equals(card))
//      {
//        isPresent = true;
//        break;
//      }
//    }

//    if (!isPresent)
//    {
//      getClient().send(ResponseFactory.build(server.uptime(), card, Type.VOTE_ERROR, "Not present"));
//      return false;
//    }

    if (request.getDestination().equals(Endpoint.VOTE_UP))
    {
      if (!card.isEligibleToVote(getClient().getUser().getRegion()))
      {
        System.out.println("Not eligible");
        // send to client.
        getClient().send(ResponseFactory.build(server.uptime(), card, Type.VOTE_ERROR, "Not Eligible"));
        return true;
      }

      final Boolean status = server.addVote(card, getClient().getUser().getRegion());

      if (status)
      {
        getClient().send(ResponseFactory.build(server.uptime(), card, Type.VOTE_SUCCESS));
      }
      else
      {
        // throw new VoteException("There was an error voting");
        System.out.println("Vote failed");
      }

      return true;
    }
    else if (request.getDestination().equals(Endpoint.VOTE_DOWN))
    {
      if (!card.isEligibleToVote(getClient().getUser().getRegion()))
      {
        getClient().send(ResponseFactory.build(server.uptime(), card, Type.VOTE_ERROR, "Not Eligible"));
        return true;
      }
      getClient().send(ResponseFactory.build(server.uptime(), card, Type.VOTE_SUCCESS));
    }

    return true;
  }
}
