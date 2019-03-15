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
import org.ccpm.dpj.entity.*;
import org.ccpm.dpj.facade.BancoFacadeLocal;
import org.ccpm.dpj.facade.CuentaFacadeLocal;
import org.ccpm.dpj.facade.TipoCuentaFacadeLocal;


/**
 *
 * @author Alvarenga Angel
 */
@ManagedBean
@RequestScoped
public class CuentaManagedBean extends UtilManagedBean implements Serializable{
    @EJB
    private TipoCuentaFacadeLocal tipoCuentaFacade;
    @EJB
    private BancoFacadeLocal bancoFacade;
    @EJB
    private CuentaFacadeLocal cuentaFacade;
    private String denominacion;
    private String nro;  
    private Long idTipoCuenta;
    private Long idBanco;
    private Cuenta cuenta;
    private List <ActionItem> lstActionItems = new ArrayList <ActionItem>();
    
    /** Creates a new instance of CuentaManagedBean */
    public CuentaManagedBean() {
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
                    if (accionAux.getNombre().equalsIgnoreCase("nuevaCuenta")) {
                        this.setAlta(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("editarCuenta")) {
                        this.setModificacion(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("borrarCuenta")) {
                        this.setBaja(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("detalleCuenta")) {
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

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public void setIdBanco(Long idBanco) {
        this.idBanco = idBanco;
    }

    public void setIdTipoCuenta(Long idTipoCuenta) {
        this.idTipoCuenta = idTipoCuenta;
    }

    public void setNro(String nro) {
        this.nro = nro;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public Long getIdBanco() {
        return idBanco;
    }

    public Long getIdTipoCuenta() {
        return idTipoCuenta;
    }

    public String getNro() {
        return nro;
    }

    public List<ActionItem> getLstActionItems() {
        return lstActionItems;
    }  
    
    @Override
    public List <Cuenta> getListElements() {
        if (this.getNombreBuscar().equals("")) {
            this.setList(this.cuentaFacade.findAll(true));
        } else {
            this.setList(this.cuentaFacade.findAll(this.getNombreBuscar().toUpperCase()));
        }
        return (List <Cuenta>) this.getList();
    }

    @Override
    public void limpiar() {
        this.setDenominacion(null);
        this.setNro(null);
        this.setIdBanco(null);
        this.setIdTipoCuenta(null);        
    }
    
    @Override
    public String crear() {
        try {
            this.cuentaFacade.create(this.getDenominacion(), this.getNro(),
                    this.getIdTipoCuenta(), this.getIdBanco());
            this.setResultado("successErrorCuenta");
            this.setMsgSuccessError("La cuenta ha sido generada con éxito");
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.limpiar();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorCuenta");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.cuentaFacade.create(this.getDenominacion(), this.getNro(),
                    this.getIdTipoCuenta(), this.getIdBanco());
            this.setResultado("nuevaCuenta");
            this.limpiar();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorCuenta");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idCuentaAux = Long.parseLong(myRequest.getParameter("id"));
        Cuenta cuentaAux = this.cuentaFacade.find(idCuentaAux);
        this.setCuenta(cuentaAux);
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest)context.getExternalContext().getRequest();
            Long idCuentaAux = Long.parseLong(myRequest.getParameter("id"));
            this.cuentaFacade.remove(idCuentaAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("cuentaConf");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorCuenta");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idCuentaAux = Long.parseLong(myRequest.getParameter("id"));
        Cuenta cuentaAux = this.cuentaFacade.find(idCuentaAux);
        this.setId(cuentaAux.getId());
        this.setDenominacion(cuentaAux.getDenominacion());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.cuentaFacade.edit(this.getId(), this.getDenominacion());
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.setResultado("successErrorCuenta");
            this.setMsgSuccessError("La cuenta ha sido editada con éxito");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorCuenta");
        }
        return this.getResultado();
    }

    public List<SelectItem> getSelectItemsBanco() {
        List <SelectItem> selectItems = new ArrayList <SelectItem>();
        List <Banco> lstBanco = bancoFacade.findAll(true);
        if (!(lstBanco.isEmpty())) {
            for (Banco bancoAux : lstBanco) {
                selectItems.add(new SelectItem(bancoAux.getId(), bancoAux.getNombre()));
            }
        }
        return selectItems;
    }
    
    public List<SelectItem> getSelectItemsTipoCuenta() {
        List <SelectItem> selectItems = new ArrayList <SelectItem>();
        List <TipoCuenta> lstTipoCuenta = tipoCuentaFacade.findAll(true);
        if (!(lstTipoCuenta.isEmpty())) {
            for (TipoCuenta tipoCuentaAux : lstTipoCuenta) {
                selectItems.add(new SelectItem(tipoCuentaAux.getId(), tipoCuentaAux.getNombre()));
            }
        }
        return selectItems;
    }
    
    @Override
    public List <SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }  

}
