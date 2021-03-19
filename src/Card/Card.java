package Card;

import java.util.Hashtable;

public class Card
{
    private final Suit suit;
    private final Rank rank;
    private final int value;
    public boolean isAce;

    private final static Hashtable<Suit, Character> suitChars = new Hashtable<Suit, Character>(){{
        put(Suit.HEARTS, '♡');
        put(Suit.DIAMONDS, '♢');
        put(Suit.CLUBS, '♣');
        put(Suit.SPADES, '♠');
    }};

    public Card(Rank rank, Suit suit)
    {
        this.rank = rank;
        this.suit = suit;

        int value = rank.ordinal();
        this.value = value < 10  ? value + 1 : 10;

        this.isAce = value == 1;
    }

    public Character suitToChar()
    {
        return suitChars.get(this.suit);
    }

    public int getValue()
    {
        return value;
    }

    public String getName()
    {
        return rank.toString();
    }

    public Suit getSuit()
    {
        return suit;
    }

    public Rank getRank()
    {
        return rank;
    }
}