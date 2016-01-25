package starvationevasion.server;


public enum Endpoint
{
  USER("user"), HAND("hand"),
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
