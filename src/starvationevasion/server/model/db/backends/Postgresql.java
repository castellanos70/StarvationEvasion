package starvationevasion.server.model.db.backends;
import java.sql.*;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;


public class Postgresql implements Backend
{

  private Connection connection;

  @Override
  public boolean connect ()
  {
    String url = "jdbc:postgresql://localhost/test";
    Properties props = new Properties();
    props.setProperty("user","fred");
    props.setProperty("password","secret");
    props.setProperty("ssl","true");
    // Connection conn = null;
    try
    {
      connection = DriverManager.getConnection(url, props);
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }

    return false;
  }

  @Override
  public void createTable (String table, Hashtable<String, Object> properties)
  {
    Statement statement = null;
    try
    {
      statement = connection.createStatement();
      // statement.executeUpdate("");
      statement.close();
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }

  }


  @Override
  public void insert (String table, Set<Object> values)
  {

  }

  @Override
  public ResultSet select (String table, String where)
  {
    return null;
  }

  @Override
  public void close ()
  {
    try
    {
      connection.close();
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
  }


}
