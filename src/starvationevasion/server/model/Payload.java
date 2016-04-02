package starvationevasion.server.model;

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.io.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Payload<K extends String, V> extends HashMap<K, V> implements Sendable
{

  @Override
  public V put (K key, V value)
  {
    if (!(value instanceof Sendable || value instanceof Number || value instanceof String || value instanceof ArrayList))
    {
      System.out.println("NOT a valid payload!");
      System.exit(0);
      return value;
    }
    if (value instanceof ArrayList)
    {
      if (!((ArrayList) value).isEmpty())
      {
        if (!(((ArrayList) value).get(0) instanceof Sendable))
        {
          System.out.println("NOT a valid payload send");
          System.exit(0);
          return value;
        }
      }
    }
    return super.put(key, value);
  }

  public V putData (V value)
  {
    return put((K) "data", value);
  }

  public V putMessage (V value)
  {
    return put((K) "message", value);
  }

  @Override
  public V get (Object key)
  {
    return super.get(key);
  }

  @Override
  public void putAll (Map<? extends K, ? extends V> m)
  {
    Iterator<? extends K> iterator = m.keySet().iterator();
    while(iterator.hasNext())
    {
      K key = iterator.next();
      put(key, m.get(key));
    }
  }

  @Override
  public V putIfAbsent (K key, V value)
  {
    // not supported
    return super.putIfAbsent(key, value);
  }

  @Override
  public void setType (String type)
  {

  }

  @Override
  public String getType ()
  {
    return (String) get("type");
  }

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument json = new JSONDocument(JSONDocument.Type.OBJECT);
    Iterator<? extends K> iterator = keySet().iterator();
    while(iterator.hasNext())
    {
      K key = iterator.next();
      V value = get(key);


      if (value instanceof String)
      {
        json.setString(key, (String) value);
      }
      else if (value instanceof Sendable)
      {
        if (value instanceof Enum)
        {
          JSONDocument doc = JSONDocument.createObject();
          doc.setString("name", ((Enum) value).name());
          json.set(key, doc);
        }
        else
        {
          json.set(key, ((JSON) value).toJSON());
        }
      }
      else if (value instanceof ArrayList)
      {
        JSONDocument doc = JSONDocument.createArray(((ArrayList) value).size());
        int i = 0;
        for (Object o : ((ArrayList) value))
        {
          doc.set(i, ((JSON) o).toJSON());
          i++;
        }
        json.set(key, doc);
      }
      else if (value instanceof Number)
      {
        json.setNumber(key, (Number) value);
      }
    }
    return json;
  }

  @Override
  public void fromJSON (Object doc)
  {
    JSONDocument _json = JSON.Parser.toJSON(doc);
    this.putAll((Map<? extends K, ? extends V>) _json.object());
  }
}
