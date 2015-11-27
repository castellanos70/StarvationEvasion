package starvationevasion.server;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by scnaegl on 11/21/15.
 */
public class Stopwatch implements Serializable {
  private final int DELAY = 1000;
  private int interval;
  private transient Timer timer;

  public Stopwatch() {
    this(300);
  }

  public Stopwatch(int interval) {
    this.interval = interval;
    timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      public void run() {
        updateInterval();
      }
    }, DELAY, DELAY);
  }

  public int getInterval() {
    return interval;
  }

  public void setInterval(int interval) {
    this.interval = interval;
  }

  public int getMinutes() {
    return interval / 60;
  }

  public int getSeconds() {
    return interval % 60;
  }

  public void stop() {
    timer.cancel();
  }

  private final int updateInterval() {
    if (interval == 1)
      timer.cancel();
    return --interval;
  }

}
