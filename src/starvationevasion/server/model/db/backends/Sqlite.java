package starvationevasion.server.model.db.backends;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


public class Sqlite implements Backend
{
  private final String url;
  private Connection connection = null;
  private Properties properties = null;

  public Sqlite(String url)
  {
    this.url = url;
  }

  @Override
  public boolean connect ()
  {
    try
    {
      connection = DriverManager.getConnection(url);
    }
    catch(SQLException e)
    {
      e.printStackTrace();
      return false;
    }
    return true;
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
  public void insert (String table, Set<String> cols, Set<Object> values)
  {
    Statement statement = null;
    try
    {
      statement = connection.createStatement();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("insert into ").append(table).append("(");

      int size = cols.size();
      int i = 0;

      for (String value : cols)
      {
        stringBuilder.append(value);

        if (size-1 == i)
        {
          stringBuilder.append(") ");
        }
        else
        {
          stringBuilder.append(", ");
        }

        i++;
      }


      stringBuilder.append("values ( ");

      size = values.size();
      i = 0;

      for (Object value : values)
      {
        if (value == null)
        {
          stringBuilder.append("NULL");
        }

        if (value instanceof String)
        {
          stringBuilder.append("'").append(value).append("'");
        }
        else if (value instanceof Number)
        {
          stringBuilder.append(value);
        }


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
      System.out.println(stringBuilder.toString());
      statement.executeUpdate(stringBuilder.toString());

    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public ResultSet select (String table, String where)
  {
    try
    {
      Statement statement = connection.createStatement();

      return statement.executeQuery("SELECT * FROM " + table + " " + where);
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
    if (connection == null)
    {
      return;
    }

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
