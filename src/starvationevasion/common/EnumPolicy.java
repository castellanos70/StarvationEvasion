package starvationevasion.common;

import com.oracle.javafx.jmx.json.JSONDocument;

import starvationevasion.server.model.Sendable;
import starvationevasion.server.model.Type;
import starvationevasion.sim.io.CSVReader;


public enum EnumPolicy implements Sendable
{
  CleanRiverIncentive,
  Cooperation,
  CovertIntelligence,
  DivertFunds,
  EducateTheWomenCampaign,
  EfficientIrrigationIncentive,
  FarmInfrastructureAid,
  FertilizerAid,
  FertilizerSubsidy,
  Filibuster,
  FoodAid,
  Fundraiser,
  Loan,
  MyPlatePromotionCampaign,
  ResearchInsectResistance,
  SpecialInterests;

  private static final String PATH_POLICY_CARD_DATA = "/PolicyCardData.csv";

  public static final int SIZE = values().length;
  private static final String[] title = new String[SIZE];
  private static final String[] ruleText = new String[SIZE];
  private static final int[] votesRequired = new int[SIZE];
  private static final int[] actionPointCost = new int[SIZE];
  private static final EnumPolicyDuration[] duration = new EnumPolicyDuration[SIZE];
  private static final EnumTargetRegionOptions[] optionsRegions = new EnumTargetRegionOptions[SIZE];
  private static final EnumTargetFoodOptions[] optionsFood = new EnumTargetFoodOptions[SIZE];
  private static final int[][] optionsX = new int[SIZE][];
  private static final GameState[] playableState = new GameState[SIZE];
  private static final String[] flavorText = new String[SIZE];
  private static final String[] flavorTextAuthor = new String[SIZE];


  public String getTitle() {return title[ordinal()];}
  public String getGameText() {return ruleText[ordinal()];}
  public int getVotesRequired() {return votesRequired[ordinal()];}
  public int getActionPointCost() {return actionPointCost[ordinal()];}

  public EnumPolicyDuration getDuration() {return duration[ordinal()];}
  public EnumRegion[] getOptionsRegions(EnumRegion owner)
  {
    EnumTargetRegionOptions options = optionsRegions[ordinal()];
    if (options == EnumTargetRegionOptions.ANY) return EnumRegion.values();
    if (options == EnumTargetRegionOptions.ANY_PLAYER) return EnumRegion.US_REGIONS;
    if (options == EnumTargetRegionOptions.ANY_WORLD) return EnumRegion.WORLD_REGIONS;
    if (options == EnumTargetRegionOptions.OWNER) return new EnumRegion[] {owner};
    if (options == EnumTargetRegionOptions.NEIGHBOR) return getNeighborPlayerRegions(owner);
    return null;
  }
  public EnumFood[] getOptionsFood()
  {
    EnumTargetFoodOptions options = optionsFood[ordinal()];
    if (options == EnumTargetFoodOptions.ANY) return EnumFood.values();
    if (options == EnumTargetFoodOptions.ANY_CROP) return EnumFood.CROP_FOODS;
    if (options == EnumTargetFoodOptions.ANY_LIVESTOCK) return EnumFood.NON_CROP_FOODS;
    return null;
  }


  public int[] getOptionsX()  {return optionsX[ordinal()];}
  public GameState getPlayableState() {return playableState[ordinal()];}
  public String getFlavorText()  {return flavorText[ordinal()];}
  public String getFlavorTextAuthor()  {return flavorTextAuthor[ordinal()];}

  public String getImagePath()
  {
    return "cardImages/" + name() + ".png";
  }


  public static EnumRegion[] getNeighborPlayerRegions(EnumRegion player)
  {
    if (player == EnumRegion.USA_CALIFORNIA)
    { return new EnumRegion[] {EnumRegion.USA_MOUNTAIN};
    }
    else if (player == EnumRegion.USA_HEARTLAND)
    { return new EnumRegion[] {EnumRegion.USA_NORTHERN_CRESCENT, EnumRegion.USA_NORTHERN_PLAINS, EnumRegion.USA_SOUTHERN_PLAINS, EnumRegion.USA_SOUTHEAST};
    }
    else if (player == EnumRegion.USA_MOUNTAIN)
    { return new EnumRegion[] {EnumRegion.USA_CALIFORNIA, EnumRegion.USA_NORTHERN_PLAINS, EnumRegion.USA_SOUTHERN_PLAINS};
    }
    else if (player == EnumRegion.USA_NORTHERN_CRESCENT)
    { return new EnumRegion[] {EnumRegion.USA_HEARTLAND, EnumRegion.USA_NORTHERN_PLAINS, EnumRegion.USA_SOUTHEAST};
    }
    else if (player == EnumRegion.USA_NORTHERN_PLAINS)
    { return new EnumRegion[] {EnumRegion.USA_MOUNTAIN, EnumRegion.USA_NORTHERN_CRESCENT, EnumRegion.USA_HEARTLAND, EnumRegion.USA_SOUTHERN_PLAINS};
    }
    else if (player == EnumRegion.USA_SOUTHEAST)
    { return new EnumRegion[] {EnumRegion.USA_SOUTHERN_PLAINS, EnumRegion.USA_NORTHERN_CRESCENT, EnumRegion.USA_HEARTLAND};
    }
    else if (player == EnumRegion.USA_SOUTHERN_PLAINS)
    { return new EnumRegion[] {EnumRegion.USA_MOUNTAIN, EnumRegion.USA_HEARTLAND, EnumRegion.USA_SOUTHEAST, EnumRegion.USA_NORTHERN_PLAINS};
    }
    else { return null;}
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
    return Type.POLICY_ENUM;
  }

  public enum EnumTargetFoodOptions
  { ANY, ANY_CROP, ANY_LIVESTOCK
  }

  public enum EnumTargetRegionOptions
  { ANY, ANY_PLAYER, ANY_WORLD, NEIGHBOR, OWNER
  }

  public enum EnumPolicyDuration
  {
    INSTANT, TURNS_1, TURNS_5
  }

  /**
   * Should be called at the start of both the simulator and client processes to read
   * all cards into the enum.
   */
  public static void load()
  {
    CSVReader fileReader = new CSVReader(PATH_POLICY_CARD_DATA, 1);

    String[] fieldList;
    // read until end of file is found

    while ((fieldList = fileReader.readRecord()) != null)
    {
      //System.out.println("EnumPolicy(): record="+fieldList[0]+", "+fieldList[2]+", len="+fieldList.length);
      EnumPolicy enumPolicy = valueOf(fieldList[0]);
      if (enumPolicy == null)
      {
        System.out.println("starvationevasion.common.EnumPolicy(): Error: Policy Not found: policy="+fieldList[0]);
        System.exit(1);
      }
      int idx = enumPolicy.ordinal();
      // 3           4              5                6                7             8              9          10           11
      //Duration	Votes Required	Action Point Cost	Option Regions	Option Foods	Option X	Playable States	FlavorText	FlavorTextAuthor	Comments

      title[idx]    = fieldList[1];
      ruleText[idx] = fieldList[2];

      //System.out.println("fieldList[4]="+fieldList[4]);
      votesRequired[idx] = Integer.parseInt(fieldList[4]);
      actionPointCost[idx] = Integer.parseInt(fieldList[5]);


      duration[idx] = EnumPolicyDuration.valueOf(fieldList[3]);
      if (!fieldList[6].equals("null")) optionsRegions[idx] = EnumTargetRegionOptions.valueOf(fieldList[6]);
      if (!fieldList[7].equals("null")) optionsFood[idx]    = EnumTargetFoodOptions.valueOf(fieldList[7]);
      if (!fieldList[9].equals("null")) playableState[idx] = GameState.valueOf(fieldList[9]);
      flavorText[idx] = fieldList[10];
      flavorTextAuthor[idx] = fieldList[11];

      if (!fieldList[8].equals("null"))
      { String[] optionXStr = fieldList[8].split(" ");
        optionsX[idx] = new int[optionXStr.length];
        for(int i=0; i<optionXStr.length; i++)
        {
          optionsX[idx][i] = Integer.parseInt(optionXStr[i]);
        }
      }
    }
    fileReader.close();
  }
}
