package starvationevasion.util.events;


public interface SocketConnectionEvent
{
  public interface Success extends SuccessEvent<Void>{}

  public interface Fail extends FailEvent<Void>{}
}
