package satm;


import satm.controller.TerminalUnitController;
import satm.controller.ViewController;
import satm.model.Accounts;

import javax.swing.*;

public class SatmLauncher {

    public static void main(String[] args){
        //start by loading persisted data
        Accounts.loadDemoDataFromFile();
        //launch ui
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ViewController().startup();
            }
        });


    }

}
