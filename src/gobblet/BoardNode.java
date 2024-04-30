package gobblet;

import java.util.List;
import java.util.ArrayList;


public class BoardNode {
private boolean maxPlayer;
private int score;
private List<BoardNode> children;
private Board board; // Board class used as an attribute
public int PrevX;
public int PrevY;
public Piece PrevPiece;

//constuctor
public BoardNode(Board board,Piece PrevPiece,int PrevX,int PrevY) {
this.board = board;
this.PrevPiece = PrevPiece;
this.PrevX = PrevX;
this.PrevY = PrevY;
}


public void deleteBoard(){
board = null;
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
List<BoardNode> childrenCopy = new ArrayList<>();
if(children!= null) {
childrenCopy = new ArrayList <> (children); // To prevent external modification
}

return childrenCopy;
}

public void addChild (BoardNode child){
//check if there is no children and create list of children
if (children == null){
children = new ArrayList<>();
}
children.add (child);
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


public void setScore(int score) { //Temporary function for testing
this.score = score;
}


}
