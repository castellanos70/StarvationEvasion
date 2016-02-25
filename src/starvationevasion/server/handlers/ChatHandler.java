package starvationevasion.server.handlers;

import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.impl.JSONStreamReaderImpl;
import starvationevasion.common.EnumRegion;
import starvationevasion.server.Worker;
import starvationevasion.server.Server;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Request;

import java.io.StringReader;

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

      if (getClient().getUser() == null)
      {
        getClient().send("login first");
      }

      String arr = request.chomp();
      request.setData(request.getData().replace(arr, " "));

      EnumRegion destination = EnumRegion.valueOf(arr);

      String data = request.getData();
      StringReader stringReader = new StringReader(data);
      JSONStreamReaderImpl s = new JSONStreamReaderImpl(stringReader);
      JSONDocument _json = s.build();

      String from = getClient().getUser().getRegion().name();

      // Determine if the destination is by username or by region

      Worker worker = server.getWorkerByRegion(destination);
      if (worker != null)
      {
        JSONDocument _msg = JSONDocument.createObject();
        _msg.setString("from", from);
        _msg.set("message", _json);
        worker.send(_msg.toString());
      }

      return true;
    }
    return false;
  }
}
