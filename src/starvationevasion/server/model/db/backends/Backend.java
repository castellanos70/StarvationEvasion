package starvationevasion.server.model.db.backends;


import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Set;

public interface Backend
{
  boolean connect();

  void createTable (String table, Hashtable<String, Object> properties);

  void insert (String table, Set<Object> values);

  ResultSet select (String table, String where);

  void close();
}
