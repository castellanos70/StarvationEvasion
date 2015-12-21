package starvationevasion.client.MegaMawile.model;

/**
 * Created by c on 11/24/2015.
 */
public enum EnumBallot
{
  YES
  {
    public int getValue()
    {
      return 1;
    }
  },
  NO
  {
    public int getValue()
    {
      return -1;
    }
  },
  ABSTAIN
    {
      public int getValue()
      {
        return 0;
      }
    };

  public abstract int getValue();
}
