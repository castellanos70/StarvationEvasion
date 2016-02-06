package starvationevasion.client.model;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Created by MohammadR on 2/5/2016.
 */
public class Chat
{
  final static public int MAX_HISTORY = 100;
  private TextFlow history = new TextFlow();

  public void addText(Text text)
  {
    if (history.getChildren().size() == MAX_HISTORY) history.getChildren().remove(0);
    history.getChildren().add(text);
  }

  public TextFlow getChat()
  {
    return history;
  }


}
