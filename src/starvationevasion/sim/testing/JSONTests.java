package starvationevasion.sim.testing;

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.common.*;

/**
 * Created by James on 2/18/2016.
 */
public class JSONTests
{
  public static void main(String[] args)
  {
    //Adding a change
    WorldData testData = new WorldData();
    testData.year = 1984;
    testData.seaLevel = .7042;

    MapPoint tokyo = new MapPoint(35.6833, 139.6833);
    MapPoint seoul = new MapPoint(37.5667, 126.9667);
    MapPoint pongyang = new MapPoint(39.0194, 125.7381);

    JSONDocument tokyoJSON = tokyo.toJSON();
    JSONDocument seoulJSON = seoul.toJSON();
    JSONDocument pongyangJSON = pongyang.toJSON();

    MapPoint tokyoCopy = new MapPoint(tokyoJSON);
    MapPoint seoulCopy = new MapPoint(seoulJSON);
    MapPoint pongyangCopy = new MapPoint(pongyangJSON);

    assert (tokyoCopy.equals(tokyo));
    assert (seoulCopy.equals(seoul));
    assert (pongyangCopy.equals(pongyang));

    SpecialEventData first = new SpecialEventData("Oceania attacks");
    first.setDollarsInDamage(1420);
    first.setDurationInMonths(15);
    first.setLatitude(33.8650f);
    first.setLongitude(151.2094f);
    first.setSeverity(85.72f);
    first.setDollarsInDamage(((long) Math.floor(1000*Math.random())));
    first.setType(SpecialEventData.EnumSpecialEvent.War);
    first.setYear(1983);
    first.setMonth(SpecialEventData.EnumMonth.August);
    first.locationList.add(tokyo);
    first.addRegion(randomRegion());
    first.addRegion(randomRegion());
    first.addRegion(randomRegion());

    JSONDocument firstJSON = first.toJSON();
    SpecialEventData firstCopy = new SpecialEventData(firstJSON);
    boolean firstVal = firstCopy.equals(first);
    System.out.println("first is " + firstVal);
    assert (firstCopy.equals(first));

    SpecialEventData second = new SpecialEventData("Eurasia attacks");
    second.setDollarsInDamage(1340);
    second.setDurationInMonths(16);
    second.setLatitude(22.2783f);
    second.setLongitude(114.1747f);
    second.setSeverity(92.63f);
    first.setDollarsInDamage(((long) Math.floor(2000*Math.random())));
    second.setType(SpecialEventData.EnumSpecialEvent.Blight);
    second.setYear(1982);
    second.setMonth(SpecialEventData.EnumMonth.December);
    second.locationList.add(pongyang);
    second.locationList.add(seoul);
    second.locationList.add(tokyo);
    second.addRegion(randomRegion());
    second.addRegion(randomRegion());
    second.addRegion(randomRegion());
    second.addRegion(randomRegion());

    JSONDocument secondJSON = second.toJSON();
    SpecialEventData secondCopy = new SpecialEventData(secondJSON);
    System.out.println(second.toString());
    System.out.println(secondCopy.toString());
    boolean equality = secondCopy.equals(second);
    System.out.println(equality);
    boolean transitive = second.equals(secondCopy);
    System.out.println(transitive);
    assert(secondCopy.equals(second));

    testData.eventList.add(first);
    testData.eventList.add(second);
    for (int i = 0; i < EnumFood.SIZE; i++)
      testData.foodPrice[i] = Math.random() * 200;

    for (int i = 0; i < EnumRegion.SIZE; i++)
    {
      RegionData temp = testData.regionData[i];
      temp.revenueBalance = (i + 1) * 100;
      temp.population = (i + 1) * 1000;
      temp.undernourished = Math.random() * temp.population;
      temp.humanDevelopmentIndex = Math.random() * 100;
      temp.ethanolProducerTaxCredit = (int) Math.random() * 100;
      for (int k = 0; k < EnumFood.SIZE; k++)
      {
        temp.foodProduced[k] = (int) Math.random() * 100;
        temp.foodExported[k] = (int) Math.random() * 100;
        temp.foodIncome[k] = (int) (temp.foodProduced[k] * testData.foodPrice[k]);
        temp.farmArea[k] = (int) Math.random() * 100;
      }
      testData.regionData[i] = temp;

      JSONDocument regionJSON = temp.toJSON();
      RegionData regionCopy = new RegionData(regionJSON);
      System.out.println(temp.equals(regionCopy));
      System.out.println();
      System.out.println();
      System.out.println();
      assert (regionCopy.equals(temp));
    }
    System.out.println("\nTesting World Data:");
    JSONDocument testJSON = testData.toJSON();
    WorldData testCopy = new WorldData(testJSON);
    assert (testData.equals(testCopy));
  }

  private static EnumRegion randomRegion()
  {
    int ordinal = (int) Math.floor(Math.random() * EnumRegion.SIZE);
    return EnumRegion.values()[ordinal];
  }
}


