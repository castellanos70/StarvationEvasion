package starvationevasion.server.model;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.common.WorldData;
import starvationevasion.server.io.JSON;

import java.util.*;

public class Payload extends HashMap<String, Object> implements Sendable
{

  public Payload (Object data)
  {
    super();
    this.putData(data);
  }

  public Payload ()
  {
    super();
  }

  @Override
  public Object put (String key, Object value)
  {
    if (!(value instanceof Sendable || value instanceof Number || value instanceof Boolean ||
            value instanceof String || value instanceof List))
    {
      System.out.println(value + " is NOT a valid Payload");
      System.exit(0);
      return value;
    }
    if (value instanceof List)
    {
      if (!((List) value).isEmpty())
      {
        if (!(((List) value).get(0) instanceof Sendable))
        {
          System.out.println(((List) value).get(0) + " NOT a valid value in a payload list.");
          System.exit(0);
          return value;
        }
      }
    }
    return super.put(key, value);
  }

  public Object putData (Object value)
  {
    return put("data", value);
  }

  public Object putMessage (Object value)
  {
    return put("message", value);
  }

  @Override
  public Object get (Object key)
  {
    return super.get(key);
  }

  public Object getData()
  {
    return get("data");
  }

  public String getMessage ()
  {
    return (String) get("message");
  }

  @Override
  public void putAll (Map<? extends String, ?> m)
  {
    Iterator<? extends String> iterator = m.keySet().iterator();
    while(iterator.hasNext())
    {
      String key = iterator.next();
      put(key, m.get(key));
    }
  }

  @Override
  public Object putIfAbsent (String key, Object value)
  {
    // not supported
    return super.putIfAbsent(key, value);
  }

  @Override
  public Type getType ()
  {
    return (Type) get("type");
  }

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument json = new JSONDocument(JSONDocument.Type.OBJECT);
    Iterator<? extends String> iterator = keySet().iterator();
    while(iterator.hasNext())
    {
      String key = iterator.next();
      Object value = get(key);


      if (value instanceof String)
      {
        json.setString(key, (String) value);
      }
      else if (value instanceof Number)
      {
        json.setNumber(key, (Number) value);
      }
      else if (value instanceof Boolean)
      {
        json.setBoolean(key, (Boolean) value);
      }
      else if (value instanceof Sendable)
      {
          json.set(key, ((JSON) value).toJSON());
      }
      else if (value instanceof List)
      {
        JSONDocument doc = JSONDocument.createArray(((List) value).size());
        int i = 0;
        for (Object o : ((List) value))
        {
          doc.set(i, ((JSON) o).toJSON());
          i++;
        }
        json.set(key, doc);
      }
    }
    return json;
  }

  @Override
  public void fromJSON (Object doc)
  {
    JSONDocument _json = JSON.Parser.toJSON(doc);
     this.putAll(_json.object());
  }
}
