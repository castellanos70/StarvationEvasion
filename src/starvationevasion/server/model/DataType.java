package starvationevasion.server.model;


public enum DataType
{
  JSON("application/json"), POJO("java"), TEXT("text"), HTML("text/html; charset=UTF-8");

  private String value = "";
  DataType (String text)
  {
    this.value = text;
  }

  @Override
  public String toString ()
  {
    return this.value;
  }
}