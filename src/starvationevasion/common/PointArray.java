package starvationevasion.common;

/**
 * This is used for JavaFX's javafx.scene.canvas.GraphicsContext.strokePolygon
 * which requires a pair of parallel arrays of coordinates.
 */
public class PointArray
{
  public double[] x, y;

  public PointArray(int size)
  {
    x = new double[size];
    y = new double[size];
  }
}
