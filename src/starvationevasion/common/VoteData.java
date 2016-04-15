package starvationevasion.common;


import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.model.Sendable;
import starvationevasion.server.model.Type;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VoteData implements Sendable
{
  private ArrayList<PolicyCard> ballot = new ArrayList<>();
  private ArrayList<PolicyCard> enacted = new ArrayList<>();
  private PolicyCard[][] cards = new PolicyCard[EnumRegion.US_REGIONS.length][2];


  public VoteData(ArrayList<PolicyCard> ballot, ArrayList<PolicyCard> enacted, PolicyCard[][] cards)
  {
    this.ballot = ballot;
    this.enacted = enacted;
    this.cards = cards;
  }

  public ArrayList<PolicyCard> getBallot ()
  {
    return ballot;
  }

  public ArrayList<PolicyCard> getEnacted ()
  {
    return enacted;
  }

  public int getVotes(EnumRegion region)
  {
    for (PolicyCard card : cards[region.ordinal()])
    {
      if (card.votesRequired() > 0)
      {
        return card.votesRequired();
      }
    }
    return -1;
  }

  public List<PolicyCard> getRegionCards (EnumRegion region)
  {
    return Arrays.asList(cards[region.ordinal()]);
  }

  @Override
  public Type getType ()
  {
    return Type.VOTE_RESULTS;
  }

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument _json = JSONDocument.createObject();
    JSONDocument _jsonEnacting = JSONDocument.createArray(enacted.size());
    JSONDocument _jsonBallot = JSONDocument.createArray(ballot.size());


    for (int i = 0; i < enacted.size(); i++)
    {
      _jsonEnacting.set(i, enacted.get(i).toJSON());
    }

    for (int i = 0; i < ballot.size(); i++)
    {
      _jsonBallot.set(i, ballot.get(i).toJSON());
    }
    _json.set("ballot", _jsonBallot);
    _json.set("enacted", _jsonEnacting);

    return _json;
  }

  @Override
  public void fromJSON (Object doc)
  {

  }
}
