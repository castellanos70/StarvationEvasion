package starvationevasion.common.gamecards;

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.model.Sendable;
import starvationevasion.server.model.Type;

public enum EnumPolicy implements Sendable
{
  Policy_CleanRiverIncentive,
  Policy_CovertIntelligence,
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
  Policy_InternationalFoodRelief,
  Policy_Loan,
  Policy_MyPlatePromotionCampaign,
  Policy_ResearchInsectResistanceGrain,
  Policy_Filibuster,
  Policy_Fundraiser,
  Policy_DiverttheFunds,
  Policy_SharetheKnowledge;
  

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
