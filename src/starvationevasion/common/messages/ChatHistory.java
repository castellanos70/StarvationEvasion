package starvationevasion.common.messages;

import java.util.Date;
import java.util.Stack;

/**
 * Created by scnaegl on 11/23/15.
 */
public class ChatHistory {
  private Stack<ChatMessage> messages = new Stack<>();

  public ChatHistory() {
    this.messages = new Stack<>();
    this.messages.setSize(20);
  }

  public void addMessage(Login player, String message) {
    messages.add(new ChatMessage(player, message));
    messages.trimToSize();
  }


  public class ChatMessage {
    private Login player;
    private String message;
    private Date sent_time;

    public ChatMessage(Login player, String message) {
      this.player = player;
      this.message = message;
      this.sent_time = new Date();
    }

    public Login getPlayer() {
      return player;
    }

    public String getMessage() {
      return message;
    }

    public Date getTime() {
      return sent_time;
    }
  }
}
