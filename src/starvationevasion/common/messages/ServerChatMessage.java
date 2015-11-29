package starvationevasion.common.messages;

import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;

import java.io.Serializable;

/**
 * Shea Polansky
 * Contains a message sent from one client to another via the server.
 * Note: No validation is performed with regards to message contents; policy
 * card messages may contain invalid cards, and text messages may contain
 * unprintable or otherwise oddly-behaving characters. It is the responsibility of the
 * client to handle such things appropriately.
 * In any given message, exactly one of the fields 'message' and 'card' will be populated;
 * the other will be null.
 */
public class ServerChatMessage implements Serializable
{
  public final String message;
  public final EnumPolicy card;
  public final EnumRegion sender;

  public ServerChatMessage(String message, EnumRegion sender)
  {
    this.message = message;
    this.sender = null;
    this.card = null;
  }

  public ServerChatMessage(EnumPolicy card, EnumRegion sender)
  {
    this.card = card;
    this.sender = sender;
    this.message = null;
  }

  /**
   * Constructs the ServerChatMessage that corresponds to a given ClientChatMessage
   * @param message the ClientChatMessage to use
   * @param sender the sender of the message
   * @return a new ServerChatMessage
   */
  public static ServerChatMessage constructFromClientMessage(ClientChatMessage message, EnumRegion sender)
  {
    if (message.message == null) return new ServerChatMessage(message.card, sender);
    else return new ServerChatMessage(message.message, sender);
  }
}
