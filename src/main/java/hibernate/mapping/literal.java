package hibernate.mapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "literal")

public class literal {
    @Id
    @Column(name = "lit_id")
    private int _id;
    @Column(name = "idi_cod")
    private String _idioma;
    @Column (name = "lit_clau")
    private String _clau;
    @Column (name = "lit_text")
    private String _text;
    
    public literal(){
        
    }
    
    public literal(int id, String idioma, String clau, String text){
        this.setId(id);
        this.setIdioma(idioma);
        this.setClau(clau);
        this.setText(text);
    }
    
    public int getId(){
        return _id;
    }
    
    public void setId(int id){
        this._id = id;
    }
    
    public String getIdioma(){
        return this._idioma;
    }
    
    public void setIdioma(String idioma){
        this._idioma = idioma;
    }
    
    public String getClau(){
        return _clau;
    }
    
    public void setClau(String clau){
        this._clau = clau;
    }
    
    public String getText(){
        return _text;
    }
    
    public void setText(String text){
        this._text = text;
    }
}
