package starvationevasion.sim.io;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class for logging messages to console. This can be changed to log to a file,
 * but currently just logs to the console.
 */
public class Logger
{
  // There shouldn't really be an instance of the logger unless we start
  // writing to a file, so leaving it as static for now

  // This should set the log level for the entire sim package and should not
  // be set outside of the top level except for testing.
  private static LogLevel logLevel;

  private Logger() {}

  public static void setLogLevel(LogLevel level)
  {
    logLevel = level;
  }

  public static void log(LogLevel level, String msg)
  {
    if (logLevel != LogLevel.OFF)
    {
      if (logLevel == LogLevel.ALL || logLevel == level)
      {
        print(level, msg);
      }
    }
  }

  private static void print(LogLevel level, String msg)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
    String outMsg = sdf.format(new Date());
    if (level == LogLevel.DEBUG)
    {
      outMsg += "***DEBUG***:  " + msg;
      System.out.println(outMsg);
    }
    else if (level == LogLevel.ERROR)
    {
      outMsg += "***ERROR***:  " + msg;
      System.err.println(outMsg);
    }
    else if (level == LogLevel.WARNING)
    {
      outMsg += "***WARNING***:  " + msg;
      System.out.println(outMsg);
    }
    else
    {
      outMsg += "***INFO***:  " + msg;
      System.out.println(outMsg);
    }
  }
}
