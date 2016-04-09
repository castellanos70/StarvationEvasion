package starvationevasion.server.model.db;


import starvationevasion.server.model.db.backends.Backend;

public abstract class Transaction<T>
{
  private final Backend db;

  public Transaction(Backend db)
  {
    this.db = db;
  }

  public abstract T getAll();

  public abstract <V> T get(V data);

  public abstract <V> T create(V data);

  public abstract <V> void delete(V data);
}
