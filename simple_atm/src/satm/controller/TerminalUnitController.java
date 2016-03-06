package satm.controller;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TerminalUnitController {
    public static TerminalUnitController INSTANCE=new TerminalUnitController();
    public static enum ExternalUnits{ENVELOPE_DEPOSIT,CASH_DISPENSER,PRINTER};
    private Properties terminalStatus=new Properties();
    String terminalControlFile="terminal.config";

    private void loadProperties(){
        try {
            InputStream inputStream=new FileInputStream(terminalControlFile);
            terminalStatus.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isUnitOk(ExternalUnits unitName){
        loadProperties();
        if(terminalStatus.containsKey(unitName.toString()) &&
                terminalStatus.get(unitName.toString()).equals("ok"))
            return true;
        else
            return false;
    }



}
