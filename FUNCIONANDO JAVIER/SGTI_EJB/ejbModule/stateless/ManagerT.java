package stateless;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import beans.Estado;
import beans.Grupo;
import beans.Realiza;
import beans.Tarea;
import beans.Tiene;
import beans.Tipo;
import beans.Usuario;

import manager.ManagerTarea;


@Stateless
public class ManagerT implements TareaRemote {
	
	@PersistenceContext(unitName = "SGTI_JPA")
	private EntityManager em;
	//ManagerTarea mt= new ManagerTarea();//manager Tarea del jpa que maneja los beans
	
		
	public boolean agregarTarea(Tarea t, Tipo tipo, Tiene tiene) {
		try {
			
			em.persist(tipo);
			em.persist(tiene);
			em.persist(t);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public List<Tarea> traerTodasTareas() {
		List<Tarea> todasT = em.createNamedQuery("todosTareas").getResultList();
		return todasT;
		
	}
	public Tarea encontrarTarea(int id) {
		Tarea t = em.find(Tarea.class, id);
		return t;
	}
	public List<Tarea> tareasPorUsuario(Usuario u) {
		//List<Tarea> tareas = em.createNativeQuery("tareasPorUsuario").setParameter(1, u.getCedula()).getResultList();
		String ORG_QUERY = "SELECT * FROM Tarea T join Realiza R on T.id=R.tarea_id where ((R.usu_cedula = ?))";		
		
		List<Tarea> tareas = em.createNativeQuery(ORG_QUERY, Tarea.class).setParameter(1, u.getCedula()).getResultList();
		return tareas;
	}
	public Tarea actualizarTarea(Tarea t) {
		t = em.merge(t);

		return t;
	}
	public boolean eliminarTarea(Tarea t) {

		try{
			em.remove(t);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}	
	}
	public boolean altaTareaRealiza(Tarea t, Realiza r) {

		try{
		em.persist(t);
		em.persist(r);
		return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean asignaTareaUsuario(Tarea t, Usuario u, Calendar fecIni) {
		boolean retorno = false;
		Realiza r = new Realiza();
		r.setTarea(t);
		r.setUsu(u);
		r.setFechaInicio(fecIni);

		if (t.agregarRealiza(r)) {
			actualizarTarea(em, t);
			retorno = true;
		}

		return retorno;
		
	}
	public boolean altaGrupo(Grupo gr) {
		try {

			em.persist(gr);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public Grupo encontrarGrupo(int id) {
		Grupo gru = em.find(Grupo.class, id);
		return gru;
	}
	public Grupo actualizarGrupo(Grupo gr) {
		gr = em.merge(gr);

		return gr;
	}
	// ASIGNAR TAREA A GRUPO
	// pasar un TIENE para que cambie el estado de la tarea
	// seria el caso de cuando se abre la tarea y se la asigna a un grupo
	// se pasa un tiene porque la tarea queda en estado ABIERTA.
	public boolean asignarTareaGrupo(Tarea t, Grupo gr) {
		boolean retorno = false;
		if (gr.asignaTarea(t))
			actualizarGrupo(em, gr);
		retorno = true;

		return retorno;
	}
	public Estado encontrarEstado(int id) {
		Estado e = em.find(Estado.class, id);
		return e;
	}
	// OBTIENE EL TIENE SIN FINALIZAR DE UNA TAREA
	public Tiene tieneDeTarea(Tarea t) {
		List<Tiene> colTiene = t.getColTiene();
		Tiene tiene = null;
		Tiene tieneIt = null;
		Iterator iter = colTiene.iterator();
		while (iter.hasNext()) {
			tieneIt = (Tiene) iter.next();
			if (tieneIt.getFechaFin() == null) {
				tiene = tieneIt;

			}

		}
		return tiene;
	}
	public boolean cambiarEstadoTarea(Tarea t, Estado est) {
		boolean retorno = false;
		Tiene ti = tieneDeTarea(t);
		if (ti != null) {
			ti.setFechaFin(Calendar.getInstance());
			actualizarTarea(em, t);
			Tiene tiene2 = new Tiene();
			tiene2.setFechaInicio(Calendar.getInstance());
			// tiene2.setEstado(encontrarEstado(1));
			tiene2.setEstado(est);
			t.agregarTiene(tiene2);
			actualizarTarea(em, t);
			retorno = true;
		}

		return retorno;
	}
	public boolean agregarEstado(Estado est) {
		try{
			em.persist(est);
			return true;
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		
	}
	public Grupo actualizarGrupo(EntityManager em, Grupo gr) {

		gr = em.merge(gr);

		return gr;
	}
	public Tarea actualizarTarea(EntityManager em, Tarea t) {

		t = em.merge(t);

		return t;
	}
	
	
}
