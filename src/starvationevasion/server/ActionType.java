package starvationevasion.server;

/**
 * @author Javier Chavez
 */

/**
 * Types of requests and responses. They could be split but
 * this is a small assignment.
 */
public enum ActionType
{
  // requests
  BUY, SELL, INVENTORY, QUIT,

  // responses
  SUCCESS, FAIL, NOTIFY, CONNECT

}