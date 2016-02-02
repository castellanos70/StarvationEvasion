package starvationevasion.client.net.common;

/**
 * States of network status
 */
public enum NetworkStatus
{
  CONNECTED,
  LOST, NOT_CONNECTED,
  DISCONNECTED,
  DISCONNECT,
  LOGGED_IN, AUTH_ERROR, TRYING, TRY;
}
