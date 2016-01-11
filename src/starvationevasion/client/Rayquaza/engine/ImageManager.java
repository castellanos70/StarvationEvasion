/**
 * ImageManager stores all images that are used as the textures.
 * Current iteration of ImageManager works with Image and BufferedImage classes.
 * <p>
 * All images must be located in the "res/image" directory or any subdirectory thereof.
 * Usage ImageManager.get[ImageClass](String "relative path to root folder");
 * <p>
 * Any files with .gif, .png, .jpg suffixes located in "res/image" are automatically loaded.
 *
 * @author Mohammad R. Yousefi
 * @param ROOT_DIRECTORY The root directory of the ImageManager resource files.
 * @param FILE_FILTER The filter used by the ImageManager.
 */
package starvationevasion.client.Rayquaza.engine;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ImageManager
{
  final static public String ROOT_PATH = "/res/image/";
  final static public File ROOT_DIRECTORY = initRootFile();
  final static public FileFilter FILE_FILTER = initFileFilter();
  final static private HashMap<String, BufferedImage> bufferedImageMap = new HashMap<>();
  final static private HashMap<String, WritableImage> fxImageMap = new HashMap<>();
  final static private List<String> vectorImagePaths = new ArrayList<>();
  final static private List<String> newVectorImagePaths = new ArrayList<>();

  static // Defaults.
  {
    if (ROOT_DIRECTORY.isDirectory())
    {
      loadFiles(ROOT_DIRECTORY, true);
    } else
    {
      String path = "/res/image";
      URL resourcePath = ImageManager.class.getResource(path);
      String jarPath = resourcePath.getPath().substring(5, resourcePath.getPath().indexOf("!"));
      JarFile jar = null;
      try
      {
        jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
      } catch (IOException e)
      {
        e.printStackTrace();
      }
      Enumeration<JarEntry> entries = jar.entries();
      while (entries.hasMoreElements())
      {
        String name = entries.nextElement().getName();
        if (name.startsWith(path.substring(1)))
        {
          String entry = name.substring(path.length());
          if (entry.length() == 0) continue;
          else if (checkSuffix(entry))
          {
            try
            {
              loadFXImage(entry);
              loadBufferedImage(entry);
            } catch (IOException e)
            {
              e.printStackTrace();
            }
          }
        }
      }
    }


    // Replaced by loadFiles
    //    loadFilesAsFXImage(ROOT_DIRECTORY, true);
    //    loadFilesAsBufferedImage(ROOT_DIRECTORY, true);
  }

  /**
   * Check the suffix of file path and reject non image paths.
   *
   * @param filePath String representing the relative file path URL.
   */
  private static boolean checkSuffix(String filePath)
  {
    filePath = filePath.toLowerCase();
    return filePath.endsWith(".png") || filePath.endsWith(".jpg") || filePath.endsWith(".gif");
  }

  /* Adds a SVGPath from the file into memory. */

  private static void addSVGImage(String imagePath)
  {
    //TODO parse SVG and add to map.
    throw new UnsupportedOperationException();
  }

  /**
   * Retrieves an image. If image is not in memory attempts to load it.
   *
   * @param imagePath The relative path to the image rooted in "res".
   */
  public static BufferedImage getBufferedImage(String imagePath)
  {
    if (bufferedImageMap.containsKey(imagePath)) return bufferedImageMap.get(imagePath);
    else
    {
      try
      {
        loadBufferedImage(imagePath);
        return bufferedImageMap.get(imagePath);
      } catch (IOException ex)
      {
        return null;
      }
    }
  }

  /**
   * Retrieves an image. If image is not in memory attempts to load it.
   *
   * @param imagePath The relative path to the image rooted in "res".
   */
  public static Image getFXImage(String imagePath)
  {
    if (fxImageMap.containsKey(imagePath)) return fxImageMap.get(imagePath);
    else
    {
      try
      {
        loadFXImage(imagePath);
        return fxImageMap.get(imagePath);
      } catch (IOException ex)
      {
        return null;
      }
    }
  }

  private static FileFilter initFileFilter()
  {
    return file -> checkSuffix(file.getPath());
  }

  private static File initRootFile()
  {
    try
    {
      URL url = ImageManager.class.getProtectionDomain().getCodeSource().getLocation();
      return new File(new File(url.toURI()), "res/image");
    } catch (URISyntaxException e)
    {
      e.printStackTrace();
      System.exit(1);
      return null;
    }
  }

  private static void loadBufferedImage(File imageFile) throws IOException
  {
    String imagePath = ROOT_DIRECTORY.toURI().relativize(imageFile.toURI()).getPath();
    if (bufferedImageMap.containsKey(imagePath)) System.out.printf("Attempting to duplicate \"%s\"\n", imagePath);
    else
    {
      bufferedImageMap.put(imagePath, ImageIO.read(imageFile));
    }
  }

  /* Adds an Image from the file into memory. Used by get methods. */
  private static void loadBufferedImage(String imagePath) throws IOException
  {
    if (bufferedImageMap.containsKey(imagePath)) System.out.printf("Attempting to duplicate \"%s\"\n", imagePath);
    else
    {
      bufferedImageMap.put(imagePath, ImageIO.read(ImageManager.class.getResourceAsStream(ROOT_PATH + imagePath)));
    }
  }

  /* Adds an Image from the file into memory. Used by get methods. */
  private static void loadFXImage(String imagePath) throws IOException
  {
    if (fxImageMap.containsKey(imagePath)) System.out.printf("Attempting to duplicate \"%s\"\n", imagePath);
    else
    {
      BufferedImage image = null;
      if (bufferedImageMap.containsKey(imagePath))
      {
        image = bufferedImageMap.get(imagePath);
      } else
      {
        try
        {
          image = ImageIO.read(ImageManager.class.getResourceAsStream(ROOT_PATH + imagePath));
        } catch (Exception e)
        {
          System.out.println(imagePath);
          e.printStackTrace();
        }
      }
      fxImageMap.put(imagePath, new WritableImage(image.getWidth(), image.getHeight()));
      SwingFXUtils.toFXImage(image, fxImageMap.get(imagePath));
    }
  }

  private static void loadFXImage(File imageFile) throws IOException
  {
    String imagePath = ROOT_DIRECTORY.toURI().relativize(imageFile.toURI()).getPath();
    if (fxImageMap.containsKey(imagePath)) System.out.printf("Attempting to duplicate \"%s\"\n", imagePath);
    else
    {
      BufferedImage image;
      if (bufferedImageMap.containsKey(imagePath))
      {
        image = bufferedImageMap.get(imagePath);
      } else
      {
        image = ImageIO.read(imageFile);
      }
      fxImageMap.put(imagePath, new WritableImage(image.getWidth(), image.getHeight()));
      SwingFXUtils.toFXImage(image, fxImageMap.get(imagePath));
    }
  }

  private static void loadFiles(File dir, boolean recursive)
  {
    loadFilesAsBufferedImage(dir, recursive);
    loadFilesAsFXImage(dir, recursive);
  }

  private static void loadFilesAsBufferedImage(File dir, boolean recursive)
  {
    List<File> children = Arrays.asList(dir.listFiles());
    children.forEach(child -> {
      if (child.isDirectory() && recursive) loadFilesAsBufferedImage(child, recursive);
      else try
      {
        if (FILE_FILTER.accept(child)) loadBufferedImage(child);
      } catch (IOException e)
      {
        e.printStackTrace();
      }
    });
  }

  private static void loadFilesAsFXImage(File dir, boolean recursive)
  {
    List<File> children = Arrays.asList(dir.listFiles());
    children.forEach(child -> {
      if (child.isDirectory() && recursive) loadFilesAsFXImage(child, recursive);
      else try
      {
        if (FILE_FILTER.accept(child)) loadFXImage(child);
      } catch (IOException e)
      {
        e.printStackTrace();
      }
    });
  }

  private static void loadImage(File imagePath) throws IOException
  {
    loadBufferedImage(imagePath);
    loadFXImage(imagePath);
  }

  /* Adds a BufferedImage and Image from the file into memory. To be used in initializer only. */
  private static void loadImage(String imagePath) throws IOException
  {
    loadBufferedImage(imagePath);
    loadFXImage(imagePath);
  }

  //  /**
  //   * FOR TESTING ONLY.
  //   */
  public static void main(String[] args) throws IOException, InterruptedException
  {
    boolean reportMap = false;
    if (args.length > 0)
    {
      for (String arg : args)
      {
        if (arg.equals("-v") || arg.equals("--verbatim")) reportMap = true;
      }
    }

    if (reportMap)
    {
      System.out.println("JAVA FX IMAGES");
      fxImageMap.forEach((key, value) -> System.out.printf("Key: %s\nValue: 0x%h\n\n", key, System.identityHashCode
          (value)));

      System.out.println("Total Loaded " + fxImageMap.keySet().size());
      System.out.println();

      System.out.println("SWING IMAGES");
      bufferedImageMap.forEach((key, value) -> System.out.printf("Key: %s\nValue: 0x%h\n\n", key, System
          .identityHashCode(value)));
      System.out.println("Total Loaded " + bufferedImageMap.keySet().size());
      System.out.println();
    }
  }
}
