package Players;

public class Score
{
    // TODO: make this private again (or dont?)
    public int score;
    private final int defaultVal;

    public Score(int initial)
    {
        score = initial;
        defaultVal = initial;
    }

    public int addScore(int n)
    {
        return score += n;
    }
    public int removeScore(int n)
    {
        return score -= n;
    }
    public void reset()
    {
        score = defaultVal;
    }
    public int getScore()
    {
        return score;
    }
}
