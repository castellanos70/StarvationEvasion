package starvationevasion.ai.commands;

public interface Command
{
  boolean run();

  String commandString();
}
