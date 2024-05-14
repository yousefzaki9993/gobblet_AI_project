package gobblet;

/**
 * Every Class that deals with the GameSystem Class will implement the Observer Class so that
 * the Notifier Class will tell them of any changes happening in the GameSystem in order to
 * maintain a flexible and clean code in GameSystem
 * 
 * Observers here are (AIPlayer, Player in GameGUI and MainGUI)
 */

public interface Observer {
    boolean isBlack();
    void startRole();
    void endGame();
    default boolean isNeutral(){
        return false;
    } 
    default void switchRole(){}
}
