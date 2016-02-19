
package starvationevasion.server.handlers;


import starvationevasion.server.Server;
import starvationevasion.server.Worker;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.Response;

public abstract class AbstractHandler
{
  private final Worker client;
  protected AbstractHandler m_successor;
  protected final Server server;
  protected Response m_response = null;

  public AbstractHandler (Server server, Worker client)
  {
    this.server = server;
    this.client = client;
  }

  public AbstractHandler setSuccessor (AbstractHandler successor)
  {
    m_successor = successor;
    return this;
  }

  protected abstract boolean handleRequestImpl (Request request);


  public final void handleRequest (Request request)
  {
    boolean handledByThisNode = handleRequestImpl(request);
    if (m_successor != null && !handledByThisNode)
    {
      m_successor.handleRequest(request);
    }
  }

  public Response getResponse ()
  {
    if (m_response != null)
    {
      Response _sending = m_response;
      _sending.setFrom(client.getName());
      m_response = null;
      return _sending;

    }
    return null;
  }

  public Worker getClient ()
  {
    return client;
  }
}
