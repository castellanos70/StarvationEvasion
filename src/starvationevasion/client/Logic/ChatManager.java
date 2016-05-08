package starvationevasion.client.Logic;

import starvationevasion.client.Networking.Client;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.EnumPolicy;

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
    System.out.println("sending message");
    if (region == null && client.getRegion() == null){
      client.getCommunicationModule().sendChat("ALL", "?" + ": " + message, card);
    } else if (client.getRegion() == null) {
      client.getCommunicationModule().sendChat(region, "?" + ": " + message, card); 
    } else {
      client.getCommunicationModule().sendChat(region, client.getRegion().toString() + ": " + message, card); 
    }
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
