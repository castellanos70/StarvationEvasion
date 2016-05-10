package starvationevasion.ai.commands;

import starvationevasion.ai.AI;
import starvationevasion.common.Util;
import starvationevasion.common.gamecards.GameCard;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Payload;
import starvationevasion.server.model.State;

public class Vote extends AbstractCommand
{
  private int tries = 2;
  private AIVotingPhase AIVote = new AIVotingPhase();

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
        	  /**
        	   * Ederin Igharoro
        	   * Instead of having the Voting decision be random for the AI,
        	   * I've created a new Class that checks different phases in 
        	   * making the voting decision.
        	   */
        	  
        	  int pros = 0;
              int cons = 0;
              
//              AIVote.readCardDetails(card);
//              
//              if(AIVote.checkResources()){pros++;}
//              else{cons++;}
//              
//              if(AIVote.checkBeneficialToSelf()){pros++;}
//              else{cons++;}
//              
//              if(AIVote.checkBeneficialToOthers()){pros++;}
//              else{cons++;}
              
              Endpoint endpoint;
              if(pros > cons)
              {
                //endpoint = Endpoint.VOTE_UP; // Vote yes
              }
              
              else if(cons > pros)
              {
                //endpoint = Endpoint.VOTE_DOWN; // Vote no
              }
              
              else
              {
                /*
                if (Util.likeliness(.45f))
                {
                  endpoint = Endpoint.VOTE_UP; // Vote yes
                }
                else
                {
                  endpoint = Endpoint.VOTE_DOWN; // Vote no
                }
                */
              }
        	  
        	  
            if (Util.likeliness(.45f))
            {
              endpoint = Endpoint.VOTE_UP;
            } 
            else
            {
              endpoint = Endpoint.VOTE_DOWN;
            }
            if(getClient().getSupportCards().contains(card)
               && Util.likeliness(0.70f))
            {
              endpoint = Endpoint.VOTE_UP;
              getClient().getSupportCards().remove(card);
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
