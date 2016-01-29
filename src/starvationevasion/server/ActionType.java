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
  QUIT,

  // responses
  SUCCESS, FAIL, NOTIFY, CONNECT,

  // NEW
  // get request for something
  // post request to add something new
  // put request to change an attribute of something
  // delete request to delete something
  GET, POST, PUT, DELETE

}