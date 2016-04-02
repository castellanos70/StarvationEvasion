package starvationevasion.server.io;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.model.Sendable;

import java.io.DataOutputStream;
import java.io.IOException;

public interface WriteStrategy
{
  /**
   * Write data to socket
   * @param s object to be written to socket
   * @param <T> Class of the object; however, must be Sendable
   * @throws IOException When there is an error writing to stream
   */
  <T extends Sendable> void write (T s) throws IOException;

  /**
   * Close the stream
   * @throws IOException
   */
  void close () throws IOException;

  /**
   * Get the current output stream
   *
   * @return Stream for writing to socket
   */
  DataOutputStream getStream ();

  /**
   * Sets the stream. This is mainly to overcome the issue if you want to switch from
   * ObjectStream functionality to PrintStream functionality
   *
   * @param outStream stream for writer
   */
  void setStream (DataOutputStream outStream);
}
