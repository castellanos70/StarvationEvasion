package starvationevasion.server.model;

import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.JSONWriter;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;

import java.util.ArrayList;
import java.util.List;

public class User
{
  private String username;
  private String password;
  private EnumRegion region;

  private ArrayList<EnumPolicy> hand = new ArrayList<>();

  public User (JSONDocument json)
  {
    username = json.getString("username");
    password = json.getString("password");
    json.getString("region");

    hand.add(EnumPolicy.Clean_River_Incentive);
  }

  public User (String[] raw)
  {
    username = raw[0];
    password = raw[1];
    if (raw.length == 3)
    {
      region = EnumRegion.valueOf(raw[2]);
    }

    hand.add(EnumPolicy.Clean_River_Incentive);
  }

  public User (String username, String password, EnumRegion region, ArrayList<EnumPolicy> hand)
  {
    this.hand = hand;
    this.username = username;
    this.password = password;
    this.region = region;
  }

  public JSONDocument toJSON ()
  {
    JSONDocument json = new JSONDocument(JSONDocument.Type.OBJECT);

    json.setString("username", username);
    json.setString("password", password);
    json.setString("region", region.toString());
    JSONDocument hand = JSONDocument.createArray();
    hand.set(0, hand.get(0));

    json.set("hand", hand);

    return json;
  }

  public String getUsername ()
  {
    return username;
  }

  public void setUsername (String username)
  {
    this.username = username;
  }

  public String getPassword ()
  {
    return password;
  }

  public void setPassword (String password)
  {
    this.password = password;
  }

  public EnumRegion getRegion ()
  {
    return region;
  }

  public void setRegion (EnumRegion region)
  {
    this.region = region;
  }

  public ArrayList<EnumPolicy> getHand ()
  {
    return hand;
  }

  public void setHand (ArrayList<EnumPolicy> hand)
  {
    this.hand = hand;
  }

  @Override
  public String toString ()
  {
    return region.toString();
  }
}
