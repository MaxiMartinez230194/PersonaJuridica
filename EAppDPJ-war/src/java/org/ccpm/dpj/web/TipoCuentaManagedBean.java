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
import org.ccpm.dpj.entity.TipoCuenta;
import org.ccpm.dpj.entity.Usuario;
import org.ccpm.dpj.facade.TipoCuentaFacadeLocal;

/**
 *
 * @author Alvarenga Angel
 */
@ManagedBean
@RequestScoped
public class TipoCuentaManagedBean extends UtilManagedBean implements Serializable{
    @EJB
    private TipoCuentaFacadeLocal tipoCuentaFacade;
    private String nombre;   
    private List <ActionItem> lstActionItems = new ArrayList <ActionItem>();
    
    /** Creates a new instance of TipoCuentaManagedBean */
    public TipoCuentaManagedBean() {
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
                    if (accionAux.getNombre().equalsIgnoreCase("nuevoTipoCuenta")) {
                        this.setAlta(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("editarTipoCuenta")) {
                        this.setModificacion(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("borrarTipoCuenta")) {
                        this.setBaja(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("detalleTipoCuenta")) {
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

    public void setLstActionItems(List<ActionItem> lstActionItems) {
        this.lstActionItems = lstActionItems;
    }

    public List<ActionItem> getLstActionItems() {
        return lstActionItems;
    }    

    public String getNombre() {
        return nombre;
    }
    
    @Override
    public List <TipoCuenta> getListElements() {
        if (this.getNombreBuscar().equals("")) {
            this.setList(this.tipoCuentaFacade.findAll(true));
        } else {
            this.setList(this.tipoCuentaFacade.findAll(this.getNombreBuscar().toUpperCase()));
        }
        return (List <TipoCuenta>) this.getList();
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
    }

    @Override
    public String crear() {
        try {
            this.tipoCuentaFacade.create(this.getNombre());
            this.setResultado("successErrorTipoCuenta");
            this.setMsgSuccessError("El tipo de cuenta ha sido generado con éxito");
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.limpiar();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoCuenta");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.tipoCuentaFacade.create(this.getNombre());
            this.setResultado("nuevoTipoCuenta");
            this.limpiar();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoCuenta");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoCuentaAux = Long.parseLong(myRequest.getParameter("id"));
        TipoCuenta tipoCuentaAux = this.tipoCuentaFacade.find(idTipoCuentaAux);
        this.setId(tipoCuentaAux.getId());
        this.setNombre(tipoCuentaAux.getNombre());        
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest)context.getExternalContext().getRequest();
            Long idTipoCuentaAux = Long.parseLong(myRequest.getParameter("id"));
            this.tipoCuentaFacade.remove(idTipoCuentaAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("tipoCuentaConf");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoCuenta");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoCuentaAux = Long.parseLong(myRequest.getParameter("id"));
        TipoCuenta tipoCuentaAux = this.tipoCuentaFacade.find(idTipoCuentaAux);
        this.setId(tipoCuentaAux.getId());
        this.setNombre(tipoCuentaAux.getNombre());        
    }

    @Override
    public String guardarEdicion() {
        try {
            this.tipoCuentaFacade.edit(this.getId(), this.getNombre());
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.setResultado("successErrorTipoCuenta");
            this.setMsgSuccessError("El tipo de cuenta ha sido editado con éxito");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoCuenta");
        }
        return this.getResultado();
    }

    @Override
    public List <SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }  

}
