package starvationevasion.client.Driver;

import starvationevasion.client.GUI.GUI;

public class Main
{
  public static void main(String args[]) throws Exception
  {
    //PhaseHandler.setCurrentPhase(ConfigPhase.class);
    StartAnimation animation=new StartAnimation();
    //animation.launch(StartAnimation.class,args);

    GUI.launch(GUI.class, args);
  }
}
