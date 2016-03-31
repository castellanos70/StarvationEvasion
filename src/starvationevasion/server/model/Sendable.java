package starvationevasion.server.model;


import starvationevasion.server.io.JSON;

import java.io.Serializable;

public interface Sendable extends Serializable, JSON
{
  void setType(String type);

  String getType();
}
