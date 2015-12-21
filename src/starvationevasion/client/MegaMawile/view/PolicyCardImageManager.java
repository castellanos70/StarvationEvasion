package starvationevasion.client.MegaMawile.view;

import javafx.scene.image.Image;
import starvationevasion.common.EnumPolicy;

/**
 * Returns the appropriate card image for the passed card.
 */
public class PolicyCardImageManager
{
  private final Image CleanRiverIncentive = new Image(this.getClass().getResourceAsStream("/starvationevasion/client" +
    "/MegaMawile/view/images/CleanRiverIncentive.png"));
  private final Image CovertIntelligence = new Image(this.getClass().getResourceAsStream("/starvationevasion/client" +
    "/MegaMawile/view/images/CovertIntelligence.png"));
  private final Image EducateTheWomenCampaign = new Image(this.getClass().getResourceAsStream
    ("/starvationevasion/client/MegaMawile/view/images/EducateTheWomenCampaign.png"));
  private final Image EfficientIrrigationIncentive = new Image(this.getClass().getResourceAsStream
    ("/starvationevasion/client/MegaMawile/view/images/EfficientIrrigationIncentive.png"));
  private final Image EthanolTaxCreditChange = new Image(this.getClass().getResourceAsStream
    ("/starvationevasion/client/MegaMawile/view/images/EthanolTaxCreditChange.png"));
  private final Image FertilizerSubsidy = new Image(this.getClass().getResourceAsStream
    ("/starvationevasion/client/MegaMawile/view/images/FertilizerSubsidy.png"));
  private final Image ForeignAidForFarmInfrastructure = new Image(this.getClass().getResourceAsStream
    ("/starvationevasion/client/MegaMawile/view/images/ForeignAidForFarmInfrastructure.png"));
  private final Image GMOSeedInsectResistanceResearch = new Image(this.getClass().getResourceAsStream
    ("/starvationevasion/client/MegaMawile/view/images/GMOSeedInsectResistanceResearch.png"));
  private final Image InternationalFoodReliefProgram = new Image(this.getClass().getResourceAsStream
    ("/starvationevasion/client/MegaMawile/view/images/InternationalFoodReliefProgram.png"));
  private final Image Loan = new Image(this.getClass().getResourceAsStream("/starvationevasion/client/MegaMawile/view/images/Loan.png"));
  private final Image MyPlatePromotionalCampaign = new Image(this.getClass().getResourceAsStream
    ("/starvationevasion/client/MegaMawile/view/images/MyPlatePromotionCampaign.png"));

  /**
   * Returns the PolicyCard image based upon the given EnumPolicy card.
   * @param card
   * @return
   */
  public Image getPolicyCardImage(EnumPolicy card)
  {
    if(card == null) return null;
    switch (card)
    {
      case Clean_River_Incentive:
        return CleanRiverIncentive;
      case Covert_Intelligence:
        return CovertIntelligence;
      case Educate_the_Women_Campaign:
        return EducateTheWomenCampaign;
      case Efficient_Irrigation_Incentive:
        return EfficientIrrigationIncentive;
      case Ethanol_Tax_Credit_Change:
        return EthanolTaxCreditChange;
      case Fertilizer_Subsidy:
        return FertilizerSubsidy;
      case Foreign_Aid_for_Farm_Infrastructure:
        return ForeignAidForFarmInfrastructure;
      case GMO_Seed_Insect_Resistance_Research:
        return GMOSeedInsectResistanceResearch;
      case International_Food_Relief_Program:
        return InternationalFoodReliefProgram;
      case Loan:
        return Loan;
      case MyPlate_Promotion_Campaign:
        return MyPlatePromotionalCampaign;
    }
    return null;
  }
}
