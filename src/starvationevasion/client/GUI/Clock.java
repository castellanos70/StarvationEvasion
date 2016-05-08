package starvationevasion.client.GUI;

import java.util.concurrent.atomic.AtomicBoolean;

import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Timer is the class responsible for representing the countdown timer for the
 * two different game phases DraftingPhase has 5 minutes VotingPhase has 2
 * minutes
 */
public class Clock extends ResizablePane
{
  private static final double SATURATION = 1;
  private static final double BALANCE = .85;
  private static final double FONT_SIZE = 80;
  private static final Font FONT = Font.font("Arial", FontWeight.BOLD, FONT_SIZE);
  private static final double FPS = 60;
  private static final double DEFAULT_TIME = 300000;

  private Text clock;
  private static long last = 0;
  private static long start = 0;
  private double totalTime = 0;
  private double elapsedTime = 0;
  private AtomicBoolean paused;
  private AnimationTimer timer;

  public Clock()
  {
    super();
    clock = new Text("0:00");
    clock.setTextOrigin(VPos.TOP);
    clock.setStroke(Color.BLACK);
    clock.setStrokeWidth((FONT_SIZE / 50));
    clock.setFont(FONT);
    clock.setManaged(false);

    paused = new AtomicBoolean();
    paused.set(true);

    this.getChildren().add(clock);
    onResize();

    totalTime = DEFAULT_TIME;

    updateClock();

    last = System.currentTimeMillis();
    start = System.currentTimeMillis();

    timer = new AnimationTimer()
    {

      @Override
      public void handle(long now)
      {
        now = System.currentTimeMillis();
        if ((now - last) / 1000 > 1 / FPS)
        {
         
          updateClock();
          last = System.currentTimeMillis();
        }
      }

    };

  }

  /**
   * updates the digital time given the amount of total time and the amount of
   * elapsed time
   */
  private void updateClock()
  {
    if (elapsedTime >= totalTime) return;

    double time = totalTime - elapsedTime;

    {// color update
      double difference = (totalTime - elapsedTime) / totalTime;
      clock.setFill(Color.hsb(120 * difference, SATURATION, BALANCE));
    }

    {// text update
      time /= 1000; // convert to seconds
      int minutes = (int) (Math.floor(time / 60));
      int seconds = (int) (time - minutes * 60);
      if (seconds < 10)
      {
        clock.setText(minutes + ":" + "0" + seconds);
      }
      else
      {
        clock.setText(minutes + ":" + seconds);
      }
    }
  }

  @Override
  public void onResize()
  {
    Bounds b = clock.getBoundsInLocal();
    double width = this.getWidth();
    double height = this.getHeight();
    double clockWidth = b.getWidth();
    double clockHeight = b.getHeight();
    double scaleFactor = 0;

    double clockAspectRatio = clockWidth / clockHeight;
    double paneAspectRatio = width / height;

    if (paneAspectRatio <= clockAspectRatio)
    {
      scaleFactor = width / clockWidth;
    }
    else
    {
      scaleFactor = height / clockHeight;
    }

    clock.setScaleX(scaleFactor);
    clock.setScaleY(scaleFactor);

    double newWidth = clockWidth * scaleFactor;
    double newHeight = clockHeight * scaleFactor;
    double diffX = (newWidth - clockWidth) / 2;
    double diffY = (newHeight - clockHeight) / 2;
    clock.setTranslateX(diffX + (width - newWidth) / 2);
    clock.setTranslateY(diffY + (height - newHeight) / 2);

    // System.out.println(clock.getTranslateY());
  }

  /**
   * Starts the clock
   */
  public void startClock()
  {
    System.out.println("starting clock");
    timer.start();
  }

  /**
   * Pauses the clock
   */
  public void pauseClock()
  {
    paused.set(true);
  }

  /**
   * Sets the total amount of time for this clock in milliseconds
   */
  public void setTime(double totalTime)
  {
    this.totalTime = totalTime;
  }

  /**
   * Resets the clock back to max time
   */
  public void reset()
  {
    elapsedTime = 0;
  }
}