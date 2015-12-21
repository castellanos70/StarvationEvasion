package starvationevasion.client.Aegislash.Networking;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.messages.*;

import java.io.Serializable;

/**
 * Wrapper for Serialized message objects that are decoded by the server
 */
public class MessageContainer
{
  private Serializable contents;

  /**
   * Constructor used to attempt to draft cards
   * @param cardToBeDrafted
   */
  public MessageContainer(DraftCard cardToBeDrafted)
  {
    setContents(cardToBeDrafted);
  }

  /**
   * Constructor used to attempt to discard a card
   * @param discard
   */
  public MessageContainer(Discard discard)
  {
    setContents(discard);
  }

  /**
   * This is a MessageContainer that contains a login with the plaintext username and password as well as the logingNonce
   * hash. The loginNonce (or salt) will be used by the server to hash the password).
   * @param uname login username
   * @param loginSalt login salt
   * @param passwd login plaintext password
   */
  public MessageContainer(String uname, String loginSalt, String passwd)
  {
    setContents(new Login(uname, loginSalt, passwd));
  }

  /**
   * Message containing a region that the user would like
   * select.
   * @param desiredRegion The region the user has specified.
   */
  public MessageContainer(EnumRegion desiredRegion)
  {
    setContents(new RegionChoice(desiredRegion));
  }

  /**
   * Message container for sending a vote to the server
   * @param voteRegion the region who's card you would like to vote on
   * @param voteType The type of vote to apply to the region's card
   */
  public MessageContainer(EnumRegion voteRegion, VoteType voteType)
  {
    setContents(new Vote(voteRegion, voteType));
  }

  /**
   * Gets the contents of the MessageContainer
   * @return the contents of the MessageContainer
   */
  public Serializable getContents()
  {
    return contents;
  }

  /**
   * Sets the contents of the MessageContainer
   * @param contents the contents to set in the MessageContainer
   */
  public void setContents(Serializable contents)
  {
    this.contents = contents;
  }

}
