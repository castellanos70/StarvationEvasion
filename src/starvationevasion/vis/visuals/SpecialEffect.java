package starvationevasion.vis.visuals;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.collections.ObservableList;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import starvationevasion.vis.controller.*;
import starvationevasion.vis.controller.EarthViewer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brett on 12/6/2015.
 * BRETT HI THIS IS TESS:) I'm changing one thing in this class before I push up.
 * I made earth no longer static inside of EarthViewer, so I will just give this class a copy
 */
public class SpecialEffect {
    private Sphere cloud;
    private Earth earth;
    private List<Sphere> specialEffects = new ArrayList<Sphere>();

    public SpecialEffect(Earth earth)
    {
      this.earth=earth;
    }

    public void buildClouds()
    {
        cloud = new Sphere(ResourceLoader.LARGE_EARTH_RADIUS*1.05);
        final PhongMaterial cloudMaterial = new PhongMaterial();
        cloudMaterial.setDiffuseMap(ResourceLoader.DIFF_CLOUD);
        cloud.setMaterial(cloudMaterial);

        earth.getEarth().getChildren().add(cloud);

        rotateAroundYAxis(cloud, 100).play();
    }

    public void buildEffect(String type, double latitude, double longitude)
    {
        double sphereExpansion = 1.05; // default is 5% larger.
        Image diffuseMap;
        int rotationSpeed = 0;
        if(type.equals("hurricane"))
        {
            diffuseMap = ResourceLoader.DIFF_HURRICANE;
            rotationSpeed = 50;
        }
        else if(type.equals("hurricaneShadow"))
        {
            diffuseMap = ResourceLoader.DIFF_HURRICANE;
            rotationSpeed = 25;
        }
        else
        {
            diffuseMap = ResourceLoader.DIFF_PINPOINT;
        }

        Sphere pin = new Sphere(ResourceLoader.LARGE_EARTH_RADIUS*sphereExpansion);
        final PhongMaterial pinMaterial = new PhongMaterial();

        pinMaterial.setDiffuseMap(diffuseMap);
        pin.setMaterial(pinMaterial);

        pin = transformNode(pin, latitude, longitude);

        specialEffects.add(pin);

        earth.getEarth().getChildren().add(pin);

        if(rotationSpeed!=0)
        {
            //matrixRotateNode(pin, latitude, longitude, 1).play();
            //pin.getTransforms().add(new Rotate(latitude, longitude, 0));
            rotateAroundAxis(pin, latitude, longitude, rotationSpeed).play();
            //rotateAroundYAxis(pin, 100).play();
        }
        if(type.equals("hurricane")) buildEffect("hurricaneShadow",latitude,longitude);

    }

    public void buildPinPoint(double latitude, double longitude)
    {
        Sphere pin = new Sphere(ResourceLoader.LARGE_EARTH_RADIUS*1.05);
        final PhongMaterial pinMaterial = new PhongMaterial();
        pinMaterial.setDiffuseMap(ResourceLoader.DIFF_PINPOINT);
        pin.setMaterial(pinMaterial);
        pin = transformNode(pin, latitude, longitude);

        specialEffects.add(pin);

        earth.getEarth().getChildren().add(pin);

    }

    public void removeSpecialEffects()
    {
        ObservableList currentUniverse = earth.getEarth().getChildren();
        for(Sphere effect : specialEffects)
        {
            currentUniverse.remove(effect);
        }
        specialEffects = null;
    }

  /**
   * Transforms the latitude and longitude to JAVAFX coordinates
   * @param shape shape to transform on
   * @param latitude degrees that ranges from -90 to 90 degrees
   * @param longitude degrees that ranges from -180 to 180 degrees
   * @return Returns the transformed shape
   */
    private Sphere transformNode(Sphere shape, double latitude, double longitude)
    {
        /* JAVAFX will use 360 degrees, while lat long will use +- 180 and 90 */
        double lon = longitude + 180;
        double lat = (latitude + 90)*2;
        shape.getTransforms().addAll(
                new Rotate(latitude, Rotate.X_AXIS),
                new Rotate(longitude, Rotate.Y_AXIS)
        );

        return shape;
    }

    private RotateTransition rotateAroundYAxis(Node node,int ROTATE_SECS)
    {
        RotateTransition rotate = new RotateTransition(Duration.seconds(ROTATE_SECS), node);
        rotate.setAxis(Rotate.Y_AXIS);
        rotate.setFromAngle(360);
        rotate.setToAngle(0);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setCycleCount(RotateTransition.INDEFINITE);

        return rotate;
    }
    private RotateTransition rotateAroundAxis(Node node, double latitude, double longitude, int ROTATE_SECS)
    {
        RotateTransition rotate = new RotateTransition(Duration.seconds(ROTATE_SECS), node);
        Point3D AXIS = new Point3D(latitude, longitude, 0.0);

        //node.getTransforms().add(new Rotate(latitude, longitude, 0));

        AXIS = new Point3D(latitude, longitude, node.getRotationAxis().getZ());
        rotate.setAxis(AXIS);
        rotate.setFromAngle(360);
        rotate.setToAngle(0);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setCycleCount(RotateTransition.INDEFINITE);

        return rotate;
    }
}
