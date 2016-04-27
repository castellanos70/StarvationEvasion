package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.Connector;
import starvationevasion.server.Server;
import starvationevasion.server.model.Request;

/**
 * Generic handler. Mainly to forces subclasses to implement handleRequestImpl method
 */
public abstract class AbstractHandler
{
  private final Connector client;
  protected AbstractHandler m_successor;
  protected final Server server;

  public AbstractHandler (Server server, Connector client)
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


  public Connector getClient ()
  {
    return client;
  }
}
