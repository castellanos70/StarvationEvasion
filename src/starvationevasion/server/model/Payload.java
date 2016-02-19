package starvationevasion.server.model;

import java.util.HashMap;

public interface Payload<K, V>
{
  HashMap<K, V> getData();

  void setData(HashMap<K, V> data);
}
