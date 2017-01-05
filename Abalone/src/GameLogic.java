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

	public int getNumberOfSelectedCells() {
		return selectedCells.size();
	}

	public boolean isSelectable(Cell cell) {
		return selectableCells.contains(cell) && (selectedCells.size() < 3);
	}

	public void setSelectable(Cell cell) {
		selectableCells.add(cell);
	}

	public void emptySelectableCells() {
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

	public void findAllNeighbours(Cell selectedCell) {
		ArrayList<Cell> toDelete = new ArrayList<Cell>();
		for (Cell selectableCell : selectableCells) {
			System.out.println(selectableCell.getxLocation() + " - " + selectableCell.getyLocation());
			int deltaX = selectableCell.getxLocation() - selectedCell.getxLocation();
			int deltaY = selectableCell.getyLocation() - selectedCell.getyLocation();
			// double distance = Math.sqrt((Math.abs(deltaX) + Math.abs(deltaY))
			// * (Math.abs(deltaX) + Math.abs(deltaY)));
			double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

			System.out.println(distance);
			System.out.println();
			if (distance > 1.5) {
				toDelete.add(selectableCell);

				// selectableCells.remove(selectableCell);
			}

		}

		selectableCells.removeAll(toDelete);
	}

	public void findThirdInLine(Cell cell) {

		Cell firstCell = selectedCells.get(0);
		Cell secondCell = selectedCells.get(1);

		int deltaX = firstCell.getxLocation() - secondCell.getxLocation();
		int deltaY = firstCell.getyLocation() - secondCell.getyLocation();

		// up right
		if (deltaX == 0 && deltaY == -1) {

		}
		// right
		if (deltaX == 0 && deltaY == -1) {

		}

		// down right
		if (deltaX == 0 && deltaY == -1) {

		}

		// down left
		if (deltaX == 0 && deltaY == -1) {

		}

		// left
		if (deltaX == 0 && deltaY == -1) {

		}

		// up left
		if (deltaX == 0 && deltaY == -1) {

		}

	}

}