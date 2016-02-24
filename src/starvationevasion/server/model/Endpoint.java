package starvationevasion.server.model;


public enum Endpoint
{
  USER_CREATE("user_create"),
  USER_READ("user_read"),
  USER_UPDATE("user_update"),
  USERS("users"),
  LOGIN("login"),


  HAND_READ("hand_read"),
  HAND_UPDATE("hand_update"),



  CHAT("chat"),
  CARD("card");


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
