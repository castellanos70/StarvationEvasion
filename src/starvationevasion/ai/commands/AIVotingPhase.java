package starvationevasion.ai.commands;

import starvationevasion.common.gamecards.GameCard;

public class AIVotingPhase 
{
	private String cost;
	private String benefit;
	public void readCardDetails(GameCard card) 
	{
		// TODO Auto-generated method stub
		/*if(card.getCardType() == EnumPolicy.Policy_Loan)
		{
			System.out.println("I AM NEW BY YOU: FOREIGN");
		}*/
		
		switch(card.getCardType())
		{
		case Policy_Loan:
			cost = "Pay loan amount with 10% interest each year";
			benefit = "$25 million each year for 10 years";
			System.out.println("I AM NEW BY YOU: LOAN");
			break;
			
		case Policy_DivertFunds:
			cost = "Discard current hand";
			benefit = "$14 million";
			System.out.println("I AM NEW BY YOU: DIVERT");
			break;
			
		case Policy_CleanRiverIncentive:
			cost = "";
			benefit = "X% tax break for farmers in my region";
			break;
			
		
			
		default:
			break;
		
		}
		
	}

	public boolean checkResources() 
	{
		// TODO Auto-generated method stub
		if(cost.startsWith("Discard"))
		{
			return true;
		}
		
		return false;
	}
	
	public boolean checkBeneficialToSelf() 
	{
		// TODO Auto-generated method stub
		int moneyInc = 0;
		int educationInc = 0;
		if(benefit.charAt(0)=='$')
		{
			int startBal = benefit.indexOf("$")+ 1;
			int endBal = benefit.indexOf(" ");
			
			moneyInc = Integer.parseInt(benefit.substring(startBal, endBal));
			
		}
		
		if(benefit.startsWith("X%")){}
		
		return false;
	}

	public boolean checkBeneficialToOthers() 
	{
		// TODO Auto-generated method stub
		return false;
	}


}
