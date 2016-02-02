package starvationevasion.client.ai;


import starvationevasion.client.ai.client.HeadlessClient;

import java.util.Map;

/**
 * Entry point for headless AI players.
 */
public class Main
{
    /**
     * Makes a new AI player and initializes him.
     *
     * @param args command line arguments.
     */
    public static void main(String args[])
    {
        Map<String, String> environment = System.getenv();

        if(environment.get("SEUSERNAME") != null)
        {
            String user = environment.get("SEUSERNAME");
            String pass = environment.get("SEPASSWORD");
            String host = environment.get("SEHOSTNAME");
            int port = Integer.parseInt(environment.get("SEPORT"));

            new HeadlessClient(user, pass, host, port);
        }

        else new HeadlessClient();
    }
}
