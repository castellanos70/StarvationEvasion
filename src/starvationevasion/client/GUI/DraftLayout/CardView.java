package starvationevasion.client.GUI.DraftLayout;
  
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.*;
import javafx.util.Callback;
import starvationevasion.client.GUI.images.ImageGetter;
import starvationevasion.common.*;

import java.util.ArrayList;
import java.util.Arrays;


public class CardView extends AbstractCard
{
  private double largeCardWidth=370;
  private double largeCardHeight=520;
  private double smallCardWidth=92.5;
  private double smallCardHeight=130;

  private double cardWidth = largeCardWidth;
  private double cardHeight =largeCardHeight;
  private double foodIconWidth=32;
  private double foodIconHeight=32;
  private AnchorPane polygonPane;
  private StackPane cardPane;
  private ImageView cardImage = new ImageView();

  int actionPointCost;
  private boolean mouseOverOctagon = false;
  private double textOctagonHeightModifier = 0;
  private double transparency = 0.50;
  private Double[] octagonPoints = new Double[]{ 
      (cardWidth*2/9), 0.0,
      (cardWidth*2/9), 0.0,
      (cardWidth*7/9), 0.0, 
      (cardWidth*8/9), (cardHeight/13),
      (cardWidth*8/9), (cardHeight/13)  +textOctagonHeightModifier,
      (cardWidth*7/9), (cardHeight*2/13)+textOctagonHeightModifier,
      (cardWidth*2/9), (cardHeight*2/13)+textOctagonHeightModifier,
      (cardWidth/9),   (cardHeight/13)  +textOctagonHeightModifier, 
      (cardWidth/9),   (cardHeight/13)
      };
  private String color = "0xaba9db";
  private final String GREEN = "009933";
  private final String BLUE = "0033cc";
  private final String PURPLE = "9933ff";
  private final String GREY = "666699";
  private final String ORANGE = "ff6600";
  private PolicyCard gameCard;

  private boolean selected=false;

  private Polygon topTrapezoid        = new Polygon();
  private Polygon bottomTrapezoid     = new Polygon();
  private Polygon bottomLeftPentagon  = new Polygon();
  private Polygon topLeftPentagon     = new Polygon();
  private Polygon bottomRightPentagon = new Polygon();
  private Polygon topRightPentagon    = new Polygon();
  private Polygon middleTextOctagon   = new Polygon();
  Circle pipOne, pipTwo, pipThree;
  private Text title, rulesText, flavorText, informationText;
  private ScrollPane textScrollPane;
  private Node xSelection, foodSelection,regionSelection;
  private EnumRegion owner;
  private EnumPolicy policy;
  private ContextMenu contextMenu;
  public CardView(EnumRegion owner, EnumPolicy policy)
  {
    this.owner = owner;
    this.policy=policy;
    gameCard = new PolicyCard(policy, owner);
    actionPointCost = gameCard.getActionPointCost();

    initSimpleCard();

    //this.getChildren().add(cardPane);
   // this.getChildren().add(cardPane);
    //initSimpleCard();

    this.setOnMouseEntered(event -> {
      initMainCard();
      setTranslateY(-400);
      setTranslateX(-cardWidth/2);
      toFront();
    });
    this.setOnMouseExited(event -> {
      if(!selected) {
        initSimpleCard();
        setTranslateY(0);
        setTranslateX(0);
      }
    });
    setOnMouseClicked(event -> {
      if(event.getButton().equals(MouseButton.SECONDARY)){
        openRightClickMenu(event);
      }
      if(event.getButton().equals(MouseButton.PRIMARY)){
        if(contextMenu!=null)contextMenu.hide();
        selected=false;
      }
    });

  }

  public CardView(EnumPolicy policy) {

    this.policy = policy;
    owner=EnumRegion.USA_HEARTLAND;
    gameCard = new PolicyCard(policy, owner);
    actionPointCost = gameCard.getActionPointCost();



    setOnMouseClicked(event -> {
      if(event.getButton().equals(MouseButton.SECONDARY)){
        openRightClickMenu(event);
      }
      if(event.getButton().equals(MouseButton.PRIMARY)){
        if(contextMenu!=null)contextMenu.hide();
        selected=false;
      }
    });

    initMainCard();
  }

  public void initMainCard(){
    this.getChildren().clear();
    cardWidth=largeCardWidth;
    cardHeight=largeCardHeight;
    cardImage.setFitWidth(cardWidth);
    cardImage.setFitHeight(cardHeight);
    cardPane = new StackPane();
    ImageView cardImage = ImageGetter.getImageForCard(policy);
    cardImage.setFitWidth(cardWidth);
    cardImage.setFitHeight(cardHeight);
    cardPane.getChildren().add(cardImage);
    //Initialize Card Objects
    initializeGameCardPolygons();
    initializeGameCardText();
    updateTextOctagon();

    polygonPane = new AnchorPane();

    polygonPane.getChildren().addAll(
            topLeftPentagon, topTrapezoid, topRightPentagon,
            middleTextOctagon,
            bottomLeftPentagon, bottomTrapezoid, bottomRightPentagon,
            title,
            foodSelection, xSelection, informationText,regionSelection, textScrollPane
    );

    switch(actionPointCost)
    {
      case 3:
        pipThree = new Circle();
        pipThree.setRadius(cardHeight/52);
        AnchorPane.setBottomAnchor(pipThree, cardHeight/52);
        AnchorPane.setLeftAnchor(pipThree, cardWidth/4-cardHeight/52);
        pipTwo = new Circle();
        pipTwo.setRadius(cardHeight/52);
        polygonPane.getChildren().addAll(pipTwo, pipThree);
        AnchorPane.setBottomAnchor(pipTwo, cardHeight/52);
        AnchorPane.setLeftAnchor(pipTwo, cardWidth*3/4-cardHeight/52);
      case 1:
        pipOne = new Circle();
        pipOne.setRadius(cardHeight/52);
        polygonPane.getChildren().addAll(pipOne);
        AnchorPane.setBottomAnchor(pipOne, cardHeight/52);
        AnchorPane.setLeftAnchor(pipOne, cardWidth/2-cardHeight/52);
        break;
      case 2:
        pipOne = new Circle();
        pipOne.setRadius(cardHeight/52);
        AnchorPane.setBottomAnchor(pipOne, cardHeight/52);
        AnchorPane.setLeftAnchor(pipOne, cardWidth/3-cardHeight/52);
        pipTwo = new Circle();
        pipTwo.setRadius(cardHeight/52);
        polygonPane.getChildren().addAll(pipTwo, pipOne);
        AnchorPane.setBottomAnchor(pipTwo, cardHeight/52);
        AnchorPane.setLeftAnchor(pipTwo, cardWidth*2/3-cardHeight/52);
        break;
    }
    cardPane.getChildren().add(polygonPane);
    this.getChildren().add(cardPane);
    timer.start();
  }

  public void initSimpleCard(){
    this.getChildren().clear();
    cardWidth=smallCardWidth;
    cardHeight=smallCardHeight;
    this.getChildren().clear();
    cardImage = ImageGetter.getImageForCard(policy);
    cardImage.setFitHeight(cardHeight);
    cardImage.setFitWidth(cardWidth);
    StackPane stackPane= new StackPane();
    if(isDrafted) {
      stackPane.setStyle("-fx-padding: 5; \n" //#090a0c
              + "-fx-background-color: green;\n"
              + "-fx-background-radius: 5;\n"
              + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
    }else if(isDiscarded){
      stackPane.setStyle("-fx-padding: 5; \n" //#090a0c
              + "-fx-background-color: firebrick;\n"
              + "-fx-background-radius: 5;\n"
              + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
    }else{
      stackPane.setStyle("-fx-padding: 5; \n" //#090a0c
              + "-fx-background-color: black;\n"
              + "-fx-background-radius: 5;\n"
              + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
    }
stackPane.getChildren().add(cardImage);
    this.getChildren().add(stackPane);
    timer.stop();
  }

  public boolean isSelect(){
    return selected;
  }
  public void setSelected(boolean selected){
    this.selected=selected;
  }

  @Override
  public EnumRegion getOwner() {
    return owner;
  }

  @Override
  public EnumPolicy getPolicy() {
    return policy;
  }

  public PolicyCard getGameCard() {return gameCard;}
  private void openRightClickMenu(MouseEvent event){
    selected=true;
    if(contextMenu!=null){
      contextMenu.hide();
    }
    contextMenu=new ContextMenu();
    MenuItem draft=new MenuItem("Draft Card");

    draft.setOnAction(event1 -> {
      selected=false;
      isDrafted=true;
    });
    MenuItem discard=new MenuItem("Discard card");
    discard.setOnAction(event1 ->{
      selected=false;
      isDiscarded=true;
    });

    MenuItem undo=new MenuItem("Undo");
    undo.setOnAction(event1 -> {
      selected=false;
      isDiscarded=false;
      isDrafted=false;
    });

    if(isDrafted||isDiscarded){
      draft.setDisable(true);
      discard.setDisable(true);
      undo.setDisable(false);
    }else undo.setDisable(true);
    contextMenu.getItems().addAll(draft,discard,undo);
    contextMenu.show(this,event.getScreenX(),event.getScreenY());
    contextMenu.setAutoHide(true);

  }

  private void initializeGameCardPolygons()
  {
    if(policy.getDuration().equals(EnumPolicy.EnumPolicyDuration.INSTANT)) color=ORANGE;
    else if(policy.getPlayableState()!= GameState.DRAFTING) color=BLUE;
    else if(policy.getPlayableState()==GameState.DRAFTING) color=PURPLE;

    topTrapezoid.getPoints().setAll((cardWidth/9), 0.0,
            (cardWidth*8/9), 0.0,
            (cardWidth*7/9), cardHeight*1/13,
            (cardWidth*2/9), cardHeight*1/13);
    AnchorPane.setTopAnchor(topTrapezoid, 0.0);
    
    bottomTrapezoid.getPoints().setAll((cardWidth/9), cardHeight*1/13,
            (cardWidth*8/9), cardHeight*1/13,
            (cardWidth*7/9), 0.0,
            (cardWidth*7/9), 0.0,
            (cardWidth*2/9), 0.0,
            (cardWidth*2/9), 0.0);
    AnchorPane.setBottomAnchor(bottomTrapezoid, 0.0);

    bottomLeftPentagon.getPoints().setAll(0.0, 0.0,
            cardWidth/9, 0.0,
            cardWidth*2/9, cardHeight/13,
            cardWidth/9, cardHeight*2/13,
            0.0, cardHeight*2/13);
    AnchorPane.setBottomAnchor(bottomLeftPentagon, 0.0);
    AnchorPane.setLeftAnchor(bottomLeftPentagon, 0.0);
    
    topLeftPentagon.getPoints().setAll(0.0, 0.0,
            cardWidth/9, 0.0,
            cardWidth*2/9, cardHeight/13,
            cardWidth/9, cardHeight*2/13,
            0.0, cardHeight*2/13);
    AnchorPane.setTopAnchor(topLeftPentagon, 0.0);
    AnchorPane.setLeftAnchor(topLeftPentagon, 0.0);
    
    bottomRightPentagon.getPoints().setAll(0.0, 0.0,
            -cardWidth/9, 0.0,
            -cardWidth*2/9, cardHeight/13,
            -cardWidth/9, cardHeight*2/13,
            0.0, cardHeight*2/13);
    AnchorPane.setBottomAnchor(bottomRightPentagon, 0.0);
    AnchorPane.setRightAnchor(bottomRightPentagon, 0.0);

    topRightPentagon.getPoints().setAll(
            0.0, 0.0,
            -cardWidth/9, 0.0,
            -cardWidth*2/9, cardHeight/13, -
            cardWidth/9, cardHeight*2/13,
            0.0, cardHeight*2/13);
    AnchorPane.setTopAnchor(topRightPentagon, 0.0);
    AnchorPane.setRightAnchor(topRightPentagon, 0.0);

    
    middleTextOctagon.getPoints().setAll(octagonPoints);
    AnchorPane.setBottomAnchor(middleTextOctagon, (cardHeight / 13));
    
    
    ArrayList<Polygon> polygonList = new ArrayList<Polygon>();
    polygonList.addAll(Arrays.asList(topTrapezoid, bottomTrapezoid, bottomLeftPentagon, topLeftPentagon, bottomRightPentagon, topRightPentagon, middleTextOctagon));
    for(Polygon p : polygonList)
    {
      p.setStrokeType(StrokeType.INSIDE);
      p.setStrokeWidth(2.0);
      p.setFill(Color.web(color, transparency));
      p.setStroke(Color.BLACK);
      p.setOnMouseEntered(me -> p.setStroke(Color.YELLOW));
      p.setOnMouseExited(me -> p.setStroke(Color.BLACK));
    }
    middleTextOctagon.setOnMouseEntered(me -> {
      mouseOverOctagon = true;
      middleTextOctagon.setStroke(Color.YELLOW);
      middleTextOctagon.setStroke(Color.YELLOW);
      middleTextOctagon.setFill(Color.BLACK);
      rulesText.setFill(Color.WHITE);
    });
    middleTextOctagon.setOnMouseExited(me -> {
      mouseOverOctagon = false;
      middleTextOctagon.setStroke(Color.BLACK);
      middleTextOctagon.setFill(Color.web(color, transparency));
      rulesText.setFill(Color.BLACK);
    });
   
  }

  private void initializeGameCardText()
  {
  //Initialize Text fields
    title = new Text(gameCard.getTitle());
    title.setWrappingWidth(200);
    title.setLineSpacing(-5);
    title.setTextAlignment(TextAlignment.CENTER);
    ArrayList<Text> textList = new ArrayList<Text>();

    initComboBoxes();

    informationText = new Text("Info");
    textList.addAll(Arrays.asList(title, informationText));
    for(Text t : textList)
    {
     // t.setStroke(Color.WHITE);
      //t.setStrokeType(StrokeType.OUTSIDE);
      //t.setStrokeWidth(1);
      t.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, 16));
      t.setFill(Color.BLACK);
      t.setFontSmoothingType(FontSmoothingType.LCD);
    }


    rulesText = new Text(gameCard.getGameText());

    rulesText.setFill(Color.BLACK);
    rulesText.setFont(Font.font(12));
    rulesText.setOnMouseEntered(me -> {
      mouseOverOctagon = true;
      middleTextOctagon.setStroke(Color.YELLOW);
      middleTextOctagon.setFill(Color.BLACK);
      rulesText.setFill(Color.WHITE);
      rulesText.autosize();
    });
    rulesText.setWrappingWidth(200);
    
    flavorText = new Text(gameCard.getFlavorText());
    flavorText.setFill(Color.WHITE);
    flavorText.setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
    flavorText.setOnMouseEntered(me -> {
      mouseOverOctagon = true;
      middleTextOctagon.setStroke(Color.YELLOW);
      middleTextOctagon.setFill(Color.BLACK);
      rulesText.setFill(Color.WHITE);
      //rulesText.setFont(Font.font(16));
      rulesText.autosize();
    });
    flavorText.setWrappingWidth(200);

    TextFlow textFlow=new TextFlow(rulesText,new Text("\n\n"),flavorText);
    textScrollPane =new ScrollPane(textFlow);
    textScrollPane.setBackground(Background.EMPTY);
    textScrollPane.getStylesheets().add("cardStyle.css");
    textScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    textScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    textScrollPane.setPrefSize(cardWidth*9/18,cardHeight*3/26);
    textFlow.setPrefSize(cardWidth*9/18,cardHeight*2/13);


    AnchorPane.setTopAnchor(title, cardHeight/288);
    AnchorPane.setLeftAnchor(title, cardWidth/4);

    AnchorPane.setTopAnchor(regionSelection, cardHeight/72);
    AnchorPane.setLeftAnchor(regionSelection, cardWidth/144);

    AnchorPane.setTopAnchor(foodSelection, cardHeight/72);
    AnchorPane.setRightAnchor(foodSelection, cardWidth/144);
   /// AnchorPane.setTopAnchor(foodSelection,0.0);
   // AnchorPane.setRightAnchor(foodSelection, 0.0);

    AnchorPane.setBottomAnchor(xSelection, cardHeight/72);
    AnchorPane.setRightAnchor(xSelection, cardWidth/144);
    
//    AnchorPane.setTopAnchor(rulesText, cardHeight*10/13-textOctagonHeightModifier);
//    AnchorPane.setLeftAnchor(rulesText, cardWidth/4);

    AnchorPane.setTopAnchor(textScrollPane, cardHeight*10/13-textOctagonHeightModifier);
    AnchorPane.setLeftAnchor(textScrollPane, cardWidth/4);

    AnchorPane.setBottomAnchor(flavorText, cardHeight*2/13);
    AnchorPane.setLeftAnchor(flavorText, cardWidth/4);

    AnchorPane.setBottomAnchor(informationText, cardHeight/18);
    AnchorPane.setLeftAnchor(informationText, cardWidth/18);


  }

  private void initComboBoxes(){
    //Configurations for X options
    int[] xOptions=policy.getOptionsX();
    if(xOptions!=null){
      if(xOptions.length==1){
        xSelection =new Text(""+xOptions[0]);
      }
      else{
        ArrayList<Integer> arrayList=new ArrayList<>();
        for(int value:xOptions) arrayList.add(value);
        ObservableList list= FXCollections.observableList(arrayList);
        ComboBox<Integer> comboBox= new ComboBox(list);
        ListCell<Integer> integerListCell=new ListCell<Integer>(){
          @Override
          protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item,empty);
            if(item==null||empty)
            {
              setItem(null);
              setGraphic(null);
            }else{

              Label label=new Label(item.toString());

              label.setFont(Font.font("helvetica",FontWeight.BOLD,(50)));
              if(item>9)label.setTranslateX(-10);
              else label.setTranslateX(10);
              label.setMinWidth(75);
              label.setTextFill(Color.BLACK);
              setGraphic(label);
            }

          }
        };
        comboBox.setButtonCell(integerListCell);
        if(gameCard.getX()==0){
          comboBox.getSelectionModel().select(0);
          gameCard.setX(comboBox.getSelectionModel().getSelectedItem());
        }else{
          comboBox.getSelectionModel().select(new Integer(gameCard.getX()));
        }
        comboBox.setOnMouseEntered(event -> selected=true);
        comboBox.setOnMouseExited(event -> selected=false );
        comboBox.setOnAction(event -> gameCard.setX(comboBox.getSelectionModel().getSelectedItem()));
        comboBox.getStylesheets().add("cardStyle.css");
        comboBox.setStyle("-fx-font: 15px \"Serif\";");

        xSelection =comboBox;
      }
    }else xSelection =new Text("");

    //Configuration for Target Food
    EnumFood[] foodOptions=policy.getOptionsFood();
    ListCell<EnumFood> listCell= new ListCell<EnumFood>(){
      @Override
      protected void updateItem(EnumFood item,boolean empty){
        super.updateItem(item,empty);
        if (item == null || empty) {
          setItem(null);
          setGraphic(null);
        } else {
          ImageView image = new ImageView(item.getIconSmall());
          image.setTranslateX(-8);
          setGraphic(image);
        }
      }
    };
    if(foodOptions!=null){
      ComboBox<EnumFood> comboBox=new ComboBox<>(FXCollections.observableList(Arrays.asList(foodOptions)));
      comboBox.setCellFactory(new Callback<ListView<EnumFood>, ListCell<EnumFood>>() {
        @Override public ListCell<EnumFood> call(ListView<EnumFood> p) {
          return new ListCell<EnumFood>() {
            @Override protected void updateItem(EnumFood item, boolean empty) {
              super.updateItem(item, empty);
              if (item == null || empty) {
                setItem(null);
                setGraphic(null);
              } else {
                ImageView image = new ImageView(item.getIconSmall());
                image.setFitWidth(foodIconWidth);
                image.setFitHeight(foodIconHeight);
                setGraphic(image);
                setTooltip(new Tooltip(item.toString()));
              }
            }
          };
        }
      });


      if(gameCard.getTargetFood()==null){
        comboBox.getSelectionModel().select(0);
        gameCard.setTargetFood(comboBox.getSelectionModel().getSelectedItem());
      }else{
        comboBox.getSelectionModel().select(gameCard.getTargetFood());
      }
      comboBox.setOnMouseEntered(event -> selected=true);
      comboBox.setOnMouseExited(event -> selected=false );
      comboBox.setOnAction(event ->{
        gameCard.setTargetFood(comboBox.getSelectionModel().getSelectedItem());
        comboBox.setButtonCell(listCell);
      });
      comboBox.setButtonCell(listCell);
      comboBox.getStylesheets().add("cardStyle.css");
      foodSelection =comboBox;
    }else foodSelection = new Text("");

    //Configurations for Region selection
    EnumRegion[] regions=policy.getOptionsRegions(owner);
    if(regions!=null){
      if(regions.length==1){
        // textList.add((Text)xSelection);
        regionSelection=new Text(regions[0].toString());
      }
      else{
        ObservableList list= FXCollections.observableList(Arrays.asList(regions));
        ComboBox<EnumRegion> comboBox= new ComboBox(list);
        if(gameCard.getTargetRegion()==null){
          comboBox.getSelectionModel().select(0);
          gameCard.setTargetRegion(comboBox.getSelectionModel().getSelectedItem());
        }else{
          comboBox.getSelectionModel().select(gameCard.getTargetRegion());
        }
        comboBox.setOnMouseEntered(event -> selected=true);
        comboBox.setOnMouseExited(event -> selected=false );
        comboBox.setOnAction(event -> gameCard.setTargetRegion(comboBox.getSelectionModel().getSelectedItem()));
        comboBox.getStylesheets().add("cardStyle.css");
        regionSelection=comboBox;
      }
    }else regionSelection=new Text("");
  }
  private void updateTextOctagon()
  {
    octagonPoints = new Double[]{ 
        (cardWidth*2/9), 0.0,
        (cardWidth*2/9), 0.0,
        (cardWidth*7/9), 0.0, 
        (cardWidth*8/9), (cardHeight/13),
        (cardWidth*8/9), (cardHeight/13)  +textOctagonHeightModifier,
        (cardWidth*7/9), (cardHeight*2/13)+textOctagonHeightModifier,
        (cardWidth*2/9), (cardHeight*2/13)+textOctagonHeightModifier,
        (cardWidth/9),   (cardHeight/13)  +textOctagonHeightModifier, 
        (cardWidth/9),   (cardHeight/13)
        };
    middleTextOctagon.getPoints().setAll(octagonPoints);
    AnchorPane.setBottomAnchor(middleTextOctagon, (cardHeight / 13));
    AnchorPane.setTopAnchor(textScrollPane, cardHeight*10/13-textOctagonHeightModifier+5);
    textScrollPane.setPrefHeight(cardHeight*3/26+textOctagonHeightModifier);
  }
  public StackPane getCardView()
  {
    return cardPane;
  }
  
  //Handles Animation of the Text area
  AnimationTimer timer = new AnimationTimer()
  {
    double speed = cardHeight/39;
    
    @Override
    public void handle(long now) 
    {
      if(mouseOverOctagon)
      {
        if(textOctagonHeightModifier < cardHeight*3/13 && gameCard.getFlavorText() != null)
        {
          textOctagonHeightModifier+=speed;
          updateTextOctagon();
        }
        else
        {
          flavorText.setVisible(true);
        }
      }
      else if(!mouseOverOctagon )
      {
        if( textOctagonHeightModifier > 0)
        {
          textOctagonHeightModifier-=speed;
          updateTextOctagon();
        }
        if(flavorText!=null)flavorText.setVisible(false);
      }
    }
  };
  
  
  
  @Override
  public void onResize() 
  {

//    initializeGameCardPolygons();
  }

    @Override
    public void reset() {

    }

}
