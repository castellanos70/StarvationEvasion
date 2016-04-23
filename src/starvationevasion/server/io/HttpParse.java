package starvationevasion.server.io;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;

public class HttpParse
{

  private String _requestLine;
  private Hashtable<String, String> _headers;
  private StringBuffer msgStringBuffer;

  public HttpParse ()
  {
    _headers = new Hashtable<>();
    msgStringBuffer = new StringBuffer();
  }

  /**
   * Parse and HTTP/Handshake request.
   */
  public void parseRequest (String request) throws IOException
  {
    BufferedReader reader = new BufferedReader(new StringReader(request));

    setRequestLine(reader.readLine());

    String header = reader.readLine();
    while(header.length() > 0)
    {
      putHeader(header);
      header = reader.readLine();
    }

    String bodyLine = reader.readLine();
    while(bodyLine != null)
    {
      appendMessageBody(bodyLine);
      bodyLine = reader.readLine();
    }

  }

  /**
   * Request-Line found at the top of the header
   *
   * @return String with Request-Line
   */
  public String getRequestLine ()
  {
    return _requestLine;
  }


  /**
   * The message-body of the
   *
   * @return String with message-body
   */
  public String getMessageBody ()
  {
    return msgStringBuffer.toString();
  }

  /**
   * Get the value of the header. Does a look up on a HashMap.
   *
   * @param key Key of the header
   *
   * @return String with the value of the header or null if not found.
   */
  public String getHeaderParam (String key)
  {
    return _headers.get(key);
  }


  private void putHeader (String header)
  {
    int idx = header.indexOf(":");
    if (idx == -1)
    {
      throw new RuntimeException();
    }
    _headers.put(header.substring(0, idx), header.substring(idx + 1, header.length()).trim());
  }

  private void setRequestLine (String requestLine)
  {
    if (requestLine == null || requestLine.length() == 0)
    {
      throw new RuntimeException();
    }
    _requestLine = requestLine;
  }


  private void appendMessageBody (String bodyLine)
  {
    msgStringBuffer.append(bodyLine).append("\r\n");
  }

}