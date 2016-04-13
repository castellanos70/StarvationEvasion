package starvationevasion.server.model.db;


import starvationevasion.server.model.db.backends.Backend;

import java.util.ArrayList;

public abstract class Transaction<T>
{
  private final Backend db;

  public Transaction(Backend db)
  {
    this.db = db;
  }

  public abstract ArrayList<T> getAll();

  public abstract <V> T get(V id);

  public abstract <V> T create(V data);

  public abstract <V> void delete(V id);

  public abstract <V> void update(V id, T data);

  Backend getDb ()
  {
    return db;
  }
}
