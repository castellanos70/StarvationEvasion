package starvationevasion.client.MegaMawile2.ai.client;

import starvationevasion.client.MegaMawile2.controller.ComputerPlayerController;
import starvationevasion.client.MegaMawile2.controller.GameController;
import starvationevasion.client.MegaMawile2.model.GameOptions;
import starvationevasion.client.MegaMawile2.model.NetworkStatus;
import starvationevasion.client.MegaMawile2.model.GameStateData;
import starvationevasion.client.MegaMawile2.model.Player;
import starvationevasion.client.MegaMawile2.net.NetworkHandler;

/**
 * Runs an AI client connected to the server.
 */
public class HeadlessClient implements GameController
{
    private ComputerPlayerController playerController;
    private Player player = new Player();

    private GameStateData gameState;
    private volatile GameOptions gameOptions = new GameOptions();

    private NetworkHandler networkHandler;

    public HeadlessClient(String user, String pass, String host, int port)
    {
        player = new Player();
        player.setUsername(user);
        player.setPassword(pass);

        gameOptions.setHost(host);
        gameOptions.setPort(port);

        init();
    }

    /**
     * For testing purposes - used when ai.Main is run independently.
     */
    public HeadlessClient()
    {
        player = new Player();

        gameOptions.setHost("localhost");
        gameOptions.setPort(27015);

        player.setUsername("shea");
        player.setPassword("sw0rdf1sh");

        init();
    }

    public void run()
    {
        long delta = 0l;

        while(true)
        {
            long lastTime = System.nanoTime();
            final long finalDelta = delta;

            update(finalDelta / 1000000000.0f);

            delta = System.nanoTime() - lastTime;
            if (delta < 20000000L)
            {
                try
                {
                    Thread.sleep((20000000L - delta) / 1000000L);
                }
                catch (Exception ignored)
                {
                }
            }
        }
    }

    @Override
    public void update(float deltaTime)
    {
        playerController.update(deltaTime);
    }

    private void init()
    {
        gameState = new GameStateData();

        System.out.println("Initializing AI client...");

        playerController = new ComputerPlayerController(gameState, gameOptions, player);

        networkHandler = new NetworkHandler(gameOptions, gameState);
        networkHandler.createClient(playerController);

        while(gameOptions.getNetworkStatus() != NetworkStatus.LOGGED_IN);
        run();
    }

}
