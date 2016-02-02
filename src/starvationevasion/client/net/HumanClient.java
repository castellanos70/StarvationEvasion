package starvationevasion.client.net;


import starvationevasion.client.model.GameOptions;
import starvationevasion.client.model.Player;
import starvationevasion.client.model.GameStateData;

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
