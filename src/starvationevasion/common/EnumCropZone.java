package starvationevasion.common;

public enum EnumCropZone
{
  POOR
  {
    public double productionRate() {return 0.20;}
  },
  ACCEPTABLE
  {
    public double productionRate() {return 0.60;}
  },
  GOOD
  {
    public double productionRate() {return 0.80;}
  },
  IDEAL
  {
    public double productionRate() {return 1.0;}
  };


 public static final int SIZE = values().length;
 public abstract double productionRate();
}
