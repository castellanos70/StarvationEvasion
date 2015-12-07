package starvationevasion.vis.visuals;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
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

    private Sphere transformNode(Sphere shape, double latitude, double longitude)
    {
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
}
