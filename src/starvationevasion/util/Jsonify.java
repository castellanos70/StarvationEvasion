package starvationevasion.util;



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Jsonify
{
  enum JsonType
  {
    STRING, NUMBER, ARRAY, OBJECT, LIST, ENUM
  }

  JsonType type() default JsonType.STRING;
}
