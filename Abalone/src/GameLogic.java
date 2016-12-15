import java.util.ArrayList;

public class GameLogic {

	private PieceType currentPlayer;
	private Cell[][] cells = new Cell[11][11];
	private ArrayList<Cell> selectableCells = new ArrayList<Cell>();
		
	
	public GameLogic(Cell[][] cells) {
		this.cells = cells;
	}
	
	public void initializeSelectable() {
		for (Cell[] line : cells) {
			for (Cell cell : line) {
				if (cell.hasPiece(currentPlayer)) selectableCells.add(cell);
			}
		}
	}

	public void setCurrentPlayer(PieceType player) {
		this.currentPlayer = player;
	}

	public PieceType getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean isSelectable(Cell cell) {
		return selectableCells.contains(cell);
	}

}