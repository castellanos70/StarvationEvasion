package worldfoodgame.common;

/**
 @author david
 created: 2015-03-27

 description: */
public enum EnumCropZone
{
 IDEAL
   {
    public double productionRate() {return 1.0;}
   },

 ACCEPTABLE
   {
    public double productionRate() {return 0.60;}
   },

 POOR
   {
    public double productionRate() {return 0.25;}
   };

 public static final int SIZE = values().length;
 public abstract double productionRate();
}
