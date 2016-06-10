package starvationevasion.ai.commands;

import starvationevasion.ai.AI;
import starvationevasion.common.Constant;
import starvationevasion.communication.Communication;
import starvationevasion.server.model.*;

import java.util.ArrayList;

public class Login extends AbstractCommand
{

  private String chosenUsername = "";
  private boolean requestSent = false;
  private ArrayList<User> users = new ArrayList<>(); // Logged in users
  private Communication COMM;
  private boolean usersReceived = false;

  public Login(AI client)
  {
    super(client);
  }

  @Override
  public boolean run()
  {
    //System.out.println(">> INSIDE run");
    // Check for mesages, then process the messages


//    if (!requestSent && getClient().getUsers().size() == 0)
//    {
//      getClient().getCommModule().send(Endpoint.USERS_LOGGED_IN, null, null);
//      // getClient().send(new
//      // RequestFactory().build(getClient().getStartNanoSec(),
//      // Endpoint.USERS_LOGGED_IN));
//      requestSent = true;
//      return true;
//    }
//    if (chosenUsername.isEmpty())
//    {
//      for (String aiName : AI_NAMES)
//      {
//        if (!getClient().getUsers().stream()
//            .anyMatch(user -> user.getUsername().equals(aiName)))
//        {
//          chosenUsername = aiName;
//          break;
//        }
//      }
//
//      return true;
//    }



    // When we know the users, let's try a username
    if (getClient().getUser() == null && usersReceived)
    {
      // Pick a possible name
      String uTemp = "";
      for(String possibleName: Constant.AI_NAMES )
      {
        // grab temp name
        uTemp = possibleName;

        // check if temp name has already been used
        for(User user: users)
        {
          // if name is the same break and try another name
          if(uTemp.equals(user.getUsername()))
          {
            uTemp = "FAIL";
            break;
          }
        }
        if(!uTemp.equals("FAIL")) break;
      }


      System.out.println(">>>> Ai PICKED USERNAME: " + uTemp);
      chosenUsername = uTemp;
      getClient().getCommModule().login(chosenUsername, "bot", null);
      return true;

    }
    return false;

  }

  @Override
  public String commandString()
  {
    // TODO Auto-generated method stub
    return "Login";
  }

  public void setUsersLoggedIn(ArrayList<Response> responses)
  {
//    System.out.println(">> INSIDE processServerInput 1");
    for (Response response : responses)
    {
//      System.out.println(">> INSIDE processServerInput 2");
      Type type = response.getType();
      Object data = response.getPayload().getData();
//      System.out.println(">>> Inside processServerInput 3");

      // Update local list of users
      if (type == Type.USERS_LOGGED_IN_LIST)
      {
//        System.out.println(">>> Inside type, ");
        users = (ArrayList<User>)data;
        usersReceived = true;
//        for(User user: users)
//        {
//          if(!(playerPolicyDrafts.keySet().contains(user)))
//          {
//            playerPolicyDrafts.put(user.getRegion(),0);
//          }
//        }
      }
    }
  }
}
