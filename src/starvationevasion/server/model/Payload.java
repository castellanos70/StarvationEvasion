package starvationevasion.server.model;

import com.oracle.javafx.jmx.json.JSONDocument;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Payload<K, V> extends HashMap<K, V> implements Sendable
{

  @Override
  public V put (K key, V value)
  {
    return super.put(key, value);
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
  public String toJSONString ()
  {
    return null;
  }

  @Override
  public JSONDocument toJSON ()
  {
    return null;
  }
}
