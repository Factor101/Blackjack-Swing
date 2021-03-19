package Card;

import Players.Player;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Deck
{
    private ArrayList<Card> deck = new ArrayList<>();
    private int decks = 1;

    public Deck(int decks)
    {
        if(decks > 0) {
            this.decks = decks;
        }
        do {
            for (Rank rank : Rank.values())
            {
                for (Suit suit : Suit.values())
                {
                    deck.add(new Card(rank, suit));
                }
            }
        } while(--decks > 0);
    }

    public void shuffle(int times)
    {
        int max, index;
        do {
            // shuffled deck
            final ArrayList<Card> newDeck = new ArrayList<>();
            // for every card
            for(int i = 0; i < 52 * decks; i++)
            {
                // update last index
                max = deck.size() - 1;
                // get random index
                index = ThreadLocalRandom.current().nextInt(0, max + 1);
                // add to new deck and remove from the old
                newDeck.add(deck.get(index));
                deck.remove(index);
            }
            // set deck to the shuffled deck
            deck = (ArrayList<Card>) newDeck.clone();
        } while(--times > 0);
    }

    public void deal(Player player)
    {
        player.hand.add(this.draw());
    }

    public ArrayList<Card> getDeck()
    {
        return deck;
    }

    public Card draw()
    {
        if(this.deck.size() == 0) return null;
        return this.deck.remove(0);
    }

    public static String toCardString(ArrayList<Card> deck)
    {
        StringBuilder cardStr = new StringBuilder();
        for(Card el : deck)
        {
            cardStr
                    .append(el.getName())
                    .append(el.suitToChar())
                    .append(" ");
        }
        return cardStr.toString();
    }
}