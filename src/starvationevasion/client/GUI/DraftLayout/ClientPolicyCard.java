package starvationevasion.client.GUI.DraftLayout;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import starvationevasion.client.GUI.GUI;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.Util;
import starvationevasion.common.gamecards.EnumPolicy;
import starvationevasion.common.gamecards.GameCard;

import java.util.ArrayList;
import java.util.Arrays;


/**`
 * 
 * Created by arirappaport on 11/15/15.
 */
public class ClientPolicyCard extends VBox
{
  private boolean isActive, isBeingVotedOn;
  private boolean isFlipped=false;
  private ImageView view;
  private EnumRegion owner;
  private GameCard policy;
  private EnumPolicy enumPolicy;
  private GUI gui;
  private boolean drafted=false;
  private boolean discarded=false;
  private boolean selected=false;
  private boolean needsFood=false;
  private boolean needsRegion=false;
  private Button draftButton=new Button("Draft Card");
  private Button discardButton=new Button("Discard Card");
  private int handIndex;
  private int xVal,yVal,zVal=0;
  private EnumRegion selectedRegion;
  private EnumFood selectedFood;
  private String style;

  /**
   * Just basic constructor
   * @param owner the Owner of the Card
   * @param policy the original policy
   * @param gui the main GUI
   */
  public ClientPolicyCard(EnumRegion owner, EnumPolicy policy, GUI gui)
  {
    this.owner=owner;
    this.policy=GameCard.create(owner,policy);
    enumPolicy=policy;
    this.gui=gui;
    setBasicCard();
    setBasicStyle();
  }

  /**
   * Gets the policy card and assigns values to the policy card
   * @return a Valid Policy Card
   */
  public GameCard getPolicyCard()
  {
    if(xVal!=0)policy.setX(xVal);
    else policy.setX(1);

    if(selectedFood==null) policy.setTargetFood(EnumFood.CITRUS);
    else policy.setTargetFood(selectedFood);
    if(selectedRegion==null) policy.setTargetRegion(EnumRegion.USA_CALIFORNIA);
    else policy.setTargetRegion(selectedRegion);
    return policy;
  }

  /**
   * A getter that indicates if the card is flipped over
   * @return boolean representing if card is flipped over
   */
  public boolean getIsFlipped()
  {
    return isFlipped;
  }

  /**
   * a setter for when the card gets flipped over
   * @param flipped what you set the flipped over to
   */
  public void setIsFlipped(boolean flipped)
  {
    isFlipped=flipped;
  }

  /**
   * a boolean indicating if the card has been drafted
   * @return the boolean representing if the card is drafted
   */
  public boolean getDrafted() {return drafted;}

  /**
   * A setter that sets a flag indicating if card is drafted or not
   * @param drafted the boolean you're setting it to
   */
  public void setDrafted(boolean drafted) {this.drafted=drafted;}

  /**
   * a boolean indicating if the card has been discard
   * @return the boolean representing if the card is discarded
   */
  public boolean isDiscarded(){return discarded;}

  /**
   * A setter that sets a flag indicating if card is discarded or not
   * @param discarded the boolean you're setting it to
   */
  public void setDiscarded(boolean discarded){this.discarded=discarded;}

  /**
   * Gets the draft button on the card
   * @return the DraftButton
   */
  public Button getDraftButton(){return draftButton;}

  /**
   * disables or enables draft button
   * @param bool the boolean if it's set or not
   */
  public void setDraftButton(boolean bool){draftButton.setDisable(bool);}

  /**
   * Gets the discard button on the card
   * @return
   */
  public Button getDiscardButton(){return discardButton;}

  /**
   * disables or enables discard button
   * @param bool the boolean if it's set or not
   */
  public void setDiscardButton(boolean bool){discardButton.setDisable(bool);}

  /**
   * gets the index the card is in hand
   * @return the index
   */
  public int getHandIndex(){return handIndex;}

  /**
   * gets if the card is selected
   * @return
   */
  public boolean isSelected(){ return selected;}

  /**
   * sets the index that the card is in the hand
   * @param handIndex
   */
  public void setHandIndex(int handIndex){this.handIndex=handIndex;}

  /**
   * sets the food that is selected
   * @param food
   */
  public void  setSelectedFood(EnumFood food){selectedFood=food;}

  /**
   * sets the region that is selected
   * @param food
   */
  public void  setSelectedRegion(EnumRegion food){selectedRegion=food;}

  /**
   * returns bool if card needs food
   * @return
   */
  public boolean needsFood(){return needsFood;}

  /**
   * returns bool if region needs food
   * @return
   */
  public boolean needsRegion(){return needsRegion;}
  //Sets the css styling for each card depending on their type
  public void setBasicStyle()
  {
    if(this.policy.getValidTargetFoods()!=null&&this.policy.getValidTargetRegions()!=null)
    {
      style="-fx-background-color: #97AAAA;" + " -fx-border-radius: 10 10 10 10;\n" +
              "  -fx-background-radius: 10 10 10 10;" + "-fx-border-style:solid;";
      this.setStyle(style);
    }
    else if(this.policy.getValidTargetFoods()!=null)
    {
      needsFood=true;
      style="-fx-background-color: rgb(151,170,170);" + " -fx-border-radius: 10 10 10 10;\n" +
              "  -fx-background-radius: 10 10 10 10;" + "-fx-border-style:solid;";
      this.setStyle(style);
    }else if(this.policy.getValidTargetRegions()!=null)
    {
      needsRegion=true;
      style="-fx-background-color: rgb(102,111,201,0.5);" + " -fx-border-radius: 10 10 10 10;\n" +
              "  -fx-background-radius: 10 10 10 10;" + "-fx-border-style:solid;";
      this.setStyle(style);
    }
    else
    {
      style="-fx-background-color: rgb(15,140,204,0.5);" + " -fx-border-radius: 10 10 10 10;\n" +
              "  -fx-background-radius: 10 10 10 10;" + "-fx-border-style:solid;";
      this.setStyle(style);
    }
  }

  /**
   * Basic getter for supportCard
   * @return a boolean
   */
  public boolean isSupportCard()
  {
    boolean bool=(policy.votesRequired()==0)?false:true;
     return bool;
  }

  /**
   * Sets the card to the basic mode
   * Just a picture and title
   */
  public void setBasicCard()
  {
    double cardHeight=gui.getBoxHeight()*4;
    double cardWidth=gui.getBoxWidth()*(3);
    this.getChildren().clear();
    this.setPrefSize(cardWidth,cardHeight);
    setMaxSize(cardWidth,cardHeight);
    Label title=new Label(policy.getTitle());
    Label gameText=new Label(policy.getGameText());
    Label cost=new Label(String.valueOf(policy.votesRequired()));
    cost.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 14));

    ImageView costImage=gui.getImageGetter().getVoteIcon();
    costImage.setFitHeight(20);
    costImage.setFitWidth(20);
    StackPane stackPane=new StackPane();
    //stackPane.getChildren().add(costImage);
    stackPane.getChildren().add(cost);
    stackPane.setAlignment(Pos.CENTER);
    gameText.setWrapText(true);
    gameText.autosize();
    title.setTextFill(Color.ALICEBLUE);
    title.setFont(Font.font("Helvetica", FontWeight.BOLD,12));
    title.setWrapText(true);
    title.setAlignment(Pos.CENTER);
    title.setTextAlignment(TextAlignment.CENTER);

    view= gui.getImageGetter().getImageForCard(enumPolicy);
    view.setFitHeight(gui.getBoxHeight()*1.33);
    view.setFitWidth(gui.getBoxHeight()*1.33);
    this.setAlignment(Pos.CENTER);
    this.getChildren().add(title);
    this.getChildren().add(view);
      this.setScaleX(1);
    this.setScaleY(1);
    this.setScaleZ(1);
    setTranslateY(0);
    toBack();
  }

  /**
   * Sets card to have title, smaller picture, and card text
   */
  public void setDetailedCard2()
  {
    isFlipped=false;
    this.setFocused(true);
    this.getChildren().clear();
    Label title=new Label(policy.getTitle());
    Label gameText=new Label(policy.getGameText());
    Label cost=new Label(String.valueOf(policy.votesRequired()));
    gameText.setWrapText(true);

    title.setFont(Font.font("helvetica",FontWeight.BOLD,9));
    title.setWrapText(true);
    title.setTextFill(Color.ALICEBLUE);
    title.setAlignment(Pos.CENTER);
    title.setTextAlignment(TextAlignment.CENTER);
    if(policy.getGameText().length()<=180)
    {
      gameText.setFont(Font.font("helvetica", FontWeight.THIN, 8));
    }else gameText.setFont(Font.font("helvetica", FontWeight.THIN, 6));
    gameText.setStyle("-fx-background-color: #91A7AD;"+" -fx-border-radius: 3 3 3 3;\n" +
                    "  -fx-background-radius: 3 3 3 3;"+"-fx-border-style:solid;");
    gameText.autosize();
    view= gui.getImageGetter().getImageForCard(enumPolicy);
    view.setFitHeight(50);
    view.setFitWidth(50);
    setPrefSize(750, 250);
    setAlignment(Pos.CENTER);
    getChildren().add(title);
    getChildren().add(view);
    getChildren().add(gameText);
    this.setScaleX(2);
    this.setScaleY(2);
    this.setTranslateY(-80);
  }
  public void setDetailedCard()
  {
    isFlipped=false;
    this.setFocused(true);
    this.getChildren().clear();
    Label title=new Label(policy.getTitle());
    Label gameText=new Label(policy.getGameText());
    Label cost=new Label(String.valueOf(policy.votesRequired()));
    gameText.setWrapText(true);

    title.setFont(Font.font("helvetica",FontWeight.BOLD,9));
    title.setWrapText(true);
    title.setTextFill(Color.ALICEBLUE);
    title.setAlignment(Pos.CENTER);
    title.setTextAlignment(TextAlignment.CENTER);
    if(policy.getGameText().length()<=180)
    {
      gameText.setFont(Font.font("helvetica", FontWeight.THIN, 8));
    }else gameText.setFont(Font.font("helvetica", FontWeight.THIN, 6));
    gameText.setStyle("-fx-background-color: #91A7AD;"+" -fx-border-radius: 3 3 3 3;\n" +
            "  -fx-background-radius: 3 3 3 3;"+"-fx-border-style:solid;");
    gameText.autosize();
    view= gui.getImageGetter().getImageForCard(enumPolicy);
    view.setFitHeight(50);
    view.setFitWidth(50);
    setPrefSize(750, 250);
    setAlignment(Pos.CENTER);
    getChildren().add(title);
    getChildren().add(view);
    getChildren().add(gameText);
    this.setScaleX(2.5);
    this.setScaleY(2.5);
    this.setTranslateY(-80);
    HBox buttonBox=new HBox();
    discardButton.setStyle("-fx-font-size: 6px;");
    draftButton.setStyle("-fx-font-size: 6px;");
    buttonBox.getChildren().add(draftButton);
    buttonBox.getChildren().add(new Label(String.valueOf(policy.votesRequired())));
    buttonBox.getChildren().add(discardButton);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.setSpacing(20);
    getChildren().add(buttonBox);
  }
  public void setDraftStyle()
  {
    setStyle("-fx-background-color: rgba(0, 100, 100, 0.5);");
  }
  public void setDiscardStyle()
  {
    setStyle("-fx-background-color: rgba(100, 0, 0, 0.5);");
  }
  public void setDrafted()
  {
    double cardHeight=gui.getBoxHeight()*4;
    double cardWidth=gui.getBoxWidth()*(3);
    this.getChildren().clear();
    this.setPrefSize(cardWidth,cardHeight);
    setMaxSize(cardWidth,cardHeight);

    Label title=new Label(policy.getTitle());
    Label gameText=new Label(policy.getGameText());
    Label cost=new Label(String.valueOf(policy.votesRequired()));
    cost.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 14));

    ImageView costImage=gui.getImageGetter().getVoteIcon();
    costImage.setFitHeight(20);
    costImage.setFitWidth(20);
    StackPane stackPane=new StackPane();
    //stackPane.getChildren().add(costImage);
    stackPane.getChildren().add(cost);
    stackPane.setAlignment(Pos.CENTER);
    gameText.setWrapText(true);
    gameText.autosize();
    title.setTextFill(Color.ALICEBLUE);
    title.setFont(Font.font("Helvetica", FontWeight.BOLD,12));
    title.setWrapText(true);
    title.setAlignment(Pos.CENTER);
    title.setTextAlignment(TextAlignment.CENTER);
    view= gui.getImageGetter().getImageForCard(enumPolicy);
    view.setFitHeight(gui.getBoxHeight()*1.33);
    view.setFitWidth(gui.getBoxHeight()*1.33);
    this.setAlignment(Pos.CENTER);
    this.getChildren().add(title);
    this.getChildren().add(view);
    this.setScaleX(1);
    this.setScaleY(1);
    this.setScaleZ(1);
    setTranslateY(10);
    setStyle("-fx-background-color: rgba(0, 100, 100, 0.5);");
    toBack();
  }
  /**
   * sets the card to have sliders and input
   */
  public void flipCardOver()
  {
    this.getChildren().clear();
    isFlipped=true;
    this.getChildren().clear();

    Label title=new Label(policy.getTitle()+"   "+policy.votesRequired());
    title.setWrapText(true);
    title.setTextFill(Color.ALICEBLUE);

    Label cost=new Label(String.valueOf(policy.votesRequired()));

    Label gameText=new Label(policy.getGameText());
    gameText.setWrapText(true);
    gameText.autosize();
    gameText.setFont(Font.font("", FontWeight.BOLD,8));

    view= gui.getImageGetter().getImageForCard(enumPolicy);
    view.setFitHeight(50);
    view.setFitWidth(50);
    discardButton.setStyle("-fx-font-size: 6px;");
    draftButton.setStyle("-fx-font-size: 6px;");
//    if(needsFood)
//    {
//      draftButton.setDisable(true);
//      draftButton.setText("Needs Produce");
//      draftButton.autosize();
//    }
    HBox buttonBox=new HBox();
    buttonBox.getChildren().add(draftButton);
    buttonBox.getChildren().add(discardButton);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.setSpacing(40);
    getChildren().add(cost);
    getChildren().add(gameText);
    addSliderFromText(gameText.getText());
    getChildren().add(buttonBox);
  }

  /**
   * Just the back of the card with simple text starvation evasion
   */
  public void backOfCard()
  {
    this.getChildren().clear();
    Label title=new Label("Starvation Evasion");
    title.setFont(Font.font("",FontWeight.EXTRA_BOLD,14));
    GridPane gridPane=new GridPane();
    gridPane.setPrefSize(150, 200);
    gridPane.add(title,0,0);
    gridPane.setAlignment(Pos.CENTER);
    this.getChildren().add(gridPane);
    this.setScaleX(1);
    this.setScaleY(1);
    this.setScaleZ(1);
  }

  /**
   * Basic red discard pile
   */
  public void discardPile()
  {
    this.getChildren().clear();
    Label title=new Label("DiscardPile");
    title.setFont(Font.font("",FontWeight.EXTRA_BOLD,14));
    GridPane gridPane=new GridPane();
    gridPane.setPrefSize(150, 200);
    gridPane.add(title,0,0);
    gridPane.setAlignment(Pos.CENTER);
    gridPane.setStyle("-fx-background-color: #d22d2d;"+" -fx-border-radius: 10 10 10 10;\n" +
            "  -fx-background-radius: 10 10 10 10;");
    this.getChildren().add(gridPane);
    this.setScaleX(1);
    this.setScaleY(1);
    this.setScaleZ(1);
  }

  /**
   * sets the card to a smaller mode for the draft layout
   */
  public void setDraftedCard()
  {
    double cardHeight=gui.getBoxHeight();
    double cardWidth=gui.getBoxWidth();
    this.getChildren().clear();
    this.setPrefSize(cardWidth,cardHeight);
    setMaxSize(cardWidth,cardHeight);

    Label title=new Label(policy.getTitle());
    Label gameText=new Label(policy.getGameText());
    Label cost=new Label(String.valueOf(policy.votesRequired()));
    cost.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 14));

    ImageView costImage=gui.getImageGetter().getVoteIcon();
    costImage.setFitHeight(20);
    costImage.setFitWidth(20);
    StackPane stackPane=new StackPane();
    stackPane.getChildren().add(cost);
    stackPane.setAlignment(Pos.CENTER);
    gameText.setWrapText(true);
    gameText.autosize();
    title.setTextFill(Color.ALICEBLUE);
    title.setFont(Font.font("Helvetica", FontWeight.BOLD,12));
    title.setWrapText(true);
    title.setAlignment(Pos.CENTER);
    title.setTextAlignment(TextAlignment.CENTER);

    view= gui.getImageGetter().getImageForCard(enumPolicy);
    view.setFitHeight(gui.getBoxHeight()*1);
    view.setFitWidth(gui.getBoxHeight()*1);
    this.setAlignment(Pos.CENTER);
    this.getChildren().add(view);
    this.setScaleX(1);
    this.setScaleY(1);
    this.setScaleZ(1);
    setTranslateY(0);
    setTranslateX(0);
  }

  /**
   * Sets the card to larger view with picture title and text for when the card is in drafted zone
   */
  public void setDetailedDraftedCard()
  {isFlipped=false;
    this.setFocused(true);
    this.getChildren().clear();
    Label title=new Label(policy.getTitle());
    Label gameText=new Label(policy.getGameText());
    Label cost=new Label(String.valueOf(policy.votesRequired()));
    gameText.setWrapText(true);

    title.setFont(Font.font("helvetica",FontWeight.BOLD,8));
    title.setWrapText(true);
    title.setTextFill(Color.ALICEBLUE);
    title.setAlignment(Pos.CENTER);
    title.setTextAlignment(TextAlignment.CENTER);
    gameText.setFont(Font.font("helvetica", FontWeight.THIN, 6));
    gameText.setStyle("-fx-background-color: #91A7AD;"+" -fx-border-radius: 3 3 3 3;\n" +
            "  -fx-background-radius: 3 3 3 3;"+"-fx-border-style:solid;");
    gameText.autosize();
    view= gui.getImageGetter().getImageForCard(enumPolicy);
    view.setFitHeight(50);
    view.setFitWidth(50);
    setPrefSize(750, 250);
    setMaxHeight(150);
    setMaxWidth(100);
    setAlignment(Pos.CENTER);
    getChildren().add(title);
    getChildren().add(view);
    getChildren().add(gameText);
    this.setScaleX(2);
    this.setScaleY(2);
  }

  /**
   * Puts a border around the card to indicate the card is selected
   */
  public void selectCard()
  {
    if(!selected)
    {
      this.setStyle(style+"-fx-border-color: gold ;"
      +"-fx-border-width: 5 ;" );
    }
    else
    {
      setBasicStyle();
      setEffect(null);
    }
    selected=!selected;
  }

  public void setVeryDetailed(){
    isFlipped=false;
    this.setFocused(true);
    this.getChildren().clear();
    Label title=new Label(policy.getTitle());
    Label gameText=new Label(policy.getGameText());
    Label cost=new Label(String.valueOf(policy.votesRequired()));
    gameText.setWrapText(true);

    //title.setFont(Font.font("helvetica",FontWeight.BOLD,9));
    title.setWrapText(true);
    title.setTextFill(Color.ALICEBLUE);
    title.setAlignment(Pos.CENTER);
    title.setTextAlignment(TextAlignment.CENTER);
    if(policy.getGameText().length()<=180)
    {
      //gameText.setFont(Font.font("helvetica", FontWeight.THIN, 8));
    }//else gameText.setFont(Font.font("helvetica", FontWeight.THIN, 6));
    gameText.setStyle("-fx-background-color: #91A7AD;"+" -fx-border-radius: 3 3 3 3;\n" +
            "  -fx-background-radius: 3 3 3 3;"+"-fx-border-style:solid;");
    gameText.autosize();
    view= gui.getImageGetter().getImageForCard(enumPolicy);
    view.setPreserveRatio(true);
    view.setFitHeight(100);
    view.setFitWidth(100);
    //setPrefSize(750, 250);
    setAlignment(Pos.CENTER);
    getChildren().add(title);
    getChildren().add(view);
    getChildren().add(gameText);
    addSliderFromText("");
    if(needsRegion())
    {
      ArrayList<EnumRegion> regions=new ArrayList<>(Arrays.asList(EnumRegion.US_REGIONS));
      //regions.add(null);
      ObservableList<EnumRegion> regionList= FXCollections.observableArrayList(regions);
      ComboBox<EnumRegion>regionSelection =new ComboBox<>(regionList);
      getChildren().add(regionSelection);
    }
    if(needsFood())
    {
      ArrayList<EnumFood> regions=new ArrayList<>(Arrays.asList(EnumFood.values()));
     // regions.add(null);
      ObservableList<EnumFood> regionList= FXCollections.observableArrayList(regions);
      ComboBox<EnumFood>regionSelection =new ComboBox<>(regionList);
      getChildren().add(regionSelection);
    }
//    this.setScaleX(2);
//    this.setScaleY(2);
//    this.setTranslateY(-80);
  }

//For parsing game text to find X,Y, and Z and swaping with the XValue,YValue, and ZValue
  private void addSliderFromText(String gameText)
  {
    /* Slider needs to be replaced with drop-down
    if(policy.getRequiredVariables(PolicyCard.EnumVariable.X)!=null)
    {
      Slider xSlider=new Slider();
      xSlider.setMin(0);
      xSlider.setMax(100);
      xSlider.setShowTickMarks(true);
      xSlider.setShowTickLabels(true);
      xSlider.setBlockIncrement(1);
      xSlider.setMinorTickCount(1);
      xSlider.setMajorTickUnit(10);
      xSlider.setSnapToTicks(true);
      Label xLabel=new Label("X value:   "+(int)xSlider.getValue());
      xLabel.setFont(Font.font("",8));
      gridPane.add(xLabel, 0, 0);
      gridPane.add(xSlider, 0, 1);
      xSlider.setOnMouseClicked(new javafx.event.EventHandler<MouseEvent>()
      {
        @Override
        public void handle(MouseEvent event)
        {
          xVal=(int)xSlider.getValue();
          xLabel.setText("X value:   " + (int)xSlider.getValue());
        }
      });
    }
    */
  }

  /**
   * sets random values to each input value on policy card to random value
   * This is used by AI
   */
  public void setRandomValues()
  {
    ArrayList<Integer> legalValueList = policy.getOptionsOfVariable();
    EnumFood[] food=policy.getValidTargetFoods();
    EnumRegion[] regions=policy.getValidTargetRegions();

    if (legalValueList == null) xVal=0;
    else
    {
      xVal=legalValueList.get(Util.rand.nextInt(legalValueList.size()));
    }


    if(food!=null)
    {
      selectedFood=(food[Util.rand.nextInt(food.length)]);
    }
    if(regions!=null)
    {
      selectedRegion=(regions[Util.rand.nextInt(regions.length)]);
    }
  }

  /**
   * gets the EnumPolicy
   * @return enumPolicy
   */
  public EnumPolicy getPolicy()
  {
    return enumPolicy;
  }


}
