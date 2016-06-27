package starvationevasion.ai.commands;

import starvationevasion.common.PolicyCard;

public class AIVotingPhase 
{
	private String cost;
	private String benefit;


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
