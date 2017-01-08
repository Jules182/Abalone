import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GameLogic {

	// FIELDS

	private PieceType currentPlayer;
	private Cell[][] cells = new Cell[11][11];
	private ArrayList<Cell> selectedCells = new ArrayList<Cell>();

	private ArrayList<Cell> selectableCells = new ArrayList<Cell>();

	private ArrayList<Cell> destinations = new ArrayList<Cell>();

	private int round = 0;

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

	public void select(Cell cell) {
		cell.getPiece().setSelectColor();
		selectedCells.add(cell);
	}

	public void deselect(Cell cell) {
		cell.getPiece().setPieceColor();
		selectedCells.remove(cell);
	}

	public PieceType getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(PieceType player) {
		this.currentPlayer = player;
		this.setPlayerName(player.name());
	}

	// Player Name property to show current player in label
	private StringProperty playerName = new SimpleStringProperty();

	// Define a getter for the property's value
	public final String getPlayerName() {
		return playerName.get();
	}

	// Define a setter for the property's value
	public final void setPlayerName(String value) {
		playerName.set(value);
	}

	// Define a getter for the property itself
	public StringProperty playerNameProperty() {
		return playerName;
	}

	public boolean isLastSelected(Cell cell) {
		if (selectedCells.isEmpty())
			return false;
		return cell == selectedCells.get(selectedCells.size() - 1);
	}

	private Cell getLastSelected() {
		if (selectedCells.isEmpty())
			return null;
		return selectedCells.get(selectedCells.size() - 1);
	}

	public ArrayList<Cell> getDestinations() {
		return destinations;
	}

	public boolean isDestination(Cell cell) {
		return destinations.contains(cell);
	}

	// METHODS

	/**
	 * Initializes the selectable cells. All cells of the current player will be
	 * selectable
	 */
	public void initializeSelectable() {
		for (Cell[] line : cells) {
			for (Cell cell : line) {
				if (cell.hasPieceOf(currentPlayer))
					selectableCells.add(cell);
			}
		}
	}

	/**
	 * Detects all neighbours of a cell. These cells become the selectable
	 * cells.
	 */
	public void findAllNeighbours() {

		initializeSelectable();

		Cell selectedCell = selectedCells.get(0);
		ArrayList<Cell> toDelete = new ArrayList<Cell>();

		for (Cell selectableCell : selectableCells) {
			int deltaX = selectableCell.getxLocation() - selectedCell.getxLocation();
			int deltaY = selectableCell.getyLocation() - selectedCell.getyLocation();
			double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
			int sum = deltaX + deltaY;

			if (distance > 1.5 && sum != 0) {
				toDelete.add(selectableCell);
			}
		}

		selectableCells.removeAll(toDelete);
	}

	public void checkDestinations() {
		destinations = new ArrayList<Cell>();
		Cell selectedCell = getLastSelected();

		for (Cell[] linesOfCells : cells) {
			for (Cell cell : linesOfCells) {
				int deltaX = cell.getxLocation() - selectedCell.getxLocation();
				int deltaY = cell.getyLocation() - selectedCell.getyLocation();
				double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
				int sum = deltaX + deltaY;

				if (distance < 1.5 && distance != 0 && sum != 0 && checkForCrash(selectedCell, cell)
						&& cell.getPiece().getPlayer() != currentPlayer) {
					System.out.println("Add x=" + cell.getxLocation() + " y=" + cell.getyLocation());

					destinations.add(cell);
				}
			}
		}
	}

	/**
	 * Detects the third piece in a line which is the last selectable cell.
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
	 * @param travelCell
	 *            Cell in which direction the cells will be moved
	 */
	public void move(Cell travelCell) {

		int X_JUSTFORSYSO = travelCell.getxLocation();
		int Y_JUSTFORSYSO = travelCell.getyLocation();

		System.out.println("Before Move: " + cells[Y_JUSTFORSYSO][X_JUSTFORSYSO].getPiece().getPlayer());

		Cell lastSelected = getLastSelected();

		int deltaX = travelCell.getxLocation() - lastSelected.getxLocation();
		int deltaY = travelCell.getyLocation() - lastSelected.getyLocation();

		for (Cell cell : selectedCells) {
			cell.getPiece().setPieceColor();
		}

		if (selectedCells.size() == 1 || inLane(travelCell)) {
			swapPiecesInLane(travelCell);
		} else {
			boolean letsdo = true;
			for (Cell cell : selectedCells) {
				Cell destination = cells[cell.getyLocation() + deltaY][cell.getxLocation() + deltaX];
				if (!dirtyHarryCheckForParallelMovement(cell, destination))
					letsdo = false;
			}
			if (letsdo) {

				for (Cell cell : selectedCells) {
					Cell destination = cells[cell.getyLocation() + deltaY][cell.getxLocation() + deltaX];
					swapPiecesParallel(cell, destination);
				}
			} else
				return;
		}

		System.out.println("After Move: " + cells[Y_JUSTFORSYSO][X_JUSTFORSYSO].getPiece().getPlayer());
		selectedCells = new ArrayList<Cell>();
		changePlayer();
	}

	private boolean checkForCrash(Cell toMove, Cell destination) {
		int deltaX = destination.getxLocation() - toMove.getxLocation();
		int deltaY = destination.getyLocation() - toMove.getyLocation();

		for (Cell cell : selectedCells) {
			Cell cellToCheck = cells[cell.getyLocation() + deltaY][cell.getxLocation() + deltaX];
			// System.out.println("Cell to check : x="+
			// cellToCheck.getxLocation() + " y=" + cellToCheck.getyLocation());
			PieceType playerType = cellToCheck.getPiece().getPlayer();
			if (playerType == currentPlayer && !selectedCells.contains(cellToCheck)) {
				// System.out.println("failed");
				return false;
			}
		}
		// System.out.println("passed");
		return true;
	}

	/**
	 * Moves the selected cells to the clicked cell by swapping the first and
	 * the travelcell
	 * 
	 * @param travelCell
	 *            Cell in which direction the cells will be moved
	 */
	private void swapPiecesInLane(Cell travelCell) {

		Cell toMove = selectedCells.get(0);

		int xToMove = toMove.getxLocation();
		int yToMove = toMove.getyLocation();
		int xDestination = travelCell.getxLocation();
		int yDestination = travelCell.getyLocation();

		int deltaX = xDestination - getLastSelected().getxLocation();
		int deltaY = yDestination - getLastSelected().getyLocation();

		// is there a piece of the other player?
		if (travelCell.getPiece().getPlayer() == getOtherPlayer()) {
			// is there a piece behind? -> dont move
			Cell cellBehind = cells[yDestination + deltaY][xDestination + deltaX];
			if (cellBehind.getCellType() == CellType.EMPTY && cellBehind.getPiece().getPlayer() != PieceType.DEFAULT) {
				System.out.println("dont move, there is a piece behind!");
				return;
			}
			// else: can I push it -> a) from the board b) on the board
			else if (cellBehind.getCellType() == CellType.GUTTER) {
				System.out.println("Piece kicked off field");
				travelCell.removePiece();
			} else if (cellBehind.getCellType() == CellType.EMPTY
					&& cellBehind.getPiece().getPlayer() == PieceType.DEFAULT) {
				System.out.println("unfriedly cell moved");
				cellBehind.addPiece(cells[yDestination][xDestination].removePiece());
			}
		}
		cells[yDestination][xDestination].addPiece(cells[yToMove][xToMove].removePiece());
	}

	/**
	 * Moves each cells to the destination cell (parallel)
	 * 
	 * @param toMove
	 *            Cell to Move
	 * @param destination
	 *            destination Cell
	 */
	private void swapPiecesParallel(Cell toMove, Cell destination) {

		int xToMove = toMove.getxLocation();
		int yToMove = toMove.getyLocation();
		int xDestination = destination.getxLocation();
		int yDestination = destination.getyLocation();

		int deltaX = xDestination - xToMove;
		int deltaY = yDestination - yToMove;

		// is there a piece of the other player?
		if (destination.getPiece().getPlayer() == getOtherPlayer()) {
			// is there a piece behind? -> dont move
			Cell cellBehind = cells[yDestination + deltaY][xDestination + deltaX];
			if (cellBehind.getCellType() == CellType.EMPTY && cellBehind.getPiece().getPlayer() != PieceType.DEFAULT) {
				System.out.println("dont move, there is a piece behind!");
				return;
			}
			// else: can I push it -> a) from the board b) on the board
			else if (cellBehind.getCellType() == CellType.GUTTER) {
				System.out.println("Piece kicked off field");
				destination.removePiece();
			} else if (cellBehind.getCellType() == CellType.EMPTY
					&& cellBehind.getPiece().getPlayer() == PieceType.DEFAULT) {
				System.out.println("unfriedly cell moved");
				cellBehind.addPiece(cells[yDestination][xDestination].removePiece());
			}
		}
		cells[yDestination][xDestination].addPiece(cells[yToMove][xToMove].removePiece());

	}

	private boolean dirtyHarryCheckForParallelMovement(Cell toMove, Cell destination) {

		// TODO HELP, PLEASE REFACTOR ME!!!1111
		int xToMove = toMove.getxLocation();
		int yToMove = toMove.getyLocation();
		int xDestination = destination.getxLocation();
		int yDestination = destination.getyLocation();

		int deltaX = xDestination - xToMove;
		int deltaY = yDestination - yToMove;

		if (destination.getPiece().getPlayer() == getOtherPlayer()) {
			// is there a piece behind? -> dont move
			Cell cellBehind = cells[yDestination + deltaY][xDestination + deltaX];
			if (cellBehind.getCellType() == CellType.EMPTY && cellBehind.getPiece().getPlayer() != PieceType.DEFAULT) {
				System.out.println("dont move, there is a piece behind!");
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the clicked cell is in lane to decide the correct swap method
	 * 
	 * @param destination
	 *            Cell in which direction the cells will be moved
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

	public void changePlayer() {

		setCurrentPlayer(getOtherPlayer());

		round++;

		initializeSelectable();

	}

	public PieceType getOtherPlayer() {
		if (round % numberOfPlayers == 0)
			return PieceType.PLAYER2;
		return PieceType.PLAYER1;
	}

	final int numberOfPlayers = 2;

	public void checkForWinner() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setTitle("Winner detected!");

		if (getScore(PieceType.PLAYER1) == 0) {
			alert.setContentText("Player 2 won!");
			alert.showAndWait();

		}

		if (getScore(PieceType.PLAYER2) == 0) {
			alert.setContentText("Player 1 won!");
			alert.showAndWait();
		}

		System.out.println("P1: " + getScore(PieceType.PLAYER1) + " - P2: " + getScore(PieceType.PLAYER2));

	}

	private int getScore(PieceType player) {
		int counter = 0;
		for (Cell[] linesOfCells : cells) {
			for (Cell cell : linesOfCells) {
				if (cell.getPiece().getPlayer() == player)
					counter++;
			}
		}
		return counter;
	}

}