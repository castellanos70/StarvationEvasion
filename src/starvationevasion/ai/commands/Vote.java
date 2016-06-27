package starvationevasion.ai.commands;

import starvationevasion.ai.AI;
import starvationevasion.ai.AI.WorldFactors;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.Util;
import starvationevasion.common.PolicyCard;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Payload;
import starvationevasion.common.GameState;

public class Vote extends AbstractCommand
{
  private int tries = 2;
  public String cost;
  public String benefit;

  public Vote(AI client)
  {
    super(client);
  }

  @Override
  public boolean run()
  {
    if (getClient().getState().equals(GameState.VOTING))
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

        for (PolicyCard card : getClient().getBallot())
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
              
              readCardDetails(card);
              
              if(checkResources(card)){pros++;}
              else{cons++;}
              
              if(checkBeneficialToSelf(card)){pros++;}
              else{cons++;}
              
              if(checkBeneficialToOthers()){pros++;}
              else{cons++;}
              
              Endpoint endpoint;
              if(pros > cons)
              {
                endpoint = Endpoint.VOTE_UP; // Vote yes
                System.out.println("An AI voted Yes");
              }
              
              else if(cons > pros)
              {
                endpoint = Endpoint.VOTE_DOWN; // Vote no
                System.out.println("An AI voted No");
              }
              
              else
              {
                
                if (Util.likeliness(.45f))
                {
                  endpoint = Endpoint.VOTE_UP; // Vote yes
                }
                else
                {
                  endpoint = Endpoint.VOTE_DOWN; // Vote no
                }
                
              }
        	  
              if(getClient().getSupportCards().contains(card)&& Util.likeliness(0.70f))
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

  public void readCardDetails(PolicyCard card)
  {
  }
  
  public boolean checkResources(PolicyCard card)
  {
		// TODO Auto-generated method stub
		if(cost.startsWith("Discard")){return true;}
		if(cost.startsWith("10%"))
		{
			return true;
			
		}
		if(cost.startsWith("none")){return true;}
		if(cost.startsWith("X million"))
		{
			int curBal = (int)getClient().factorMap.get(WorldFactors.REVENUEBALANCE).get(getClient().worldDataSize-1)[0];
			System.out.println("current Ball  = " + curBal);
			
			int xAmount = card.getX();
			
			if(xAmount >= curBal){return false;}
			else{return true;}
		}
		if(cost.startsWith("5 thousand tons"))
		{
			card.getTargetFood();
		}
		
		return false;
		
  }
  public boolean checkBeneficialToSelf(PolicyCard card)
  {
		// TODO Auto-generated method stub
		int moneyInc = 0;
		int educationInc = 0;
		if(benefit.charAt(0)=='$')//You get money
		{
			int startBal = benefit.indexOf("$")+ 1;
			int endBal = benefit.indexOf(" ");
			
			moneyInc = Integer.parseInt(benefit.substring(startBal, endBal));
			
			int curBal = (int)getClient().factorMap.get(WorldFactors.REVENUEBALANCE).get(getClient().worldDataSize-1)[0];
			
			if(curBal >= moneyInc*2){return false;}
			else{return true;}

			
		}
		
		if(benefit.startsWith("X%"))
		{
			//int percentage = card.getX();
			return true;
			
		}
		if(benefit.startsWith("20%")){return true;}
		
		if(benefit.startsWith("X million"))
		{
			int aiBal = card.getX();
			
		}
		
		if(benefit.startsWith("Educate woman"))
		{
			if(card.getTargetRegion().equals(EnumRegion.WORLD_REGIONS))
			{
				double curHdi = (double)getClient().factorMap.get(WorldFactors.HDI)
						.get(getClient().worldDataSize-1)[0];
				if(curHdi<=70){return true;}
				//if()
			}
		}
		
		if(benefit.startsWith("Development")){return true;}
		
		if(benefit.startsWith("none")){return false;}
		
		return false;
		
  }
  
  public boolean checkBeneficialToOthers() 
  {
		// TODO Auto-generated method stub
	  if(benefit.startsWith("Development"))
	  {
		  if (Util.likeliness(.45f)){return true;}
	  }
	  if(benefit.startsWith("Educate woman"))
	  {
		  if (Util.likeliness(.45f)){return true;}
	  }
	  if(cost.startsWith("5 thousand tons"))
	  {
		  if (Util.likeliness(.45f)){return true;}
	  }
	  if(benefit.startsWith("Owner of this card"))
	  {
		  if (Util.likeliness(.45f)){return true;}
	  }
	  
	  if(benefit.startsWith("Fertilizer"))
	  {
		  if (Util.likeliness(.45f)){return true;}
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
