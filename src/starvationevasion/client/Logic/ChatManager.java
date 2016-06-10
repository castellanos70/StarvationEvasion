package starvationevasion.client.Logic;

import starvationevasion.client.Networking.Client;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.card.EnumPolicy;

/**
 * Manages communication with client and chatnode
 */
public class ChatManager
{
  private String chat="";
  private  Client client;
  public ChatManager(Client client)
  {
  this.client=client;
  }

  /**
   * Sends a chat to the client who will send a request
    * @param message A String representing message
   * @param region The region you are sending message to
   * @param card The card you are sending to the server
   */
  public void sendChatToServer(String message,EnumRegion region,EnumPolicy card)
  {
    client.getCommunicationModule().sendChat(region, region.toString() + ": " + message, card);
    //client.sendChatMessage(client.getRegion().toString()+": "+message,region);
    chat+=message+"\n";
  }

  /**
   * Called by Client, updates the chat for the chatnode
   * @param message
   */
  public void sendChatToClient(String message)
  {
    chat+=message+"\n";

  }

  /**
   * gets all chat messages so far
   * @return
   */
  public String getChat()
  {
    return chat;
  }
}
