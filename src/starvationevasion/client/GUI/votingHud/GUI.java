package starvationevasion.client.GUI.votingHud;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
public class GUI extends Application
{

  private Stage primaryStage;
  private int maxHeight=800;
  private int maxWidth=1400;
  private Scene scene;
 

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    
    this.primaryStage = primaryStage;
    
    VotingNode votingNode = new VotingNode(maxHeight, maxHeight);
    
    
    scene = new Scene(votingNode, maxWidth, maxHeight);
    this.primaryStage.setTitle("Voting Screen Test");
    
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  
  /**
   * Simple getter in case any node needs to get the stage
   */
  public Stage getPrimaryStage()
  {
    return this.primaryStage;
  }

  public static void main(String[] args)
  {
    launch(args);
  }

  public int getMaxWidth()
  {
    
    return maxWidth;
  }

  public int getMaxHeight()
  {
    return maxHeight;
  }
  


}
