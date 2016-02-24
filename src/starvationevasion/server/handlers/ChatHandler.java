package starvationevasion.server.handlers;

import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.impl.JSONStreamReaderImpl;
import starvationevasion.common.EnumRegion;
import starvationevasion.server.Worker;
import starvationevasion.server.Server;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Request;

public class ChatHandler extends AbstractHandler
{
  public ChatHandler (Server server, Worker client)
  {
    super(server, client);
  }
  @Override
  protected boolean handleRequestImpl (Request request)
  {
    if (request.getDestination().equals(Endpoint.CHAT))
    {

      EnumRegion destination = EnumRegion.valueOf(request.getData()[0]);

      // need to parse into JSONDocument
      String data = request.getData()[1];

      String from = getClient().getUser().getRegion().name();

      // Determine if the destination is by username or by region

      Worker worker = server.getWorkerByRegion(destination);
      if (worker != null)
      {
        JSONDocument _msg = JSONDocument.createObject();
        _msg.setString("from", from);
        _msg.setString("message", "what the...");
      }

      return true;
    }
    return false;
  }
}
