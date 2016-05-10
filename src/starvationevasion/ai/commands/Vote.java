package starvationevasion.ai.commands;

import starvationevasion.ai.AI;
import starvationevasion.ai.AI.WorldFactors;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.Util;
import starvationevasion.common.gamecards.GameCard;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Payload;
import starvationevasion.server.model.State;

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

  public void readCardDetails(GameCard card)
  {
	  switch(card.getCardType())
	  {
		case Policy_Loan:
			cost = "10% interest of $25 million each year";
			benefit = "$25 million each year for 10 years";
			System.out.println("I AM NEW BY YOU: LOAN");
			break;
			
		case Policy_DivertFunds:
			cost = "Discard current hand";
			benefit = "$14 million";
			System.out.println("I AM NEW BY YOU: DIVERT");
			break;
			
		case Policy_CleanRiverIncentive:
			cost = "none";
			benefit = "X% tax break for farmers in my region";
			System.out.println("I AM NEW BY YOU: CLEAN_RIVER");
			break;
			
		case Policy_EducateTheWomenCampaign:
			cost = "US sends a total of $70 million to educate woman of the target world";
			benefit = "Educate woman of the target world " +
					  "region including reading, basic business and farming techniques.";
			System.out.println("I AM NEW BY YOU: EDUCATE_WOMEN");
			break;
			
	      case Policy_EfficientIrrigationIncentive:
	        cost = "none";
	        benefit = "X% of money spent by farmers " +
	        		  "in player\'s region for improved irrigation efficiency is tax deductible";
	        System.out.println("I AM NEW BY YOU: EFFICENT_IRRI");
	        break;
	        
	      case Policy_EthanolTaxCreditChange:
	    	  cost = "none";
		      benefit = "X% tax credit to cost of ethanol production, including " +
		    		    "cellulosic ethanol.";
		      System.out.println("I AM NEW BY YOU: ETHANOL");
	        break;
	        
	      case Policy_FertilizerSubsidy:
	    	  cost = "none";
		      benefit = "20% rebate to farmers in your region purchasing " +
		    		  	"commercial fertilizer or feed supplements for target crop or live stock";
		      System.out.println("I AM NEW BY YOU: FERTILIZER");
	        break;
	        
	      case Policy_FarmInfrastructureSubSaharan:
	    	  cost = "X million dollars";
		      benefit = "Development " +
		    		    "of farming infrastructure to Sub-Saharan Africa.";
		      System.out.println("I AM NEW BY YOU: FARM");
	        break;
	        
	      case Policy_FertilizerAidCentralAsia:
	    	  cost = "X million dollars";
		      benefit = "Fertilizer to CentralAsia.";
		      System.out.println("I AM NEW BY YOU: FERTILIZER_ASIA");
	        break;
	        
	      case Policy_FertilizerAidMiddleAmerica:
	    	  cost = "X million dollars";
		      benefit = "Fertilizer to Middle America.";
		      System.out.println("I AM NEW BY YOU: FERTILIZER_AMERICA");
	        break;
	        
	      case Policy_FertilizerAidOceania:
	    	  cost = "X million dollars";
		      benefit = "Fertilizer to Oceania.";
		      System.out.println("I AM NEW BY YOU: FERTILIZER_OCEANIA");
	        break;
	        
	      case Policy_FertilizerAidSouthAsia:
	    	  cost = "X million dollars";
		      benefit = "Fertilizer to SouthAsia.";
		      System.out.println("I AM NEW BY YOU: FERTILIZER_SOUTHASIA");
	        break;
	        
	      case Policy_FertilizerAidSubSaharan:
	    	  cost = "X million dollars";
		      benefit = "Fertilizer to Sub Saharan Africa.";
	        break;
	        
	      case Policy_ResearchInsectResistanceGrain:
	    	  cost = "X million dollars";
		      benefit = "GMO seed research " +
		    		  	"for increasing insect resistance of a grain crop.";
	        break;
	        
	      case Policy_InternationalFoodRelief:
	    	  cost = "X million dollars";
		      benefit = "Purchase " +
		    		  	"from its local farmers surplus commodity food for redistribution to where it is" +
		    		  	"most needed.";
	        break;
	        
	      case Policy_MyPlatePromotionCampaign:
	    	  cost = "X million dollars";
		      benefit = "Promoting " +
		    		  	"public awareness of the United States Department of Agriculture's MyPlate nutrition guide.";
	        break;
	        
	      case Policy_Fundraiser:
	    	  cost = "none";
		      benefit = "$1 million dollars";
	        break;
	        
	        
	      case Policy_SpecialInterests:
	    	  cost = "none";
		      benefit = "Owner of this card " +
		    		  	"gains $100 million that they may spend " +
		    		  	"only to support policies drafted this turn";
	      	break;
	      case Policy_FoodReliefCentralAsia:
	    	  cost = "5 thousand tons of target food to CentralAsia";
		      benefit = "none";
	        break;
	      case Policy_FoodReliefMiddleAmerica:
	    	  cost = "5 thousand tons of target food to Middle America";
		      benefit = "none";
	        break;
	      case Policy_FoodReliefOceania:
	    	  cost = "5 thousand tons of target food to Oceania";
		      benefit = "none";
	        break;
	      case Policy_FoodReliefSouthAsia:
	    	  cost = "5 thousand tons of target food to SouthAsia";
		      benefit = "none";
	        break;
	      case Policy_FoodReliefSubSaharan:
	    	  cost = "5 thousand tons of target food to Sub Saharan Africa";
		      benefit = "none";
	        break;
		
			
		default:
			cost = "none";
			benefit = "none";
			break;
			
	  }
  }
  
  public boolean checkResources(GameCard card) 
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
  public boolean checkBeneficialToSelf(GameCard card) 
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
