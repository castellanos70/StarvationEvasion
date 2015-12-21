package starvationevasion.client.Aegislash;

import starvationevasion.client.Aegislash.Driver.StartAnimation;

public class MainClient_Aegislash
{
  public static void main(String args[]) throws Exception
  {
    //PhaseHandler.setCurrentPhase(ConfigPhase.class);
    StartAnimation animation=new StartAnimation();
    animation.launch(StartAnimation.class,args);

    //starvationevasion.client.Aegislash.GUI.launch(starvationevasion.client.Aegislash.GUI.class,args);
  }
}
