package errorControl;


//import hibernate.HibernateManager;
import mongo.MongoUtil;

public class ErrorControl {
	
	//The connector to obtain the error texts with hibernate
	//private MongoUtil _connector;
	
	//The connector to obtain the error texts with mongo
	private MongoUtil _connector;
	
	//The different types of errors contemplated by the program.
	public static enum ErrorType{
		PARAMETERNUMBER,		//Incorrect number of parameters
		SORTTYPEUNKNOWN,		//An invalid sort method has been used
		IDIOMUNKNOWN,			//The idiom selected is unknown
		PARAMETERNULL,			//No parameters have been entered
		SESSIONNULL,			//No open session of hibernate found
		SESSIONOPEN,			//The session is already open
		SESSIONCLOSE,			//The session is already close
		SEARCHNULL,				//The search has not worked
		DIRECTORYNOTFOUND,		//Directory not found
		FILENOTFOUND,			//File not found
		COMMANDNOTFOUND,		//Command not found
		DIRECTORYEXIST,			//The directory already exist
		FILEEXIST,				//The file already exist
		DIRECTORYEMPTY,			//The directory is empty
		NOMOREUP				//The user has tried to up higher than possible in the root of directories
	}

	//The class builder
	public ErrorControl (MongoUtil connector) {
		this._connector = connector;
	}
	//It will return a String according to the type of error that is given.
	public String getErrorText(ErrorType type) {
		String text = "";
		text = "ERROR:";
		
		switch(type) {
		case PARAMETERNUMBER:
			text = text+_connector.getError("parameternumber");
			break;
		case SORTTYPEUNKNOWN:
			text = text+_connector.getError("sorttypeunknown");
			break;
		case IDIOMUNKNOWN:
			text = text+_connector.getError("idiomunknown");
			break;
		case PARAMETERNULL:
			text = text+_connector.getError("parameternull");
			break;
		case SESSIONNULL:
			text = text+_connector.getError("sessionnull");
			break;
		case SESSIONOPEN:
			text = text+_connector.getError("sessionopen");
			break;
		case SESSIONCLOSE:
			text = text+_connector.getError("sessionclose");
			break;
		case SEARCHNULL:
			text = text+_connector.getError("searchnull");
			break;
		case DIRECTORYNOTFOUND:
			text = text+_connector.getError("directorynotfound");
			break;
		case FILENOTFOUND:
			text = text+_connector.getError("filenotfound");
			break;
		case COMMANDNOTFOUND:
			text = text+_connector.getError("commandnotfound");
			break;
		case DIRECTORYEXIST:
			text = text+_connector.getError("directoryexist");
			break;
		case FILEEXIST:
			text = text+_connector.getError("fileexist");
			break;
		case DIRECTORYEMPTY:
			text = text+_connector.getError("directoryempty");
			break;
		case NOMOREUP:
			text = text+_connector.getError("nomoreup");
			break;
		default:
			break;
		}
		
		return text;
	}
	
	//It will print the error in the console
	public void printError(ErrorType type) {
		System.err.println(getErrorText(type));
	}
}
