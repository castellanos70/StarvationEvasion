package starvationevasion.server;

/**
 * @author Javier Chavez
 *
 */


import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.impl.JSONStreamReaderImpl;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.Arrays;


/**
 * Class that mimics the TCP layer
 *
 * @param <T> class extending tcp
 */
public abstract class TCP<T extends TCP>
{
  public static String DELIMITER="\\n";
  private ActionType request;
  private int quantityData;
  private float monetaryData;
  private double time;
  private String msg = " ";
  private String path = "";
  private String from = "";
  private JSONDocument payload;





  public TCP(ActionType type, double time, String data)
  {
    this.request = type;
    this.msg = data;
    this.time = time;
  }


  public TCP(ActionType type, double time, JSONDocument payload)
  {
    this.request = type;
    this.payload = payload;
    this.time = time;
  }


  /**
   * Turn a string into a TCP
   *
   * @param s valid TCP string (see. toString())
   */
  public TCP(String s) throws Exception
  {
    System.out.println(s);

    String[] _dataArray = s.split(DELIMITER);

    if (_dataArray.length == 0)
    {
      throw new Exception("Improper format");
    }

    // action
    String[] _requestData = _dataArray[0].split(" ");

    if (_requestData.length < 2)
    {
      throw new Exception("Improper format");
    }


    request = ActionType.valueOf(_requestData[0]);

    path = _requestData[1];

    // payload may bot be avail
    if (_dataArray.length == 1)
    {
      return;
    }

    JSONStreamReaderImpl sk = new JSONStreamReaderImpl(new StringReader(_dataArray[1]));
    payload = sk.build();


  }

  public void setTime(double time)
  {
    this.time = time;
  }

  public void setMsg(String msg)
  {
    this.msg = msg;
  }

  public void setRequest(ActionType request)
  {
    this.request = request;
  }

  public String getMsg()
  {
    return msg;
  }

  public ActionType getRequest()
  {
    return request;
  }

  public int getQuantityData()
  {
    return quantityData;
  }

  public float getMonetaryData()
  {
    return monetaryData;
  }

  public double getTime()
  {
    return time;
  }

  public String getStringTime()
  {
    return String.format("%.3f", time);
  }

  public String getStringMonetaryData()
  {
    return String.format("%01.02f", monetaryData);
  }

  public String getPath ()
  {
    return path;
  }

  public void setPath (String path)
  {
    this.path = path;
  }

  public String getFrom ()
  {
    return from;
  }

  public void setFrom (String from)
  {
    this.from = from;
  }

  public JSONDocument getPayload ()
  {
    return payload;
  }

  public void setPayload (JSONDocument payload)
  {
    this.payload = payload;
  }
}