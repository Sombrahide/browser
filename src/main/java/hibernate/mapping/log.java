package hibernate.mapping;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "log")

public class log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private int _id;
    @Column(name = "log_timestamp")
    private Timestamp _timestamp;
    @Column(name = "log_command")
    private String _command;
    @Column(name = "log_parameters")
    private String _parameters;
    
    public log(){
        
    }
    
    public log(Timestamp timestamp, String command, String parameters){
        this.setTimestamp(timestamp);
        this.setCommand(command);
        this.setParameters(parameters);
    }
    
    public int getId(){
        return _id;
    }
    
    public void setId(int id){
        this._id = id;
    }
    
    public Timestamp getTimestamp(){
        return this._timestamp;
    }
    
    public void setTimestamp(Timestamp timestamp){
        this._timestamp = timestamp;
    }
    
    public String getCommand(){
    	return this._command;
    }
    
    public void setCommand(String command){
    	this._command = command;
    }
    
    public String getParameters(){
    	return this._parameters;
    }
    
    public void setParameters(String parameters){
    	this._parameters = parameters;
    }
}
