package starvationevasion.common;

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.io.JSON;
import starvationevasion.server.model.Sendable;

import java.util.List;

/**
 * This structure contains all data of a particular region that the simulator shares with
 * each client via the Server.
 */

public class RegionData implements Sendable
{
  // removed final... with my reason being that if it is that important maybe we should use a getter....
  public EnumRegion region;

  /**
   * This field is zero for non-player regions.
   * Total player revenue in millions of dollars for the current simulation year.
   * This is the past year's revenue balance plus new taxes earned during the
   * current turn of three years, minus expenses during the current turn of three years.
   */
  public int revenueBalance;


  /**
   * This region's population (in thousands of people) during the current year.
   */
  public int population;


  /**
   * This region's percent of undernourished people at start of the current year.
   */
  public double undernourished;


  /**
   * This region's Human Development Index at the start of the current year.
   */
  public double humanDevelopmentIndex;

  /**
   * This region's production (in metric tons) of each foodType during the past turn (3 years).<br><br>
   * Index by EnumFood.ordinal()
   */
  public long[] foodProduced = new long[EnumFood.SIZE];


  /**
   * This is the farm income in millions of dollars from to the region's
   * production of each foodType during the past turn (3 years).<br><br>
   * This includes farm income for food consumed as well as income from exported
   * foods.<br><br>
   * Note: foodProduced*foodPrice for a given food is the gross income. Gross income will
   * always be greater than this field with is the profet (gross less expenses). Note also
   * that different regions have different efficiency levels depending on the crop, climate,
   * infrastructure, tax breaks, and other factors.<br><br>
   *
   *
   * Index by EnumFood.ordinal()
   */
  public int[] foodIncome = new int[EnumFood.SIZE];


  /**
   * This region's food exported (in metric tons) of each foodType during the current year
   * indexed by EnumFood.ordinal(). Positive indicates export, negative indicates
   * import.<br><br>
   *
   * The region's consumption of for each foodType is:<br>
   * {@link #foodProduced}[i]+@link #foodImported}[i]-{@link #foodExported}[i]
   */
  public long[] foodImported = new long[EnumFood.SIZE];

  /**
   * This region's food exported (in metric tons) of each foodType during the current year
   * indexed by EnumFood.ordinal(). Positive indicates export, negative indicates
   * import.<br><br>
   *
   * The region's consumption of for each foodType is:<br>
   * {@link #foodProduced}[i]+@link #foodImported}[i]-{@link #foodExported}[i]
   */
  public long[] foodExported = new long[EnumFood.SIZE];


  /**
   * This region's current ethanol producer tax credit as an
   * integer percentage from [0 through 100]. This percentage of all profits earned
   * by farmers in this
   * region for sale of farm products used in ethanol production is tax deductible.
   */
  public int ethanolProducerTaxCredit;

  /**
   * This region's total land area (in square kilometers).
   */
  public int landArea;

  /**
   * This region's land area (in square kilometers)
   * being used for farm production of each crop type at the end of the current year.
   */
  public int[] farmArea = new int[EnumFood.SIZE];

  public RegionData (EnumRegion region)
  {
    this.region = region;
  }

  /**
   * @return Data stored in this structure as a formatted String.
   */
  public String toString ()
  {
    String msg = region.toString();
    if (region.isUS())
    {
      msg += "[$" + revenueBalance + "]";
    }

    msg += String.format(": pop=%d(%.1f), HDI=%.2f ", population, undernourished, humanDevelopmentIndex);

    for (EnumFood food : EnumFood.values())
    {
      msg += String.format("[%s:p=%d, i=%d, x=%d]",
                           food,
               foodProduced[food.ordinal()],
               foodImported[food.ordinal()],foodExported[food.ordinal()]);
      if (food != EnumFood.DAIRY)
      {
        msg += ", ";
      }
    }

    return msg;
  }

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument json = new JSONDocument(JSONDocument.Type.OBJECT);
    json.setString("region", region.toString());
    json.setNumber("revenue-balance", revenueBalance);
    json.setNumber("population", population);
    json.setNumber("undernourished", undernourished);
    json.setNumber("human-development-index", humanDevelopmentIndex);
    json.setNumber("ethanol", ethanolProducerTaxCredit);

    JSONDocument _producedArray = JSONDocument.createArray(foodProduced.length);
    for (int i = 0; i < foodProduced.length; i++)
    {
      _producedArray.setNumber(i, foodProduced[i]);
    }
    json.set("food-produced", _producedArray);

    JSONDocument _incomeArray = JSONDocument.createArray(foodIncome.length);
    for (int i = 0; i < foodIncome.length; i++)
    {
      _incomeArray.setNumber(i, foodIncome[i]);
    }
    json.set("food-income", _incomeArray);

    JSONDocument _exportArray = JSONDocument.createArray(foodExported.length);
    for (int i = 0; i < foodExported.length; i++)
    {
      _exportArray.setNumber(i, foodExported[i]);
    }
    json.set("food-exported", _exportArray);


    JSONDocument _farmArray = JSONDocument.createArray(farmArea.length);
    for (int i = 0; i < farmArea.length; i++)
    {
      _farmArray.setNumber(i, farmArea[i]);
    }
    json.set("farmArea", _farmArray);

    return json;
  }

  @Override
  public void fromJSON (Object doc)
  {
    JSONDocument json  = JSON.Parser.toJSON(doc);

    region = EnumRegion.valueOf(json.getString("region"));
    revenueBalance = (int) json.getNumber("revenue-balance");
    population = (int) json.getNumber("population");
    undernourished = (double) json.getNumber("undernourished");
    humanDevelopmentIndex = (double) json.getNumber("human-development-index");
    ethanolProducerTaxCredit = (int) json.getNumber("ethanol");

    List<Object> producedArray = json.get("food-produced").array();
    for (int i = 0; i < producedArray.size(); i++)
    {
      foodProduced[i] = (int) producedArray.get(i);
    }

    List<Object> incomeArray = json.get("food-income").array();
    for (int i = 0; i < incomeArray.size(); i++)
    {
      foodIncome[i] = (int) incomeArray.get(i);
    }

    List<Object> exportArray = json.get("food-exported").array();
    for (int i = 0; i < exportArray.size(); i++)
    {
      foodExported[i] = (int) exportArray.get(i);
    }

    List<Object> farmArray = json.get("farmArea").array();
    for (int i = 0; i < farmArray.size(); i++)
    {
      farmArea[i] = (int) farmArray.get(i);
    }
  }

  @Override
  public boolean equals (Object o)
  {
    if (o == this)
    {
      return true;
    }
    if (!(o instanceof RegionData))
    {
      return false;
    }
    RegionData comp = (RegionData) o;
    if (comp.region.ordinal() != this.region.ordinal())
    {
      return false;
    }
    if (comp.revenueBalance != this.revenueBalance)
    {
      return false;
    }
    if (comp.population != this.population)
    {
      return false;
    }
    if (Double.compare(comp.undernourished, this.undernourished) != 0)
    {
      return false;
    }
    if (Double.compare(comp.humanDevelopmentIndex, this.humanDevelopmentIndex) != 0)
    {
      return false;
    }

    return true;
  }

  @Override
  public void setType (String type)
  {

  }

  @Override
  public String getType ()
  {
    return null;
  }
}
