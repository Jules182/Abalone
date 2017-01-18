import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;

public class GameLogic {

	// FIELDS

	final int numberOfPlayers = 2;

	private PieceType currentPlayer;
	private Cell[][] cells = new Cell[11][11];

	private ArrayList<Cell> selectedCells = new ArrayList<Cell>();

	private ArrayList<Cell> selectableCells = new ArrayList<Cell>();

	private ArrayList<Cell> destinations = new ArrayList<Cell>();

	private int round = 0;
	private Boolean moved;

	// GETTER / SETTER

	public void setCells(Cell[][] cells) {
		this.cells = cells;
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
		updateSelectablePieces();
	}

	public void deselect(Cell cell) {
		cell.getPiece().setPieceColor();
		selectedCells.remove(cell);
		updateSelectablePieces();
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

	// SELECTABLE PIECES AND DESTINATIONS

	/**
	 * Initializes the selectable cells. All cells of the current player will be selectable
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
	 * Detects all neighbours of a cell and removes the non-neighbor cells from selectable
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
				// remove cells that are not neighbors
				toDelete.add(selectableCell);
			}
		}

		selectableCells.removeAll(toDelete);
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
	 * Checks where selected Pieces can move and highlights those destinations
	 */

	public void checkDestinations() {
		unmarkDestinations();
		
		if (getNumberOfSelectedCells() == 0) return;
		
		destinations = new ArrayList<Cell>();
		Cell selectedCell = getLastSelected();

		for (Cell[] linesOfCells : cells) {
			for (Cell cell : linesOfCells) {
				int deltaX = cell.getxLocation() - selectedCell.getxLocation();
				int deltaY = cell.getyLocation() - selectedCell.getyLocation();
				double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
				int sum = deltaX + deltaY;

				if (distance < 1.5 && sum != 0 && 
						checkForCrash(selectedCell, cell)
						&& (!cell.hasPieceOf(currentPlayer))) {
					System.out.println("Add x=" + cell.getxLocation() + " y=" + cell.getyLocation());

					destinations.add(cell);
				}
			}
		}
		markDestinations();
	}

	public void markDestinations() {
		for (Cell destination : getDestinations()) {
			destination.polygon.setFill(Color.LIGHTSTEELBLUE);
		}
	}

	public void unmarkDestinations() {
		for (Cell destination : getDestinations()) {
			destination.polygon.setFill(Color.LIGHTBLUE);
		}
	}

	// MOVEMENT

	/**
	 * Moves the selected cells towards the clicked cell (named: destinationCell)
	 * 
	 * @param destinationCell
	 *            Cell in which direction the cells will be moved
	 */
	public void move(Cell destinationCell) {

		moved = true;

		System.out.println("Before Move: "
				+ cells[destinationCell.getyLocation()][destinationCell.getxLocation()].getPiece().getPlayer());

		// destination is always determined in relation to last selected cell
		Cell lastSelected = getLastSelected();

		int deltaX = destinationCell.getxLocation() - lastSelected.getxLocation();
		int deltaY = destinationCell.getyLocation() - lastSelected.getyLocation();

		ArrayList<Piece> movedPieces = new ArrayList<Piece>();

		for (Cell cell : selectedCells) {
			movedPieces.add(cell.getPiece());
		}

		if (selectedCells.size() == 1 || isInLane(destinationCell)) {
			// TODO change to swappiecesinparallel, change to "movement" instead of 2 methods
			swapPiecesInLane(destinationCell);
		} else {
			boolean letsdo = true;
			for (Cell cell : selectedCells) {
				Cell destination = cells[cell.getyLocation() + deltaY][cell.getxLocation() + deltaX];
				if (destination.isPlayerCell()) {
					System.out.println("dont move, there is no space!");
					letsdo = false;
				}
			}
			if (letsdo) {
				for (Cell toMove : selectedCells) {
					Cell destination = cells[toMove.getyLocation() + deltaY][toMove.getxLocation() + deltaX];
					destination.addPiece(toMove.removePiece());
				}
			} else {
				moved = false;
			}
		}

		System.out.println("After Move: "
				+ cells[destinationCell.getyLocation()][destinationCell.getxLocation()].getPiece().getPlayer());

		if (moved) {
			// wenn movement eine einzige methode ist:
			// TODO cells[yDestination][xDestination].addPiece(cells[yToMove][xToMove].removePiece());

			for (Piece piece : movedPieces) {
				piece.setPieceColor();
			}

			selectedCells = new ArrayList<Cell>();
			unmarkDestinations();
			checkForWinner();
			changePlayer();
		}
	}

	/**
	 * Moves the selected cells to the clicked cell by swapping the first and the destination
	 * 
	 * @param destination
	 *            Cell in which direction the cells will be moved
	 */
	private void swapPiecesInLane(Cell destination) {

		Cell toMove = selectedCells.get(0);

		int xDestination = destination.getxLocation();
		int yDestination = destination.getyLocation();

		int deltaX = xDestination - getLastSelected().getxLocation();
		int deltaY = yDestination - getLastSelected().getyLocation();

		// is there a piece of the other player?
		if (destination.hasPieceOf(getOtherPlayer())) {
			// is there a piece behind? -> dont move
			Cell cellBehind = cells[yDestination + deltaY][xDestination + deltaX];
			if (cellBehind.isPlayerCell()) {
				System.out.println("dont move, there is a piece behind!");
				moved = false;
				return;
			}
			// else: can I push it -> a) from the board b) on the board
			else if (cellBehind.isGutter()) {
				System.out.println("Piece kicked off field");
				destination.removePiece();
			} else if (cellBehind.isEmptyCell()) {
				System.out.println("unfriendly cell moved");
				cellBehind.addPiece(destination.removePiece());
			}
		}
		destination.addPiece(toMove.removePiece());
	}

	// CHECKS

	/**
	 * 
	 * @param toMove
	 * @param destination
	 * @return
	 */

	private boolean checkForCrash(Cell toMove, Cell destination) {
		int deltaX = destination.getxLocation() - toMove.getxLocation();
		int deltaY = destination.getyLocation() - toMove.getyLocation();

		for (Cell cell : selectedCells) {
			Cell cellToCheck = cells[cell.getyLocation() + deltaY][cell.getxLocation() + deltaX];
			if (cellToCheck.hasPieceOf(currentPlayer) && !isSelected(cellToCheck)) {
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
	private boolean isInLane(Cell destination) {

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
				if (cell.hasPieceOf(player))
					counter++;
			}
		}
		return counter;
	}

	public void updateSelectablePieces() {
		// update selectablePieces properly depending on how many pieces are selected

		switch (getNumberOfSelectedCells()) {
		case 0:
			initializeSelectable();
			break;
		case 1:
			findAllNeighbours();
			break;
		case 2:
			findThirdInLine();
			break;
		case 3:
			emptySelectableCells();
			break;
		default:
			initializeSelectable();
			break;
		}
	}

}