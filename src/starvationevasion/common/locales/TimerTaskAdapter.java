package starvationevasion.common.locales;

import java.util.TimerTask;

/**
 * Shea Polansky
 * Class name and description goes here
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
