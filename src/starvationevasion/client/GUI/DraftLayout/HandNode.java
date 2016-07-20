package starvationevasion.client.GUI.DraftLayout;

import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import starvationevasion.client.GUI.GUI;
import starvationevasion.client.GUI.ResizablePane;
import starvationevasion.common.EnumPolicy;

import java.util.ArrayList;
import java.util.Arrays;


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
  private final boolean TEST_LAYOUT =true;

  private static final double MAX_SMALL_SIZE = 1;
  private static final double MIN_MOUSE_PICKUP = 0;
  private static final double MAX_HOVER_HEIGHT = 2;
  private static final double CURVE_BREADTH = .2;
  private static final double MIN_GAP_SIZE = 0;
  private static final double CARD_RATIO = .77;
  
  private AbstractCard[] cards;// = new ResizablePane[7];
  //private ResizablePane[] cards = new ResizablePane[7];
  private EnumPolicy[] policies;
  private double[] xLayouts = new double[7];
  private double lastWidth = 0;
  private double lastHeight = 0;
  
  private GUI gui;


  public HandNode(GUI gui){
    super();
    if(TEST_LAYOUT) cards=new AbstractCard[7];
    else cards=new CardNode[7];
    this.gui = gui;
    this.setPickOnBounds(false);
    
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
    for (AbstractCard c: cards){
      if (c != null){
        c.reset();
      }
    }
  }

  @Override
  public Node getCardView() {
    return null;
  }

  /**
   * Returns an arraylist of all the Drafted Cards in this
   * hand
   * 
   * @return
   */
  public ArrayList<AbstractCard> getDraftedCards(){
    ArrayList<AbstractCard> draftedCards = new ArrayList<>();
    
    for (AbstractCard c: cards){
      if (c!=null&&c.isDrafted){
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
  public ArrayList<AbstractCard> getDiscardedCards(){
    ArrayList<AbstractCard> discardedCards = new ArrayList<>();
    
    for (AbstractCard c: cards){
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

    if(policies!=null&&policies.length!=0&&!policies.equals(this.policies))
    {
      this.policies = policies;
      createCards();
    }
  }
  
  /**
   * Returns the current EnumPolicy array
   */
  public EnumPolicy[] getPolicies(){
    return policies;
  }
  
  /**
   * Creates the CardNode elements that will be displayed in this pane
   */
  public void createCards(){
    removeOldCards();
    VBox vBox=new VBox();
    for (int i = 0; i < policies.length; i++){
      if(TEST_LAYOUT)cards[i] = new CardView(gui.getAssignedRegion(), policies[i]);
      else cards[i] = new CardNode(gui.getAssignedRegion(), policies[i]);
      cards[i].setManaged(false);
//      if(TEST_LAYOUT){
//       // vBox.setPrefSize(185,260);
//        StackPane pane=(StackPane)cards[i].getCardView();
//
//        pane.setPrefSize(92.5,130);
//        System.out.println(pane.getWidth()+ " " + pane.getHeight());
//        System.out.println(pane.getChildren());
//        vBox.getChildren().add(pane);

      vBox.getChildren().add(cards[i]);
//      }
      //vBox.getChildren().add(cards[i]);
      //this.getChildren().add(vBox);
    }
    this.getChildren().add(vBox);
    onResize();
  }
  
  public void removeOldCards()
  {
    this.getChildren().clear();
    for (int i = 0; i < cards.length; i++){
      this.getChildren().remove(cards[i]);
    }
  }
  

  @Override
  public void onResize()
  {
    if (cards[0] == null) return;
    
    double width = this.getWidth()/7;
    double height = this.getHeight();
    
    for (int i = 0; i < cards.length; i++){

      AbstractCard card = cards[i];
      if (card == null) return;
      double cardWidth = width*(7d/8d);
      double cardHeight = cardWidth/CARD_RATIO;
      
      if (cardHeight > height*MAX_SMALL_SIZE){
        cardHeight = height*MAX_SMALL_SIZE;
        cardWidth = cardHeight*CARD_RATIO;
      }
      
      card.setSize(cardWidth, cardHeight);
      card.setLayoutX(width*i);
      card.setLayoutY(height - cardHeight);
      
      xLayouts[i] = width*i;
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
  public void calculateMouseOver(double x, double y) {
    if (cards[0] == null) return;

    double minMousePickup = this.getHeight() * MIN_MOUSE_PICKUP;

    if (!TEST_LAYOUT) {
      if (y > minMousePickup) {

        double height = this.getHeight();
        double width = this.getWidth();
        double maxMousePickup = height - lastHeight / 2;
        if (y > maxMousePickup) y = maxMousePickup;

        double peak = (y - minMousePickup) / (maxMousePickup - minMousePickup);

        peak *= peak;

        double shift = x / width;
        double breadth = CURVE_BREADTH;

        double[] constraints = new double[7];
        double total = 0;
        int maxIndex = 0;

        for (int i = 0; i < 7; i++) {
          double constraint = (i / 7d + 1 / 14d);

          {//gaussian equation
            constraint -= shift;
            constraint *= constraint;
            constraint /= (breadth * breadth);
            constraint *= -1;
            constraint = 1 / 7d + peak * Math.pow(Math.E, constraint);
          }
          if (constraints[i] > constraints[maxIndex]) maxIndex = i;
          constraints[i] = constraint;
          total += constraint;
//        System.out.println(constraint + ", " + i);

        }
//      System.out.println("---------------");
        double totalWidth = 0;


        for (int i = 0; i < 7; i++) {
          AbstractCard card = cards[i];

          double sizeFactor = CARD_RATIO;
          double cardWidth = (constraints[i] / total) * width;
          double tempWidth = cardWidth;

//        System.out.println(x+" "+y);
//        System.out.println(card.localToScene(card.getLayoutBounds()));


          if (cardWidth / sizeFactor > height * MAX_HOVER_HEIGHT) {
            cardWidth = height * MAX_HOVER_HEIGHT * sizeFactor;
          }

          double xPos = totalWidth + (tempWidth - cardWidth) / 2;
          totalWidth += tempWidth;

          cardWidth -= cardWidth * MIN_GAP_SIZE;
          double cardHeight = cardWidth / CARD_RATIO;
          double yPos = height - cardHeight;

          card.setSize(cardWidth, cardHeight);
          card.setLayoutX(xPos);
          card.setLayoutY(yPos);
        }

      } else {

        for (int i = 0; i < cards.length; i++) {
          AbstractCard card = cards[i];
          card.setSize(lastWidth, lastHeight);
          card.setLayoutX(xLayouts[i]);
          card.setLayoutY(this.getHeight() - lastHeight);
        }
      }
    }else if(false){
      for(AbstractCard abstractCard:cards) {
        CardView card = (CardView) abstractCard;
        if (card.localToParent(card.getLayoutBounds()).contains(x, y) && !card.isSelect()) {
          card.initMainCard();
          card.setSelected(true);
          card.translateYProperty().set(-400);
        }else if(card.isSelect())
        {
          card.translateYProperty().set(0);
          card.setSelected(false);
          card.initSimpleCard();
        }
      }
    }
  }
}
