

// basic implementation of a skin
// imports necessary for this class to work
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;

// class definition for a custom skin
class CellSkin extends SkinBase<Cell> implements Skin<Cell> {
	public CellSkin(Cell cc) {
		// call the super class constructor
		super(cc);
	}
}