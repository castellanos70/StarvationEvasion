package starvationevasion.server.model;


public interface Renderable<T>
{
  String getViewPath();

  T setViewPath(String s);
}
