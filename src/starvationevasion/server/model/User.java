package starvationevasion.server.model;

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;
import starvationevasion.server.io.JSON;
import starvationevasion.util.Jsonify;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable, JSON
{
  @Jsonify
  private String username;
  private String password;

  @Jsonify
  private EnumRegion region;
  private boolean isActive = false;

  @Jsonify(type = Jsonify.JsonType.LIST)
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

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument json = new JSONDocument(JSONDocument.Type.OBJECT);

     json.setString("username", username);
    // json.setString("password", password);
    json.setString("region", region.toString());
    JSONDocument _hand = JSONDocument.createArray(this.hand.size());
    for (int i = 0; i < this.hand.size(); i++)
    {
      _hand.setString(i, hand.get(i).toString());
    }

    json.set("hand", _hand);

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

  public boolean isActive ()
  {
    return isActive;
  }

  public void setActive (boolean active)
  {
    isActive = active;
  }

  @Override
  public String toString ()
  {
    return region.toString();
  }
}
