package starvationevasion.common;


import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.model.Sendable;

/**
 * Defines types of possible special events.
 */
public enum EnumSpecialEvent implements Sendable
{
  HURRICANE
          {
            @Override
            public JSONDocument toJSON ()
            {
              return null;
            }

            @Override
            public void fromJSON (Object doc)
            {

            }

            @Override
            public void setType (String type)
            {

            }

            @Override
            public String getType ()
            {
              return null;
            }
          }, DROUGHT
        {
          @Override
          public JSONDocument toJSON ()
          {
            return null;
          }

          @Override
          public void fromJSON (Object doc)
          {

          }

          @Override
          public void setType (String type)
          {

          }

          @Override
          public String getType ()
          {
            return null;
          }
        };
}
