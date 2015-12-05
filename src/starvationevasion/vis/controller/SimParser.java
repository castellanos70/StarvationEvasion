package starvationevasion.vis.controller;

import starvationevasion.common.MapPoint;
import starvationevasion.io.XMLparsers.GeographyXMLparser;
import starvationevasion.sim.GeographicArea;
import starvationevasion.vis.visuals.EarthViewer;

import java.util.Collection;

/**
 * Created by Tess Daughton on 11/15/15.
 * This class will be called by Visualizer in order to parse the simulation data that the
 * Visualizer is passed from the client
 * Displays the selected country
 */
public class SimParser
{
    EarthViewer passedEarthViewer;
    public SimParser(double lat, double lon, EarthViewer e)
    {
        passedEarthViewer = e;
        init(lat, lon);
    }

    private void init(double lat, double lon)
    {
        /* TODO: Clarify if visual will have access to MapPoint class */
        MapPoint p = new MapPoint(lat, lon);

      /*parse the location data to find where the user clicked on the map*/
        Collection<GeographicArea> modelGeography = new GeographyXMLparser().getGeography();
        for (GeographicArea a : modelGeography) {
            if(a.containsMapPoint(p))
            {
            /*TODO: send this info to another method to decide what to show the user*/
                System.out.println("clicked on " + a.getName());
                passedEarthViewer.setRegionTitle(a.getName());
            }
        }
    }


}
