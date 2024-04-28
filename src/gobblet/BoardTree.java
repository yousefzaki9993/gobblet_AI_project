package gobblet;

import java.util.LinkedList;
import java.util.List;


public class BoardTree {

	public BoardNode rootNode;
	private List<BoardTree> childNodes;

	public BoardTree(BoardNode rootNode) {
		this.rootNode = rootNode;
		this.childNodes = new LinkedList<>();
	}

	public void addChild(BoardTree childNode) {
		this.childNodes.add(childNode);
	}


	public BoardNode getValue() {
		return rootNode;
	}

	public List<BoardTree> getChildNodes() {
		return childNodes;
	}

	public void generateTree(int depth) throws CloneNotSupportedException {
		if(depth == 0) return;
		
		Board board = rootNode.getBoard();
		List<Piece> movables = board.getMovables();
		
		
		for(int i=0;i<movables.size();i++) {
			Piece piece = movables.get(i);
			if((rootNode.isMaxPlayer() && piece.isBlack())||(!rootNode.isMaxPlayer() && !piece.isBlack())) {
				for(int j=0;j<4;j++) {
					for(int k=0;k<4;k++) {
						if(board.isLegal(piece, j, k)) {
							Board cloneBoard = board.clone();
							cloneBoard.move(cloneBoard.getPiece(piece.getX(), piece.getY()), j, k);
							BoardNode boardnode = new BoardNode(cloneBoard);
							if(rootNode.isMaxPlayer()) boardnode.setMinimizer();
							else boardnode.setMaximizer();
							rootNode.addChild(boardnode);
							BoardTree boardTree = new BoardTree(boardnode);
							this.addChild(boardTree);
							boardTree.generateTree(depth-1);
						}
					}
				}
			}
		}  	
		
	}

}
