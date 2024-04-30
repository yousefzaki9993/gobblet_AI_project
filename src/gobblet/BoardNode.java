package boardnode;

import java.util.List;
import java.util.ArrayList;


public class BoardNode {
    private boolean maxPlayer;
    private int score;
    private List<BoardNode> children;
    private Board board;  // Board class used as an attribute

    //constuctor
    public BoardNode(Board board) {
        this.board = board;
    }

    // Getters and setters for other attributes

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
    
    // methods 
    
    public List<BoardNode> getChildren (){
    List<BoardNode> childrenCopy = new ArrayList <> (children); // To prevent external modification 
        return childrenCopy;
    }
    
    public void addChild (BoardNode child){
        //check if there is no children and create list of children 
        if (children == null){
        children = new ArrayList<>(); 
        }
        children.add (child);
    }
    
    public void evaluate (int depth, int scoreEval){
        
    }
    
    public int getScore (){
        return score;
    }
    
    public void setMaximizer (){
        maxPlayer = true;
        
        if (children != null){
        for (BoardNode child : children){
            child.setMinimizer ();
        }
      }
    }
    
    public void setMinimizer (){
        maxPlayer = false;
        
        if (children != null){
        for (BoardNode child : children){
            child.setMaximizer ();
        }
      }
    }
    
    public boolean isMaxPlayer (){
        return maxPlayer ;
    }
 
    
}


    
