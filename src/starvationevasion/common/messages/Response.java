package starvationevasion.common.messages;

/**
 * Shea Polansky
 * Possible server responses to client messages.
 * The server *will* respond to every message with an instance of the Response class. The only
 * exception to this is when the server has decided to forcibly disconnect a given client.
 * The server may disconnect, with or without sending a notification, any client that
 * sends three or more messages that result in error Responses in a given minute.
 * Note that a message may be 'illegal' (e.g. attempting to play two vote-required policy
 * cards in a specific turn or attempting to login with bad credentials)
 * but correctly transmitted. In this case, a message of Response.OK
 * will be transmitted to the client, followed by a more specific rejection message.
 */
public enum Response
{
  /**
   * Message was corrupt, could not be deserialized to a known class, etc.
   */
  BAD_MESSAGE
      {
        /**
         * @return true if this message represents an error, false otherwise.
         */
        public boolean isError()
        {
          return true;
        }
      },
  /**
   * Message was not appropriate at this time (e.g. sending a 'card played' message
   * when a vote message was expected.
   */
  INAPPROPRIATE
      {
        /**
         * @return true if this message represents an error, false otherwise.
         */
        public boolean isError()
        {
          return true;
        }
      },
  /**
   * Any other error
   */
  OTHER_ERROR
      {
        /**
         * @return true if this message represents an error, false otherwise.
         */
        public boolean isError()
        {
          return true;
        }
      },
  /**
   * Message was correctly transmitted and received.
   */
  OK
      {
        /**
         * @return true if this message represents an error, false otherwise.
         */
        public boolean isError()
        {
          return false;
        }
      }
}
