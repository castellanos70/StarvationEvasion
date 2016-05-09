package starvationevasion.common.gamecards;

import com.oracle.javafx.jmx.json.JSONDocument;

import starvationevasion.server.model.Sendable;
import starvationevasion.server.model.Type;

public enum EnumPolicy implements Sendable
{
  /* Notice:
   * Commented out cards are cards that are not even remotely working. As in, they require
   * more things in other areas to be implemented (like for cards that have you look through
   * your discard pile, first the ability to look at your discard needs to be implemented in).
   * 
   * If you make the card function, then uncomment it here, as well as both switch() in 
   * GameCard's constructor and actionPointCost(). If you don't, I'll be mildly disappointed.
   */ 
  Policy_CleanRiverIncentive, 
  //Policy_CovertIntelligence, 
  Policy_DivertFunds, 
  Policy_EducateTheWomenCampaign, 
  Policy_EfficientIrrigationIncentive, 
  Policy_EthanolTaxCreditChange, 
  Policy_FarmInfrastructureSubSaharan, 
  Policy_FertilizerAidCentralAsia, 
  Policy_FertilizerAidMiddleAmerica, 
  Policy_FertilizerAidOceania, 
  Policy_FertilizerAidSouthAsia, 
  Policy_FertilizerAidSubSaharan, 
  Policy_FertilizerSubsidy, 
  //Policy_Filibuster,
  Policy_FoodReliefCentralAsia, 
  Policy_FoodReliefMiddleAmerica, 
  Policy_FoodReliefOceania, 
  Policy_FoodReliefSouthAsia,
  Policy_FoodReliefSubSaharan,
  Policy_Fundraiser, 
  Policy_InternationalFoodRelief, 
  Policy_Loan, 
  Policy_MyPlatePromotionCampaign, 
  //Policy_Redraft, 
  Policy_ResearchInsectResistanceGrain, 
  //Policy_SearchforAnswers, 
  //Policy_SharedKnowledge, 
  Policy_SpecialInterests;
  
  public String getImagePath()
  {
    return "cardImages/" + name() + ".png";
  }

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument _json = JSONDocument.createObject();
    _json.setString("name", name());
    return _json;
  }

  @Override
  public void fromJSON (Object doc)
  {

  }

  @Override
  public Type getType ()
  {
    return Type.POLICY;
  }

  public static final int SIZE = values().length;
}
