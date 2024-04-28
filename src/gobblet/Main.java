package gobblet;

import java.util.List;

public class Main {
	
    public static double bytesToMegabytes(long bytes) {
        return (double) bytes / (1024 * 1024);
    }
	
	public static void printBoard(Board board) {
		for(int i=0;i<4;i++) {
			for(int j=0;j<4;j++) {
				
				if(board.getPiece(j, i) == null) System.out.print("0 ");
				else if(board.getPiece(j, i).isBlack() == true) System.out.print("1 ");
				else System.out.println("2 ");
			}
			System.out.println("");
		}
	}
	

	public static void main(String[] args) throws CloneNotSupportedException{

		
		Board board = new Board();
//		
//		
		BoardNode rootNode = new BoardNode(board);
		rootNode.setMaximizer();
		BoardTree treeRoot = new BoardTree(rootNode);
		


		long Before = System.currentTimeMillis();
		treeRoot.generateTree(3);
		long After = System.currentTimeMillis();
		
		System.out.println("Time taken to generate tree in seconds: "+(After-Before)*Math.pow(10, -3));
		System.out.println(treeRoot.rootNode.getChildren().size());
		
		


	}

}
