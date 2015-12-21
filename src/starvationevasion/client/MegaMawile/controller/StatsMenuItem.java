package starvationevasion.client.MegaMawile.controller;

import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.control.MenuItem;
import starvationevasion.common.RegionData;


public class StatsMenuItem extends MenuItem
{
  public RegionData data;
  ObservableBooleanValue item;

  public StatsMenuItem(RegionData data)
  {
    super(data.region.toString());
    this.data = data;

  }
}
