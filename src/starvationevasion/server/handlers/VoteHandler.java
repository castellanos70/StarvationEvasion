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
    if (server.getGameState().equals(State.VOTING))
    {
      Payload data = new Payload();
      m_response = new Response("Not eligible to vote.");
      if (request.getDestination().equals(Endpoint.VOTE_UP))
      {
        PolicyCard card = (PolicyCard) request.getPayload().getData();
        if (!card.isEligibleToVote(getClient().getUser().getRegion()))
        {
          // send to client.
          getClient().send(m_response);
        }


      }
      else if (request.getDestination().equals(Endpoint.VOTE_DOWN))
      {
      }

      return true;
    }
    return false;
  }
}
