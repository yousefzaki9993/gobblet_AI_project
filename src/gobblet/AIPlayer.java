
package gobblet;


public class AIPlayer implements Observer{
    private final boolean isBlack;
    private final GameSystem gs;
    private final int difficulty;
    private boolean running = true;
    
    
    public AIPlayer(boolean isBlack,int difficulty, GameSystem gs){//1 easy 2 normal 3 hard
        this.isBlack = isBlack;
        this.difficulty = difficulty;
        this.gs = gs;
    }
    @Override
    public boolean isBlack() {
        return isBlack;
    }

    //method called when the CPU is playing
    @Override
    public void startRole() {
        if(!running)return;
        MoveEvaloator mv = new MoveEvaloator(gs.getBoardCopy());
        //switch the evaluation function and time taken according to difficulty chosen
        switch (difficulty){
            case 1:
                mv.Eval(isBlack, 1f, new Easy(),1);//time limit is not important 
                break;
            case 2:
                mv.Eval(isBlack, 0.25f, new Easy(),2);
                break;
            case 3:
                mv.Eval(isBlack, 2f, new Easy(),10); // maximum depth is not important
                break;
            default:
                throw new IllegalArgumentException("difficulty must be 1, 2 or 3!");
                
        }
        //pick and move the pieces according to evaluation
        gs.pick(isBlack, mv.getPieceToMove().getX(), mv.getPieceToMove().getY());
        gs.move(isBlack,mv.getDestX() , mv.getDestY());
        
    }

    @Override
    public void endGame() {
        running = false; // end game
    }
    
}
