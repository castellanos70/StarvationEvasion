package starvationevasion.sim;

public enum EnumFarmMethod
{
  CONVENTIONAL
  {
    public String toString() {return "Conventional";}
  },
  
  
  ORGANIC  
  {
    public String toString() {return "Organic";}
  },
  
  GMO  
  {
    public String toString() {return "GMO";}
  };
  
  public static final int SIZE = values().length;
}
