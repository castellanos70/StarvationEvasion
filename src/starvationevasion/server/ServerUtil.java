package starvationevasion.server;

import starvationevasion.common.Tuple;
import starvationevasion.common.Util;
import starvationevasion.common.messages.Hello;

import java.io.IOException;
import java.util.Map;

/**
 * Shea Polansky
 * Static server utility methods
 */
public class ServerUtil
{
  public void StartAIProcess(String[] command, String hostname, int port, String username, String password)
  {
    ProcessBuilder processBuilder = new ProcessBuilder(command);
    final Map<String, String> environment = processBuilder.environment();
    environment.put("SEHOSTNAME", hostname);
    environment.put("SEPORT", "" + port);
    environment.put("SEUSERNAME", username);
    environment.put("SEPASSWORD", password);
    try
    {
      processBuilder.start();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private static final String AI_NAME_PREFIX = "(BOT) ";
  private static final String[] AI_NAMES =
      {"Emma", "Olivia", "Noah", "Sophia", "Liam", "Mason", "Isabella", "Jacob", "William", "Ethan"};
  public Tuple<String, String> getRandomAILogin()
  {
    return new Tuple<>(AI_NAME_PREFIX + AI_NAMES[Util.rand.nextInt(AI_NAMES.length)], Hello.generateRandomLoginNonce());
  }
}
