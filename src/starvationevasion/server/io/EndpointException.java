package starvationevasion.server.io;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

public class EndpointException extends Exception
{
  public EndpointException(String message) {
    super(message);
  }

  public EndpointException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
