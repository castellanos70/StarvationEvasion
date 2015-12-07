package starvationevasion.vis.visuals;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
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
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
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
        if(type.equals("hurricane")) buildHurricane(latitude, longitude);
        else if(type.equals("forestFire")) buildForestFire(latitude, longitude);
        else if(type.equals("flood")) buildFlood(latitude, longitude);
        else if(type.equals("drought")) buildDrought(latitude, longitude);
        else if(type.equals("blight")) buildBlight(latitude, longitude);

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
       // double lon = longitude + 180;
        //double lat = (latitude + 90)*2;

        shape.getTransforms().addAll(
                new Rotate(latitude, Rotate.X_AXIS),
                new Rotate(longitude, Rotate.Y_AXIS)
        );

        return shape;
    }

    private void buildHurricane(double latitude, double longitude)
    {
        Image diffuseMap = ResourceLoader.DIFF_HURRICANE;
        int rotationSpeed = 50;
        double sphereExpansion = 1.05;

        Sphere pin = new Sphere(ResourceLoader.LARGE_EARTH_RADIUS*sphereExpansion);
        PhongMaterial pinMaterial = new PhongMaterial();

        pinMaterial.setDiffuseMap(diffuseMap);
        pin.setMaterial(pinMaterial);

        pin = transformNode(pin, latitude, longitude);

        specialEffects.add(pin);

        earth.getEarth().getChildren().add(pin);
        //rotateAroundAxis(pin, latitude, longitude, rotationSpeed).play();

        // BUILD SHADOW

        diffuseMap = ResourceLoader.DIFF_HURRICANESHADOW;
        rotationSpeed = 25;

        pin = new Sphere(ResourceLoader.LARGE_EARTH_RADIUS*sphereExpansion);
        pinMaterial = new PhongMaterial();

        pinMaterial.setDiffuseMap(diffuseMap);
        pin.setMaterial(pinMaterial);

        pin = transformNode(pin, latitude, longitude);

        specialEffects.add(pin);

        earth.getEarth().getChildren().add(pin);
        //rotateAroundAxis(pin, latitude, longitude, rotationSpeed).play();
    }

    private void buildForestFire(double latitude, double longitude)
    {
        Image diffuseMap = ResourceLoader.DIFF_FORESTFIRE;
        int rotationSpeed = 3;
        double sphereExpansion = 1.02;

        Sphere pin = new Sphere(ResourceLoader.LARGE_EARTH_RADIUS*sphereExpansion);
        PhongMaterial pinMaterial = new PhongMaterial();

        pinMaterial.setDiffuseMap(diffuseMap);
        pin.setMaterial(pinMaterial);

        pin = transformNode(pin, latitude, longitude);

        specialEffects.add(pin);

        earth.getEarth().getChildren().add(pin);

        //rotateAroundAxis(pin, latitude, longitude, rotationSpeed).play();
    }

    private void buildFlood(double latitude, double longitude)
    {
        Image diffuseMap = ResourceLoader.DIFF_FLOOD;
        int rotationSpeed = 3;
        double sphereExpansion = 1.02;

        Sphere pin = new Sphere(ResourceLoader.LARGE_EARTH_RADIUS*sphereExpansion);
        PhongMaterial pinMaterial = new PhongMaterial();

        pinMaterial.setDiffuseMap(diffuseMap);
        pin.setMaterial(pinMaterial);

        pin = transformNode(pin, latitude, longitude);

        specialEffects.add(pin);

        earth.getEarth().getChildren().add(pin);

        //rotateAroundAxis(pin, latitude, longitude, rotationSpeed).play();
    }
    private void buildDrought(double latitude, double longitude)
    {
        Image diffuseMap = ResourceLoader.DIFF_DROUGHT;
        int rotationSpeed = 3;
        double sphereExpansion = 1.02;

        Sphere pin = new Sphere(ResourceLoader.LARGE_EARTH_RADIUS*sphereExpansion);
        PhongMaterial pinMaterial = new PhongMaterial();

        pinMaterial.setDiffuseMap(diffuseMap);
        pin.setMaterial(pinMaterial);

        pin = transformNode(pin, latitude, longitude);

        specialEffects.add(pin);

        earth.getEarth().getChildren().add(pin);

        //rotateAroundAxis(pin, latitude, longitude, rotationSpeed).play();
    }

    private void buildBlight(double latitude, double longitude)
    {
        Image diffuseMap = ResourceLoader.DIFF_BLIGHT;
        int rotationSpeed = 3;
        double sphereExpansion = 1.02;

        Sphere pin = new Sphere(ResourceLoader.LARGE_EARTH_RADIUS*sphereExpansion);
        PhongMaterial pinMaterial = new PhongMaterial();

        pinMaterial.setDiffuseMap(diffuseMap);
        pin.setMaterial(pinMaterial);

        pin = transformNode(pin, latitude, longitude);

        specialEffects.add(pin);

        earth.getEarth().getChildren().add(pin);

        //rotateAroundAxis(pin, latitude, longitude, rotationSpeed).play();
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

        AXIS = new Point3D(node.getRotationAxis().getX(), node.getRotationAxis().getY(), node.getRotationAxis().getZ());
        rotate.setAxis(AXIS);
        rotate.setFromAngle(360);
        rotate.setToAngle(0);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setCycleCount(RotateTransition.INDEFINITE);

        return rotate;
    }
}
