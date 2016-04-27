package starvationevasion.server.io;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.io.formatters.Format;
import starvationevasion.server.model.DataType;
import starvationevasion.server.model.Encryptable;
import starvationevasion.server.model.Response;
import starvationevasion.server.model.Sendable;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface WriteStrategy extends Encryptable
{
  /**
   * Write data to socket
   * @param s object to be written to socket

   * @throws IOException When there is an error writing to stream
   */
  void write (Response s) throws IOException,
                                 BadPaddingException,
                                 InvalidKeyException,
                                 IllegalBlockSizeException,
                                 NoSuchPaddingException,
                                 NoSuchAlgorithmException;

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

  void setFormatter (DataType formatter);
}
