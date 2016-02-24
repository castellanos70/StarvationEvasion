package starvationevasion.util;


import starvationevasion.server.io.JSON;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class JsonAnnotationProcessor
{
  public static <T extends JSON> String gets (T inst)
  {
    for (Field f : inst.getClass().getDeclaredFields())
    {
      /**
       * Ensure the RetentionPolicy of 'Jsonify' is RUNTIME.
       */
      if (f.isAnnotationPresent(Jsonify.class))
      {
         Annotation[] annotations = f.getDeclaredAnnotations();
        for (Annotation annotation : annotations)
        {
          if(annotation instanceof Jsonify){
            Jsonify an = (Jsonify) annotation;
            try
            {

              f.setAccessible(true);
              if (an.type() == Jsonify.JsonType.ENUM)
              {
                System.out.println((f.get(inst).toString()) + " " + an.type());
              }
              else if (an.type() == Jsonify.JsonType.STRING)
              {
                System.out.println(f.get(inst) + " " + an.type());
              }
              else if (an.type() == Jsonify.JsonType.NUMBER)
              {
                System.out.println(String.valueOf(f.get(inst)) + " " + an.type());
              }
              else if (an.type() == Jsonify.JsonType.LIST)
              {
                ArrayList s = (ArrayList) f.get(inst);
                System.out.println(Arrays.toString(s.toArray()) + " " + an.type());
              }
            }
            catch(IllegalAccessException e)
            {
              // e.printStackTrace();
            }
          }
        }

      }
    }
    return "";
  }
}
