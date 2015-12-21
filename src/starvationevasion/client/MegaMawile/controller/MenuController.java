package starvationevasion.client.MegaMawile.controller;


import com.sun.javafx.collections.ObservableListWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import starvationevasion.client.MegaMawile.model.GameOptions;
import starvationevasion.client.MegaMawile.model.NetworkStatus;
import starvationevasion.client.MegaMawile.view.StartupScreen;
import starvationevasion.common.EnumRegion;
import starvationevasion.common.Tuple;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static starvationevasion.client.MegaMawile.model.NetworkStatus.*;

public class MenuController implements Initializable, GameController
{
  public boolean active = false;
  private final GameOptions options;
  private final Stage view;
  private GameEngine engine;

  @FXML
  private ComboBox comboBox;
  @FXML
  private TextField address, port, usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Button connectBtn, cancelButton, loginBtn, regionBtn;
  @FXML
  private Label statusLabel, statusLabelAuth;

  public MenuController(GameOptions options, GameEngine engine)
  {
    this.engine = engine;
    this.options = options;
    view = new StartupScreen(this);

  }

  private void connect()
  {
    if (!address.getText().equalsIgnoreCase(options.getHost()))
    {
      options.setHost(address.getText());
    }
    else
    {
      if (options.getNetworkStatus() == CONNECTED)
      {
        options.setNetworkStatus(NetworkStatus.DISCONNECT);
      }
    }

    if (!port.getText().equalsIgnoreCase(String.valueOf(options.getPort())))
    {
      options.setPort(Integer.parseInt(port.getText()));
    }
    else
    {
      if (options.getNetworkStatus() == CONNECTED)
      {
        options.setNetworkStatus(NetworkStatus.DISCONNECT);
      }
    }

    if (options.getNetworkStatus() == NOT_CONNECTED)
    {
      options.setNetworkStatus(TRY);
    }


    active = true;
  }


  public void show()
  {
    view.show();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources)
  {

    regionBtn.setDisable(true);
    loginBtn.setDisable(true);

    regionBtn.setOnMouseClicked(event -> {
      Tuple<String, EnumRegion> value = (Tuple<String, EnumRegion>) comboBox.getValue();
      engine.getNetworkHandler().getOurClient().requestRegion(value.b.toString());
    });


    cancelButton.setOnMouseClicked(event -> System.exit(0));

    connectBtn.setOnMouseClicked(event -> connect());

    loginBtn.setOnMouseClicked(event -> login());
  }

  private void login()
  {
    if (options.getNetworkStatus() == CONNECTED)
    {
      engine.getPlayer().setUsername(usernameField.getText());
      engine.getPlayer().setPassword(passwordField.getText());
      engine.getPlayer().setStatus(TRY);
    }
  }

  private float counter = 0f;

  @Override
  public void update(float deltaTime)
  {
    statusLabel.setText(options.getNetworkStatus().name());
    statusLabelAuth.setText(engine.getPlayer().getStatus().name());

    if (engine.getPlayer().getStatus() == LOGGED_IN && engine.getPlayer().getRegion() == null)
    {
      regionBtn.setDisable(false);
      counter += 1;
    }

    if (counter >= 200)
    {
      counter = 0;
      ArrayList<Tuple<String, EnumRegion>> l = new ArrayList<>();
      for (EnumRegion enumRegion : engine.getGameState().getAvailableRegions())
      {
        Tuple s = new Tuple<String, EnumRegion>(enumRegion.toString(), enumRegion)
        {
          @Override
          public String toString()
          {
            return this.a;
          }
        };
        l.add(s);
      }

      comboBox.setItems(new ObservableListWrapper<>(l));
    }

    if (options.getNetworkStatus() == CONNECTED && loginBtn.isDisabled())
    {
      loginBtn.setDisable(false);
    }

    if (view.isShowing() && options.getNetworkStatus() == CONNECTED && engine.getPlayer().getStatus() == LOGGED_IN && engine.getPlayer().getRegion() != null )
    {
      view.close();
    }
  }
}
