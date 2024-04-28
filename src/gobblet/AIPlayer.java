
package gobblet;


public class AIPlayer implements Observer{
    private final boolean isBlack;
    private final GameSystem gs;
    private final char difficulty;
    public AIPlayer(boolean isBlack,char difficulty, GameSystem gs){//1 easy 2 normal 3 hard
        this.isBlack = isBlack;
        this.difficulty = difficulty;
        this.gs = gs;
    }
    @Override
    public boolean isBlack() {
        return isBlack;
    }

    @Override
    public void startRole() {
        

        
    }

    @Override
    public void endGame() {
    }
    
}
