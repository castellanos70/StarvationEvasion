package starvationevasion.server.model;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.io.JSON;

import java.util.*;

/**
 * Class to allow for passing arbitrary data over network. The underlying data structure is a
 * HashMap to help with conversion to JSON
 */
public class Payload extends HashMap<String, Object> implements Sendable
{

  /**
   * Create and add object. adds object to Data key!
   *
   * @param data Object to be stored. Stores in key "data"
   */
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
      throw new IllegalArgumentException(String.valueOf(value) + " in NOT allowed. Value must be " +
                                                 "String, Number, Boolean, List, or Sendable.");
    }
    if (value instanceof List)
    {
      if (!((List) value).isEmpty())
      {
        if (!(((List) value).get(0) instanceof Sendable))
        {
          throw new IllegalArgumentException("List contents must be: String, Number, Boolean, List, or Sendable.");
        }
      }
    }
    return super.put(key, value);
  }

  /**
   * Puts object in the map inside the value of "data" key
   *
   * @param value object to be stored
   * @return Object that was stored.
   */
  public Object putData (Object value)
  {
    return put("data", value);
  }

  /**
   * Puts string is the message key
   *
   * @param stringMessage string to be stored
   * @return Object that was stored.
   */
  public String putMessage (String stringMessage)
  {
    return (String) put("message", stringMessage);
  }

  @Override
  public Object get (Object key)
  {
    return super.get(key);
  }

  /**
   * Retrieves value for key "data"
   *
   * @return object stored at "data"
   */
  public Object getData()
  {
    return get("data");
  }

  /**
   * Retrieves the value for the key "message"
   *
   * @return String that was stored in message
   */
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
