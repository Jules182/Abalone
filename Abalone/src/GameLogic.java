import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;

public class GameLogic {

	// FIELDS
	
	private PieceType currentPlayer;
	private Boolean moved;
	private Cell[][] cells = new Cell[11][11];
	private ArrayList<Cell> selectedCells = new ArrayList<Cell>();
	private ArrayList<Cell> selectableCells = new ArrayList<Cell>();
	private ArrayList<Cell> destinations = new ArrayList<Cell>();
	private CustomControl customControl;

	// GETTER / SETTER

	public GameLogic(CustomControl customControl) {
		this.customControl = customControl;
	}

	public void setCells(Cell[][] cells) {
		this.cells = cells;
	}

	public int getNumberOfSelectedCells() {
		return selectedCells.size();
	}

	public boolean isSelectable(Cell cell) {
		return selectableCells.contains(cell) && (selectedCells.size() < 3);
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

	public boolean isLastSelected(Cell cell) {
		if (selectedCells.isEmpty())
			return false;
		return cell == selectedCells.get(selectedCells.size() - 1);
	}

	public boolean isDestination(Cell cell) {
		return destinations.contains(cell);
	}

	private ArrayList<Cell> getDestinations() {
		return destinations;
	}

	private Cell getLastSelected() {
		if (selectedCells.isEmpty())
			return null;
		return selectedCells.get(selectedCells.size() - 1);
	}

	/**
	 *  METHODS
	 */

	// SELECTABLE PIECES AND DESTINATIONS

	
	/**
	 *  update selectablePieces properly depending on how many pieces are selected
	 */
	private void updateSelectablePieces() {
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
	 * Detects all neighbours of a cell and removes the non-neighbor cells from selectableCells
	 */
	private void findAllNeighbours() {

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
	 * Detects the third piece in a line which is the last selectable cell when two are selected
	 */
	private void findThirdInLine() {
		
		initializeSelectable();

		Cell firstCell = selectedCells.get(0);
		Cell secondCell = selectedCells.get(1);

		int deltaX = firstCell.getxLocation() - secondCell.getxLocation();
		int deltaY = firstCell.getyLocation() - secondCell.getyLocation();

		Cell lastPossibleCell = null;

		int xOfThirdPiece = secondCell.getxLocation() - deltaX;
		int yOfThirdPiece = secondCell.getyLocation() - deltaY;

		for (Cell selectableCell : selectableCells) {
			if (selectableCell.getxLocation() == xOfThirdPiece && selectableCell.getyLocation() == yOfThirdPiece) {
				lastPossibleCell = selectableCell;
			}
		}

		selectableCells = new ArrayList<Cell>();
		if (lastPossibleCell != null)
			selectableCells.add(lastPossibleCell);
	}

	private void emptySelectableCells() {
		selectableCells = new ArrayList<Cell>();
	}

	/**
	 * Checks where selected Pieces can move and highlights those destinations
	 */

	public void checkDestinations() {
		unmarkDestinations();

		if (getNumberOfSelectedCells() == 0)
			return;

		destinations = new ArrayList<Cell>();
		Cell selectedCell = getLastSelected();

		for (Cell[] linesOfCells : cells) {
			for (Cell cell : linesOfCells) {
				int deltaX = cell.getxLocation() - selectedCell.getxLocation();
				int deltaY = cell.getyLocation() - selectedCell.getyLocation();
				double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
				int sum = deltaX + deltaY;

				if (distance < 1.5 && sum != 0 && checkForCrash(selectedCell, cell)
						&& (!cell.hasPieceOf(currentPlayer))) {

					destinations.add(cell);
				}
			}
		}
		markDestinations();
	}

	private void markDestinations() {
		for (Cell destination : getDestinations()) {
			destination.polygon.setFill(Color.LIGHTSTEELBLUE);
		}
	}

	private void unmarkDestinations() {
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
		ArrayList<Piece> movedPieces = new ArrayList<Piece>();
		for (Cell cell : selectedCells) {
			movedPieces.add(cell.getPiece());
		}

		// destination is always determined in relation to last selected cell
		Cell lastSelected = getLastSelected();

		int deltaX = destinationCell.getxLocation() - lastSelected.getxLocation();
		int deltaY = destinationCell.getyLocation() - lastSelected.getyLocation();

		if (selectedCells.size() == 1 || isInLane(destinationCell)) {
			movePiecesInLane(destinationCell);
		} else {
			// check: parallel movement possible?
			boolean parallelMove = true;
			for (Cell cell : selectedCells) {
				Cell destination = cells[cell.getyLocation() + deltaY][cell.getxLocation() + deltaX];
				// if there is a player cell in the way
				if (destination.isPlayerCell()) {
					parallelMove = false;
				}
			}
			// 
			if (parallelMove) {
				for (Cell toMove : selectedCells) {
					Cell destination = cells[toMove.getyLocation() + deltaY][toMove.getxLocation() + deltaX];
					destination.addPiece(toMove.removePiece());
				}
			} else {
				moved = false;
			}
		}

		if (moved) {
			for (Piece piece : movedPieces) {
				piece.setPieceColor();
			}
			selectedCells = new ArrayList<Cell>();
			unmarkDestinations();
			setPiecesLeft(getPiecesLeft(PieceType.PLAYER1), getPiecesLeft(PieceType.PLAYER2));
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
	private void movePiecesInLane(Cell destination) {

		Cell toMove = selectedCells.get(0);

		int xDestination = destination.getxLocation();
		int yDestination = destination.getyLocation();

		int deltaX = xDestination - getLastSelected().getxLocation();
		int deltaY = yDestination - getLastSelected().getyLocation();

		// is there an opponent's piece?
		if (destination.hasPieceOf(getOpponent())) {
			// is there a piece behind? -> don't move
			Cell cellBehind = cells[yDestination + deltaY][xDestination + deltaX];

			if (cellBehind.hasPieceOf(getCurrentPlayer())) {
				moved = false;
				return;
			} else {
				// push pieces depending on number of selected cells
				switch (getNumberOfSelectedCells()) {
				case 1:
					moved = false;
					return;
				case 2:
					if (cellBehind.isPlayerCell()) {
						moved = false;
						return;
					}
					// move one piece
					else if (cellBehind.isGutter())
						destination.removePiece();
					else if (cellBehind.isEmptyCell())
						cellBehind.addPiece(destination.removePiece());
					break;
				case 3:
					// move one piece
					if (cellBehind.isGutter()) {
						destination.removePiece();
						break;
					} else if (cellBehind.isEmptyCell()) {
						cellBehind.addPiece(destination.removePiece());
						break;
					}

					Cell cell2Behind = null;
					try {
						cell2Behind = cells[yDestination + deltaY + deltaY][xDestination + deltaX + deltaX];
					} catch (ArrayIndexOutOfBoundsException e) {
						moved = false;
						return;
					}
					// move two pieces
					if (cell2Behind.isPlayerCell()) {
						moved = false;
						return;
					} else if (cell2Behind.isGutter()) {
						cellBehind.removePiece();
						cellBehind.addPiece(destination.removePiece());
					} else if (cellBehind.hasPieceOf(getOpponent())) {
						cell2Behind.addPiece(cellBehind.removePiece());
						cellBehind.addPiece(destination.removePiece());
					}
					break;

				default:
					break;
				}
			}
		}
		destination.addPiece(toMove.removePiece());
	}

	// CHECKS

	/**
	 * For destination highlighting: would the selected cell crash into other cells?
	 * 
	 * @param toMove: cell to move
	 * @param destination: cell to check
	 * @return move possible?
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

		if (getNumberOfSelectedCells() == 3) {
			Cell thirdCell = selectedCells.get(2);
			deltaX = thirdCell.getxLocation() - destination.getxLocation();
			deltaY = thirdCell.getyLocation() - destination.getyLocation();

		}

		return deltaX == deltaXSelected && deltaY == deltaYSelected;
	}

	private void changePlayer() {
		setCurrentPlayer(getOpponent());
		initializeSelectable();
	}

	private PieceType getOpponent() {
		if (getCurrentPlayer() == PieceType.PLAYER1)
			return PieceType.PLAYER2;
		else
			return PieceType.PLAYER1;
	}

	private void checkForWinner() {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setTitle("Winner detected!");

		if (getPiecesLeft(PieceType.PLAYER1) == 8) {
			alert.setContentText("Player 2 won!");
			alert.showAndWait();
			customControl.resetGame();
		}

		if (getPiecesLeft(PieceType.PLAYER2) == 8) {
			alert.setContentText("Player 1 won!");
			alert.showAndWait();
			customControl.resetGame();
		}

	}

	private int getPiecesLeft(PieceType player) {
		int counter = 0;
		for (Cell[] linesOfCells : cells) {
			for (Cell cell : linesOfCells) {
				if (cell.hasPieceOf(player))
					counter++;
			}
		}
		return counter;

	}

	// PROPERTIES

	// properties to show changing values in labels
	private StringProperty playerName = new SimpleStringProperty();
	private StringProperty playerColor = new SimpleStringProperty();
	private StringProperty p1PiecesLeft = new SimpleStringProperty();
	private StringProperty p2PiecesLeft = new SimpleStringProperty();
	private StringProperty p1PiecesTaken = new SimpleStringProperty();
	private StringProperty p2PiecesTaken = new SimpleStringProperty();

	// property setters
	public final void setPiecesLeft(int P1PiecesLeft, int P2PiecesLeft) {
		p1PiecesLeft.set(Integer.toString(getPiecesLeft(PieceType.PLAYER1)));
		p2PiecesLeft.set(Integer.toString(getPiecesLeft(PieceType.PLAYER2)));
		p1PiecesTaken.set(Integer.toString(14 - getPiecesLeft(PieceType.PLAYER2)));
		p2PiecesTaken.set(Integer.toString(14 - getPiecesLeft(PieceType.PLAYER1)));
	}

	public final void setPlayerName(String value) {
		playerName.set(value);
		if (value == "PLAYER1")
			playerColor.set("-fx-background-color: green");
		if (value == "PLAYER2")
			playerColor.set("-fx-background-color: red");
	}

	// property getters
	public StringProperty playerNameProperty() {
		return playerName;
	}

	public StringProperty playerColorProperty() {
		return playerColor;
	}

	public StringProperty getP1PiecesLeftProperty() {
		return p1PiecesLeft;
	}

	public StringProperty getP2PiecesLeftProperty() {
		return p2PiecesLeft;
	}

	public StringProperty getP1PiecesTakenProperty() {
		return p1PiecesTaken;
	}

	public StringProperty getP2PiecesTakenProperty() {
		return p2PiecesTaken;
	}

}