package starvationevasion.util;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * This file allows you to read in a zip/jar file but only keep the contents that
 * are of interest to you. It will not modify the original file, but will instead create
 * a new zip file.
 *
 * For its arguments, it needs
 *
 *        args[0] -- direct path to the existing zip file
 *        args[1] -- direct path AND name of the new zip file you want to generate
 *        args[2] -- direct path to a list of files to exclude (these do not need to be direct paths since they
 *                   represent entries inside of the zip archive)
 *
 * @author Justin Hall
 */
public class ZipEditor
{
  /**
   * Creates a new ZipEditor which will result in a brand new zip file if nothing went wrong.
   * Note that if the arguments passed into it are of length 0, it will perform a simple
   * copy of the existing zip to the new zip.
   *
   * @param existingZip path to the existing zip file
   * @param newZipFile name of the new zip file (exact path + name + extension)
   * @param args valid arguments to use while producing the new zip file
   */
  public ZipEditor(String existingZip, String newZipFile, String ... args)
  {
    try
    {
      existingZip = existingZip.replace('\\', '/');
      newZipFile = newZipFile.replace('\\', '/');
      ZipFile zipFile = new ZipFile(new File(existingZip));
      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(newZipFile));
      final HashSet<String> BLACKLISTED_FILES = processArgs(args);
      System.err.println("BLACKLIST " + BLACKLISTED_FILES.toString());

      while (entries.hasMoreElements())
      {
        ZipEntry entry = entries.nextElement();
        System.out.println("Current entry: " + entry.getName());
        //System.out.print("\tEntry status: ");

        if (isAccepted(BLACKLISTED_FILES, entry.getName()))
        {
          System.out.println("\tEntry status: Accepted");
          BufferedInputStream reader = new BufferedInputStream(zipFile.getInputStream(entry));
          zipOut.putNextEntry(new ZipEntry(entry.getName()));
          while (reader.available() > 0) zipOut.write(reader.read());
          reader.close();
          zipOut.closeEntry();
        }
        else System.out.println("\tEntry status: Rejected");
      }
      zipOut.finish();
      zipFile.close();
      zipOut.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private boolean isAccepted(HashSet<String> blacklist, String file)
  {
    file = file.replace('\\', '/');
    file = file.replaceAll("/", "");
    file = file.replaceAll(" ", "");
    return !blacklist.contains(file) && !containsApproximate(blacklist, file);
  }

  private boolean containsApproximate(HashSet<String> blacklist, String file)
  {
    for (String str : blacklist)
    {
      if (file.contains(str)) return true;
    }
    return false;
  }

  private HashSet<String> processArgs(String ... args)
  {
    HashSet<String> fileCommandMap = new HashSet<>(100);
    for (String str : args) fileCommandMap.add(str);
    return fileCommandMap;
  }

  public static String getArgList(String filepath)
  {
    StringBuilder args = new StringBuilder(1000);
    try
    {
      BufferedReader read = new BufferedReader(new FileReader(new File(filepath)));
      String line;
      int i = 0;
      while ((line = read.readLine()) != null)
      {
        if (i > 1) args.append(" ");
        line = line.replace('\\', '/');
        line = line.replaceAll("/", "");
        line = line.replaceAll(" ", "");
        args.append(line);
        if (i == 0) args.append(" ");
        ++i;
      }
    }
    catch (Exception e)
    {
      System.err.println("Invalid argument");
      e.printStackTrace();
      System.exit(-1);
    }
    System.out.println(args.toString());
    return args.toString();
  }

  /**
   * Main entry point.
   *
   * @param args args[0] -- direct path to the existing zip file
   *             args[1] -- direct path AND name of the new zip file you want to generate
   *             args[2] -- direct path to a list of files to exclude (these do not need to be direct paths since they
   *                        represent entries inside of the zip archive)
   */
  public static void main(String[] args)
  {
    if (args.length != 3) throw new IllegalArgumentException("3 arguments to main required");
    String _existingZip = args[0];
    String _newZip = args[1];
    String[] argList = getArgList(args[2]).split(" ");
    new ZipEditor(_existingZip, _newZip, argList);
  }
}
