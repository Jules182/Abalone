import java.util.ArrayList;

public class GameLogic {

	private PieceType currentPlayer;
	private Cell[][] cells = new Cell[11][11];
	private ArrayList<Cell> selectedCells = new ArrayList<Cell>();

	private ArrayList<Cell> selectableCells = new ArrayList<Cell>();

	public GameLogic(Cell[][] cells) {
		this.cells = cells;
	}

	public void initializeSelectable() {
		for (Cell[] line : cells) {
			for (Cell cell : line) {
				if (cell.hasPiece(currentPlayer))
					selectableCells.add(cell);
			}
		}
	}

	public ArrayList<Cell> getSelectedCells() {
		return selectedCells;
	}
	
	public void setSelectedCells(ArrayList<Cell> selectedCells) {
		this.selectedCells = selectedCells;
	}
	
	public int getNumberOfSelectedCells(){
		return selectedCells.size();
	}
	
	public boolean isSelectable(Cell cell) {
		return selectableCells.contains(cell) && (selectedCells.size() < 3);
	}

	public void setSelectable(Cell cell) {
		selectableCells.add(cell);
	}
	
	public void emptySelectableCells(){
		selectableCells = new ArrayList<Cell>();
	}

	public boolean isSelected(Cell cell) {
		return selectedCells.contains(cell);
	}

	public void setSelected(Cell cell) {
		selectedCells.add(cell);
	}

	public void deSelect(Cell cell) {
		selectedCells.remove(cell);
	}

	public void setCurrentPlayer(PieceType player) {
		this.currentPlayer = player;
	}

	public PieceType getCurrentPlayer() {
		return currentPlayer;
	}

}