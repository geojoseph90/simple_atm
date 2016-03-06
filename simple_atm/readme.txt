Assumptions
-----------
The terminal IO devices where modelled as simple text boxes for the purpose of demonstrating their functionality.
It was assumed status of terminal devices was updated by some external processes to the terminal.config file. Eg. cash dispenser slot failure.
The account data was modelled only using basic entity fields. And they are stored in text files instead of database
The bank central service would be polling the ATMS end of day or on a schedule, instead of the ATM publishing changes

Design
----------
The project design followed MVC design pattern.
The controller can be found at satm.controller package
The satm.model package contains the models
The satm.views package contains the different views of the ATM

The demo data is stored in map.ser file
The terminal.config file stores the mock external terminal status data.

Demo data
---------
Valid test accounts are:
cardnumber / pin
90000024 / 0076
90000011 / 9735

The demo data is stored in the map.ser file under SATM parent folder

Running
-------
run SatmLauncher class under src/satm package


Modifying Terminal Device Status
--------------------------------
Edit terminal.config file under SATM parent folder.
Modify status property to a non-ok value to indicate failure of terminal devices.
eg:
CASH_DISPENSER=fail
This will mimic failure of cash dispenser slot and prevent cash withdrawal from ATM.

Revert value as following to get the device working
CASH_DISPENSER=ok 