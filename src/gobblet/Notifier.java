package gobblet;

import java.util.ArrayList;
import java.util.List;

public class Notifier {
    private boolean paused=false;
    boolean nextPlayer;
    private List<Observer> observers = new ArrayList<>();
    
    public void pause(){
        paused = true;
    }
    public void resume(){
        paused = false;
        notifyPlayer(nextPlayer);
    }
    
    public void add(Observer o) {
        observers.add(o);
    }
    
    public boolean remove(Observer o) {
        return observers.remove(o);
    }

    public void notifyPlayersEnd() {
        for (Observer o : observers) {
            o.endGame();
        }
    }

    public void notifyPlayer(boolean isBlack) {
        if(paused){
            nextPlayer = isBlack;
            return;
        }
//        Thread t = new Thread(() -> {
            for (Observer o : observers) {
                o.switchRole();
            }
//        });
//        t.start();
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

    public void addNeutralObserver(Observer o) {
        if (!o.isNeutral()) {
            throw new IllegalArgumentException("non-playing observers must be neutrals!!");
        }
        observers.add(o);
    }
}
