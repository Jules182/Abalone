import java.util.ArrayList;


public class GameLogic {
	
	// FIELDS

	private PieceType currentPlayer;
	private Cell[][] cells = new Cell[11][11];
	private ArrayList<Cell> selectedCells = new ArrayList<Cell>();

	private ArrayList<Cell> selectableCells = new ArrayList<Cell>();
	
	// CONSTRCTOR

	public GameLogic(Cell[][] cells) {
		this.cells = cells;
	}
	
	// GETTER / SETTER
	
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
	
	public boolean isLastSelected(Cell cell) {
		if (selectedCells.isEmpty())
			return false;
		return cell == selectedCells.get(selectedCells.size() - 1);
	}
	
//	private Cell getLastSelected(){
//		if (selectedCells.isEmpty())
//			return null;
//		return selectedCells.get(selectedCells.size() - 1);
//	}
	
	// METHODS

	/**
	 * Initilizes the selectable cells.
	 * All cells of the player will be slected.
	 */
	public void initializeSelectable() {
		for (Cell[] line : cells) {
			for (Cell cell : line) {
				if (cell.hasPiece(currentPlayer))
					selectableCells.add(cell);
			}
		}
	}


	/**
	 * Detects all neighbours of a cell. 
	 * These cells became the selectable cells.
	 */
	public void findAllNeighbours() {

		initializeSelectable();

		Cell selectedCell = selectedCells.get(0);
		ArrayList<Cell> toDelete = new ArrayList<Cell>();

		for (Cell selectableCell : selectableCells) {
			int deltaX = selectableCell.getxLocation() - selectedCell.getxLocation();
			int deltaY = selectableCell.getyLocation() - selectedCell.getyLocation();
			double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

			if (distance > 1.5) {
				toDelete.add(selectableCell);
			}
		}

		selectableCells.removeAll(toDelete);
	}

	/**
	 * Detects the third piece in a line with is the last selectable cell.
	 */
	public void findThirdInLine() {

		initializeSelectable();

		Cell firstCell = selectedCells.get(0);
		Cell secondCell = selectedCells.get(1);

		int deltaX = firstCell.getxLocation() - secondCell.getxLocation();
		int deltaY = firstCell.getyLocation() - secondCell.getyLocation();

		Cell lastPossibleCell = null;

		int xOfThrirdPiece = secondCell.getxLocation() - deltaX;
		int yOfThrirdPiece = secondCell.getyLocation() - deltaY;

		for (Cell selectableCell : selectableCells) {
			if (selectableCell.getxLocation() == xOfThrirdPiece && selectableCell.getyLocation() == yOfThrirdPiece) {
				lastPossibleCell = selectableCell;
			}
		}

		selectableCells = new ArrayList<Cell>();
		if (lastPossibleCell != null)
			selectableCells.add(lastPossibleCell);

	}

	/**
	 * Moves the selected cells to the clicked cell (named: travelcell)
	 * 
	 * @param travelCell Cell in which direction the cells will be moved
	 */
	public void move(Cell travelCell) {
		
		int X_JUSTFORSYSO = travelCell.getxLocation();
		int Y_JUSTFORSYSO = travelCell.getyLocation();
		
		System.out.println(
				"Before Move: " + cells[Y_JUSTFORSYSO][X_JUSTFORSYSO].getStone().getPlayer());

		Cell lastSelected = selectedCells.get(selectedCells.size() - 1);

		int deltaX = travelCell.getxLocation() - lastSelected.getxLocation();
		int deltaY = travelCell.getyLocation() - lastSelected.getyLocation();

		for (Cell cell : selectedCells) {
			Cell cellToCheck = cells[cell.getyLocation() + deltaY][cell.getxLocation() + deltaX];
			PieceType playerType = cellToCheck.getStone().getPlayer();
			if (playerType == currentPlayer && !selectedCells.contains(cellToCheck)) {
				System.out.println("Do not move in your own pieces!");
				return;
			}
		}
		
		if (selectedCells.size()==1||inLane(travelCell)) {
			swapPiecesInLane(travelCell);
		} else {
			for (Cell cell : selectedCells) {
				Cell destination = cells[cell.getyLocation() + deltaY][cell.getxLocation() + deltaX];
				swapPiecesParallel(cell, destination);
			}
		}

		System.out.println(
				"After Move: " + cells[Y_JUSTFORSYSO][X_JUSTFORSYSO].getStone().getPlayer());
	}

	/**
	 * Moves the selected cells to the clicked cell by swapping the first and the travelcell
	 * @param travelCell Cell in which direction the cells will be moved
	 */
	private void swapPiecesInLane(Cell travelCell) {
		
		Cell toMove = selectedCells.get(0);

		int xToMove = toMove.getxLocation();
		int yToMove = toMove.getyLocation();
		int xDestination = travelCell.getxLocation();
		int yDestination = travelCell.getyLocation();

		cells[yToMove][xToMove] = travelCell;
		cells[yDestination][xDestination] = toMove;

		toMove.setxLocation(xDestination);
		toMove.setyLocation(yDestination);
		travelCell.setxLocation(xToMove);
		travelCell.setyLocation(yToMove);

	}


	/**
	 * Moves each cells to the destination cell (parallel)
	 * 
	 * @param toMove Cell to Move
	 * @param destination destionation Cell
	 */
	private void swapPiecesParallel(Cell toMove, Cell destination) {

		int xToMove = toMove.getxLocation();
		int yToMove = toMove.getyLocation();
		int xDestination = destination.getxLocation();
		int yDestination = destination.getyLocation();

		cells[yToMove][xToMove] = destination;
		cells[yDestination][xDestination] = toMove;

		toMove.setxLocation(xDestination);
		toMove.setyLocation(yDestination);
		destination.setxLocation(xToMove);
		destination.setyLocation(yToMove);

	}

	/**
	 * Checks if the clicked cell is in lane to decide the correct swap method
	 * 
	 * @param destination Cell in which direction the cells will be moved
	 * @return is the cell in lane?
	 */
	private boolean inLane(Cell destination) {

		Cell firstCell = selectedCells.get(0);
		Cell secondCell = selectedCells.get(1);

		int deltaXSelected = firstCell.getxLocation() - secondCell.getxLocation();
		int deltaYSelected = firstCell.getyLocation() - secondCell.getyLocation();

		int deltaX = secondCell.getxLocation() - destination.getxLocation();
		int deltaY = secondCell.getyLocation() - destination.getyLocation();

		return deltaX == deltaXSelected && deltaY == deltaYSelected;
	}

}