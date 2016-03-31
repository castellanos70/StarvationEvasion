package starvationevasion.PreprocessingTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by James on 3/30/2016.
 */
public class JavaWebCrawler_IndexMundi
{
  public static void main(String[] args) {
    URL url;
    InputStream is = null;
    BufferedReader br;
    String line;

    try {
      url = new URL("http://www.indexmundi.com/agriculture/?country=af&commodity=barley&graph=imports");
      is = url.openStream();  // throws an IOException
      br = new BufferedReader(new InputStreamReader(is));

      while ((line = br.readLine()) != null) {
        System.out.println(line);
      }
    } catch (MalformedURLException mue) {
      mue.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } finally {
      try {
        if (is != null) is.close();
      } catch (IOException ioe) {
        // nothing to see here
      }
    }
  }
}
