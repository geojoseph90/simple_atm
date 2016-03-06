package satm.controller;

/**
 * Class will mock the external terminal status instead of loading from file
 *
 */
public class MockTerminalUnitController extends TerminalUnitController{

    boolean allUnitsOkay=true;
    public void isAllUnitsOkay(boolean value){
        allUnitsOkay=value;
    }

    @Override
    public boolean isUnitOk(ExternalUnits unitName) {
        return allUnitsOkay;
    }
}
