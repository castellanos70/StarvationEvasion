package starvationevasion.server;

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;

import java.util.ArrayList;
import java.util.List;

public class User
{
  String username;
  String password;
  EnumRegion region;

  ArrayList<EnumPolicy> hand = new ArrayList<>();


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
    //Not sure we want to be carrying the password around like this
    json.setString("password", password);
    json.setNumber("region", region.ordinal());

    JSONDocument jHandArray = JSONDocument.createArray();
    for(int i = 0; i < hand.size(); i++)
      jHandArray.setNumber(i, hand.get(i).ordinal());
    json.set("hand", jHandArray);


    //TODO Make clear JSON arrays work
    //TODO Make all Enum converts match

    return json;
  }

  public User (JSONDocument json)
  {
    username = json.getString("username");
    password = json.getString("password");
    region = EnumRegion.values()[(int) json.getNumber("region")];

    // .array() should be giving a list of the JSON Numbers set in toJSON
    List<Object> jHandParse = json.get("hand").array();
    for(int i = 0; i < jHandParse.size(); i++)
      hand.add(EnumPolicy.values()[(int) jHandParse.get(i)]);
  }

}
