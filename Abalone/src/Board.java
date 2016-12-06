/**
 * 
 * @author Markus
 *
 */

@Deprecated
public class Board {

	// variablen:
	private Cell[][] cells = new Cell[11][11]; // array in dem die Zellen gespeichert sind, das iteriere ich komplett durch

	public Board() {		
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				cells[i][j] = new Cell();
			}
		}
		// initialize Cells
	}

	public Cell getCell(int x, int y) {
		return cells[x][y];
	}

	public Cell[][] getCells() {
		return cells;
	}

	public void setCells(Cell[][] cells) {
		this.cells = cells;
	}
}
