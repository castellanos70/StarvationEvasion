package starvationevasion.client.GUI.DraftLayout;

import java.util.ArrayList;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.GUI.ResizablePane;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.gamecards.EnumPolicy;

/**
 * @author Ben Matthews
 * 
 * HandNode creates a ResizablePane that contains an array
 * of CardNode objects. Cards are dynamically resized using a gaussian
 * area distribution based on the location of the mouse, e.g. a card hovered
 * over directly by themouse will be much larger than the other cards surounding it
 *
 */
public class HandNode extends ResizablePane
{
  private static final boolean DEBUG = false;
  private static final double MAX_SMALL_SIZE = 1;
  private static final double MIN_MOUSE_PICKUP = 0;
  private static final double MAX_HOVER_HEIGHT = 2;
  private static final double CURVE_BREADTH = .2;
  private static final double MIN_GAP_SIZE = 0;
  private static final double CARD_RATIO = .77;
  
  private ArrayList<CardNode> cards;
  private ArrayList<EnumPolicy> policies;
  private ArrayList<Double> xLayouts;
  private double lastWidth = 0;
  private double lastHeight = 0;
  
  private GUI gui;
  
  public HandNode(GUI gui){
    super();
    this.gui = gui;
    this.setPickOnBounds(false);
    
    cards = new ArrayList<>();
    policies = new ArrayList<>();
    xLayouts = new ArrayList<>();
    
    if (DEBUG) { //creates a default hand
      for (int i = 0; i < 7; i++){
        int index = (int) (Math.random()*EnumPolicy.values().length);
        EnumPolicy policy = EnumPolicy.values()[index];
        policies.add(policy);
        CardNode card = new CardNode(EnumRegion.USA_CALIFORNIA, policy);
        card.setCards(cards);
        cards.add(card);
        card.setManaged(false);
        this.getChildren().add(card);
      }
    }
    
    this.setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, .6), null, null)));
    
//    this.setOnMouseMoved(new EventHandler<MouseEvent>(){
//      @Override
//      public void handle(MouseEvent event)
//      {
//        calculateMouseOver(event.getX(), event.getY());
//      }
//    });
//    
//    this.setOnMouseExited(new EventHandler<MouseEvent>(){
//      @Override
//      public void handle(MouseEvent event)
//      {
//        onResize();
//      }
//    });
    
    onResize();
  }
  
  /**
   * Resets drafts or discards performed on this hand
   */
  public void reset(){
    for (CardNode c: cards){
      if (c != null){
        c.reset();
      }
    }
  }
  
  /**
   * Returns an arraylist of all the Drafted Cards in this
   * hand
   * 
   * @return
   */
  public ArrayList<CardNode> getDraftedCards(){
    ArrayList<CardNode> draftedCards = new ArrayList<>();
    
    for (CardNode c: cards){
      if (c.isDrafted){
        draftedCards.add(c);
      }
    }
    
    return draftedCards;
  }
  
  /**
   * Returns an arraylist of all the Discarded Cards in this
   * hand
   * 
   * @return
   */
  public ArrayList<CardNode> getDiscardedCards(){
    ArrayList<CardNode> discardedCards = new ArrayList<>();
    
    for (CardNode c: cards){
      if (c.isDiscarded){
        discardedCards.add(c);
      }
    }
    
    return discardedCards;
  }
  
  /**
   * sets the array of enumPolicies that will represent this users hand
   * 
   * @param policies
   */
  public void setPolicies(EnumPolicy[] policies){
    this.policies.clear();
    for (EnumPolicy policy: policies){
      this.policies.add(policy);
    }
    createCards();
  }
  
  /**
   * Returns the current EnumPolicy array
   */
  public EnumPolicy[] getPolicies(){
    EnumPolicy[] holder = new EnumPolicy[policies.size()];
    for (int i = 0; i < policies.size(); i++){
      holder[i] = policies.get(i);
    }
    
    return holder;
  }
  
  /**
   * Creates the CardNode elements that will be displayed in this pane
   */
  public void createCards(){
    if (gui == null) return;
    removeOldCards();
    for (int i = 0; i < policies.size(); i++){
      CardNode card = new CardNode(gui.getAssignedRegion(), policies.get(i));
      card.setCards(cards);
      card.setManaged(false);
      xLayouts.add(new Double(0));
      cards.add(card);
      this.getChildren().add(card);
    }
    onResize();
    for (CardNode c: cards){
      c.onResize();
    }
  }
  
  public void removeOldCards()
  {
    xLayouts.clear();
    for (int i = 0; i < cards.size(); i++){
      this.getChildren().remove(cards.get(i));
    }
    cards.clear();
  }
  

  @Override
  public void onResize()
  {
    if (cards.size() == 0) return;
    
    double width = this.getWidth()/(double)cards.size();
    double height = this.getHeight();
    
    for (int i = 0; i < cards.size(); i++){
      
      CardNode card = cards.get(i);
      if (card == null) return;
      double cardWidth = width*(1d - MIN_GAP_SIZE);
      double cardHeight = cardWidth/CARD_RATIO;
      
      if (cardHeight > height*MAX_SMALL_SIZE){
        cardHeight = height*MAX_SMALL_SIZE;
        cardWidth = cardHeight*CARD_RATIO;
      }
      
      card.setSize(cardWidth, cardHeight);
      card.setLayoutX(width*i);
      card.setLayoutY(height - cardHeight);
      
      if (xLayouts.size() - 1 < i){
        xLayouts.add(new Double(0));
      }
      xLayouts.set(i, width*i);
      
      lastWidth = cardWidth;
      lastHeight = cardHeight;
    }
  }
  
  /**
   * calculates the guassian distribution of sizes based on the
   * given relative x and y position of the mouse
   * 
   * @param x
   * @param y
   */
  public void calculateMouseOver(double x, double y){
    if (cards.size() == 0) return;
    
//    double halfWidth = this.getWidth()/2;
//    double xModifier = 10d - 2*Math.abs(x - halfWidth)/halfWidth;
//    System.out.println(xModifier);
    
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
      
      double[] constraints = new double[cards.size()];
      double total = 0;
      
      for (double i = 0; i < 7; i++){
        double constraint = (i/cards.size() + 1/14d);
        
        {//gaussian equation
          constraint -= shift;
          constraint *= constraint;
          constraint /= (breadth*breadth);
          constraint *= -1;
          double a = peak*Math.pow(Math.E, constraint);
          constraint = 1d/cards.size() + a*a;
        }
        
        constraints[(int)i] = constraint;
        total += constraint;
        
      }
      double totalWidth = 0;
      
      for (int i = 0; i < cards.size(); i++){
        CardNode card = cards.get(i);
        
        double sizeFactor = CARD_RATIO;
        double cardWidth = (constraints[i]/total)*width;
        double tempWidth = cardWidth;
        
        if (cardWidth/sizeFactor > height*MAX_HOVER_HEIGHT){
          cardWidth = height*MAX_HOVER_HEIGHT*sizeFactor;
        }
        
        double xPos = totalWidth + (tempWidth - cardWidth)/2;
        totalWidth += tempWidth;
        
        cardWidth -= cardWidth*MIN_GAP_SIZE;
        double cardHeight = cardWidth/CARD_RATIO;
        double yPos = height - cardHeight;
        double scaleX = cardWidth/card.getWidth();
        double scaleY = cardHeight/card.getHeight();
        card.setScaleX(scaleX);
        card.setScaleY(scaleY);
        card.setLayoutX(xPos + (cardWidth - card.getWidth())/2);
        card.setLayoutY(yPos + (cardHeight - card.getHeight())/2);
      }
      
    } else {
      
      for (int i = 0; i < cards.size(); i++){
        CardNode card = cards.get(i);
        card.setSize(lastWidth, lastHeight);
        card.setLayoutX(xLayouts.get(i));
        card.setLayoutY(this.getHeight() - lastHeight);
      }
    }
  }
  
}
