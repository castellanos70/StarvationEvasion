package starvationevasion.ai.commands;
import starvationevasion.ai.AI;


public abstract class AbstractCommand implements Command
{
  private final AI client;

  public AbstractCommand(AI client)
  {
    this.client = client;
  }

  public AI getClient ()
  {
    return client;
  }
}