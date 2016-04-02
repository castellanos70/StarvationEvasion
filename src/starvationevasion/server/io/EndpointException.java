package starvationevasion.server.io;


public class EndpointException extends Exception
{
  public EndpointException(String message) {
    super(message);
  }

  public EndpointException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
