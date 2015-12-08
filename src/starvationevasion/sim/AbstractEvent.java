package starvationevasion.sim;


import starvationevasion.common.EnumSpecialEvent;
import starvationevasion.common.MapPoint;

import java.awt.*;


/**
 * Defines a special event. Takes a land area type and applies effects to the land region based on event.
 *
 * T is the type of area the event effects (territory, region, etc...)
 */
public abstract class AbstractEvent<T extends AbstractTerritory>
{
  private EnumSpecialEvent eventType;
  private int duration;
  T landArea;

  /**
   * Creates a special event object
   *
   * @param eventType Type of event this will be
   * @param landArea The land area (Territory, Region, etc...) this event effects
   * @param duration How many simulator years this event lasts
   */
  public AbstractEvent(EnumSpecialEvent eventType, T landArea, int duration)
  {
    this.eventType = eventType;
    this.landArea = landArea;
    this.duration = duration;
  }

  /**
   * Applies effects of the event to the to the landArea. This assumes the event lasts one year (due to the
   * resolution of the simulator). When the effect is applied then the duration is decreased by 1 year.
   */
  public void applyEffects()
  {
    duration -= 1;
  }

  /**
   * Return the longitue and latitude location of the event. This will usually be the center of the land area.
   *
   * @return latitude and longitude stored in a MapPoint.
   */
  public abstract MapPoint getLocation();

  public EnumSpecialEvent getEventType()
  {
    return eventType;
  }

  public int getDuration()
  {
    return duration;
  }

  public T getLandArea()
  {
    return landArea;
  }
}
