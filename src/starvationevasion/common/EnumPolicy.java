package starvationevasion.common;

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.model.Sendable;

public enum EnumPolicy implements Sendable
{
  Clean_River_Incentive,
  Covert_Intelligence,
  Educate_the_Women_Campaign,
  Efficient_Irrigation_Incentive,
  Ethanol_Tax_Credit_Change,
  Fertilizer_Subsidy,
  Foreign_Aid_for_Farm_Infrastructure,
  GMO_Seed_Insect_Resistance_Research,
  International_Food_Relief_Program,
  Loan,
  MyPlate_Promotion_Campaign;

  @Override
  public JSONDocument toJSON ()
  {
    return null;
  }

  @Override
  public void fromJSON (Object doc)
  {

  }

  @Override
  public String getType ()
  {
    return null;
  }

  @Override
  public void setType (String type)
  {

  }
  public static final int SIZE = values().length;
}
