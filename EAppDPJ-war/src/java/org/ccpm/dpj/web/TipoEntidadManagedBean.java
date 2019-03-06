/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import org.ccpm.dpj.entity.TipoEntidad;
import org.ccpm.dpj.entity.Usuario;
import org.ccpm.dpj.facade.TipoEntidadFacadeLocal;

/**
 *
 * @author Matias Zakowicz
 */
@ManagedBean
@RequestScoped
public class TipoEntidadManagedBean extends UtilManagedBean implements Serializable{

    @EJB
    private TipoEntidadFacadeLocal tipoEntidadFacade;
    private String leyenda;   
    private List <ActionItem> lstActionItems = new ArrayList <ActionItem>();
    
    /** Creates a new instance of TipoEntidadManagedBean */
    public TipoEntidadManagedBean() {
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
                    if (accionAux.getNombre().equalsIgnoreCase("nuevoTipoEntidad")) {
                        this.setAlta(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("editarTipoEntidad")) {
                        this.setModificacion(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("borrarTipoEntidad")) {
                        this.setBaja(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("detalleTipoEntidad")) {
                        this.setDetalle(true);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Error al recuperar las acciones del usuario");
        }
    }

    public void setLstActionItems(List<ActionItem> lstActionItems) {
        this.lstActionItems = lstActionItems;
    }

    public List<ActionItem> getLstActionItems() {
        return lstActionItems;
    }    

    public String getLeyenda() {
        return leyenda;
    }

    public void setLeyenda(String leyenda) {
        this.leyenda = leyenda;
    }

    
    
    @Override
    public List <TipoEntidad> getListElements() {
        if (this.getNombreBuscar().equals("")) {
            this.setList(this.tipoEntidadFacade.findAll(true));
        } else {
            this.setList(this.tipoEntidadFacade.findAll(this.getNombreBuscar().toUpperCase()));
        }
        return (List <TipoEntidad>) this.getList();
    }

    @Override
    public void limpiar() {
        this.setLeyenda(null);
    }

    @Override
    public String crear() {
        try {
            this.tipoEntidadFacade.create(this.getLeyenda());
            this.setResultado("successErrorTipoEntidad");
            this.setMsgSuccessError("El tipo de entidad ha sido generado con éxito");
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.limpiar();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoEntidad");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.tipoEntidadFacade.create(this.getLeyenda());
            this.setResultado("nuevoTipoEntidad");
            this.limpiar();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoEntidad");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoEntidadAux = Long.parseLong(myRequest.getParameter("id"));
        TipoEntidad tipoEntidadAux = this.tipoEntidadFacade.find(idTipoEntidadAux);
        this.setId(tipoEntidadAux.getId());
        this.setLeyenda(tipoEntidadAux.getLeyenda());        
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest)context.getExternalContext().getRequest();
            Long idTipoEntidadAux = Long.parseLong(myRequest.getParameter("id"));
            this.tipoEntidadFacade.remove(idTipoEntidadAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("tipoEntidadConf");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoEntidad");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoEntidadAux = Long.parseLong(myRequest.getParameter("id"));
        TipoEntidad tipoEntidadAux = this.tipoEntidadFacade.find(idTipoEntidadAux);
        this.setId(tipoEntidadAux.getId());
        this.setLeyenda(tipoEntidadAux.getLeyenda());        
    }

    @Override
    public String guardarEdicion() {
        try {
            this.tipoEntidadFacade.edit(this.getId(), this.getLeyenda());
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.setResultado("successErrorTipoEntidad");
            this.setMsgSuccessError("El tipo de entidad ha sido editado con éxito");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoEntidad");
        }
        return this.getResultado();
    }

    @Override
    public List <SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }  

}
