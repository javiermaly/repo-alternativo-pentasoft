package beans;

import java.io.Serializable;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Estado
 *
 */
@Entity
public class Estado implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String descripcion;
	
	@OneToMany(fetch=FetchType.EAGER)
	private List<Estado> colEstadosSgtes=new ArrayList<Estado>();
	
	private static final long serialVersionUID = 1L;

	public Estado() {
		super();
	}   
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}   
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public List<Estado> getColEstadosSgtes() {
		return colEstadosSgtes;
	}
	public void setColEstadosSgtes(List<Estado> colEstadosSgtes) {
		this.colEstadosSgtes = colEstadosSgtes;
	}
	
	public boolean addEstadoSgte(Estado est){
		return colEstadosSgtes.add(est);
	}
	
   
}
