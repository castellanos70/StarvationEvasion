package starvationevasion.vis.controller;

import starvationevasion.common.MapPoint;
import starvationevasion.io.XMLparsers.GeographyXMLparser;
import starvationevasion.sim.GeographicArea;

import java.util.Collection;

/**
 * Created by Tess Daughton on 11/15/15.
 * This class will be called by Visualizer in order to parse the simulation data that the
 * Visualizer is passed from the client
 * Displays the selected country
 */
public class SimParser
{

    public String parse(double lat, double lon)
    {
       /* TODO: Clarify if visual will have access to MapPoint class.
       ** Yes, you have access to the MapPoint class.  -- Peter Blemel
       */
        MapPoint p = new MapPoint(lat, lon);

      /*parse the location data to find where the user clicked on the map*/
        // The GeographicArea is a closed polygon representing a part of a territory.  For example,
        // Catalina Island off of the coast of California would be one GeographicArea, while mainland
        // California is another region.  Both are aggregated into The Territory of California.  It is
        // a convenient coincidence that the polygon's name is the same as a Territory in most cases,
        // but I haven't verified that this is always true. -- Peter Blemel
        //
        Collection<GeographicArea> modelGeography = new GeographyXMLparser().getGeography();
        for (GeographicArea a : modelGeography)
        {
            if (a.containsMapPoint(p))
            {
            /*TODO: send this info to another method to decide what to show the user*/
                System.out.println("clicked on " + a.getName());
                return a.getName();
            }
        }
        return "No Name on Record";
    }
}
