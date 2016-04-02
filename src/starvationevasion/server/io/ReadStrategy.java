package starvationevasion.server.io;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import java.io.DataInputStream;
import java.io.IOException;

public interface ReadStrategy<Result>
{
  /**
   * Read in data
   *
   * @return Generic return
   *
   * @throws IOException            when there is an error with connection
   * @throws ClassNotFoundException When a object is sent and not found
   */
  Result read () throws IOException, ClassNotFoundException;

  /**
   * Shutdown the reading
   *
   * @throws IOException
   */
  void close () throws IOException;

  /**
   * Get the stream for reading in
   *
   * @return stream to read from
   */
  DataInputStream getStream ();

  /**
   * Sets the stream. This is mainly to overcome the issue if you want to switch from
   * ObjectStream functionality to PrintStream functionality
   *
   * @param inStream stream used to read from
   */
  void setStream (DataInputStream inStream);
}
