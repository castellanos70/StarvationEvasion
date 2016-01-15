package starvationevasion.client.MegaMawile2.net;


import starvationevasion.client.MegaMawile2.model.GameOptions;
import starvationevasion.client.MegaMawile2.model.Player;
import starvationevasion.client.MegaMawile2.model.GameStateData;
import starvationevasion.client.MegaMawile2.net.AbstractClient;

import java.util.Scanner;


public class HumanClient extends AbstractClient
{
  // Read in lines from console.
  Scanner keyboard;
  public HumanClient(GameOptions options, Player player, GameStateData gameState)
  {
    super(options, player, gameState);
    keyboard = new Scanner(System.in);
  }
}
