package starvationevasion.common.locales;

import java.util.TimerTask;

/**
 * Shea Polansky
 * Creates a TimerTask that runs a given Runnable.
 * This is for adapting lambdas/method references for use as TimerTasks.
 */
public class TimerTaskAdapter extends TimerTask
{
  private final Runnable runnable;

  public TimerTaskAdapter(Runnable runnable)
  {
    this.runnable = runnable;
  }

  @Override
  public void run()
  {
    runnable.run();
  }
}
