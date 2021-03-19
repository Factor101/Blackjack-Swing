package com.Main;

import javax.swing.*;
import Card.*;
import Players.Player;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Main
{
    public static final JFrame frame = new JFrame("♠ Blackjack ♠");
    public static final Player self = new Player();
    public static final Player dealer = new Player();
    public static Deck curDeck;
    public static final Dimension screen = new Dimension(800, 200);
    public static final int deckNumber = 8;
    public static interface voidFn
    {
        void run();
    }

    public static void main(String[] args)
    {
        curDeck = new Deck(deckNumber);
        curDeck.shuffle(2);

        //#region GUI setup
        frame.setSize(screen);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
        //endregion
    }

    private static void placeComponents(JPanel panel)
    {
        panel.setLayout(null);

        //#region scoreboard
        JLabel gameScore = new JLabel(
                "You: " +
                        self.getRoundScore().getScore() +
                        " | Dealer: " +
                        dealer.getRoundScore().getScore()
        );
        gameScore.setBounds(screen.width / 2 - 100, 5, 400, 25);
        panel.add(gameScore);
        //#endregion

        //#region initial draw
        // draw 2 cards for each player
        self.hand.add(curDeck.draw());
        dealer.hand.add(curDeck.draw());
        self.hand.add(curDeck.draw());
        dealer.hand.add(curDeck.draw());
        //#endregion

        //#region self labels
        JLabel selfTotal = new JLabel(
                "Current Total: " +
                        self.updateScore()
        );
        selfTotal.setBounds(10,20,120,25);
        panel.add(selfTotal);

        JLabel selfCards = new JLabel(
                "Current Cards: " +
                        Deck.toCardString(self.hand)
        );
        selfCards.setBounds(10,50,400,25);
        panel.add(selfCards);
        //#endregion

        //#region dealer labels
        JLabel dealerTotal = new JLabel(
                "Current Total: " +
                        dealer.hand.get(1).getValue() + " + 1 Unknown Card"
        );
        dealerTotal.setBounds(screen.width / 2,20,300,25);
        panel.add(dealerTotal);

        JLabel dealerCards = new JLabel(
                "Current Cards: " +
                        dealer.hand.get(1).getName() +
                        dealer.hand.get(1).suitToChar() +
                        " + 1 Unknown Card"
        );
        dealerCards.setBounds(screen.width / 2,50,400,25);
        panel.add(dealerCards);
        //#endregion

        //#region end button
        JButton finalButton = new JButton("♠ ${Player} Busted! Click to play again. ♠");
        finalButton.setBounds(
                screen.width / 2 - 150,
                screen.height / 2 - 75,
                300,
                100
        );
        finalButton.addActionListener(event -> {
            panel.remove(0);
            placeComponents(panel);
            refreshFrame();
        });
        //#endregion

        //#region end function
        voidFn onEnd = () -> {
            self.reset();
            dealer.reset();

            curDeck = new Deck(deckNumber);
            curDeck.shuffle(2);

            panel.removeAll();
            panel.add(finalButton);

            refreshFrame();
        };
        //#endregion

        //#region hit button
        JButton hitButton = new JButton("Hit");
        hitButton.setBounds(10, 100, 80, 25);
        hitButton.addActionListener(e -> {
            self.hand.add(curDeck.draw());
            selfTotal.setText(
                    "Current Total: " +
                            self.updateScore()
            );

            selfCards.setText("Current Cards: " + Deck.toCardString(self.hand));

            if(self.getScore() > 21) {
                finalButton.setText("♠ You Busted! Click to play again. ♠");
                dealer.getRoundScore().addScore(1);
                onEnd.run();
            }
        });
        panel.add(hitButton);
        //#endregion

        //#region stand button
        JButton standButton = new JButton("Stand");
        standButton.setBounds(170, 100, 80, 25);
        standButton.addActionListener(e -> {
            //#region dealer
            while(dealer.shouldHit() && dealer.getScore() < 22)
            {
                dealer.hand.add(curDeck.draw());
                dealerTotal.setText(
                        "Current Total: " +
                                dealer.updateScore()
                );
                dealerCards.setText("Current Cards: " + Deck.toCardString(dealer.hand));
                frame.update(frame.getGraphics());
                try {
                    TimeUnit.SECONDS.sleep((long)1.0);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
            //#endregion

            //#region game endings
            if(dealer.getScore() > 21) {
                finalButton.setText("♠ Dealer Busted! Click to play again. ♠");
                self.getRoundScore().addScore(1);
            } else {
                // dealer stand
                if(dealer.getScore() > self.getScore()) {
                    // dealer wins
                    finalButton.setText("♠ Dealer Won! Click to play again. ♠");
                    dealer.getRoundScore().addScore(1);
                } else if(dealer.getScore() < self.getScore()) {
                    // self win
                    finalButton.setText("♠ You Won! Click to play again. ♠");
                    self.getRoundScore().addScore(1);
                } else {
                    // draw
                    finalButton.setText("♠ Draw! Click to play again. ♠");
                }
            }
            onEnd.run();
            //#endregion
        });
        panel.add(standButton);
        //#endregion
    }
    // there is no way this is good practice
    public static void refreshFrame()
    {
        frame.update(frame.getGraphics());
    }
}