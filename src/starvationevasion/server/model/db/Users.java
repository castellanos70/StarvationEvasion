package starvationevasion.server.model.db;

import starvationevasion.common.EnumRegion;
import starvationevasion.server.io.NotImplementedException;
import starvationevasion.server.model.User;
import starvationevasion.server.model.db.backends.Backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;


public class Users extends Transaction<User>
{
  private static ArrayList<User> cache = new ArrayList<>();
  private boolean dirty = true;

  public Users (Backend db)
  {
    super(db);
  }

  @Override
  public ArrayList<User> getAll ()
  {
    getDb().connect();
    ResultSet results = getDb().select("user", "");
    if (!dirty)
    {
      return cache;
    }

    try
    {
      cache.clear();
      while(results.next())
      {
        cache.add(initUser(results));
      }


      results.close();
      dirty = false;
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
    getDb().close();
    return cache;
  }

  @Override
  public <V> User get (V user)
  {
    User found = null;
    getDb().connect();
    if (!dirty)
    {
      for (User _user : cache)
      {
        if(_user.equals(user))
        {
          found = _user;
        }
      }
    }

    ResultSet results = getDb().select("user", "where username='" + ((User) user).getUsername() + "'");
    try
    {
      int i =results.getFetchSize();
      if (i > 0)
      {
        if (results.first())
        {
          found = initUser(results);
        }
      }
      results.close();
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
    getDb().close();

    return found;
  }

  @Override
  public <V> User create (V data)
  {
    getDb().connect();
    dirty = true;
    User user = ((User) data);
    LinkedHashSet<Object> values = new LinkedHashSet<>();
    values.add(user.getUsername());
    values.add(user.getPassword());
    values.add(user.getSalt());
    values.add(user.getRegion());

    LinkedHashSet<String> cols = new LinkedHashSet<>();
    cols.add("username");
    cols.add("password");
    cols.add("salt");
    cols.add("region");

    getDb().insert("user",cols, values);
    getDb().close();
    return user;
  }

  @Override
  public <V> void delete (V data)
  {
    dirty = true;
    throw new NotImplementedException();
  }

  @Override
  public <V> void update (V username, User data)
  {
    dirty = true;
    throw new NotImplementedException();
  }


  private User initUser (ResultSet results) throws SQLException
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
