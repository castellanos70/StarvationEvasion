package starvationevasion.sim.datamodels;

import starvationevasion.common.EnumRegion;

/**
 * Created by Alfred on 11/15/15.
 *
 * The State class describes a state in the United States. <br></>
 * Each is classified by region, total land, total farm land, and <br></>
 * components to calculate how much land is dedicated to a specific <br></>
 * piece of agriculture.
 *
 */

public class State
{
  /**
   * The states name.
   */
  private String name;

  /**
   * The states region.
   */
  private EnumRegion region;

  /**
   * Class constructor creates a new State Object with <br></>
   * a data string of comma separated values
   * @param data
   */
  public State(String data)
  {

  }
}
