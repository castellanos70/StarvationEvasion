package starvationevasion.util.events;


import starvationevasion.server.model.User;

public interface AuthenticationEvent
{
  interface Success extends SuccessEvent<User>{}

  interface Fail extends FailEvent<Void>{}
}
