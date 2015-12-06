package starvationevasion.vis.visuals;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import starvationevasion.vis.controller.*;
import starvationevasion.vis.controller.EarthViewer;

/**
 * Created by Brett on 12/6/2015.
 */
public class SpecialEffect {
    private Sphere cloud;

    public SpecialEffect() {}

    public void buildClouds()
    {
        //Sphere cloud = new Sphere(ResourceLoader.LARGE_EARTH_RADIUS*1.05);
        cloud = new Sphere(ResourceLoader.LARGE_EARTH_RADIUS*1.05);
        final PhongMaterial cloudMaterial = new PhongMaterial();
        cloudMaterial.setDiffuseMap(ResourceLoader.DIFF_CLOUD);
        cloud.setMaterial(cloudMaterial);
        EarthViewer.earth.getUniverse().getChildren().addAll(cloud);
        rotateAroundYAxis(cloud, 10).play();
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
