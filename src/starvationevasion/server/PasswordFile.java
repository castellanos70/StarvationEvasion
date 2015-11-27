package starvationevasion.server;

import starvationevasion.common.EnumRegion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Shea Polansky
 * Loads a password file. A password file is a tab-delimited file
 * where rows are delimited by either "\n" OR "\r\n".
 * Each row is of the form USER_NAME\tPASSWORD\tREGION, where no fields contain tab
 * characters, and REGION is a (case-sensitive) value from EnumRegions.
 * The region may be omitted from ALL records; if this is the case, the server
 * will instead allow each player to choose their own region.
 * An example password file can be found in the 'data/config' directory.
 * USER_NAMEs and REGIONs must be unique; passwords may be duplicated.
 */
public class PasswordFile
{
  private static Pattern assignedCountriesPattern =
      Pattern.compile("^(?<username>[^\t]+)\t+(?<password>[^\t]+)\t+(?<region>\\w+)$");
  private static Pattern userChosenCountriesPattern =
      Pattern.compile("^(?<username>[^\t]+)\t+(?<password>[^\t]+)\t+(?<region>\\w+)$");

  /**
   * A Map mapping (case-sensitive) usernames to (case-sensitive) passwords.
   */
  public final Map<String, String> credentialMap;

  /**
   * A Map mapping (case-sensitive) usernames to specific EnumRegions.
   * MAY BE NULL, in the case where the password file is formatted for
   * user-chosen countries.
   */
  public final Map<String, EnumRegion> regionMap;
  public PasswordFile(List<String> content)
  {
    if (content.stream().allMatch(assignedCountriesPattern.asPredicate()))
    {
      Map<String, String> tempCredentialMap = new HashMap<>();
      Map<String, EnumRegion> tempRegionMap = new HashMap<>();
      for (String line : content)
      {
        Matcher m = assignedCountriesPattern.matcher(line);
        //noinspection ResultOfMethodCallIgnored
        m.matches(); //necessary for Java to actually look for matches, because
                     //calling .matcher() doesn't automatically for some reason(!!!!)
        tempCredentialMap.put(m.group("username"), m.group("password"));
        tempRegionMap.put(m.group("username"), EnumRegion.valueOf(m.group("region")));
      }
      credentialMap = Collections.unmodifiableMap(tempCredentialMap);
      regionMap = Collections.unmodifiableMap(tempRegionMap);
    }
    else if (content.stream().allMatch(userChosenCountriesPattern.asPredicate()))
    {
      Map<String, String> tempCredentialMap = new HashMap<>();
      for (String line : content)
      {
        Matcher m = assignedCountriesPattern.matcher(line);
        tempCredentialMap.put(m.group("username"), m.group("password"));
      }
      credentialMap = Collections.unmodifiableMap(tempCredentialMap);
      regionMap = null;
    }
    else throw new IllegalArgumentException(
          "An attempt was made to instantiate a PasswordFile with malformed input.");
  }

  public static PasswordFile loadFromFile(String path) throws IOException
  {
    return new PasswordFile(Files.readAllLines(Paths.get(path)));
  }
}
