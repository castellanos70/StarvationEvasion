package starvationevasion.GuiTestCode.mainHud.hand;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import starvationevasion.GuiTestCode.common.NodeTemplate;

public class HandNode extends NodeTemplate
{
  private static final double MAX_SMALL_SIZE = .5;
  private static final double MIN_MOUSE_PICKUP = .4;
  private static final double MAX_HOVER_HEIGHT = 1;
  private static final double CURVE_BREADTH = .2;
  private static final double MIN_GAP_SIZE = 1/8d;
  
  private CardNode[] cards = new CardNode[7];
  private double[] xLayouts = new double[7];
  private double lastWidth = 0;
  private double lastHeight = 0;
  
  public HandNode(double width, double height){
    super();
    this.setWidth(width);
    this.setHeight(height);
    
    this.setOnMouseMoved(new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event)
      {
        calculateMouseOver(event.getX(), event.getY());
      }
    });
    
    this.setOnMouseExited(new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event)
      {
        onResize();
      }
    });
    
    for (int i = 0; i < cards.length; i++){
      cards[i] = new CardNode(i);
      cards[i].setManaged(false);
      this.getChildren().add(cards[i]);
    }
    
    onResize();
  }

  @Override
  public void onResize()
  {
    double width = this.getWidth()/7;
    double height = this.getHeight();
    
    for (int i = 0; i < cards.length; i++){
      
      CardNode card = cards[i];
      if (card == null) return;
      double cardWidth = width*(7d/8d);
      double cardHeight = cardWidth/card.getSizeRatio();
      
      if (cardHeight > height*MAX_SMALL_SIZE){
        cardHeight = height*MAX_SMALL_SIZE;
        cardWidth = cardHeight*card.getSizeRatio();
      }
      
      card.setSize(cardWidth, cardHeight);
      card.setLayoutX(width*i);
      card.setLayoutY(height - cardHeight);
      
      xLayouts[i] = width*i;
      lastWidth = cardWidth;
      lastHeight = cardHeight;
    }
  }
  
  private void calculateMouseOver(double x, double y){
    
    double minMousePickup = this.getHeight()*MIN_MOUSE_PICKUP;
    
    if (y > minMousePickup){
      
      double height = this.getHeight();
      double width = this.getWidth();
      double maxMousePickup = height - lastHeight/2;
      if (y > maxMousePickup) y = maxMousePickup;
      
      double peak = (y-minMousePickup)/(maxMousePickup - minMousePickup);
      
      peak *= peak;
      
      double shift = x/width;
      double breadth = CURVE_BREADTH;
      
      double[] constraints = new double[7];
      double total = 0;
      
      for (int i = 0; i < 7; i++){
        double constraint = (i/7d + 1/14d);
        
        {//gaussian equation
          constraint -= shift;
          constraint *= constraint;
          constraint /= (breadth*breadth);
          constraint *= -1;
          constraint = 1/7d + peak*Math.pow(Math.E, constraint);
        }
        
        constraints[i] = constraint;
        total += constraint;
//        System.out.println(constraint + ", " + i);
        
      }
//      System.out.println("---------------");
      double totalWidth = 0;
      
      for (int i = 0; i < 7; i++){
        CardNode card = cards[i];
        
        double sizeFactor = card.getSizeRatio();
        double cardWidth = (constraints[i]/total)*width;
        double tempWidth = cardWidth;
        
        if (cardWidth/sizeFactor > height*MAX_HOVER_HEIGHT){
          cardWidth = height*MAX_HOVER_HEIGHT*sizeFactor;
        }
        
        double xPos = totalWidth + (tempWidth - cardWidth)/2;
        totalWidth += tempWidth;
        
        cardWidth -= cardWidth*MIN_GAP_SIZE;
        double cardHeight = cardWidth/card.getSizeRatio();
        double yPos = height - cardHeight;
        
        card.setSize(cardWidth, cardHeight);
        card.setLayoutX(xPos);
        card.setLayoutY(yPos);
      }
      
    } else {
      
      for (int i = 0; i < cards.length; i++){
        CardNode card = cards[i];
        card.setSize(lastWidth, lastHeight);
        card.setLayoutX(xLayouts[i]);
        card.setLayoutY(this.getHeight() - lastHeight);
      }
    }
  }
  
}
