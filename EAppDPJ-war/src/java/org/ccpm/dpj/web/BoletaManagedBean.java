/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.web;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.ccpm.dpj.entity.*;
import org.ccpm.dpj.facade.BoletaFacadeLocal;
import org.ccpm.dpj.facade.EstadoBoletaFacadeLocal;
import org.ccpm.dpj.facade.ParteDiarioFacadeLocal;
import org.ccpm.dpj.utilidad.Modulo11;


/**
 *
 * @author Alvarenga Angel
 */
@ManagedBean
@SessionScoped
public class BoletaManagedBean extends UtilManagedBean implements Serializable{
    @EJB
    private BoletaFacadeLocal boletaFacade;
    @EJB
    private ParteDiarioFacadeLocal parteDiarioFacade;
    @EJB
    private EstadoBoletaFacadeLocal estadoBoletaFacade;
    
    private Boleta boleta;
    private Long nroCorrelBusqueda;
    private Date fecEmDesdeBusqueda;
    private Date fecEmHastaBusqueda;
    private Date fecPagoDesdeBusqueda;
    private Date fecPagoHastaBusqueda;
    private Long idEstadoBusqueda;
    private Date fechaParteDiario;
    private String adjunto;
    private String contenidoArchivo;
    private boolean mostrarTextArea=false;
    private String error;
    private String totalReg;
    private String nroBoleta="4567890";
    
    private List <ActionItem> lstActionItems = new ArrayList <ActionItem>();
    private List <ParteDiario> parteDiarioList = new ArrayList <ParteDiario>();
    private List <ParteDiario> listaParteDiario;
    private int tamanioListElement;
   
    
    /** Creates a new instance of BoletaTasaManagedBean */
    public BoletaManagedBean() {
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
                    if (accionAux.getNombre().equalsIgnoreCase("nuevaBoleta")) {
                        this.setAlta(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("editarBoleta")) {
                        this.setModificacion(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("borrarBoleta")) {
                        this.setBaja(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("detalleBoleta")) {
                        this.setDetalle(true);
                    }
                    
                }
                
            }
        } catch (Exception e) {
            throw new Exception("Error al recuperar las acciones del usuario");
        }
    }

    public String getNroBoleta() {
        Modulo11 asd = new Modulo11();
        return String.valueOf(asd.obtenerSumaPorDigitos(asd.invertirCadena(nroBoleta)));
    }

    public void setNroBoleta(String nroBoleta) {
        this.nroBoleta = nroBoleta;
    }
    
    
    
    public int getTamanioListElement() {
        return this.getListElements().size();
    }

    public void setTamanioListElement(int tamanioListElement) {
        this.tamanioListElement = tamanioListElement;
    }

    public String getTotalReg() {
        return totalReg;
    }

    public void setTotalReg(String totalReg) {
        this.totalReg = totalReg;
    }

    
    
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    

    public boolean isMostrarTextArea() {
        return mostrarTextArea;
    }

    public void setMostrarTextArea(boolean mostrarTextArea) {
        this.mostrarTextArea = mostrarTextArea;
    }
    
    

    public String getContenidoArchivo() {
        return contenidoArchivo;
    }

    public void setContenidoArchivo(String contenidoArchivo) {
        this.contenidoArchivo = contenidoArchivo;
    }

    
    
    public Date getFechaParteDiario() {
        return fechaParteDiario;
    }

    public void setFechaParteDiario(Date fechaParteDiario) {
        this.fechaParteDiario = fechaParteDiario;
    }

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }
    
    

    public void setLstActionItems(List<ActionItem> lstActionItems) {
        this.lstActionItems = lstActionItems;
    }

    public void setBoleta(Boleta boleta) {
        this.boleta = boleta;
    }

    public void setIdEstadoBusqueda(Long idEstadoBusqueda) {
        this.idEstadoBusqueda = idEstadoBusqueda;
    }

    public void setNroCorrelBusqueda(Long nroCorrelBusqueda) {
        this.nroCorrelBusqueda = nroCorrelBusqueda;
    }

    public void setFecEmDesdeBusqueda(Date fecEmDesdeBusqueda) {
        this.fecEmDesdeBusqueda = fecEmDesdeBusqueda;
    }

    public void setFecEmHastaBusqueda(Date fecEmHastaBusqueda) {
        this.fecEmHastaBusqueda = fecEmHastaBusqueda;
    }

    public void setFecPagoDesdeBusqueda(Date fecPagoDesdeBusqueda) {
        this.fecPagoDesdeBusqueda = fecPagoDesdeBusqueda;
    }

    public void setFecPagoHastaBusqueda(Date fecPagoHastaBusqueda) {
        this.fecPagoHastaBusqueda = fecPagoHastaBusqueda;
    }

    public Date getFecPagoDesdeBusqueda() {
        return fecPagoDesdeBusqueda;
    }

    public Date getFecPagoHastaBusqueda() {
        return fecPagoHastaBusqueda;
    }

    public Date getFecEmDesdeBusqueda() {
        return fecEmDesdeBusqueda;
    }

    public Date getFecEmHastaBusqueda() {
        return fecEmHastaBusqueda;
    }

    public Long getIdEstadoBusqueda() {
        return idEstadoBusqueda;
    }

    public Long getNroCorrelBusqueda() {
        return nroCorrelBusqueda;
    }

    public Boleta getBoleta() {
        return boleta;
    }

    public List<ActionItem> getLstActionItems() {
        return lstActionItems;
    }

    @Override
    public List<Boleta> getListElements() {
        if (bandera){ //controla la cantidad de llamados al facade
        this.setList(this.boletaFacade.findAll(this.getNroCorrelBusqueda(),
                this.getFecEmDesdeBusqueda(), this.getFecEmHastaBusqueda(),
                this.getFecPagoDesdeBusqueda(), this.getFecPagoHastaBusqueda(),
                this.getIdEstadoBusqueda()));
        this.setBandera(false);
        }
        return (List<Boleta>) this.getList();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        List <SelectItem> selectItems = new ArrayList <SelectItem>();
        List <EstadoBoleta> lstEstado = estadoBoletaFacade.findAll(true);
        if (!(lstEstado.isEmpty())) {
            for (EstadoBoleta estadoAux : lstEstado) {
                selectItems.add(new SelectItem(estadoAux.getId(), estadoAux.getNombre()));
            }
        }
        return selectItems;
    }

    @Override
    public void actualizar() {
        this.setNroCorrelBusqueda(null);
        this.setFecEmDesdeBusqueda(null);
        this.setFecEmHastaBusqueda(null);
        this.setFecPagoDesdeBusqueda(null);
        this.setFecPagoHastaBusqueda(null);
        this.setIdEstadoBusqueda(null);
    }
    
    @Override
    public void limpiar() {
        this.setFechaParteDiario(null);
        //this.setAdjunto("");
    }

    

    @Override
    public String crearOtro() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idBoletaAux = Long.parseLong(myRequest.getParameter("id"));
        this.setBoleta(this.boletaFacade.find(idBoletaAux));
    }

    @Override
    public void guardarBorrado() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void prepararParaEditar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String guardarEdicion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    


    @Override
    public String crear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    

    }

