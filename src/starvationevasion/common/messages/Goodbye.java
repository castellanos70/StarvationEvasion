package starvationevasion.common.messages;

import java.io.Serializable;

/**
 * Shea Polansky
 * Sent by the server to the client when it is disconnecting the client, or
 * from the client to the server for the same reason.
 * If the client is sending the message, the proscribed reason is
 * "User disconnect", if the user has chosen to disconnect. In the case of
 * send/receive errors, or other reasons not relating to "the user wanted to leave",
 * other messages may be sent.
 */
public class Goodbye implements Serializable
{
  public final String disconnectMessage;

  public Goodbye(String disconnectMessage)
  {
    this.disconnectMessage = disconnectMessage;
  }
}
