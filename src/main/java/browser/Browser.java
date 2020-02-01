package browser;

import hibernate.HibernateManager;
//In first place the imports
import hibernate.HibernateUtil;
import errorControl.ErrorControl;
import errorControl.ErrorControl.ErrorType;

import java.util.Scanner;
import java.util.logging.Level;

import browser.TreatEntry.EntryType;
import browser.TreatEntry.Idiom;
import browser.TreatEntry.SortType;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Browser {
	// The diferents variables from the program
	private Idiom _idiom; // The program idiom
	private File _previousDirectory; // The previous directory in which we were
	private File _currentDirectory; // The current directory in which we are
	private EntryType _lastEntry; // The last type of command entered
	private SimpleDateFormat _sdf; // The default format of our program for dates
	private HibernateManager _connector; // An object with the ability to extract data data from databases
	private ErrorControl _ec; // Our error control object
	private Scanner _sc; // The scanner of the keyboard

	// The main to start the program
	public static void main(String args[]) {
		// This line removes any hibernate startup log.
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
		new Browser();
	}

	// The constructor of the class
	public Browser() {
		_idiom = Idiom.ENGLISH;
		_currentDirectory = new File(System.getProperty("user.dir"));
		_lastEntry = null;
		_sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		_connector = new HibernateManager(_idiom);
		_ec = new ErrorControl(_connector);
		_sc = new Scanner(System.in);

		init();
	}

	// The method to start the browser, first examines whether or not the command is
	// a scrip,
	// if not, it will execute the "selectCommand ()" method to examine the rest of
	// the possibilities
	private void init() {
		String entryText;
		String[] parameters;
		TreatEntry treatEntryText;
		while (_lastEntry != EntryType.EXIT) {
			System.out.print(_currentDirectory.getAbsolutePath() + "> ");
			entryText = _sc.nextLine();
			treatEntryText = new TreatEntry(entryText);
			parameters = treatEntryText.obtainParameters();
			uploadLog(treatEntryText);
			switch (treatEntryText.obtainEntryType()) {
			case SCRIPT:
				this.commandSCRIPT(parameters[0]);
				break;
			case EXIT:
				this.commandEXIT();
				break;
			default:
				selectCommand(treatEntryText);
				break;
			}
		}
		_sc.close();
		_connector.shutdown();
	}

	// A small method to obtain the necessary data and create a log
	private void uploadLog(TreatEntry entry) {
		Timestamp ts = new Timestamp((new Date().getTime()));
		String parameters = "";
		if (entry.obtainParameters() != null) {
			for (String p : entry.obtainParameters()) {
				parameters = parameters + p + " ";
			}
		}
		_connector.uploadLog(ts, entry.obtainCommand(), parameters);
	}

	// Find the type of entry and execute the appropriate method
	private void selectCommand(TreatEntry entry) {
		String[] parameters = entry.obtainParameters();
		switch (entry.obtainEntryType()) {
		case GOTO:
			this.commandGOTO(parameters[0]);
			break;
		case GOLAST:
			this.commandGOLAST();
			break;
		case LIST:
			this.commandLIST();
			break;
		case SORTBY:
			this.commandSORTBY(entry.obtainSortType());
			break;
		case UP:
			this.commandUP();
			break;
		case INFOFILE:
			this.commandINFOFILE(parameters[0]);
			break;
		case INFODIR:
			this.commandINFODIR(parameters[0]);
			break;
		case HELP:
			if (parameters[0] != null) {
				this.commandHELP(parameters[0]);
			} else {
				this.commandHELP();
			}
			break;
		case CREATEDIR:
			this.commandCREATEDIR(parameters);
			break;
		case CREATEFILE:
			this.commandCREATEFILE(parameters);
			break;
		case DELETEDIR:
			this.commandDELETEDIR(parameters);
			break;
		case DELETEFILE:
			this.commandDELETEFILE(parameters);
			break;
		case CHANGEIDIOM:
			this.commandCHANGEIDIOM(entry.obtainIdiom());
			break;
		case CLEARLOG:
			this.commandCLEARLOG();
			break;
		case ERROR:
			this.commandERROR(entry.obtainError());
			break;
		default:
			break;
		}
	}

	// Here will be all the different functions that will use the commands//

	// This method is used to travel between different directories
	private void commandGOTO(String parameter) {
		File auxD;
		if (_currentDirectory.getPath() == "") {
			auxD = new File(parameter);
		} else {
			auxD = new File(_currentDirectory.getPath() + _currentDirectory.separator + parameter);
		}
		if (auxD.exists() && auxD.isDirectory()) {
			_previousDirectory = _currentDirectory;
			_currentDirectory = auxD;
		} else {
			_ec.printError(ErrorType.DIRECTORYNOTFOUND);
		}
		_lastEntry = EntryType.GOTO;
		auxD = null;
	}

	// This method serves to travel to the last directory in which it was
	private void commandGOLAST() {
		File auxD;
		if (_previousDirectory != null || _currentDirectory != null) {
			auxD = _currentDirectory;
			_currentDirectory = _previousDirectory;
			_previousDirectory = auxD;
			_lastEntry = EntryType.GOLAST;
			auxD = null;
		} else {
			_ec.printError(ErrorType.DIRECTORYNOTFOUND);
		}
	}

	// List all items within the current directory
	private void commandLIST() {
		String[] elementsList = _currentDirectory.list();
		if (elementsList != null && elementsList.length > 0) {
			for (String item : elementsList) {
				System.out.println(">> " + item);
			}
		} else {
			_ec.printError(ErrorType.DIRECTORYEMPTY);
		}
		_lastEntry = EntryType.LIST;
		elementsList = null;
	}

	// Try to go to the parent directory of the current directory
	private void commandUP() {
		if (_currentDirectory.getAbsoluteFile().getParentFile() != null) {
			_currentDirectory = _currentDirectory.getAbsoluteFile().getParentFile();
		} else {
			_ec.printError(ErrorType.NOMOREUP);
		}
		_lastEntry = EntryType.UP;
	}

	// Displays the information of the referenced file
	private void commandINFOFILE(String parameter) {
		File auxD = new File(parameter);
		if (auxD.exists() && auxD.isFile()) {
			System.out.println("	>> " + _connector.getLiteral("name") + ": " + auxD.getName());
			System.out.println("	>> " + _connector.getLiteral("lastModified") + ": "
					+ _sdf.format(new Date(auxD.lastModified())));
		} else {
			_ec.printError(ErrorType.FILENOTFOUND);
		}
		_lastEntry = EntryType.INFOFILE;
		auxD = null;
	}

	// Displays the information of the referenced directory
	private void commandINFODIR(String parameter) {
		File auxD = new File(parameter);
		if (auxD.exists() && auxD.isDirectory()) {
			System.out.println("	>> " + _connector.getLiteral("name") + ": " + auxD.getName());
			System.out.println("	>> " + _connector.getLiteral("lastModified") + ": "
					+ _sdf.format(new Date(auxD.lastModified())));
		} else {
			_ec.printError(ErrorType.DIRECTORYNOTFOUND);
		}
		_lastEntry = EntryType.INFODIR;
		auxD = null;
	}

	// By not entering parameters this method will be called, showing all available
	// commands
	private void commandHELP() {
		System.out.println(_connector.getLiteral("commandList") + ": " + "\n>>goto" + "\n>>golast" + "\n>>list"
				+ "\n>>up" + "\n>>infofile" + "\n>>infodir" + "\n>>help" + "\n>>createdir" + "\n>>createfile"
				+ "\n>>sortby" + "\n>>deletedir" + "\n>>deletefile" + "\n>>changeidiom" + "\n>>clearlog" + "\n>>script"
				+ "\n>>exit" + "\n");
	}

	// Entering a parameter will give a brief description of the command entered
	private void commandHELP(String parameter) {
		switch (parameter) {
		case "goto":
			System.out.println("GoTo:" + "\n*" + _connector.getLiteral("commandList_" + parameter) + ".\n");
			break;
		case "golast":
			System.out.println("GoLast:" + "\n*" + _connector.getLiteral("commandList_" + parameter) + ".\n");
			break;
		case "list":
			System.out.println("List:" + "\n*" + _connector.getLiteral("commandList_" + parameter) + ".\n");
			break;
		case "up":
			System.out.println("Up:" + "\n*" + _connector.getLiteral("commandList_" + parameter) + ".\n");
			break;
		case "infofile":
			System.out.println("InfoFile:" + "\n*" + _connector.getLiteral("commandList_" + parameter) + ".\n");
			break;
		case "infodir":
			System.out.println("InfoDir:" + "\n*" + _connector.getLiteral("commandList_" + parameter) + ".\n");
			break;
		case "help":
			System.out.println("Help:" + "\n*" + _connector.getLiteral("commandList_" + parameter) + ".\n");
			break;
		case "createdir":
			System.out.println("CreateDir:" + "\n*" + _connector.getLiteral("commandList_" + parameter) + ".\n");
			break;
		case "createfile":
			System.out.println("CreateFile:" + "\n*" + _connector.getLiteral("commandList_" + parameter) + ".\n");
			break;
		case "sortby":
			System.out.println("SortBy:" + "\n*" + _connector.getLiteral("commandList_" + parameter) + ".\n" + " NAME: "
					+ _connector.getLiteral("commandList_" + parameter + "_name") + "\n" + " DATE: "
					+ _connector.getLiteral("commandList_" + parameter + "_date") + "\n");
			break;
		case "deletedir":
			System.out.println("DeleteDir:" + "\n*" + _connector.getLiteral("commandList_" + parameter) + ".\n");
			break;
		case "deletefile":
			System.out.println("DeleteFile:" + "\n*" + _connector.getLiteral("commandList_" + parameter) + ".\n");
			break;
		case "changeidiom":
			System.out.println("ChangeIdiom:" + "\n*" + _connector.getLiteral("commandList_" + parameter) + ".\n"
					+ " ESP: " + _connector.getLiteral("commandList_" + parameter + "_esp") + "\n" + " ENG: "
					+ _connector.getLiteral("commandList_" + parameter + "_eng") + "\n");
			break;
		case "clearlog":
			System.out.println("ClearLog: " + "\n*" + _connector.getLiteral("commandList_" + parameter) + ".\n");
			break;
		case "script":
			System.out.println("Script:" + "\n*" + _connector.getLiteral("commandList_" + parameter) + ".\n");
			break;
		case "exit":
			System.out.println("Exit:" + "\n*" + _connector.getLiteral("commandList_" + parameter) + ".\n");
			break;
		default:
			_ec.printError(ErrorType.COMMANDNOTFOUND);
			break;
		}
	}

	// Will try to create the given directories as parameters
	private void commandCREATEDIR(String[] parameterArray) {
		File auxD;
		for (String parameter : parameterArray) {
			auxD = new File(parameter);
			if (!auxD.exists()) {
				auxD.mkdir();
			} else {
				_ec.printError(ErrorType.DIRECTORYEXIST);
			}
		}
		_lastEntry = EntryType.CREATEDIR;
		auxD = null;
	}

	// Will try to create the given files as parameters
	private void commandCREATEFILE(String[] parameterArray) {
		File auxD;
		for (String parameter : parameterArray) {
			auxD = new File(parameter);
			if (!auxD.exists()) {
				try {
					auxD.createNewFile();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			} else {
				_ec.printError(ErrorType.FILEEXIST);
			}
			_lastEntry = EntryType.CREATEFILE;
			auxD = null;
		}
	}

	// Delete marked directories and all their content
	private void commandDELETEDIR(String[] parameterArray) {
		File auxD;
		for (String parameter : parameterArray) {
			auxD = new File(parameter);
			if (auxD.exists() && auxD.isDirectory()) {
				String[] entries = auxD.list();
				for (String s : entries) {
					File currentFile = new File(auxD.getPath(), s);
					currentFile.delete();
				}
				auxD.delete();
			} else {
				_ec.printError(ErrorType.DIRECTORYNOTFOUND);
			}
		}
		_lastEntry = EntryType.DELETEDIR;
		auxD = null;
	}

	// Delete selected files
	private void commandDELETEFILE(String[] parameterArray) {
		File auxD;
		for (String parameter : parameterArray) {
			auxD = new File(parameter);
			if (auxD.exists() && auxD.isDirectory()) {
				auxD.delete();
			} else {
				_ec.printError(ErrorType.FILENOTFOUND);
			}
		}
	}

	// Sort the elements of a directory of the selected form
	private void commandSORTBY(SortType sortType) {
		String[] entries = _currentDirectory.list();
		String auxS;
		switch (sortType) {
		case NAME:
			for (int i = 0; i < entries.length; i++) {
				for (int y = i + 1; y < entries.length; y++) {
					if (entries[i].compareTo(entries[y]) > 0) {
						auxS = entries[i];
						entries[i] = entries[y];
						entries[y] = auxS;
					}
				}
			}
			break;
		case DATE:
			File[] entriesFiles = new File[entries.length];
			File auxD;
			for (int i = 0; i < entries.length; i++) {
				entriesFiles[i] = new File(entries[i]);
			}
			for (int i = 0; i < entries.length; i++) {
				for (int y = i + 1; y < entries.length; y++) {
					if (entriesFiles[i].lastModified() < entriesFiles[y].lastModified()) {
						auxS = entries[i];
						entries[i] = entries[y];
						entries[y] = auxS;

						auxD = entriesFiles[i];
						entriesFiles[i] = entriesFiles[y];
						entriesFiles[y] = auxD;
					}
				}
			}
			break;
		}
		if (entries != null && entries.length > 0) {
			for (String item : entries) {
				System.out.println(">> " + item);
			}
		} else {
			_ec.printError(ErrorType.DIRECTORYEMPTY);
		}
		_lastEntry = EntryType.SORTBY;
		entries = null;
		auxS = null;
	}

	// Let the customer change the idiom
	private void commandCHANGEIDIOM(Idiom idiom) {
		_idiom = idiom;
		_connector.changeIdiom(idiom);
		System.out.println(_connector.getLiteral("changeidiom") + "\n");
		_lastEntry = EntryType.CHANGEIDIOM;
	}

	// a command that cleans all logs
	private void commandCLEARLOG() {
		_connector.dropLog();
		System.out.println(_connector.getLiteral("clearlog"));
		_lastEntry = EntryType.CLEARLOG;
	}

	// Run a user-given script
	private void commandSCRIPT(String parameter) {
		var scriptScanner = new Scanner(parameter);
		String p;
		TreatEntry te;
		while (scriptScanner.hasNextLine()) {
			p = scriptScanner.nextLine();
			te = new TreatEntry(p);
			this.selectCommand(te);
		}
		scriptScanner.close();
		System.out.println(_connector.getLiteral("script") + "\n");
		_lastEntry = EntryType.SCRIPT;
	}

	// He tells the program to stop the while and finish the program
	private void commandEXIT() {
		System.out.println(_connector.getLiteral("exit") + "\n");
		_lastEntry = EntryType.EXIT;
	}

	// Print the last error made
	private void commandERROR(ErrorType error) {
		_ec.printError(error);
		_lastEntry = EntryType.ERROR;
	}
}
