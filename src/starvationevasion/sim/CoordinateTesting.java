package starvationevasion.sim;

import starvationevasion.common.Constant;
import starvationevasion.common.EnumRegion;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Testing out Joel's MapPoint calculator
 */
public class CoordinateTesting
{
  private final int MAX_LAT = 720;//maximum number of latitude
  private final int MAX_LON = 1440;//maximum number of longitudes
  private final double degrees = 0.25;//resolution
  private final double DEFAULT_LAT = -89.875;//starting latitude from the climate data
  private final double DEFAULT_LON = 0.125;//starting longitude from the climate data
  private final double SEC_MONTH = 24 * 60 * 60;
  //file name : Climate_[Year]_[Month number]
  //File name's directory
  private final String PREFIX = "./data/sim/climate/";//File's directory
  private final String SUFFIX = ".txt";//File's type

  //Historical
  private final String HISTORICAL_FILE_TASMIN = "Historical_tasmin_";
  private final String HISTORICAL_FILE_TASMAX = "Historical_tasmax_";
  private final String HISTORICAL_FILE_PR = "Historical_pr_";
  private final String[] HISTORICAL = {HISTORICAL_FILE_TASMIN, HISTORICAL_FILE_TASMAX, HISTORICAL_FILE_PR};

  //RCP45
  private final String RCP45_FILE_TASMIN = "rcp45_tasmin_";
  private final String RCP45_FILE_TASMAX = "rcp45_tasmax_";
  private final String RCP45_FILE_PR = "rcp45_pr_";
  private final String[] RCP45 = {RCP45_FILE_TASMIN, RCP45_FILE_TASMAX, RCP45_FILE_PR};

  //RCP85
  private final String RCP85_FILE_TASMIN = "rcp85_tasmin_";
  private final String RCP85_FILE_TASMAX = "rcp85_tasmax_";
  private final String RCP85_FILE_PR = "rcp85_pr_";
  private final String[] RCP85 = {RCP85_FILE_TASMIN, RCP85_FILE_TASMAX, RCP85_FILE_PR};

  private final String[] MONTH_LABELS = {Constant.Month.JAN.full(), Constant.Month.FEB.full(), Constant.Month.MAR.full(), Constant.Month.APR.full()
    , Constant.Month.MAY.full(), Constant.Month.JUN.full(), Constant.Month.JLY.full(), Constant.Month.AUG.full(),
    Constant.Month.SEP.full(), Constant.Month.OCT.full(), Constant.Month.NOV.full(), Constant.Month.DEC.full()};
  private final int[] MONTH_DAYS = {Constant.Month.JAN.days(), Constant.Month.FEB.days(), Constant.Month.MAR.days(), Constant.Month.APR.days()
    , Constant.Month.MAY.days(), Constant.Month.JUN.days(), Constant.Month.JLY.days(), Constant.Month.AUG.days(),
    Constant.Month.SEP.days(), Constant.Month.OCT.days(), Constant.Month.NOV.days(), Constant.Month.DEC.days()};

  private Simulator sim = new Simulator();

  public static void main(String[] args)
  {
    String currentDirectory;
    currentDirectory = System.getProperty("user.dir");
    System.out.println("Current working directory : " + currentDirectory);
    CoordinateTesting test = new CoordinateTesting();
    Deque<WriteValues> viable = new LinkedList<>();
    test.findViablePoints(viable);
    test.readAndWrite(viable, 2000, 2000, 1, test.HISTORICAL, "Historical");//do Historical data
    test.makeZipArchive("Historical", 2000);
    test.readAndWrite(viable, 2010, 2050, 5, test.RCP45, "RCP45");
    test.readAndWrite(viable, 2010, 2050, 5, test.RCP85, "RCP85");
  }

  /**
   * Takes a coordinate point and gets the region the point is in.
   *
   * @param latitude
   * @param longitude
   * @return Null if over the ocean, else returns the region
   */
  private boolean latlonCheck(double latitude, double longitude)
  {
    EnumRegion enumRegion= sim.getRegion(latitude, longitude);
    if (enumRegion == null)
    {
      return false;
    }
    else
    {
      return true;
    }
  }

  private double convertToOneEighty(double longitude)
  {
    return (longitude > 180) ? longitude - 360 : longitude;
  }

  /**
   * @param viable    - A deque of WriteValues objects
   * @param startYear - The start year of data
   * @param endYear   - the end year of data
   * @param labels    - HISTORICAL, RCP45, RCP85 String[] Labels
   */
  private void readAndWrite(Deque<WriteValues> viable, int startYear, int endYear, int increment, String[] labels, String title)
  {
    Deque<WriteValues> viableCoor = viable;
    try
    {
      for (int year = startYear; year <= endYear; year += increment)
      {
        double latitude = DEFAULT_LAT;
        String inputFileMin = PREFIX + labels[0] + year + SUFFIX;//filename for the tasmin
        String inputFileMax = PREFIX + labels[1] + year + SUFFIX;//filename for the tasmax
        String inputFilePR = PREFIX + labels[2] + year + SUFFIX;//filename for the pr

        //readers for tasmin, tasmax, and pr
        FileReader fileReaderMin = new FileReader(inputFileMin);
        BufferedReader bufferedReaderMin = new BufferedReader(fileReaderMin);
        FileReader fileReaderMax = new FileReader(inputFileMax);
        BufferedReader bufferedReaderMax = new BufferedReader(fileReaderMax);
        FileReader fileReaderPR = new FileReader(inputFilePR);
        BufferedReader bufferedReaderPR = new BufferedReader(fileReaderPR);

        String lineMin, lineMax, linePR;//lines of data from the file
        String delimiter = "[, ]+";
        String[] parsedMin, parsedMax, parsedPR;//the parsed version of data using delimiter
        int day = 0;//keeps track of days
        int month = 0;//keeps track of the month currently on

        //each line of the file is a latitude coordinate
        while (((lineMin = bufferedReaderMin.readLine()) != null) &&
          ((lineMax = bufferedReaderMax.readLine()) != null) &&
          ((linePR = bufferedReaderPR.readLine()) != null))
        {
          parsedMin = lineMin.split(delimiter);//one latitude of tasmin info for the globe
          parsedMax = lineMax.split(delimiter);//one latitude of tasmax info for the globe
          parsedPR = linePR.split(delimiter);//one latitude of pr info for the globe
          int count = 1; //skips [0][0] in the parsed
          //if(parsedMin[count].contains(lat)) parse the rest of the lines
          //else
          for (double longitude = DEFAULT_LON; longitude < 360; longitude += degrees)
          {
            double convert = convertToOneEighty(longitude);
            Point.Double temp = viableCoor.peekFirst().getPoint();//checks to see if this is a viable coordinate point
            if (temp.getX() == latitude && temp.getY() == convert)//gets each parsed data if it is
            {
              double min = Double.parseDouble(parsedMin[count]);
              double max = Double.parseDouble(parsedMax[count]);
              double pr = Double.valueOf(parsedPR[count]);
              WriteValues hold = viableCoor.pollFirst();//pops from dequeue
              hold.adjustValues(min, max, pr);//adjusts these values
              viableCoor.addLast(hold);//pushes to dequeue
            }
            count++;//increment the parsed counter
          }
          latitude += degrees;
          if (latitude > 90)
          {//resets latitude and increment the day
            latitude = DEFAULT_LAT;//restart latitude counter
            day++;//increment the day
            if (day == MONTH_DAYS[month])
            {//makes an out file for the month
              File output = new File(PREFIX + "Climate_"+title + "_" + year + "_" + (month + 1) + ".csv");
              System.out.println("Start: " + MONTH_LABELS[month]);
              output.createNewFile();

              BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(output));
              bufferedWriter.flush();//makes sure it is empty
              //format of data
              bufferedWriter.write("TempMonthLow, TempMonthHigh, TempMeanDailyLow, " +
                "TempMeanDailyHigh, Rain\n");

              for (int i = 0; i < viableCoor.size(); i++)
              {
                WriteValues val = viableCoor.pollFirst();//gets the next object
                bufferedWriter.write(String.format("%.3f", val.getMinMin()) + "," + String.format("%.3f", val.getMinAvg()) + ","
                  + String.format("%.3f", val.getMaxMax()) + "," + String.format("%.3f", val.getMaxAvg()) + ","
                  + String.format("%.3f", val.getPrAvg()) + "\n");

                val.reset();//reset values
                viableCoor.addLast(val);
              }
              bufferedWriter.close();
              System.out.println("Finished: " + MONTH_LABELS[month]);
              month++;//increment the month
              day = 0;
            }
          }
        }
        bufferedReaderMin.close();
        bufferedReaderMax.close();
        bufferedReaderPR.close();
        fileReaderMin.close();
        fileReaderMax.close();
        fileReaderPR.close();
        makeZipArchive(title, year);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void makeZipArchive(String title, int year)
  {
    String suffix = ".csv";
    String zipFile = PREFIX +"Climate_"+ title + "_" + year + "_" + "archive.zip";
    try
    {
      // create byte buffer
      byte[] buffer = new byte[1024];
      FileOutputStream fos = new FileOutputStream(zipFile);
      ZipOutputStream zos = new ZipOutputStream(fos);
      for (int month = 0; month < MONTH_LABELS.length; month++)
      {
        File srcFile = new File(PREFIX +"Climate_"+ title + "_" + year + "_" + (month + 1) + suffix);
        FileInputStream fis = new FileInputStream(srcFile);
        // begin writing a new ZIP entry, positions the stream to the start of the entry data
        zos.putNextEntry(new ZipEntry(srcFile.getName()));
        int length;
        while ((length = fis.read(buffer)) > 0)
        {
          zos.write(buffer, 0, length);
        }
        zos.closeEntry();
        // close the InputStream
        fis.close();
      }
      // close the ZipOutputStream
      zos.close();
    }
    catch (IOException ioe)
    {
      System.out.println("Error creating zip file: " + ioe);
    }

  }

  private void findViablePoints(Deque<WriteValues> viable)
  {
    System.out.println("Start viable");

    try
    {
      String fileName = PREFIX + "Climate_Viable_Coordinates.csv";

      File file = new File(fileName);

      if (file.createNewFile())
      {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write("Latitude, Longitude\nlatitude, longitude");
        for (double lat = DEFAULT_LAT; lat <= 90; lat += degrees)
        {
          for (double lon = DEFAULT_LON; lon <= 360; lon += degrees)
          {
            double temp = convertToOneEighty(lon);
            if (latlonCheck(lat, temp))
            {
              Point.Double point = new Point.Double(lat, temp);
              viable.addLast(new WriteValues(point));
              bufferedWriter.write(lat + ", " + temp + "\n");
            }
          }
        }
        bufferedWriter.close();
      }
      else
      {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        String delimiter = "[, ]+";
        String[] parsed;

        //each line of the file is a latitude coordinate
        while ((line = bufferedReader.readLine()) != null)
        {
          parsed = line.split(delimiter);//one day of info for the globe
          Point.Double point = new Point.Double(Double.parseDouble(parsed[0]), Double.parseDouble(parsed[1]));
          viable.addLast(new WriteValues(point));
        }
        bufferedReader.close();
        fileReader.close();
      }

    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    System.out.println("Done viable");
  }

  protected class WriteValues
  {
    private double minavg = 0;
    private double maxavg = 0;
    private double pravg = 0;
    private int mintotal = 0;
    private int maxtotal = 0;
    private int prtotal = 0;
    private double minmin = 1000;
    private double maxmax = -1000;
    private Point.Double point;

    WriteValues(Point.Double point)
    {
      this.point = point;
    }

    protected void adjustValues(double min, double max, double pr)
    {
      addAvg(min, max, pr);
      adjustMin(min);
      adjustMax(max);
    }

    protected void addAvg(double minadd, double maxadd, double pradd)
    {
      if (minadd != 1E20)
      {
        minavg += minadd;
        mintotal++;
      }
      if (maxadd != 1E20)
      {
        maxavg += maxadd;
        maxtotal++;
      }
      if (pradd != 1E20)
      {
        pravg += (pradd * SEC_MONTH);
        prtotal++;
      }
    }

    protected void adjustMin(double minmin)
    {
      if (minmin < this.minmin && minmin != 1E20)
      {
        this.minmin = minmin;
      }
    }

    protected void adjustMax(double maxmax)
    {
      if (maxmax > this.maxmax && maxmax != 1E20)
      {
        this.maxmax = maxmax;
      }
    }


    protected void reset()
    {
      minavg = 0;
      maxavg = 0;
      pravg = 0;
      mintotal = 0;
      maxtotal = 0;
      prtotal = 0;
      minmin = 1000;
      maxmax = -1000;
    }

    protected double getMinAvg()
    {
      if (minavg == 0) return -1;
      return minavg / mintotal - 273;
    }

    protected double getMaxAvg()
    {
      if (maxavg == 0) return -1;
      return maxavg / maxtotal - 273;
    }

    protected double getPrAvg()
    {
      if (pravg == 0) return -1;
      return (pravg) / prtotal;
    }

    protected double getMinMin()
    {
      return minmin - 273;
    }

    protected double getMaxMax()
    {
      return maxmax - 273;
    }

    protected Point.Double getPoint()
    {
      return point;
    }
  }
}
