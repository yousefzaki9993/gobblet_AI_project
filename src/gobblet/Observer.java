package gobblet;

public interface Observer {
    boolean isBlack();
    void startRole();
    void endGame();
    default boolean isNeutral(){
        return false;
    } 
}
