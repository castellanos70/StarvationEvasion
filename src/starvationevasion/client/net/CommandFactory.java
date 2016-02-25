package starvationevasion.client.net;


public class CommandFactory
{
  private static CommandFactory ourInstance = new CommandFactory();


  public static CommandFactory getInstance ()
  {
    return ourInstance;
  }

  private CommandFactory ()
  {
  }

  public static String createUser(String uname, String pwd, String region)
  {
    return "user_create username pwd [region]";
  }
}
