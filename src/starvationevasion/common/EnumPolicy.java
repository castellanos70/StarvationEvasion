package starvationevasion.common;
import java.util.ResourceBundle;

/**
 * Policy Cards game text, draft affects, votes required and model effects.
 */
public enum EnumPolicy
{
  /**
   * Title: {@value #TITLE_GMO_Seed_Insect_Resistance_Research}<br><br>
   * Game Text: {@value #TEXT_GMO_Seed_Insect_Resistance_Research}<br><br>
   *
   * Draft Affects: When drafting this policy, player selects a crop and an
   * amount X to be paid by EACH player who approves the policy. <br><br>
   *
   * Votes Required: 1 through all<br>
   * Eligible Regions: All U.S.<br><br>
   *
   * Model Effects: 5% bonus to effectiveness of total dollars spent per
   * participating region.<br><br>
   *
   * Starting 10 years after research, permanent reduction in kg/km<sup>2</sup>
   * of pesticides for target crop. All player and non-player regions in world
   * who permit GMO in their region receive this benefit.<br>
   * Benefit yield is an ease-in-out cubic bezier function of effective
   * dollars spent and target crop.
   */

  GMO_Seed_Insect_Resistance_Research
    { public int votesRequired() {return 1;}
      public boolean voteWaitForAll() {return true;}
      public String getTitle(){ return TITLE_GMO_Seed_Insect_Resistance_Research;}
      public String getGameText(){ return TEXT_GMO_Seed_Insect_Resistance_Research;}
  },


  /**
   * Title: {@value #TITLE_International_Food_Relief_Program}<br><br>
   * Game Text: {@value #TEXT_International_Food_Relief_Program}<br><br>
   *
   * Draft Affects: When drafting this policy, player
   * selects an amount X to be paid by EACH player who approves the policy. <br><br>
   *
   * Votes Required: 1 through all<br>
   * Eligible Regions: All U.S.<br><br>
   *
   * Model Effects: Commodity food is distributed to relief world hunger
   * in the most efficient manor discovered by the sim where
   * efficiency calculation is based on the type of commodity
   * available in each participating region, each
   * country's nutritional need and each country's import penalty function.
   * Note: depending on yields, different regions may contribute different
   *    mixes of commodities.<br><br>
   *
   * 10% bonus to effectiveness of total dollars spent per participating region.<br><br>
   *
   * Food purchased for relief inflates the global sell price of the food type by a
   * direct reduction of supply without effect on demand (since those to whom the
   * relief is delivered are presumed to lack the resources to have been part of the demand).
   */

  International_Food_Relief_Program
    { public int votesRequired() {return 1;}
      public boolean voteWaitForAll() {return true;}
      public String getTitle(){ return TITLE_International_Food_Relief_Program;}
      public String getGameText(){ return TEXT_International_Food_Relief_Program;}
    },


  /**
   * Title: {@value #TITLE_Efficient_Irrigation_Incentive}<br><br>
   * Game Text: {@value #TEXT_Efficient_Irrigation_Incentive}<br><br>
   *
   * Draft Affects: When drafting this policy, player selects a percentage X [1% through 100%].<br><br>
   *
   * Votes Required: Automatic<br>
   *
   * Model Effects: The sim estimates the number and location of farms within
   * the region and the amount spent by those farms on improved irrigation. The
   * model will need a current distribution of irrigation efficiency levels, the
   * four control points of an ease-in-out cubic Bezier function giving efficiency
   * increase as a function of dollars spent and a distribution of current water
   * sources being used by farms.<br><br>
   * Depending on the region and the area within the region, the effects of lower
   * water usage might include a permanent (through endgame) reduction of farming
   * costs, aquifer depletion, and/or river flow depletion. Increased irrigation
   * efficiency also reduces pesticides, fertilizer, and turbidity levels in
   * outflowing rivers.<br><br>
   *
   * X% of the money that the sim estimates is spent for improved irrigation
   * is deducted from the regions tax revenue at the start of the next turn.
   */

  Efficient_Irrigation_Incentive
    { public int votesRequired() {return 0;}
      public boolean voteWaitForAll() {return false;}
      public String getTitle(){ return TITLE_Efficient_Irrigation_Incentive;}
      public String getGameText(){ return TEXT_Efficient_Irrigation_Incentive;}
    }
  ,


  /**
   * Title: {@value #TITLE_Foreign_Aid_for_Farm_Infrastructure}<br><br>
   * Game Text: {@value #TEXT_Foreign_Aid_for_Farm_Infrastructure}<br><br>
   *
   * Draft Affects: When drafting this policy, player selects target world
   * region and X million dollars.<br><br>
   *
   * Votes Required: 4<br>
   * Eligible Regions: All US<br><br>
   *
   * Model Effects: model needs four control points of each ease-in-out cubic Bezier
   * function giving investment verses food trade penalty function reduction. This one
   * time spending permanently reduces the regions penalty function.
   * If approved, each US region must pay X million.
   <br><br>
   */

  Foreign_Aid_for_Farm_Infrastructure()
    { public int votesRequired() {return 4;}
      public boolean voteWaitForAll() {return false;}
      public String getTitle(){ return TITLE_Foreign_Aid_for_Farm_Infrastructure;}
      public String getGameText(){ return TEXT_Foreign_Aid_for_Farm_Infrastructure;}
    }  ,


  /**
   * Title: {@value #TITLE_Covert_Intelligence}<br><br>
   * Game Text: {@value #TEXT_Covert_Intelligence}<br><br>
   *
   * Votes Required: Automatic<br><br>
   *
   * Model Effects: The fact that such cards as this exist, implies the draw pile must
   * be set at the start of the game rather than cards being randomly picked at draw
   * time.<br><br>
   */

  Covert_Intelligence
    { public int votesRequired() {return 0;}
      public boolean voteWaitForAll() {return false;}
      public String getTitle(){ return TITLE_Covert_Intelligence;}
      public String getGameText(){ return TEXT_Covert_Intelligence;}
    } ,


  /**
   * Title: {@value #TITLE_Clean_River_Incentive}<br><br>
   * Game Text: {@value #TEXT_Clean_River_Incentive}<br><br>
   *
   * Draft Affects: When drafting this policy, player selects a percentage Y
   * [1% through 100%] and Z% [1% through 100%].<br><br>
   *
   * Votes Required: Automatic<br><br>
   *
   * Model Effects: The sim estimates the number and location of farms
   * within the region and the amount spent by those farms on improved irrigation.
   * The model will need a current amount of pesticides and fertilizers in use
   * per square km, the current percentage that goes into rivers, and, the four
   * control points of each ease-in-out cubic Bezier function giving pesticide
   * and fertilizer use verses yield for each of the 12 farm products and
   * pesticide and fertilizer use verses cost of alternative methods including
   * more expensive seeds,  natural pest controls, and landscape for reduced run-off.<br><br>
   * The Y% tax break is applied to the regions tax revenue on the next turn.
   <br><br>
   */

  Clean_River_Incentive
    { public int votesRequired() {return 0;}
      public boolean voteWaitForAll() {return false;}
      public String getTitle(){ return TITLE_Clean_River_Incentive;}
      public String getGameText(){ return TEXT_Clean_River_Incentive;}
    }  ,


  /**
   * Title: {@value #TITLE_MyPlate_Promotion_Campaign}<br><br>
   * Game Text: {@value #TEXT_MyPlate_Promotion_Campaign}<br><br>
   *
   * Draft Affects: When drafting this policy, player selects X million dollars.<br><br>
   *
   * Votes Required: Automatic <br><br>
   *
   * Model Effects: Model uses four control points of an ease-in-out cubic Bezier
   * function giving shift in food preference demand verses advertising dollars
   * spent. The effect is largest in the region running the campaign, but also
   * effects world regions in direct proportion to that regions import levels of the
   * effected food categories.<br><br>
   */

  MyPlate_Promotion_Campaign
    { public int votesRequired() {return 0;}
      public boolean voteWaitForAll() {return false;}
      public String getTitle(){ return TITLE_MyPlate_Promotion_Campaign;}
      public String getGameText(){ return TEXT_MyPlate_Promotion_Campaign;}
    }  ,


  /**
   * Title: {@value #TITLE_Ethanol_Production_Tax_Credit_Change}<br><br>
   * Game Text: {@value #TEXT_Ethanol_Production_Tax_Credit_Change}<br><br>
   *
   * Draft Affects: When drafting this policy, the model must inform
   * the player of X and require the player selects Y.<br><br>
   *
   * Votes Required: Automatic <br><br>
   *
   * Model Effects: <br><br>
   */

  Ethanol_Production_Tax_Credit_Change
    { public int votesRequired() {return 0;}
      public boolean voteWaitForAll() {return false;}
      public String getTitle(){ return TITLE_Ethanol_Production_Tax_Credit_Change;}
      public String getGameText(){ return TEXT_Ethanol_Production_Tax_Credit_Change;}
    }  ,


  /**
   * Title: {@value #TITLE_Fertilizer_Subsidy}<br><br>
   * Game Text: {@value #TEXT_Fertilizer_Subsidy}<br><br>
   *
   * Draft Affects: When drafting this policy, player selects target food product and percentage Y.<br><br>
   *
   * Votes Required: Automatic<br><br>
   *
   * Model Effects: This policy makes conventional farming of target crop more
   * economic and therefore shifts farmer plantings. It also affects the use of
   * the fertilizer on existing crops causing a change in yield and a change in
   * fertilizer run off.<br><br>
   */

  Fertilizer_Subsidy
    { public int votesRequired() {return 0;}
      public boolean voteWaitForAll() {return false;}
      public String getTitle(){ return TITLE_Fertilizer_Subsidy;}
      public String getGameText(){ return TEXT_Fertilizer_Subsidy;}
    }  ,


  /**
   * Title: {@value #TITLE_Educate_the_Women_Campaign}<br><br>
   * Game Text: {@value #TEXT_Educate_the_Women_Campaign}<br><br>
   *
   * Draft Affects: When drafting this policy, player selects target world
   * region and X million dollars.<br><br>
   *
   * Votes Required: <br>
   * Eligible Regions: <br><br>
   *
   * Model Effects:  model needs four control points of each ease-in-out cubic Bezier
   * function giving investment verses food trade penalty function reduction. This one
   * time spending permanently reduces the regions penalty function.<br><br>
   * If approved, each US region must pay X million.
   */

  Educate_the_Women_Campaign
    { public int votesRequired() {return 4;}
      public boolean voteWaitForAll() {return false;}
      public String getTitle(){ return TITLE_Educate_the_Women_Campaign;}
      public String getGameText(){ return TEXT_Educate_the_Women_Campaign;}
    };

  /** Resources for the default locale */
  private final ResourceBundle res = ResourceBundle.getBundle("starvationevasion.common.locales.strings");

  //public final String title;
  //public final String gameText;

  /**
   * @return 0 if the policy is automatic. Otherwise, returns the number of
   * votes required for the policy to be enacted.
   */
  public abstract int votesRequired();

  /**
   * @return true if voting should continue until all eligible players
   * have voted on this policy. Return false if voting should stop as soon as
   * the required number of votes have been reached.
   */
  public abstract boolean voteWaitForAll();


  public static final int SIZE = values().length;

  private static final String TITLE_GMO_Seed_Insect_Resistance_Research =
     "GMO Seed Insect Resistance Research";
  private static final String TEXT_GMO_Seed_Insect_Resistance_Research =
    "Each participating region spends X million dollars to fund GMO seed research " +
        "for increasing insect resistance of target crop.";

  private static final String TITLE_International_Food_Relief_Program =
    "International Food Relief Program";
  private static final String TEXT_International_Food_Relief_Program =
        "Each participating region spends X million dollars to purchase " +
        "their own regions commodity food for relief of world hunger.";

  private static final String TITLE_Efficient_Irrigation_Incentive =
      "Efficient Irrigation Incentive";
  private static final String TEXT_Efficient_Irrigation_Incentive =
        "From now through the start of the next turn, X% of money spent by farmers " +
          "in players region for improved irrigation efficiency is tax deductible.";

  private static final String TITLE_Foreign_Aid_for_Farm_Infrastructure =
      "Foreign Aid for Farm Infrastructure";
  private static final String TEXT_Foreign_Aid_for_Farm_Infrastructure =
        "The US sends 7X million dollars in foreign aid for capital development " +
        "of farming infrastructure of target world region.";

  private static final String TITLE_Covert_Intelligence =
      "Covert Intelligence";
  private static final String TEXT_Covert_Intelligence =
      "You get to covertly examine target player's hand and the top two cards " +
        "of that player's deck. You may target yourself. " +
        "During the voting phase, other players will see that you have " +
        "played this card, but not know its target. Bonus: If you can " +
        "correctly answer a hidden research question, you examine the top seven " +
        "cards of the target player's deck.";

  private static final String TITLE_Clean_River_Incentive =
      "Clean River Incentive";
  private static final String TEXT_Clean_River_Incentive =
        "X% tax break for farmers in my region who reduce by Y% the outflow of "+
        "pesticides and fertilizers from their farms into the rivers.";

  private static final String TITLE_MyPlate_Promotion_Campaign =
      "MyPlate Promotion Campaign";
  private static final String TEXT_MyPlate_Promotion_Campaign =
        "You spend X million dollars on an advertising campaign within your region promoting " +
        "public awareness of the United States Department of Agricultures MyPlate nutrition guide.";

  private static final String TITLE_Ethanol_Production_Tax_Credit_Change =
      "Ethanol Production Tax Credit Change";
  private static final String TEXT_Ethanol_Production_Tax_Credit_Change =
        "Currently an ethanol producer located in my region is entitled " +
          "to a credit of $X per gallon of ethanol produced, including " +
          "cellulosic ethanol. This policy changes that to $Y per gallon.";

  private static final String TITLE_Fertilizer_Subsidy =
      "Fertilizer or Feed Subsidy";
  private static final String TEXT_Fertilizer_Subsidy =
        "This policy offers a subsidy of X% rebate to farmers in your region purchasing " +
          "commercial fertilizer for target crop or feed supplements for target live stock.";

  private static final String TITLE_Educate_the_Women_Campaign =
      "Educate the Women Campaign";
  private static final String TEXT_Educate_the_Women_Campaign =
        "The US sends 7X million dollars to educate woman of the target world " +
          "region in reading, basic business and farming techniques.";



  public abstract String getTitle();
  public abstract String getGameText();
}
