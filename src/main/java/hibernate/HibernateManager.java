package hibernate;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Session;

import browser.TreatEntry.Idiom;
import errorControl.ErrorControl;
import errorControl.ErrorControl.ErrorType;
import hibernate.mapping.error;
import hibernate.mapping.literal;
import hibernate.mapping.log;

public class HibernateManager {
	private Session session;
	private String idiom;
	private ErrorControl ec;
	
	// A method for change the idiom of research in the querys
		public void changeIdiom(Idiom idiom) {
			switch (idiom) {
			case SPANISH:
				this.idiom = "esp";
				break;
			case ENGLISH:
				this.idiom = "eng";
				break;
			}
		}

		// The constructor of the class
		public HibernateManager(Idiom idiom) {
			//ec = new ErrorControl(this);
			session = HibernateUtil.getSessionFactory().openSession();
			changeIdiom(idiom);
		}
		
		// Method for open the session
		public void turnon() {
			if (!session.isOpen()) {
				session = HibernateUtil.getSessionFactory().openSession();
			}else {
				ec.printError(ErrorType.SESSIONOPEN);
			}
		}
		
		// Method for close the session
		public void shutdown() {
			if(session.isOpen()) {
				session.close();
			}else {
				ec.printError(ErrorType.SESSIONCLOSE);
			}
		}

		// A method to get the text from the literal table
		public String getLiteral(String clau) {
			if (session != null) {
				List<literal> result = (List<literal>) (session
						.createQuery("from literal where idi_cod like '" + this.idiom + "' and lit_clau like '" + clau + "'")
						.list());
				if (!result.isEmpty()) {
					return result.get(0).getText();
				} else {
					ec.printError(ErrorType.SEARCHNULL);
					return "";
				}
			} else {
				ec.printError(ErrorType.SESSIONNULL);
				return "";
			}
		}

		// A method to get the text from the literal table
		public String getError(String clau) {
			if (session != null) {
				List<error> result = (List<error>) (session
						.createQuery("from error where idi_cod like '" + this.idiom + "' and err_clau like '" + clau + "'")
						.list());
				if (!result.isEmpty()) {
					return result.get(0).getText();
				} else {
					System.err.println("ERROR: The session is closed");
					return "";
				}
			} else {
				ec.printError(ErrorType.SESSIONNULL);
				return "";
			}
		}

		// A method for get all the logs
		private List<log> getLog() {
			if (session != null) {
				List<log> result = (List<log>) (session
						.createQuery("from log")
						.list());
				if (!result.isEmpty()) {
					return result;
				} else {
					ec.printError(ErrorType.SEARCHNULL);
					return null;
				}
			} else {
				ec.printError(ErrorType.SESSIONNULL);
				return null;
			}
		}
		// A method for save the logs
		public void uploadLog(Timestamp timestamp, String command, String parameters) {
			session.beginTransaction();
			log l = new log(timestamp, command, parameters);
			session.save(l);
			session.getTransaction().commit();
		}
		
		//  A method for delete all the logs
		public void dropLog(){
			if (session != null) {
				session.beginTransaction();
				session.createSQLQuery("TRUNCATE TABLE log").executeUpdate();
				session.getTransaction().commit();
				session.clear();
			} else {
				ec.printError(ErrorType.SESSIONNULL);
			}	
		}
}
