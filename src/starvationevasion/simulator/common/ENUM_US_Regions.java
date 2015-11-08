package starvationevasion.simulator.common;

/**
 * Created by Joel on 11/7/2015.
 */
public class ENUM_US_Regions
{
}
public enum EnumCropType
{
  WHEAT
          {
            public String toString() {return "Wheat";}
          },


  RICE
          {
            public String toString() {return "Rice";}
          },

  CORN
          {
            public String toString() {return "Corn";}
          },

  SOY
          {
            public String toString() {return "Soy";}
          },

  OTHER_CROPS
          {
            public String toString() {return "Other Crops";}
          };

  public static final int SIZE = values().length;
}
