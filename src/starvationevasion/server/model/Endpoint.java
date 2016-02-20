package starvationevasion.server.model;


public enum Endpoint
{
  USER_CREATE("user_create"),
  USER_READ("user_read"),
  USER_UPDATE("user_update"),

  LOGIN("login"),


  HAND("hand"),

  CHAT("chat"),
  CARD("card?");


  private String url;

  Endpoint(String url)
  {
    this.url = url;
  }

  public String getUrl ()
  {
    return url;
  }
}
