package starvationevasion.common.messages;

import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Shea Polansky
 * Sent from the client to the server to send a chat message.
 * Will contain *either* a message (type String) or an EnumPolicy
 * to show to the recipients. No validation is performed on either
 * cards or message strings - it is up to clients to ensure that
 * poorly formatted messages are handled appropriately.
 * Note: The corresponding ServerChatMessage object will be delivered
 * to only the recipients listed in the object; if a client wants verification
 * that the message was delivered, it should include itself as a recipient.
 * In any given message, exactly one of the fields 'message' and 'card' will be populated;
 * the other will be null.
 * messageRecipients will ignore duplicate values (only one message will be sent per recipient), and
 * will simply not deliver messages to regions that are not connected or not valid (e.g. not within the US).
 */
public class ClientChatMessage implements Serializable
{
  public final String message;
  public final EnumPolicy card;
  public final EnumRegion[] messageRecipients;

  public ClientChatMessage(String message, EnumRegion[] messageRecipients)
  {
    this.message = message;
    this.messageRecipients = Arrays.copyOf(messageRecipients, messageRecipients.length);
    card = null;
  }

  public ClientChatMessage(EnumPolicy card, EnumRegion[] messageRecipients)
  {
    this.card = null;
    this.messageRecipients = Arrays.copyOf(messageRecipients, messageRecipients.length);
    message = null;
  }
}
