
package starvationevasion.server.handlers;


import starvationevasion.server.ClientWorker;
import starvationevasion.server.Request;
import starvationevasion.server.Response;
import starvationevasion.server.ServerMaster;

public abstract class AbstractHandler
{
  private final ClientWorker client;
  protected AbstractHandler m_successor;
  protected final ServerMaster serverMaster;
  protected Response m_response = null;

  public AbstractHandler (ServerMaster server, ClientWorker client)
  {
    this.serverMaster = server;
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

  public ClientWorker getClient ()
  {
    return client;
  }
}
