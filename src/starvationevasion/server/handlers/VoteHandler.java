package starvationevasion.server.handlers;

import starvationevasion.common.card.AbstractPolicy;
import starvationevasion.server.Connector;
import starvationevasion.server.Server;
import starvationevasion.server.model.*;

public class VoteHandler extends AbstractHandler
{
  public VoteHandler (Server server, Connector client)
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
    AbstractPolicy card = (AbstractPolicy) request.getPayload().getData();
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
//      getClient().send(new ResponseFactory().build(server.uptime(), card, Type.VOTE_ERROR, "Not present"));
//      return false;
//    }

    if (request.getDestination().equals(Endpoint.VOTE_UP))
    {
      if (!card.isEligibleToVote(getClient().getUser().getRegion()))
      {
        System.out.println("Not eligible");
        // send to client.
        getClient().send(new ResponseFactory().build(server.uptime(), card, Type.VOTE_ERROR, "Not Eligible"));
        return true;
      }
      boolean status;
      synchronized(server)
      {
        status = server.addVote(card, getClient().getUser().getRegion());
      }

      if (status)
      {
        getClient().send(new ResponseFactory().build(server.uptime(), card, Type.VOTE_SUCCESS));
        System.out.println("Vote recorded");
      }
      else
      {
        // throw new VoteException("There was an error voting");
        getClient().send(new ResponseFactory().build(server.uptime(), card, Type.VOTE_ERROR, "Try to vote again."));
        //.System.out.println("Vote failed");
      }

      return true;
    }
    else if (request.getDestination().equals(Endpoint.VOTE_DOWN))
    {
      if (!card.isEligibleToVote(getClient().getUser().getRegion()))
      {
        getClient().send(new ResponseFactory().build(server.uptime(), card, Type.VOTE_ERROR, "Not Eligible"));
        return true;
      }
      getClient().send(new ResponseFactory().build(server.uptime(), card, Type.VOTE_SUCCESS));
    }

    return true;
  }
}
