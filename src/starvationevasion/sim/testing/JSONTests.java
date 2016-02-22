package starvationevasion.sim.testing;

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.common.*;

/**
 * Created by James on 2/18/2016.
 */
public class JSONTests
{
  static void main()
  {
    //    //Adding a change
    //    WorldData testData = new WorldData();
    //    testData.year = 1984;
    //    testData.seaLevel = .7042;
    //
    //    MapPoint tokyo = new MapPoint(35.6833, 139.6833);
    //    MapPoint seoul = new MapPoint(37.5667, 126.9667);
    //    MapPoint pongyang = new MapPoint(39.0194, 125.7381);
    //
    //    JSONDocument tokyoJSON = tokyo.toJSON();
    //    JSONDocument seoulJSON = seoul.toJSON();
    //    JSONDocument pongyangJSON = pongyang.toJSON();
    //
    //    MapPoint tokyoCopy = new MapPoint(tokyoJSON);
    //    MapPoint seoulCopy = new MapPoint(seoulJSON);
    //    MapPoint pongyangCopy = new MapPoint(pongyangJSON);
    //
    //    assert(tokyoCopy == tokyo);
    //    assert(seoulCopy == seoul);
    //    assert(pongyangCopy == pongyang);
    //
    //    SpecialEventData first = new SpecialEventData("Oceania attacks");
    //    first.setDollarsInDamage(1420);
    //    first.setDurationInMonths(15);
    //    first.setLatitude(33.8650f);
    //    first.setLongitude(151.2094f);
    //    first.setSeverity(85.72f);
    //    first.setType(SpecialEventData.EnumSpecialEvent.War);
    //    first.setYear(1983);
    //    first.locationList.add(tokyo);
    //    first.addRegion(randomRegion());
    //    first.addRegion(randomRegion());
    //    first.addRegion(randomRegion());
    //
    //    JSONDocument firstJSON = first.toJSON();
    //    SpecialEventData firstCopy = new SpecialEventData(firstJSON);
    //    assert(firstCopy == first);
    //
    //    SpecialEventData second = new SpecialEventData("Eurasia attacks");
    //    second.setDollarsInDamage(1340);
    //    second.setDurationInMonths(16);
    //    second.setLatitude(22.2783f);
    //    second.setLongitude(114.1747f);
    //    second.setSeverity(92.63f);
    //    second.setType(SpecialEventData.EnumSpecialEvent.Blight);
    //    second.setYear(1982);
    //    second.locationList.add(pongyang);
    //    second.locationList.add(seoul);
    //    second.locationList.add(tokyo);
    //    first.addRegion(randomRegion());
    //    first.addRegion(randomRegion());
    //    first.addRegion(randomRegion());
    //    first.addRegion(randomRegion());
    //
    //    JSONDocument secondJSON = first.toJSON();
    //    SpecialEventData secondCopy = new SpecialEventData(secondJSON);
    //    assert(secondCopy == second);
    //
    //    testData.eventList.add(first);
    //    testData.eventList.add(second);
    //    for(int i = 0; i < EnumFood.SIZE; i++)
    //      testData.foodPrice[i] = Math.random()*200;
    //
    //    for(int i = 0; i < EnumRegion.SIZE; i++)
    //    {
    //      RegionData temp = testData.regionData[i];
    //      temp.revenueBalance = (i+1)*100;
    //      temp.population = (i+1)*1000;
    //      temp.undernourished = Math.random()*temp.population;
    //      temp.humanDevelopmentIndex = Math.random()*100;
    //      temp.ethanolProducerTaxCredit = (int) Math.random()*100;
    //      for(int k = 0; k < EnumFood.SIZE; k++)
    //      {
    //        temp.foodProduced[k] = (int) Math.random() * 100;
    //        temp.foodExported[k] = (int) Math.random() * 100;
    //        temp.foodIncome[k] = (int) (temp.foodProduced[k]*testData.foodPrice[k]);
    //        temp.farmArea[k] = (int) Math.random() * 100;
    //      }
    //      testData.regionData[i] = temp;
    //
    //      JSONDocument regionJSON = temp.toJSON();
    //      RegionData regionCopy = new RegionData(regionJSON);
    //      assert(regionCopy == temp);
    //    }
    //
    //    JSONDocument testJSON = testData.toJSON();
    //    WorldData testCopy = new WorldData(testJSON);
    //    assert(testData == testCopy);
    //  }
    //
    //  private static EnumRegion randomRegion()
    //  {
    //    int ordinal = (int)Math.floor(Math.random()*EnumRegion.SIZE);
    //    return EnumRegion.values()[ordinal];
    //  }
  }
}
