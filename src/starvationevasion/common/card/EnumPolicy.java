package starvationevasion.common.card;

import com.oracle.javafx.jmx.json.JSONDocument;

import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.GameState;
import starvationevasion.server.model.Sendable;
import starvationevasion.server.model.Type;

import java.util.ArrayList;
import java.util.EnumSet;

public enum EnumPolicy implements Sendable
{



  Policy_DivertFunds
  {
    public String getTitle(){ return "Divert Funds";}
    public String getGameText()
    { return "Discard your current hand. Gain $14 million.";
    }
    public String getFlavorText()
    { return "No one can come up with a good idea. Good thing we're paid anyways.";
    }

    public int getActionPointCost() {return 1;}
    public EnumSet<GameState> getUsableStates()
    {
      return EnumSet.of(GameState.DRAFTING);
    }
  },


  Policy_Cooperation
    {
      public String getTitle(){ return "Cooperation";}
      public String getGameText()
      { return "Allow neighboring region to leverage your unused infrastructure granting them " +
        "a 1d10 percent boost to production for each food category through the next turn.";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }


      public EnumSet<EnumRegion> getOptionsRegion(EnumRegion player)
      {
        if (player == EnumRegion.USA_CALIFORNIA)
        { return  EnumSet.of(EnumRegion.USA_MOUNTAIN);
        }
        else if (player == EnumRegion.USA_HEARTLAND)
        { return  EnumSet.of(EnumRegion.USA_NORTHERN_CRESCENT, EnumRegion.USA_NORTHERN_PLAINS, EnumRegion.USA_SOUTHERN_PLAINS, EnumRegion.USA_SOUTHEAST);
        }
        else if (player == EnumRegion.USA_MOUNTAIN)
        { return  EnumSet.of(EnumRegion.USA_CALIFORNIA, EnumRegion.USA_NORTHERN_PLAINS, EnumRegion.USA_SOUTHERN_PLAINS);
        }
        else if (player == EnumRegion.USA_NORTHERN_CRESCENT)
        { return  EnumSet.of(EnumRegion.USA_HEARTLAND, EnumRegion.USA_NORTHERN_PLAINS, EnumRegion.USA_SOUTHEAST);
        }
        else if (player == EnumRegion.USA_NORTHERN_PLAINS)
        { return  EnumSet.of(EnumRegion.USA_MOUNTAIN, EnumRegion.USA_NORTHERN_CRESCENT, EnumRegion.USA_HEARTLAND, EnumRegion.USA_SOUTHERN_PLAINS);
        }
        else if (player == EnumRegion.USA_SOUTHEAST)
        { return  EnumSet.of(EnumRegion.USA_SOUTHERN_PLAINS, EnumRegion.USA_NORTHERN_CRESCENT, EnumRegion.USA_HEARTLAND );
        }
        else if (player == EnumRegion.USA_SOUTHERN_PLAINS)
        { return  EnumSet.of(EnumRegion.USA_MOUNTAIN, EnumRegion.USA_HEARTLAND, EnumRegion.USA_SOUTHEAST, EnumRegion.USA_NORTHERN_PLAINS );
        }
        else { return null;}
      }
    },

  /**
   * Model Effects:  model needs four control points of each ease-in-out cubic Bezier
   * function giving investment verses food trade penalty function reduction. This one
   * time spending permanently reduces the regions penalty function.<br><br>
   * If approved, each US region must pay $10 million.
   */
  Policy_EducateTheWomenCampaign
    {
      public String getTitle(){ return "Educate the Women Campaign";}
      public String getGameText()
      { return "The US sends a total of $70 million to educate woman of the target world " +
        "region including reading, basic business and farming techniques.";;
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }

      public int votesRequired() {return 4;}
    },
  Policy_EfficientIrrigationIncentive
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  Policy_EthanolTaxCreditChange
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  Policy_FarmInfrastructureSubSaharan
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  Policy_FertilizerAidCentralAsia
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  Policy_FertilizerAidMiddleAmerica
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  Policy_FertilizerAidOceania
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  Policy_FertilizerAidSouthAsia
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  Policy_FertilizerAidSubSaharan
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  Policy_FertilizerSubsidy
    {
      public String getTitle(){ return "Divert Funds";}
      public String getGameText()
      { return "Discard your current hand. Gain $14 million.";
      }
      public String getFlavorText()
      { return "No one can come up with a good idea. Good thing we're paid anyways.";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  //Policy_Filibuster,
  Policy_FoodReliefCentralAsia
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  Policy_FoodReliefMiddleAmerica
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  Policy_FoodReliefOceania
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  Policy_FoodReliefSouthAsia
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  Policy_FoodReliefSubSaharan
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  Policy_Fundraiser
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  Policy_InternationalFoodRelief
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  Policy_Loan
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  Policy_MyPlatePromotionCampaign
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  //Policy_Redraft, 
  Policy_ResearchInsectResistanceGrain
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    },
  //Policy_SearchforAnswers, 
  //Policy_SharedKnowledge, 
  //Policy_McLibelII,
  //Policy_TexasAramilloBeefII,
  //Policy_FixColoradoMeat,
  //Policy CaliforniaVendingMachine,
  //Policy_DryPortAgreement,
  //Policy_BiofuelProduction
  //Policy_HealthierFoodInSchools,
  Policy_SpecialInterests
    {
      public String getTitle(){ return "";}
      public String getGameText()
      { return "";
      }
      public String getFlavorText()
      { return "";
      }
      public String getFlavorTextAuthor()
      { return "";
      }

      public int getActionPointCost() {return 1;}
      public EnumSet<GameState> getUsableStates()
      {
        return EnumSet.of(GameState.DRAFTING);
      }
    };







  public String getImagePath()
  {
    return "cardImages/" + name() + ".png";
  }
  public abstract String getTitle();
  public abstract String getGameText();
  public abstract int getActionPointCost();
  public abstract EnumSet<GameState> getUsableStates();
  public abstract int votesRequired();

  public String getFlavorText() {return null;}
  public String getFlavorTextAuthor()  {return null;}

  public EnumSet<EnumRegion> getOptionsRegion(EnumRegion player) {return null;}
  public EnumSet<EnumFood> getOptionsFood() {return null;}
  public ArrayList<Integer> getOptionsMillionDollars() {return null;}
  public ArrayList<Integer> getOptionsPercentage() {return null;}


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
    return Type.POLICY_ENUM;
  }

  public static final int SIZE = values().length;
}
