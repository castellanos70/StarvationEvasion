package starvationevasion.communication;

import starvationevasion.server.model.Type;

/**
 * A ResponseListener listens for very specific responses from the server (managed
 * by a Communication module). When these responses occur, the Communication module
 * will push the data to the listener.
 */
public interface ResponseListener
{
  void processResponse(Type type, Object data);
}
