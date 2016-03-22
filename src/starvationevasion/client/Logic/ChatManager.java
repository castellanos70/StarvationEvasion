package starvationevasion.client.Logic;

import starvationevasion.client.Client;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;

/**
 * Created by Dayloki on 3/9/2016.
 */
public class ChatManager
{
  private String chat="";
  private  Client client;
  public ChatManager(Client client)
  {
  this.client=client;
  }
  public void sendChatToServer(String message,EnumRegion region,EnumPolicy card)
  {
    String msg="chat " +region.toString()+" {\"card\":null,\"text\":\""+client.getRegion().toString()+": "+message+"\"}";
    client.writeToServer(msg);
    chat+=message+"\n";
  }
  public void sendChatToClient(String message)
  {
    chat+=message+"\n";
  }
  public String getChat()
  {
    return chat;
  }
}
