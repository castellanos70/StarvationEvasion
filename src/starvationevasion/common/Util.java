package starvationevasion.common;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static starvationevasion.common.Constant.ANON_NAME_ARRAY;

/**
 * public static methods that might be useful in as utilities.
 */
public class Util
{
  /**
   * There need only be one instance of a random number generator used
   * by all the parts of the program. Let this be it.
   */
  public static Random rand = new Random();

  public static int randInt (int min, int max)
  {
    return rand.nextInt((max - min) + 1) + min;
  }

  public static boolean likeliness (float likeliness)
  {
    return rand.nextFloat() <= likeliness;
  }


  public static float linearInterpolate(float x1, float x2, float x3, float y1, float y3)
  {
    if (x2 <= x1) return y1;
    if (x2 >= x3) return y3;

    float w = (x2-x1)/(x3-x1);
    float y2 = y1*(1-w) + y3*w;

    return y2;
  }

  public static Color brighten(Color color, double percentage)
  {
    int r = (int)Math.min(255.0,color.getRed()*(1.0+percentage));
    int g = (int)Math.min(255.0,color.getGreen()*(1.0+percentage));
    int b = (int)Math.min(255.0,color.getBlue()*(1.0+percentage));
    return new Color(r,g,b);
  }



  public static SecretKey endServerHandshake (Socket s, KeyPair keyPair)
  {
    // first
    byte[] sdf = new byte[128];
    try
    {
      s.getInputStream().read(sdf);
      String decryptedMsg = decrypt(sdf, keyPair.getPrivate());

      byte[] msg = Base64.getDecoder().decode(decryptedMsg);
      return  new SecretKeySpec(msg, "AES");

    }
    catch(IOException e)
    {
      System.out.println("There was an error ending handshake.");
    }
    return null;
  }

  public static void startServerHandshake (Socket s, KeyPair keyPair, String streamType)
  {
    // first
    byte[] publicKey = keyPair.getPublic().getEncoded();
    String pubKeyStr = Base64.getEncoder().encodeToString(publicKey);

    try
    {
      PrintWriter write = new PrintWriter(s.getOutputStream(), true);

      write.print("RSA-Socket-Key: " + pubKeyStr + "\n" + streamType +"\n");
      write.flush();
    }
    catch(IOException e)
    {
      System.out.println("There was an error starting handshake.");
    }

  }


  public static final String[] names = new String[ANON_NAME_ARRAY.length];

  /**
   * This is used for creating http responses
   * Internal use of server only.
   * @return String of the current datetime for valid HTTP header response
   */
  public static final String getServerTime ()
  {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

    return dateFormat.format(calendar.getTime());
  }

  public static String generateName () {
    return ANON_NAME_ARRAY[Util.rand.nextInt(ANON_NAME_ARRAY.length)]
              + "-" + Long.valueOf(String.valueOf(System.currentTimeMillis()), 16);
  }

  public static String decrypt(byte[] text, PrivateKey key)
  {
    byte[] dectyptedText = null;
    try
    {
      // get an RSA cipher object and print the provider
      final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

      // decrypt the text using the private key
      cipher.init(Cipher.DECRYPT_MODE, key);
      dectyptedText = cipher.doFinal(text);

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    return new String(dectyptedText);
  }


  /**
   * Given a file name returns the absolute path of that file in URL format.
   * taken from:
   * http://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html
   *
   * @param filename within the search path.
   * @return Absolute path of that file in URL format
   */
  public static String convertToFileURL(String filename)
  {
    String path = new File(filename).getAbsolutePath();
    if (File.separatorChar != '/')
    {
      path = path.replace(File.separatorChar, '/');
    }

    if (!path.startsWith("/"))
    {
      path = "/" + path;
    }
    return "file:" + path;
  }




  public static ArrayList<String> getFilesInDir(String dirName)
  {
    File folder = new File(dirName);

    if (!folder.isDirectory()) return null;

    ArrayList<String> files = new ArrayList<>();

    for (File f : folder.listFiles())
    {
      if (!f.isHidden()) files.add(f.getPath());
    }
    return files;
  }
}
