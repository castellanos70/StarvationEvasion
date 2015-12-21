package starvationevasion.client.MegaMawile.model;

/**
 * States of network status
 */
public enum NetworkStatus
{
  TRY,
  CONNECTED,
  LOST, NOT_CONNECTED,
  DISCONNECTED,
  DISCONNECT,
  LOGGED_IN, AUTH_ERROR, TRYING;
}
