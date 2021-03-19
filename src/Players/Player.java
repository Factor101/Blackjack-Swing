package Players;

import Card.Card;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Player
{
    public final ArrayList<Card> hand = new ArrayList<>();
    public final Score handScore = new Score(0);
    public boolean didBust = false;

    private final Score roundScore = new Score(0);
    private int aces;

    public Player()
    {

    }

    public int updateScore()
    {
        int tempScore = 0;
        aces = 0;

        for(Card e : hand)
        {
            if(e.isAce) {
                aces++;
                continue;
            }
            tempScore += e.getValue();
        }
        for(int i = 0; i < aces; i++)
        {
            // if we would bust or if we have too many aces to claim the 11
            if(tempScore + 11 > 21 || tempScore + 11 + aces - 1 > 21) {
                tempScore += aces;
                break;
            } else {
                tempScore += 11;
            }
        }
        handScore.score = tempScore;
        return handScore.getScore();
    }

    public void reset()
    {
        hand.clear();
        handScore.reset();
        this.didBust = false;
    }

    public boolean shouldHit()
    {
        int curScore = this.handScore.getScore();
        // dont hit on these
        if(curScore >= 18) return false;
        // if we only have one ace we wont bust
        if(this.aces == 1) return true;
        // cant bust
        if(curScore <= 10) return true;
        // 17 = 30% chance, 16 = 40%, 15 = 50%, etc
        return ThreadLocalRandom.current().nextInt(0, 100) <= 10 * (20 - curScore);
    }

    public int getScore()
    {
        return handScore.getScore();
    }

    public Score getRoundScore()
    {
        return roundScore;
    }
}