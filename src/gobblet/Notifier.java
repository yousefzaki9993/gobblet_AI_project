
package gobblet;

import java.util.ArrayList;
import java.util.List;


public class Notifier {
    private List<Observer> observers = new ArrayList<>();
    public void add(Observer o){
        observers.add(o);
    }
    public boolean remove(Observer o){
        return observers.remove(o);
    }
    public void notifyPlayersEnd(){
        for(Observer o : observers){
            o.endGame();
        }
    }
    public void notifyPlayer(boolean isBlack){
        for(Observer o : observers){
            if(!o.isNeutral() && o.isBlack() == isBlack)o.startRole();
        }
    }

    public void addNeutralObserver(Observer o) {
        if(!o.isNeutral())throw new IllegalArgumentException("non-playing observers must be neutrals!!");
        observers.add(o);
    }
}
