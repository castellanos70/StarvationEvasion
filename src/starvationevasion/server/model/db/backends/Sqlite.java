package starvationevasion.server.model.db.backends;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Map;


public class Sqlite implements Backend
{
  Connection connection = null;

  @Override
  public boolean connect ()
  {
    try
    {
      connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
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
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("create table ").append(table).append("(id integer, ");


      int size = properties.entrySet().size();
      int i = 0;
      for (Map.Entry<String, Object> entry : properties.entrySet())
      {
        stringBuilder.append(entry.getKey()).append(" ").append(entry.getValue());


        if (size-1 == i)
        {
          stringBuilder.append(")");
        }
        else
        {
          stringBuilder.append(", ");
        }

        i++;
      }

      statement.executeUpdate(stringBuilder.toString());
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void insert (String table)
  {
    Statement statement = null;
    try
    {
      statement = connection.createStatement();
      statement.executeUpdate("insert into " + table + " values(1, 'leo')");
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public ResultSet select (String table)
  {
    try
    {
      Statement statement = connection.createStatement();
      return statement.executeQuery("SELECT * FROM " + table);
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
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
