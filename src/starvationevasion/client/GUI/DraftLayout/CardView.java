package starvationevasion.client.GUI.DraftLayout;
  
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.*;
import javafx.util.Callback;
import starvationevasion.client.GUI.images.ImageGetter;
import starvationevasion.common.EnumFood;
import starvationevasion.common.EnumPolicy;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;

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
  private Node voteCostText, foodSelection,regionSelection;
  private EnumRegion owner;
  private EnumPolicy policy;

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
      initSimpleCard();
      setTranslateY(0);
      setTranslateX(0);
    });
  }

  public CardView(EnumPolicy policy) {

    this.policy = policy;
    owner=EnumRegion.USA_HEARTLAND;
    gameCard = new PolicyCard(policy, owner);
    actionPointCost = gameCard.getActionPointCost();

    initMainCard();
  }
  public void initMainCard(){
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
            rulesText, flavorText,
            foodSelection, voteCostText, informationText,regionSelection
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
    cardWidth=smallCardWidth;
    cardHeight=smallCardHeight;
    this.getChildren().clear();
    cardImage = ImageGetter.getImageForCard(policy);
    cardImage.setFitHeight(cardHeight);
    cardImage.setFitWidth(cardWidth);
    this.getChildren().add(cardImage);
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

  private void initializeGameCardPolygons()
  {
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
      p.setOnMouseEntered(new EventHandler<MouseEvent>()
      {
        public void handle(MouseEvent me)
        {
          p.setStroke(Color.YELLOW);
        }
      });
      p.setOnMouseExited(me -> p.setStroke(Color.BLACK));
    }
    middleTextOctagon.setOnMouseEntered(me -> {
      mouseOverOctagon = true;
      middleTextOctagon.setStroke(Color.YELLOW);
      middleTextOctagon.setStroke(Color.YELLOW);
      middleTextOctagon.setFill(Color.BLACK);
    });
    middleTextOctagon.setOnMouseExited(me -> {
      mouseOverOctagon = false;
      middleTextOctagon.setStroke(Color.BLACK);
      middleTextOctagon.setFill(Color.web(color, transparency));
    });
    
   
  }
  private void initializeGameCardText()
  {
  //Initialize Text fields
    title = new Text(gameCard.getTitle());
    title.setWrappingWidth(200);
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
    });
    rulesText.setWrappingWidth(200);
    
    flavorText = new Text(gameCard.getFlavorText());
    flavorText.setFill(Color.WHITE);
    flavorText.setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
    flavorText.setOnMouseEntered(me -> {
      mouseOverOctagon = true;
      middleTextOctagon.setStroke(Color.YELLOW);
      middleTextOctagon.setFill(Color.BLACK);
    });
    flavorText.setWrappingWidth(200);
    
    AnchorPane.setTopAnchor(title, cardHeight/36);
    AnchorPane.setLeftAnchor(title, cardWidth/4);

    AnchorPane.setTopAnchor(regionSelection, cardHeight/18);
    AnchorPane.setLeftAnchor(regionSelection, 0.0);

    AnchorPane.setTopAnchor(foodSelection, cardHeight/18);
   // AnchorPane.setRightAnchor(foodSelection, cardWidth/18);
//    AnchorPane.setTopAnchor(foodSelection,0.0);
    AnchorPane.setRightAnchor(foodSelection, 0.0);

    AnchorPane.setBottomAnchor(voteCostText, cardHeight/18);
    AnchorPane.setRightAnchor(voteCostText, cardWidth/18);
    
    AnchorPane.setTopAnchor(rulesText, cardHeight*10/13-textOctagonHeightModifier);
    AnchorPane.setLeftAnchor(rulesText, cardWidth/4);

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
       // textList.add((Text)voteCostText);
        voteCostText=new Text(""+xOptions[0]);
      }
      else{
        ArrayList<Integer> arrayList=new ArrayList<>();
        for(int value:xOptions) arrayList.add(value);
        ObservableList list= FXCollections.observableList(arrayList);
        ComboBox<Integer> comboBox= new ComboBox(list);
        comboBox.getSelectionModel().select(0);
        voteCostText=comboBox;
      }
    }else voteCostText=new Text("");

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
          image.setFitWidth(foodIconWidth);
          image.setFitHeight(foodIconHeight);
          setGraphic(image);
        }
      }
    };
    if(foodOptions!=null){
      ComboBox<EnumFood> comboBox=new ComboBox<EnumFood>(FXCollections.observableList(Arrays.asList(foodOptions)));

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
              }
            }
          };
        }
      });


      comboBox.setOnAction(event -> comboBox.setButtonCell(listCell));
      comboBox.setButtonCell(listCell);
      comboBox.getSelectionModel().select(0);

      foodSelection =comboBox;
    }else foodSelection = new Text("");

    //Configurations for Region selection
    EnumRegion[] regions=policy.getOptionsRegions(owner);
    System.out.println(Arrays.toString(regions));
    if(regions!=null){
      if(regions.length==1){
        // textList.add((Text)voteCostText);
        regionSelection=new Text(regions[0].toString());
      }
      else{
        ObservableList list= FXCollections.observableList(Arrays.asList(regions));
        ComboBox<Integer> comboBox= new ComboBox(list);
        comboBox.getSelectionModel().select(0);
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
    AnchorPane.setTopAnchor(rulesText, cardHeight*10/13-textOctagonHeightModifier+5);
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
