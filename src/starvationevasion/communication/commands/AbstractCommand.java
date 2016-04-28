package starvationevasion.communication.commands;


import starvationevasion.ai.AI;
import starvationevasion.communication.AITest;

public abstract class AbstractCommand implements Command
{
  private final AITest client;

  public AbstractCommand(AITest client)
  {
    this.client = client;
  }

  public AITest getClient ()
  {
    return client;
  }
}
