package spring2015code.common;

public enum EnumLandType
{
  CULTIVATED
  {
    public boolean isCultivated() {return true;}
  },
  
  UNCULTIVATED_ARABLE  
  {
    public boolean isCultivated() {return false;}
  },
  
  NONARABLE
  {
    public boolean isCultivated() {return false;}
  };
  
  public static final int SIZE = values().length;
  public abstract boolean isCultivated();
}
