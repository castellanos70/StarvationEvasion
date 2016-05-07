package starvationevasion.server.model;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.EnumPolicy;
import starvationevasion.server.Connector;
import starvationevasion.server.io.NotImplementedException;

import javax.crypto.SecretKey;
import java.beans.Transient;
import java.util.ArrayList;

public class User implements Encryptable, Sendable
{
  public volatile transient int actionPointsRemaining = 3;
  public volatile transient int policyCardsDiscarded = 0;
  public volatile transient int drafts = 0;
  public volatile transient int draftVoteCard = 0;
  public transient boolean isDone = false; // cannot be volatile since

  private transient Connector worker;
  private transient String salt;
  private transient volatile boolean isLoggedIn = false;
  private transient boolean isPlaying = false;

  private String username;
  private transient String password;
  private EnumRegion region;
  private volatile ArrayList<EnumPolicy> hand;
  private boolean anonymous = false;

  public User ()
  {
    this("");
  }

  public User (String username, String password, EnumRegion region, ArrayList<EnumPolicy> hand)
  {
    this.username = username;
    this.hand = hand;
    this.region = region;
    this.password = Encryptable.bytesToHex(encrypt(password.getBytes()));
  }

  public User (String username)
  {
    this(username, "", null, new ArrayList<>());
  }

  /**
   * Returns the username
   *
   * @return username username string of user
   */
  public String getUsername ()
  {
    return username;
  }

  /**
   * Set the username NOTE: must be unique
   *
   * @param username unique username
   */
  public void setUsername (String username)
  {
    this.username = username;
  }

  /**
   * Get the users region
   * @return Region this user belongs to
   */
  public EnumRegion getRegion ()
  {
     return region;
  }

  /**
   * Set the region of the user.
   *
   * US only if planning on playing game
   *
   * @param region region of user
   */
  public void setRegion (EnumRegion region)
  {
     this.region = region;
  }

  /**
   * Get the hand of the user
   *
   * @return cards that currently belong to user
   */
  public ArrayList<EnumPolicy> getHand ()
  {
    return hand;
  }

  /**
   * Set the current list of cards that the user has
   *
   * @param hand list of cards to replace current hand
   */
  public void setHand (ArrayList<EnumPolicy> hand)
  {
    this.hand = hand;
  }

  /**
   * Check if the user is logged in
   *
   * @return true if logged in
   */
  @Transient
  public boolean isLoggedIn ()
  {
    return isLoggedIn;
  }

  /**
   * Set the user logged in status. Set to true when user logs in.
   *
   * @param loggedIn user's logged in status. false if not logged in
   */
  @Transient
  public void setLoggedIn (boolean loggedIn)
  {
    isLoggedIn = loggedIn;
  }

  /**
   * Check to see if the player has signaled they are ready.
   *
   * @return true if playing
   */
  @Transient
  public boolean isPlaying ()
  {
    return isPlaying;
  }

  /**
   * Set the users playing status. Set to true when user sends ready
   *
   * @param playing true if playing
   */
  @Transient
  public void setPlaying (boolean playing)
  {
    isPlaying = playing;
  }

  /**
   * Get the password
   * @return encrypted password
   */
  @Transient
  public String getPassword ()
  {
    return password;
  }

  /**
   * Sets the password and encrypts it
   *
   * @param password password for auth
   */
  @Transient
  public void setPassword (String password)
  {
    encrypt(password.getBytes());
  }

  /**
   * Set the encrypted password
   *
   * @param password encrypted password
   * @param salt key that is used to check for authentication.
   *
   * The salt must be able to reproduce the encrypted password
   */
  @Transient
  public void setEncryptedPassword (String password, String salt)
  {
    this.password = password;
    this.salt = salt;
  }

  @Transient
  public String getSalt ()
  {
    return salt;
  }

  @Transient
  public synchronized Connector getWorker ()
  {
    return worker;
  }

  @Transient
  public User setWorker (Connector worker)
  {
    this.worker = worker;
    return this;
  }

  @Override
  public String toString ()
  {
    StringBuilder _sb = new StringBuilder();
    _sb.append(String.valueOf(username))
       .append(" ")
       .append("in ")
       .append(String.valueOf(region));
    if (isLoggedIn)
    {
      _sb.append(" is online");
    }
    else
    {
      _sb.append(" is offline");
    }

    return _sb.toString();
  }

  @Override
  public byte[] encrypt (byte[] password)
  {
    if (salt == null)
    {
      salt = Encryptable.generateKey();
    }

    return Encryptable.generateHashedPassword(salt.getBytes(), password);
  }

  @Override
  public byte[] decrypt (byte[] msg)
  {
    throw new NotImplementedException();
  }

  @Override
  public boolean isEncrypted ()
  {
    return false;
  }

  @Override
  public Encryptable setEncrypted (boolean encrypted, SecretKey key)
  {
    throw new NotImplementedException();
  }

  @Override
  public Type getType ()
  {
    return Type.USER;
  }

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument json = new JSONDocument(JSONDocument.Type.OBJECT);

    json.setString("username", username);
    // json.setString("password", password);
    json.setString("region", String.valueOf(region));
    json.setString("type", getType().name());
    // json.setString("last-login", time);
    json.setBoolean("online", isLoggedIn);
    json.setBoolean("playing", isPlaying);

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
    throw new NotImplementedException();
  }

  @Transient
  public void reset ()
  {
    actionPointsRemaining = 3;
    policyCardsDiscarded = 0;
    drafts = 0;
    draftVoteCard = 0;
    isDone = false;
  }

  public void setAnonymous (boolean anonymous)
  {
    this.anonymous = anonymous;
  }

  public boolean isAnonymous ()
  {
    return anonymous;
  }

  @Override
  public boolean equals (Object obj)
  {
    if (obj instanceof User)
    {
      return this.username.equals(((User) obj).getUsername());
    }
    return false;
  }
}
