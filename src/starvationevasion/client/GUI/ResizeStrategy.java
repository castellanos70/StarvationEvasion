package starvationevasion.client.GUI;

/**
 * @author Ben
 * 
 * this class contains 4 double feilds that define how a
 * Node's x,y,width, and height are calculated in another node
 * 
 * each feild represents the percentage of a node's area that will be used by this
 * node in each direction, such that if x = .05 then 5% of the area of the parent
 * node will be calculated and assigned as tthe xLayout of the node with this strategy
 *
 */
public class ResizeStrategy
{
  public double percentageX;
  public double percentageY;
  public double percentageWidth;
  public double percentageHeight;
  
  /**
   * defualt constructor takes in a x,y,width and height
   * percentage
   * 
   * @param percentageX
   * @param percentageY
   * @param percentageWidth
   * @param percentageHeight
   */
  public ResizeStrategy(double percentageX, double percentageY, double percentageWidth, double percentageHeight){
    this.percentageX = percentageX;
    this.percentageY = percentageY;
    this.percentageWidth = percentageWidth;
    this.percentageHeight = percentageHeight;
  }
  
}
