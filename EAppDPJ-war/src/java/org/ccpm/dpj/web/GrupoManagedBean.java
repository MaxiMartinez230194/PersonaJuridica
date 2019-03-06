/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.ccpm.dpj.entity.ActionItem;
import org.ccpm.dpj.entity.Grupo;
import org.ccpm.dpj.entity.Usuario;
import org.ccpm.dpj.facade.GrupoFacadeLocal;

/**
 *
 * @author angel
 */
@ManagedBean
@RequestScoped
public class GrupoManagedBean extends UtilManagedBean implements Serializable{
    @EJB
    private GrupoFacadeLocal grupoFacade;
    private String nombre;
    private String descripcion;
    private List <ActionItem> lstActionItems = new ArrayList <ActionItem>();
    
    /** Creates a new instance of GrupoManagedBean */
    public GrupoManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.prepararAcciones(sessionBean.getUsuario());
            } catch (Exception e) { }
        }
    }
    
    public void prepararAcciones(Usuario usuario) throws Exception {
        try {
            List <Grupo> listGrupo = usuario.getGrupos();
            if (!(listGrupo.isEmpty())) {
                for (Grupo grupoAux : listGrupo) {
                    this.lstActionItems.addAll(grupoAux.getAcciones());
                }
            }
            this.lstActionItems.addAll(usuario.getAcciones());
            if (!(lstActionItems.isEmpty())) {
                for (ActionItem accionAux : lstActionItems) {
                    if (accionAux.getNombre().equalsIgnoreCase("nuevoGrupo")) {
                        this.setAlta(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("editarGrupo")) {
                        this.setModificacion(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("borrarGrupo")) {
                        this.setBaja(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("detalleGrupo")) {
                        this.setDetalle(true);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Error al recuperar las acciones del usuario");
        }
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setLstActionItems(List<ActionItem> lstActionItems) {
        this.lstActionItems = lstActionItems;
    }

    public List<ActionItem> getLstActionItems() {
        return lstActionItems;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getNombre() {
        return nombre;
    }
    
    @Override
    public List <Grupo> getListElements() {
        if (this.getNombreBuscar().equals("")) {
            this.setList(grupoFacade.findAll(true));
        } else {
            this.setList(grupoFacade.findAll(this.getNombreBuscar().toUpperCase()));
        }
        return (List <Grupo>) this.getList();
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
        this.setDescripcion(null);
    }

    @Override
    public String crear() {
        try {
            grupoFacade.create(this.getNombre(), this.getDescripcion());
            this.setResultado("successErrorGrupo");
            this.setMsgSuccessError("El grupo ha sido generado con éxito");
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.limpiar();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorGrupo");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            grupoFacade.create(this.getNombre(), this.getDescripcion());
            this.setResultado("nuevoGrupo");
            this.limpiar();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorGrupo");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idGrupoAux = Long.parseLong(myRequest.getParameter("id"));
        Grupo grupoAux = this.grupoFacade.find(idGrupoAux);
        this.setId(grupoAux.getId());
        this.setNombre(grupoAux.getNombre());
        this.setDescripcion(grupoAux.getDescripcion());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest)context.getExternalContext().getRequest();
            Long idGrupoAux = Long.parseLong(myRequest.getParameter("id"));
            this.grupoFacade.remove(idGrupoAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("grupoConf");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorAcciones");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idGrupoAux = Long.parseLong(myRequest.getParameter("id"));
        Grupo grupoAux = this.grupoFacade.find(idGrupoAux);
        this.setId(grupoAux.getId());
        this.setNombre(grupoAux.getNombre());
        this.setDescripcion(grupoAux.getDescripcion());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.grupoFacade.edit(this.getId(), this.getNombre(), this.getDescripcion());
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.setResultado("successErrorGrupo");
            this.setMsgSuccessError("El grupo ha sido editado con éxito");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorGrupo");
        }
        return this.getResultado();
    }

    @Override
    public List <SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }  

}

