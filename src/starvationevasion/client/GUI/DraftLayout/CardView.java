package starvationevasion.client.GUI.DraftLayout;
  
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import starvationevasion.client.GUI.ResizablePane;
import starvationevasion.client.GUI.images.ImageGetter;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.EnumPolicy;
import starvationevasion.common.gamecards.GameCard;


public class CardView extends ResizablePane
{
  private double cardWidth = 370;
  private double cardHeight = 520;
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
  private GameCard gameCard;

  private Polygon topTrapezoid        = new Polygon();
  private Polygon bottomTrapezoid     = new Polygon();
  private Polygon bottomLeftPentagon  = new Polygon();
  private Polygon topLeftPentagon     = new Polygon();
  private Polygon bottomRightPentagon = new Polygon();
  private Polygon topRightPentagon    = new Polygon();
  private Polygon middleTextOctagon   = new Polygon();
  Circle pipOne, pipTwo, pipThree;
  private Text title, voteNumberText, voteCostText, rulesText, flavorText, informationText;
  
  public CardView(EnumRegion owner, EnumPolicy policy)
  {
    gameCard = GameCard.create(owner, policy);
    actionPointCost = gameCard.actionPointCost(policy);
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
        voteNumberText, voteCostText, informationText
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

    timer.start();
  }
  private void initializeGameCardPolygons() 
  {
    topTrapezoid.getPoints().setAll(new Double[] {
        (cardWidth/9),   0.0,
        (cardWidth*8/9), 0.0,
        (cardWidth*7/9), cardHeight*1/13, 
        (cardWidth*2/9), cardHeight*1/13 
        });
    AnchorPane.setTopAnchor(topTrapezoid, 0.0);
    
    bottomTrapezoid.getPoints().setAll(new Double[] { 
        (cardWidth/9),   cardHeight*1/13,
        (cardWidth*8/9), cardHeight*1/13,
        (cardWidth*7/9), 0.0,
        (cardWidth*7/9), 0.0,
        (cardWidth*2/9), 0.0,
        (cardWidth*2/9), 0.0 
        });
    AnchorPane.setBottomAnchor(bottomTrapezoid, 0.0);

    bottomLeftPentagon.getPoints().setAll(new Double[] {
        0.0,           0.0,
        cardWidth/9,   0.0, 
        cardWidth*2/9, cardHeight/13,
        cardWidth/9,   cardHeight*2/13,
        0.0,           cardHeight*2/13
        });
    AnchorPane.setBottomAnchor(bottomLeftPentagon, 0.0);
    AnchorPane.setLeftAnchor(bottomLeftPentagon, 0.0);
    
    topLeftPentagon.getPoints().setAll(new Double[] {
        0.0,           0.0, 
        cardWidth/9,   0.0, 
        cardWidth*2/9, cardHeight/13, 
        cardWidth/9,   cardHeight*2/13,
        0.0,           cardHeight*2/13 
        });
    AnchorPane.setTopAnchor(topLeftPentagon, 0.0);
    AnchorPane.setLeftAnchor(topLeftPentagon, 0.0);
    
    bottomRightPentagon.getPoints().setAll(new Double[] { 
        0.0,            0.0,
        -cardWidth/9,   0.0,
        -cardWidth*2/9, cardHeight/13,
        -cardWidth/9,   cardHeight*2/13, 
        0.0,            cardHeight*2/13 
        });
    AnchorPane.setBottomAnchor(bottomRightPentagon, 0.0);
    AnchorPane.setRightAnchor(bottomRightPentagon, 0.0);

    topRightPentagon.getPoints().setAll(
        new Double[] { 
        0.0,            0.0, 
        -cardWidth/9,   0.0, 
        -cardWidth*2/9, cardHeight/13, -
        cardWidth/9,    cardHeight*2/13, 
        0.0,            cardHeight*2/13
        });
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
      p.setOnMouseExited(new EventHandler<MouseEvent>()
      {  
        public void handle(MouseEvent me) 
        {
          p.setStroke(Color.BLACK);
        }
      });
    }
    middleTextOctagon.setOnMouseEntered(new EventHandler<MouseEvent>() 
    {
      public void handle(MouseEvent me) 
      {
        mouseOverOctagon = true;
        middleTextOctagon.setStroke(Color.YELLOW);
        middleTextOctagon.setFill(Color.BLACK);
      }
    });
    middleTextOctagon.setOnMouseExited(new EventHandler<MouseEvent>()
    {  
      public void handle(MouseEvent me) 
      {
        mouseOverOctagon = false;
        middleTextOctagon.setStroke(Color.BLACK);
        middleTextOctagon.setFill(Color.web(color, transparency));
      }
    });
    
   
  }
  private void initializeGameCardText()
  {
  //Initialize Text fields
    title = new Text(gameCard.getTitle());
    title.setWrappingWidth(200);
    
    voteNumberText = new Text(""+gameCard.votesRequired());
    voteCostText = new Text("$200");
    informationText = new Text("Info");
    
    ArrayList<Text> textList = new ArrayList<Text>();
    textList.addAll(Arrays.asList(title, voteNumberText, voteCostText, informationText));
    for(Text t : textList)
    {
      t.setStroke(Color.BLACK);
      t.setStrokeType(StrokeType.OUTSIDE);
      t.setStrokeWidth(1);
      t.setFont(Font.font("Helvetica", 18));
      t.setFill(Color.WHITE);
      t.setFontSmoothingType(FontSmoothingType.LCD);
    }
    
    
    
    rulesText = new Text(gameCard.getGameText());
    rulesText.setFill(Color.WHITE);
    rulesText.setFont(Font.font(12));
    rulesText.setOnMouseEntered(new EventHandler<MouseEvent>() 
    {
      public void handle(MouseEvent me) 
      {
        mouseOverOctagon = true;
        middleTextOctagon.setStroke(Color.YELLOW);
        middleTextOctagon.setFill(Color.BLACK);
      }
    });
    rulesText.setWrappingWidth(200);
    
    flavorText = new Text(gameCard.getFlavorText());
    flavorText.setFill(Color.WHITE);
    flavorText.setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
    flavorText.setOnMouseEntered(new EventHandler<MouseEvent>() 
    {
      public void handle(MouseEvent me) 
      {
        mouseOverOctagon = true;
        middleTextOctagon.setStroke(Color.YELLOW);
        middleTextOctagon.setFill(Color.BLACK);
      }
    });
    flavorText.setWrappingWidth(200);
    
    AnchorPane.setTopAnchor(title, cardHeight/36);
    AnchorPane.setLeftAnchor(title, cardWidth/4);
    
    AnchorPane.setTopAnchor(voteNumberText, cardHeight/18);
    AnchorPane.setRightAnchor(voteNumberText, cardWidth/18);
    
    AnchorPane.setBottomAnchor(voteCostText, cardHeight/18);
    AnchorPane.setRightAnchor(voteCostText, cardWidth/18);
    
    AnchorPane.setTopAnchor(rulesText, cardHeight*10/13-textOctagonHeightModifier);
    AnchorPane.setLeftAnchor(rulesText, cardWidth/4);

    AnchorPane.setBottomAnchor(flavorText, cardHeight*2/13);
    AnchorPane.setLeftAnchor(flavorText, cardWidth/4);

    AnchorPane.setBottomAnchor(informationText, cardHeight/18);
    AnchorPane.setLeftAnchor(informationText, cardWidth/18);
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
        flavorText.setVisible(false);
      }
    }
  };
  
  
  
  @Override
  public void onResize() 
  {
//    initializeGameCardPolygons();
  }

}
