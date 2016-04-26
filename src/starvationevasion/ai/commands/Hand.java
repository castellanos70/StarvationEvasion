package starvationevasion.ai.commands;


import starvationevasion.ai.AI;
import starvationevasion.common.*;
import starvationevasion.common.policies.CovertIntelligencePolicy;
import starvationevasion.common.policies.EfficientIrrigationIncentivePolicy;
import starvationevasion.common.policies.EthanolTaxCreditChangePolicy;
import starvationevasion.common.policies.FertilizerSubsidyPolicy;
import starvationevasion.server.model.*;

import java.util.Random;

public class Hand extends AbstractCommand
{

  private int tries = 3;
  public Hand (AI client)
  {
    super(client);
  }

  @Override
  public boolean run ()
  {
    if (tries == 0)
    {
      return false;
    }

    if (getClient().getUser().getHand() == null || getClient().getUser().getHand().size() <= 6)
    {
      tries--;
      getClient().send(new RequestFactory().build(getClient().getStartNanoSec(),
                                            Endpoint.HAND_READ));
      return true;
    }
    return false;

  }
}
