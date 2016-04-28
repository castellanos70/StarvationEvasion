package starvationevasion.communication.AITest.commands;


import starvationevasion.communication.AITest.AI;

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
