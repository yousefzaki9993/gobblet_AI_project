package gobblet;

import java.util.ArrayList;
import java.util.List;

/**
 * The Notifier class acts as the Subject in the Observer design pattern.
 *
 * This class is responsible for maintaining a list of observers and notifying them when its state changes.
 * The state change could be anything that the observers (other objects in the system) should be aware of.
 * More details about Observers are explained in the Observer Class.
 *
 * For example, in GameSystem class, a state change could be a player's move, game pause, new game, etc.
 *
 * Key methods include:
 * - add(Observer): Adds an observer to the list of observers.
 * - remove(Observer): Removes an observer from the list.
 * - notifyPlayer(): Notifies all registered observers of a state change.
 *
 * This class plays a crucial role in ensuring that all relevant parts of the system are kept in sync with 
 * its state changes.
 * 
 * It allows for a loose coupling between the GameSystem Class and all of the 
 * Observers(AIPlayer, GameGUI & Player classes, MainGUI), which promotes code flexibility and extensibility.
 */


public class Notifier {
    private boolean paused=false; // If true, the game is paused. If false, the game is running.
    boolean nextPlayer; // If true, it's the Black's turn. If false, it's the White's turn.
    private List<Observer> observers = new ArrayList<>(); // Observers could be any class that implements the Observer interface.
    
    /**
     * This method is called when the user wants to pause the game
     */
    public void pause(){
        paused = true;
    }

    /**
     * This method is called when the user wants to resume the game
     */
    public void resume(){
        paused = false;
        notifyPlayer(nextPlayer);
    }
    
    /**
     * Adds an observer to be notified of state changes.
     */
    public void add(Observer o) {
        observers.add(o);
    }
    
    /**
     * Removes an observer so that it no longer receives state updates.
     */
    public boolean remove(Observer o) {
        return observers.remove(o);
    }

    /**
     * Loops through the observers and notifies them that game has ended
     */
    public void notifyPlayersEnd() {
        for (Observer o : observers) {
            o.endGame();
        }
    }

    /**
     * Responsible for notifying every observer for their roles
     */
    public void notifyPlayer(boolean isBlack) {
        /**
         * When game is paused, it saves the turn for the currently playing player so his turn doesn't go to waste
         */
        if(paused){
            nextPlayer = isBlack;
            return;
        }

        /**
         * Notifying Neutral Obeservers(GUI) that roles of players has changed (new turns) to update the GUI
         */
        for (Observer o : observers) {
            o.switchRole();
        }

        /**
         * Looping on Player Observers and notifying them that their turn has started
         */
        for (Observer o : observers) {
            if (!o.isNeutral() && o.isBlack() == isBlack) {
                if (o instanceof AIPlayer) {
                    Thread t1 = new Thread(() -> {
                        o.startRole();
                    });
                    t1.start();
                } else {
                    o.startRole();
                }
            }
        }
    }

    /**
     * Adding Neutral Observers (non-playing observers) to the list
     */
    public void addNeutralObserver(Observer o) {
        if (!o.isNeutral()) {
            throw new IllegalArgumentException("non-playing observers must be neutrals!!");
        }
        observers.add(o);
    }
}