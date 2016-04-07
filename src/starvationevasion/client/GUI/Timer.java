package starvationevasion.client.GUI;
import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.Calendar;

/**
 * Timer is the class responsible for representing the countdown timer for the two different game phases
 * DraftingPhase has 5 minutes
 * VotingPhase hase 2 minutes
 */
public class Timer extends Label
{
  private boolean fiveMinutes, fourMinutes, threeMinutes, twoMinutes, oneMinute, zeroMinutes;
  public boolean votePhase;
  public Calendar time;
  public Timeline timeline;
  private int minutesElapsed;
  private int secondsElapsed;
  private boolean firstTime;
  private String draftOrVote;

  /**
   * Constructor for the timer
   * @param draftOrVote String which tells the timer if it's drafting or voting
   */
  public Timer(String draftOrVote)
  {
    this.draftOrVote = draftOrVote;
    this.setFont(Font.font(null, FontWeight.BOLD, 40));
    time = Calendar.getInstance();
    if(draftOrVote.equals("draft")) this.setText("5:00");
    else this.setText("2:00");
  }

  /**
   * Function which starts the timer
   */
  public void start() {
    if(draftOrVote.equals("draft"))
    {
      minutesElapsed = 0;
      fiveMinutes = true;
    }
    else
    {
      minutesElapsed = 3;
      twoMinutes = true;
      votePhase = true;
    }
    firstTime = true;
    timeline = new Timeline(
      new KeyFrame(Duration.seconds(0),
        actionEvent -> {

          String secondString, minuteString;
          minuteString =  "0" + "";
          if(firstTime) firstTime = false;
          else secondsElapsed++;
          if(fiveMinutes)
          {
            minuteString = "5";
            fiveMinutes = false;
            fourMinutes = true;
          }
          else if(fourMinutes)
          {
            minuteString = "4";
            if((secondsElapsed) % 60 ==  0 && minutesElapsed == 1)
            {
              fourMinutes = false;
              threeMinutes = true;
            }
            if(minutesElapsed == 0) minutesElapsed++;
          }
          else if(threeMinutes)
          {
            minuteString = "3";
            if(secondsElapsed % 60 ==  0 && minutesElapsed == 2)
            {
              threeMinutes = false;
              twoMinutes = true;
            }
            if(minutesElapsed == 1) minutesElapsed++;
          }
          else if(twoMinutes)
          {
            minuteString = "2";
            if((secondsElapsed % 60 ==  0 && minutesElapsed == 3) || votePhase)
            {
              twoMinutes = false;
              oneMinute = true;
            }
            if(minutesElapsed == 2) minutesElapsed++;
          }
          else if(oneMinute)
          {
            minuteString = "1";
            if(secondsElapsed % 60 ==  0 && minutesElapsed == 4)
            {
              oneMinute = false;
            }
            if(minutesElapsed == 3) minutesElapsed++;
          }
          if(secondsElapsed % 60 ==  0)
          {
            secondString =  "00";
            if(zeroMinutes) stopTimeLine();
            if(!fiveMinutes && !fourMinutes && !threeMinutes && !twoMinutes && !oneMinute) zeroMinutes = true;
          }
          else if((60 - (secondsElapsed % 60) < 10))secondString = "0" +  (60 - (secondsElapsed % 60)) + "";
          else secondString = (60 - (secondsElapsed % 60)) + "";
          setText(minuteString + ":" + secondString);
        }
      ),
      new KeyFrame(Duration.seconds(1))
    );
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  private void stopTimeLine()
  {
    timeline.stop();
  }
}

