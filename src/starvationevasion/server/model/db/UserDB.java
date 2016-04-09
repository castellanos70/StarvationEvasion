package starvationevasion.server.model.db;

import starvationevasion.common.EnumRegion;
import starvationevasion.server.model.User;
import starvationevasion.server.model.db.backends.Backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;


public class UserDB extends Transaction<User>
{
  private ArrayList<User> cache = new ArrayList<>();
  private boolean dirty = true;

  public UserDB (Backend db)
  {
    super(db);
  }

  @Override
  public ArrayList<User> getAll ()
  {
    ResultSet results = getDb().select("user", "");
    if (!dirty)
    {
      return cache;
    }
    cache.clear();
    try
    {

      while(results.next())
      {
        // int id = results.getInt("id");
        cache.add(createUser(results));
      }


      results.close();
      dirty = false;
      return cache;
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public <V> User get (V data)
  {
    if (!dirty)
    {
      for (User user : cache)
      {
        if(user.getUsername().equals(data))
        {
          return user;
        }
      }
    }

    ResultSet results = getDb().select("user", "where username='" + data + "'");
    try
    {
      int i =results.getFetchSize();
      if (i > 0)
      {
        if (results.first())
        {
          return createUser(results);
        }
      }
      results.close();
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public <V> User create (V data)
  {
    dirty = true;
    User user = ((User) data);
    LinkedHashSet<Object> values = new LinkedHashSet<>();

    values.add(user.getUsername());
    values.add(user.getPassword());
    values.add(user.getSalt());
    values.add(user.getRegion());

    getDb().insert("user", values);
    return user;
  }

  @Override
  public <V> void delete (V data)
  {
    dirty = true;
  }


  private User createUser(ResultSet results) throws SQLException
  {
    String username = results.getString("username");
    String password = results.getString("password");
    String salt = results.getString("salt");
    EnumRegion region = EnumRegion.getRegion(results.getString("region"));

    User u = new User();
    u.setEncryptedPassword(password, salt);
    u.setUsername(username);
    u.setRegion(region);
    return u;
  }
}
