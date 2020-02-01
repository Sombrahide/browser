package browser;

import errorControl.ErrorControl.ErrorType;

public class TreatEntry {
	
	//An enum for the possible actions that we can perform in our program, including an enum for cases in which we have an error
    public enum EntryType {
        GOTO,
        GOLAST,
        LIST,
        UP,
        INFOFILE,
        INFODIR,
        HELP,
        CREATEDIR,
        CREATEFILE,
        SORTBY,
        DELETEDIR,
        DELETEFILE,
        CHANGEIDIOM,
        SCRIPT,
        CLEARLOG,
        ERROR,
        EXIT;
    }
    
    //Another enum for different types of management
    public enum SortType {
        NAME,
        DATE;
    }
    
    //The idioms of the program
    public enum Idiom{
    	SPANISH,
    	ENGLISH
    }
    
    
    private String _entry;
    private ErrorType _error;
    private EntryType _entryType;
    private Idiom _idiom;
    private SortType _sortType;
    private String[] _parameters;

    //It will treat the given parameters, transforming the first one into an EntryType, 
    //the rest returning them into an array of strings and checking that there is no error with the number of parameters
    private void loadObject() {
        String auxText = this._entry;

        if (auxText != null && !auxText.isEmpty()) {
            String[] elements = auxText.split(" ");
            elements[0] = elements[0].toLowerCase();

            switch (elements[0]) {
                case "goto":
                    if (elements.length == 2) {
                        this._parameters = new String[1];
                        this._parameters[0] = elements[1];
                        this._entryType = EntryType.GOTO;
                    } else {
                        this._entryType = EntryType.ERROR;
                        this._error = ErrorType.PARAMETERNUMBER;
                    }
                    break;
                case "golast":
                    if (elements.length == 1) {
                        this._entryType = EntryType.GOLAST;
                    } else {
                        this._entryType = EntryType.ERROR;
                        this._error = ErrorType.PARAMETERNUMBER;
                    }
                    break;
                case "list":
                    if (elements.length == 1) {
                        this._entryType = EntryType.LIST;
                        this._parameters = new String[1];
                        this._parameters[0] = null;
                    } else if (elements.length == 2) {
                        this._entryType = EntryType.LIST;
                        this._parameters = new String[1];
                        this._parameters[0] = elements[1];
                    } else {
                        this._entryType = EntryType.ERROR;
                        this._error = ErrorType.PARAMETERNUMBER;
                    }
                    break;
                case "up":
                    if (elements.length == 1) {
                        this._entryType = EntryType.UP;
                    } else {
                        this._entryType = EntryType.ERROR;
                        this._error = ErrorType.PARAMETERNUMBER;
                    }
                break;
                case "infofile":
                    if (elements.length > 1) {
                        this._entryType = EntryType.INFOFILE;
                        this._parameters = new String[elements.length - 1];
                        for (int i = 0; i < _parameters.length; i++) {
                            this._parameters[i] = elements[i + 1];
                        }
                    } else {
                        this._entryType = EntryType.ERROR;
                        this._error = ErrorType.PARAMETERNUMBER;
                    }
                    break;
                case "infodir":
                    if (elements.length > 1) {
                        this._entryType = EntryType.INFODIR;
                        this._parameters = new String[elements.length - 1];
                        for (int i = 0; i < _parameters.length; i++) {
                            this._parameters[i] = elements[i + 1];
                        }
                    } else {
                        this._entryType = EntryType.ERROR;
                        this._error = ErrorType.PARAMETERNUMBER;
                    }
                    break;
                case "help":
                    if (elements.length == 1) {
                        this._entryType = EntryType.HELP;
                        this._parameters = new String[1];
                        this._parameters[0] = null;
                    } else if (elements.length == 2) {
                        this._entryType = EntryType.HELP;
                        this._parameters = new String[1];
                        this._parameters[0] = elements[1].toLowerCase();
                    } else {
                        this._entryType = EntryType.ERROR;
                        this._error = ErrorType.PARAMETERNUMBER;
                    }
                    break;
                case "createdir":
                    if (elements.length > 1) {
                        this._entryType = EntryType.CREATEDIR;
                        this._parameters = new String[elements.length - 1];
                        for (int i = 0; i < _parameters.length; i++) {
                            this._parameters[i] = elements[i + 1];
                        }
                    } else {
                        this._entryType = EntryType.ERROR;
                        this._error = ErrorType.PARAMETERNUMBER;
                    }
                    break;
                case "createfile":
                    if (elements.length > 1) {
                        this._entryType = EntryType.CREATEFILE;
                        this._parameters = new String[elements.length - 1];
                        for (int i = 0; i < _parameters.length; i++) {
                            this._parameters[i] = elements[i + 1];
                        }
                    } else {
                        this._entryType = EntryType.ERROR;
                        this._error = ErrorType.PARAMETERNUMBER;
                    }
                    break;
                case "sortby":
                    if (elements.length == 2) {
                        this._entryType = EntryType.SORTBY;
                        elements[1] = elements[1].toLowerCase();
                        switch (elements[1]) {
                            case ("name"):
                                _sortType = SortType.NAME;
                                break;
                            case ("date"):
                                _sortType = SortType.DATE;
                                break;
                            default:
                            	this._entryType = EntryType.ERROR;
                                this._error = ErrorType.SORTTYPEUNKNOWN;
                                break;
                        }
                    } else {
                        this._entryType = EntryType.ERROR;
                        this._error = ErrorType.PARAMETERNUMBER;
                    }
                    break;
                case "deletedir":
                    if (elements.length > 1) {
                        this._entryType = EntryType.DELETEDIR;
                        this._parameters = new String[elements.length - 1];
                        for (int i = 0; i < _parameters.length; i++) {
                            this._parameters[i] = elements[i + 1];
                        }
                    } else {
                        this._entryType = EntryType.ERROR;
                        this._error = ErrorType.PARAMETERNUMBER;
                    }
                    break;
                case "deletefile":
                    if (elements.length > 1) {
                        this._entryType = EntryType.DELETEFILE;
                        this._parameters = new String[elements.length - 1];
                        for (int i = 0; i < _parameters.length; i++) {
                            this._parameters[i] = elements[i + 1];
                        }
                    } else {
                        this._entryType = EntryType.ERROR;
                        this._error = ErrorType.PARAMETERNUMBER;
                    }
                    break;
                case "changeidiom":
                    if (elements.length == 2){
                        this._entryType = EntryType.CHANGEIDIOM;
                        this._parameters = new String[elements.length - 1];
                        this._parameters[0] = elements[1];
                    
                        elements[1] = elements[1].toLowerCase();
                        switch (elements[1]) {
                            case ("esp"):
                                _idiom = Idiom.SPANISH;
                                break;
                            case ("eng"):
                                _idiom = Idiom.ENGLISH;
                                break;
                            default:
                            	this._entryType = EntryType.ERROR;
                                this._error = ErrorType.IDIOMUNKNOWN;
                                break;
                        }
                    } else {
                        this._entryType = EntryType.ERROR;
                        this._error = ErrorType.PARAMETERNUMBER;
                    }
                    break;
                case "clearlog":
                	if (elements.length == 1) {
                        this._entryType = EntryType.CLEARLOG;
                    } else {
                        this._entryType = EntryType.ERROR;
                        this._error = ErrorType.PARAMETERNUMBER;
                    }
                    break;
                case "script":
                    if (elements.length == 1){
                        this._entryType = EntryType.SCRIPT;
                        this._parameters = new String[elements.length - 1];
                        this._parameters[0] = elements[1];}
                    else {
                        this._entryType = EntryType.ERROR;
                        this._error = ErrorType.PARAMETERNUMBER;
                    }
                    break;
                case "exit":
                    if (elements.length == 1) {
                        this._entryType = EntryType.EXIT;
                    } else {
                        this._entryType = EntryType.ERROR;
                        this._error = ErrorType.PARAMETERNUMBER;
                    }
                    break;
                default:
                    this._entryType = EntryType.ERROR;
                    this._error = ErrorType.COMMANDNOTFOUND;
                    break;
            }
        } else {
            this._entryType = EntryType.ERROR;
            this._error = ErrorType.PARAMETERNULL;
        }
    }

    //It will return the type of command
    public EntryType obtainEntryType() {
        return this._entryType;
    }
    
    //It will return the command in string and lower case
    public String obtainCommand(){
    	String auxText = this._entry;
        String[] elements = auxText.split(" ");
    	return elements[0].toLowerCase();
    }
    
    //It will return an array of command parameters
    public String[] obtainParameters() {
        return this._parameters;
    }

    //Will return the sort method
    public SortType obtainSortType() {
        return this._sortType;
    }
    
    //Will return the idiom enum
    public Idiom obtainIdiom() {
    	return this._idiom;
    }

    //It will return the LAST error generated
    public ErrorType obtainError() {
        return _error;
    }

    //This is the constructor of the class, you must enter the text to be treated (in this case, the command and its parameters)
    public TreatEntry(String entry) {
        this._entry = entry;
        loadObject();
    }
}
