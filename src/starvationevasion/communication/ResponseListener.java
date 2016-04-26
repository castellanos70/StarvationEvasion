package starvationevasion.communication;

import starvationevasion.server.model.Type;

/**
 * A ResponseListener listens for very specific responses from the server (managed
 * by a Communication module). When these responses occur, the Communication module
 * will push the data to the listener.
 *
 * @author Justin Hall, George Boujaoude
 */
public interface ResponseListener
{
  /**
   * This is called to deal with the data sent over the server in the form of a Response object.
   *
   * Example: If the type is USER, the data will be a serialized User instance (it can be cast
   *          directly to a User object).
   * @param type type of response from the server
   * @param data data associated with the response
   */
  void processResponse(Type type, Object data);
}
