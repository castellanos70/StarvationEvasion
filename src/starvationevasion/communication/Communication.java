package starvationevasion.communication;

import starvationevasion.common.EnumRegion;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Response;
import starvationevasion.server.model.Sendable;
import starvationevasion.server.model.Type;

import java.util.ArrayList;

/**
 * The Communication class is meant to be implemented by a generic module
 * that will be responsible for both sending to and receiving data from
 * a server instance.
 *
 * For StarvationEvasion, an AI or UI client will instantiate an object
 * that implements Communication so that they are capable of maintaining contact
 * with the server. They themselves should not need to know anything about how
 * this is done or what happens when data is sent/received. They only need to know
 * how to pull data from the Communication module and send data through the module, which
 * should be a simple process for both.
 *
 * For any class implementing Communication, the following should be upheld ::
 *
 *      1) It should be entirely thread-safe. The hope will be that the server
 *         communicates with a Communication module as it needs to, and an AI or
 *         UI client will *also* be able to make requests of the Communication module
 *         whenever they want (Ex: as part of their main update loop). As a result,
 *         there should be no issues with any number of threads interfacing with the
 *         same Communication instance. This also takes much of the burden off of
 *         classes that use a Communication module to be thread-safe themselves.
 *
 *      2) For all incoming data from the server, the module is responsible for pre-processing
 *         that data and organizing it in such a way that the AI and UI clients that are
 *         depending upon the module can retrieve the data in a convenient manner. For example,
 *         if the server is sending an update about changes in world data, the module must
 *         keep a list of changes since the last time the AI/UI client requested the latest
 *         data.
 *
 * @author Javier Chavez (javierc@cs.unm.edu), Justin Hall, George Boujaoude
 */
public interface Communication
{
  /**
   * This gets the starting nano time that was received from the server. Note that server responses
   * of type TIME include this data, but this means that no listener needs to be bound to this type
   * as the Communication module will manage this itself.
   *
   * @return starting nano time from the server
   */
  double getStartNanoTime();

  /**
   * The CommModule will stall whatever thread this is called on while it attempts to connect to
   * the server. A single Communication module should be bound to a host/port combination that does not
   * change after instantiation.
   *
   * When called, this should first check to see if it is already connected. If not, the full
   * connection routine should be performed.
   *
   * The primary reason for this function's existence is to
   *        1) allow for an initial connect attempt, and
   *        2) to allow future connect attempts should a disconnect event happen unexpectedly
   */
  void connect();

  /**
   * Disposes of this module. All existing threads should be shut down and data cleared out. No
   * existing connection should remain.
   *
   * Calling connect() undoes this.
   */
  void dispose();

  /**
   * Returns the current connection state of the module. If at any point the module loses connection
   * with the server, this will return false.
   *
   * @return true if connected to the server and false if not
   */
  boolean isConnected();

  /**
   * Attempts to send a new login request to the server with the given information.
   * @param username username as a string
   * @param password password as a string
   * @param region region associated with the logging-in user
   * @return true if the request succeeded in sending and false if not (NOTE :: This *does not* reflect
   *         the status of the login - only whether the request was sent or not)
   */
  boolean login(String username, String password, EnumRegion region);

  /**
   * Note :: Check the Endpoint that you are using before you set data to null - some are safe
   *         to use without extra data associate with them while some are not (If they say "No Payload"
   *         with their comment block, it should be fine to not include extra data). See
   *         starvationevasion.server.model.Endpoint for more information.
   *
   * Attempts to send a new request to the server tagged with the given endpoint. The "data" field
   * is potentially optional, while the "message" field is entirely optional. Put null for both of
   * these if you do not wish to use them.
   *
   * @param endpoint endpoint to tag the request with
   * @param data Sendable object to attach to the request - the most common type for this is Payload
   *             (see starvationevasion.server.model.Payload for more information)
   * @param message optional message to attach - this *must* be null if data is also null
   * @return true if the request was sent successfully and false if anything went wrong
   */
  boolean send(Endpoint endpoint, Sendable data, String message);

  /**
   * This function attempts to send a new chat request to the server. Note that for destination,
   * the only two types that can be placed here (at the moment) are String and EnumRegion.
   *
   * The "data" field is for attaching extra data to the chat request. At the moment the only
   * supported type is PolicyCard.
   *
   * @param destination where to send the chat
   * @param text chat text
   * @param data extra data (optional - can be null)
   * @param <T> type of destination (String, EnumRegion)
   * @param <E> type of data (PolicyCard only at the moment)
   * @return true if the request succeeded and false if anything went wrong
   */
  <T, E> boolean sendChat(T destination, String text, E data);

  /**
   * All messages sent from the server since the last time this function was called will be packaged
   * up into an array list and returned, and all existing messages within the Communication module's
   * internal storage queue will be cleared out.
   *
   * The returned ArrayList will contain an ordered list of responses, where the first (index 0) reflects
   * the *oldest* in the list and the last (index size-1) represents the most recent response from
   * the server.
   *
   * @return ordered list of server messages since the last time this was called
   */
  ArrayList<Response> pollMessages();
}
