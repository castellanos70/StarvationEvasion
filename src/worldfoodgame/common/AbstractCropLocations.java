package worldfoodgame.common;

/**
 * The AbstractCropLocations class should be extended by a class that
 * somehow represents which 100 square kilometer units within a particular
 * country are assigned to which EnumLandType.<br><br>
 * 
 * Note: there must be one-and-only-one element of this structure for each 
 * of 100 square kilometer units of the country. However, the development team
 * may or may not want to associate each element to a particular geographic 
 * location within the country. The only information this structure needs to 
 * maintain is:
 * <ol>
 *   <li> The total number of units assigned to each crop.</li>
 *   <li> The change in land usage (from one crop to another or from 
 *   EnumLandType.UNCULTIVATED_ARABLE to a crop) between two instances of this 
 *   class for the same country.</li>
 * </ol>
 */
public class AbstractCropLocations
{

}
