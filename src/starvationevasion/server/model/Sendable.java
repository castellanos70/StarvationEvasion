package starvationevasion.server.model;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.server.io.JSON;

import java.io.Serializable;

/**
 * Sendable is a collection of classes that enable implementing classes to be
 * send down a socket pipeline.
 */
public interface Sendable extends Serializable, JSON
{
  void setType(String type);

  String getType();
}
