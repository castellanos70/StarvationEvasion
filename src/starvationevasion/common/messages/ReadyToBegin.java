package starvationevasion.common.messages;

import java.io.Serializable;

/**
 * Shea Polansky
 * A message to clients sent to indicate either a) that all regions are taken and the game
 * is ready to begin, or b) that the previous ready state has been cancelled because someone has
 * e.g. unassigned themselves from a region.
 * If a given message is going to indicate a ready state, it will include the current server time,
 * and the time (also in server time) the game will automatically begin if it is not cancelled.
 * The date/time fields are represented as Unix timestamps (see: https://en.wikipedia.org/wiki/Unix_time).
 * If the message is a cancel ready state message, the values of the date/time fields is undefined.
 * No guarantee is made as to what time the server will report; differences in time zone or clock drift may
 * result in seemingly inaccurate time responses. It is the responsibility of the client to properly
 * address this issue. (Although it is probably a safe bet that for the time scales relevant to the game,
 * one second on the server will be measured as one second on the client).
 */
public class ReadyToBegin implements Serializable
{
  public ReadyToBegin(boolean isReady, long currentServerTime, long gameStartServerTime)
  {
    this.isReady = isReady;
    this.currentServerTime = currentServerTime;
    this.gameStartServerTime = gameStartServerTime;
  }

  public final boolean isReady;
  public final long currentServerTime;
  public final long gameStartServerTime;
}
