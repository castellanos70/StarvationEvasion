package starvationevasion.server;


import starvationevasion.server.io.ReadStrategy;
import starvationevasion.server.io.WriteStrategy;
import starvationevasion.server.model.Response;
import starvationevasion.server.model.User;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface Connector
{

  ReadStrategy getReader ();

  void setReader (ReadStrategy reader);

  void setWriter (WriteStrategy writer);

  WriteStrategy getWriter ();

  void setUser (User user);

  User getUser();

  void send (Response data);

  void shutdown();

  Class getConnectionType();

}
