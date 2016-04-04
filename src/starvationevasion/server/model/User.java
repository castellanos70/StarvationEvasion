package starvationevasion.server.model;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

public class User implements Encryptable, Sendable
{
  private String username;
  private String password;
  private String salt;
  private EnumRegion region;
  private boolean isActive = false;
  private ArrayList<EnumPolicy> hand = new ArrayList<>();

  public User()
  {

  }

  public User (String[] raw)
  {
    this();
//    this.put("username", raw[0]);
     username = raw[0];
//    this.put("password", raw[1]);
     password = raw[1];
    if (raw.length == 3)
    {
       region = EnumRegion.valueOf(raw[2]);
//      this.put("region", EnumRegion.valueOf(raw[2]));
    }

//    this.put("hand", new ArrayList<>());
//    ((ArrayList<EnumPolicy>)this.get("hand")).add(EnumPolicy.Clean_River_Incentive);
    hand.add(EnumPolicy.Clean_River_Incentive);

  }

  public User (String username, String password, EnumRegion region, ArrayList<EnumPolicy> hand)
  {
    this();
//    this.put("hand", hand);
     this.username = username;
//    this.put("username", username);
    encrypt(password, null);
     this.region = region;
//    this.put("region", region);
  }

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument json = new JSONDocument(JSONDocument.Type.OBJECT);
    
    json.setString("username", username);
    json.setString("password", password);
    json.setString("region", region.name());
    json.setString("type", getType().name());

//    ArrayList list = (ArrayList) this.get("hand");
    JSONDocument _hand = JSONDocument.createArray(hand.size());
    for (int i = 0; i < hand.size(); i++)
    {
      _hand.set(i, hand.get(i).toJSON());
    }

    json.set("hand", _hand);

    return json;
  }

  @Override
  public void fromJSON (Object doc)
  {

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
    encrypt(password, null);
  }

  public void setEncryptedPassword (String password, String salt)
  {
//    this.put("password", password);
     this.password = password;
//    this.put("salt", salt);
     this.salt = salt;
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

  @Override
  public void encrypt (String pwd, String key)
  {
    // String salt = "";
    if (key == null || key.isEmpty())
    {
      salt = Encryptable.generateKey();
//      this.put("salt", salt);
    }
//    this.put("password", Encryptable.generateHashedPassword(salt, pwd));
     password = Encryptable.generateHashedPassword(salt, pwd);
  }

  @Override
  public <T> T decrypt (String msg, String key)
  {
    throw new NotImplementedException();
  }

  public String getSalt ()
  {
    return salt;
  }

  @Override
  public Type getType ()
  {
    return Type.USER;
  }
}
