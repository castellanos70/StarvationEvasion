package starvationevasion.testing;

import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;


/**
 * Test set for localized strings.
 * Created by peter on 11/16/2015.
 */

public class LocalizationTests {
    public static void testDefaultLocale()
    {
        for (EnumFood food : EnumFood.values())
        {
            System.out.println(food.toString() + " : " + food.toLongString());
        }

        for (EnumRegion region : EnumRegion.values())
        {
            System.out.println(region.toString());
        }
    }

    public static void main(String[] args)
    {
        testDefaultLocale();
    }
}
