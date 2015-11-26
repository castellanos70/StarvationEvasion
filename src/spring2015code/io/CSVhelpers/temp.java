package spring2015code.io.CSVhelpers;


import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSV_FileReader
{
  private BufferedReader reader = null;

  public CSV_FileReader(String path, int headerLines)
  {
    try
    {
      reader = new BufferedReader(new FileReader(path));
      for (int i=0; i<headerLines; i++)
      { reader.readLine();
      }
    }
    catch (IOException e)
    { String msg = "IO Exception: " + e.getMessage();
      JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  public String[] readWordsOnLine()
  {
    String str = null;
    try
    {
      str = reader.readLine();
    }
    catch (IOException e)
    { String msg = "readWordsOnLine(): IO Exception: " + e.getMessage();
      JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE);
      e.printStackTrace();
    }


    if (str == null) return null;
    return  str.split(",");
  }

  public static void main(String[ ] args)
  {
    CSV_FileReader fileReader = new CSV_FileReader("example.csv", 2);

    int line = 1;
    while (true)
    {
      String[] strArray = fileReader.readWordsOnLine();
      if (strArray==null) break;
      System.out.println("=========> Line " + line + "======================");
      for (String str : strArray)
      { System.out.println(str);
      }
      line++;
    }

    try
    {
      fileReader.reader.close();
    }
    catch (IOException e)
    { String msg = "readWordsOnLine(): IO Exception: " + e.getMessage();
      JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }
}
