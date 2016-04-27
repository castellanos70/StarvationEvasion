package starvationevasion.communication.ClientTest;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.policies.EnumPolicy;
import starvationevasion.communication.CommModule;

/**
 * Manages communication with client and chatnode
 */
public class ChatManager
{
  private String chat="";
  private final CommModule COMM;
  public ChatManager(CommModule comm)
  {
    COMM=comm;
  }

  /**
   * Sends a chat to the client who will send a request
   * @param message A String representing message
   * @param region The region you are sending message to
   * @param card The card you are sending to the server
   */
  public void sendChatToServer(String message, EnumRegion region, EnumPolicy card)
  {
    //COMM.sendChatMessage(client.getRegion().toString()+": "+message,region);
    COMM.sendChat(region, region.toString()+ ": " + message, null);
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

