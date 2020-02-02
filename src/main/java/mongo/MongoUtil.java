package mongo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Projections.*;

import browser.TreatEntry.Idiom;

import org.bson.Document;

import errorControl.ErrorControl.ErrorType;
import hibernate.mapping.literal;

public class MongoUtil {
	MongoClient _mongoClient;
	MongoDatabase _database;
	String _idiom;
	
	// A method for change the idiom of research in the querys
	public void changeIdiom(Idiom idiom) {
		switch (idiom) {
		case SPANISH:
			this._idiom = "esp";
			break;
		case ENGLISH:
			this._idiom = "eng";
			break;
		}
	}
			
	//The constructor of the class
	public MongoUtil(Idiom idiom){
		changeIdiom(idiom);
		_mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		_database = _mongoClient.getDatabase("directorybrowser");
	}
	
	// A method to get the text from the literal collection
	public String getLiteral(String clau) {
		MongoCollection<Document> collection = _database.getCollection("literal");
		Document doc = collection.find(and(eq("idi_cod", _idiom), eq("lit_clau", clau))).projection(fields(include("lit_text"), excludeId())).first();
		String result = doc.get("lit_text").toString();
		return result;
	}
	
	// A method to get the text from the error collection
	public String getError(String clau) {
		MongoCollection<Document> collection = _database.getCollection("error");
		Document doc = collection.find(and(eq("idi_cod", _idiom), eq("err_clau", clau))).projection(fields(include("err_text"), excludeId())).first();
		String result = doc.get("err_text").toString();
		return result;
	}
	
	// A method for save the logs
	public void uploadLog(Timestamp timestamp, String command, String parameters) {
		MongoCollection<Document> collection = _database.getCollection("log");
		Document log = new Document("timestamp", (Date) timestamp)
				.append("command", command)
				.append("parameters", parameters);
		collection.insertOne(log);
	}
	
	// A method for delete all the logs
	public void dropLog() {
		MongoCollection<Document> collection = _database.getCollection("log");
		collection.deleteMany(new Document());
	}
	
	public void shutdown(){
		_mongoClient.close();
	}
}
