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
import org.ccpm.dpj.entity.TasaServicio;
import org.ccpm.dpj.entity.Usuario;
import org.ccpm.dpj.facade.TasaServicioFacadeLocal;

/**
 *
 * @author Alvarenga Angel
 */
@ManagedBean
@RequestScoped
public class TasaServicioManagedBean extends UtilManagedBean implements Serializable{
    @EJB
    private TasaServicioFacadeLocal tasaFacade;
    private String nombre;
    private Double monto;
    private boolean select;
    private List <ActionItem> lstActionItems = new ArrayList <ActionItem>();
    
    /** Creates a new instance of TasaServicioManagedBean */
    public TasaServicioManagedBean() {
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
                    if (accionAux.getNombre().equalsIgnoreCase("nuevaTasa")) {
                        this.setAlta(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("editarTasa")) {
                        this.setModificacion(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("borrarTasa")) {
                        this.setBaja(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("detalleTasa")) {
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

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public void setLstActionItems(List<ActionItem> lstActionItems) {
        this.lstActionItems = lstActionItems;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isSelect() {
        return select;
    }

    public List<ActionItem> getLstActionItems() {
        return lstActionItems;
    }

    public Double getMonto() {
        return monto;
    }

    public String getNombre() {
        return nombre;
    }
    
    @Override
    public List <TasaServicio> getListElements() {
        if (this.getNombreBuscar().equals("")) {
            this.setList(tasaFacade.findAll(true));
        } else {
            this.setList(tasaFacade.findAll(this.getNombreBuscar().toUpperCase()));
        }
        return (List <TasaServicio>) this.getList();
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
        this.setMonto(null);
    }

    @Override
    public String crear() {
        try {
            this.tasaFacade.create(this.getNombre(), this.getMonto(), this.isSelect());
            this.setResultado("successErrorTasaServicio");
            this.setMsgSuccessError("La tasa de servicio ha sido generada con éxito");
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.limpiar();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTasaServicio");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.tasaFacade.create(this.getNombre(), this.getMonto(), this.isSelect());
            this.setResultado("nuevaTasaServicio");
            this.limpiar();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTasaServicio");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTasaServicioAux = Long.parseLong(myRequest.getParameter("id"));
        TasaServicio tasaServicioAux = this.tasaFacade.find(idTasaServicioAux);
        this.setId(tasaServicioAux.getId());
        this.setNombre(tasaServicioAux.getNombre());
        this.setMonto(tasaServicioAux.getMonto());
        this.setSelect(tasaServicioAux.isVisible());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest)context.getExternalContext().getRequest();
            Long idTasaServicioAux = Long.parseLong(myRequest.getParameter("id"));
            this.tasaFacade.remove(idTasaServicioAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("tasaServicioConf");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTasaServicio");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTasaServicioAux = Long.parseLong(myRequest.getParameter("id"));
        TasaServicio tasaServicioAux = this.tasaFacade.find(idTasaServicioAux);
        this.setId(tasaServicioAux.getId());
        this.setNombre(tasaServicioAux.getNombre());
        this.setMonto(tasaServicioAux.getMonto());
        this.setSelect(tasaServicioAux.isVisible());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.tasaFacade.edit(this.getId(), this.getNombre(), this.getMonto(), this.isSelect());
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.setResultado("successErrorTasaServicio");
            this.setMsgSuccessError("La tasa de servicio ha sido editada con éxito");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTasaServicio");
        }
        return this.getResultado();
    }

    @Override
    public List <SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }  
    
    @Override
    public void actualizar(){
        this.limpiar();
    }

}


