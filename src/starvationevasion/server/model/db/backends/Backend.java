package starvationevasion.server.model.db.backends;


import java.sql.ResultSet;
import java.util.Hashtable;

public interface Backend
{
  boolean connect();

  void createTable (String table, Hashtable<String, Object> properties);

  void insert (String table);

  ResultSet select (String table);

  void close();
}
