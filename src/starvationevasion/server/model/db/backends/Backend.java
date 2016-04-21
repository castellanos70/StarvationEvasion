package starvationevasion.server.model.db.backends;


import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Set;

public interface Backend
{
  /**
   * Connect to database.
   *
   * @return boolean true if successful connection
   */
  boolean connect();

  /**
   * Create a table
   *
   * @param table name of table to create
   * @param properties table of column name (key) and column type value
   *
   * With some cleverness, column constraints can be applied.
   *
   * TODO explicitly support constraints
   */
  void createTable (String table, Hashtable<String, Object> properties);

  /**
   * Insert data into the database
   *
   * @param table name of the table to insert into
   * @param cols name of the columns corresponding to values
   * @param values actual values to be saved
   */
  void insert (String table, Set<String> cols, Set<Object> values);

  /**
   * Query data
   *
   * @param table name of the table to be queried from
   * @param where where statement.
   * @return set of rows.
   */
  ResultSet select (String table, String where);

  /**
   * Query data
   *
   * @param table name of the table to be queried from
   * @param where where statement.
   * @return set of rows.
   */
  ResultSet update (String table, String where, Set<String> cols, Set<Object> values);

  /**
   * Query data
   *
   * @param table name of the table to be queried from
   * @param where where statement.
   */
  void delete (String table, String where);

  /**
   * Close the connection to the database.
   */
  void close();
}
