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
import org.ccpm.dpj.entity.Configuracion;
import org.ccpm.dpj.entity.Usuario;
import org.ccpm.dpj.facade.ConfiguracionFacadeLocal;

/**
 *
 * @author Matias Zakowicz
 */
@ManagedBean
@RequestScoped
public class ConfiguracionManagedBean extends UtilManagedBean implements Serializable{

    @EJB
    private ConfiguracionFacadeLocal configuracionFacade;
    private int totaldiasReserva;   
    private List <ActionItem> lstActionItems = new ArrayList <ActionItem>();
    
    /** Creates a new instance of ConfiguracionManagedBean */
    public ConfiguracionManagedBean() {
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
                    if (accionAux.getNombre().equalsIgnoreCase("nuevoConfiguracion")) {
                        this.setAlta(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("editarConfiguracion")) {
                        this.setModificacion(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("borrarConfiguracion")) {
                        this.setBaja(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("detalleConfiguracion")) {
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

    public int getTotaldiasReserva() {
        return totaldiasReserva;
    }

    public void setTotaldiasReserva(int totaldiasReserva) {
        this.totaldiasReserva = totaldiasReserva;
    }

   

    
    
   @Override
    public List <Configuracion> getListElements() {
        
            this.setList(this.configuracionFacade.findAll());
        
        return (List <Configuracion>) this.getList();
    }

    

   


    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idConfiguracionAux = Long.parseLong(myRequest.getParameter("id"));
        Configuracion configuracionAux = this.configuracionFacade.find(idConfiguracionAux);
        this.setId(configuracionAux.getId());
        this.setTotaldiasReserva(configuracionAux.getTotalDiasReserva());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest)context.getExternalContext().getRequest();
            Long idConfiguracionAux = Long.parseLong(myRequest.getParameter("id"));
            this.configuracionFacade.remove(idConfiguracionAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("configuracionConf");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorConfiguracion");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idConfiguracionAux = Long.parseLong(myRequest.getParameter("id"));
        Configuracion configuracionAux = this.configuracionFacade.find(idConfiguracionAux);
        this.setId(configuracionAux.getId());
        this.setTotaldiasReserva(configuracionAux.getTotalDiasReserva());   
    }

    @Override
    public String guardarEdicion() {
        try {
            this.configuracionFacade.edit(this.getId(), this.getTotaldiasReserva());
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.setResultado("successErrorProrroga");
            this.setMsgSuccessError("La configuración ha sida editada con éxito");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorProrroga");
        }
        return this.getResultado();
    }

    @Override
    public List <SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }  

    @Override
    public void limpiar() {
        
    }

    @Override
    public String crear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String crearOtro() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
