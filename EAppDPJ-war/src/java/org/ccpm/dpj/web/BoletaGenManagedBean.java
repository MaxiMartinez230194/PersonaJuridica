/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.web;

import botdetect.web.jsf.JsfCaptcha;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.ccpm.dpj.entity.TasaServicio;
import org.ccpm.dpj.facade.BoletaFacadeLocal;
import org.ccpm.dpj.facade.TasaServicioFacadeLocal;
import org.ccpm.dpj.reporte.BoletaReport;
import org.ccpm.dpj.utilidad.DPJResource;
import org.ccpm.dpj.utilidad.TasaServicioUI;
import org.ccpm.dpj.utilidad.Utilidad;

/**
 *
 * @author Alvarenga Angel
 */
@ManagedBean
@SessionScoped
public class BoletaGenManagedBean extends UtilManagedBean implements Serializable{
    @EJB
    private TasaServicioFacadeLocal tasaServicioFacade;
    @EJB
    private BoletaFacadeLocal boletaFacade;
    private List<Long> nroBoleta;
    private JsfCaptcha captcha;
    private String captchaCode;
    private boolean correctLabelVisible = false;
    private boolean visible = false;
    private List <TasaServicioUI> listaTasas;
    private String totalMostrar;
    private String nroAleatorio;
        
    /** Creates a new instance of BoletaManagedBean */
    public BoletaGenManagedBean() {
        
    }
    
    @PostConstruct
    private void init() {
        this.limpiar();
        this.cargarTasas();
    }
    
    public void cargarTasas(){
        List <TasaServicio> lstTasas = this.tasaServicioFacade.findAll(true, true);
        List <TasaServicioUI> lstTasaAux = new ArrayList<TasaServicioUI>();
        if(!lstTasas.isEmpty()){
            for(TasaServicio tasaAux : lstTasas){
                lstTasaAux.add(new TasaServicioUI(tasaAux));
            }
        }
        this.setListaTasas(lstTasaAux);
    }
    @Override
    public List <TasaServicioUI> getListElements() { 
//        List <TasaServicio> lstTasas = this.tasaServicioFacade.findAll(true, true);
//        List <TasaServicioUI> lstTasaAux = new ArrayList<TasaServicioUI>();
//        if(!lstTasas.isEmpty()){
//            for(TasaServicio tasaAux : lstTasas){
//                lstTasaAux.add(new TasaServicioUI(tasaAux));
//            }
//        }
//        this.setList(lstTasaAux);        
//        return (List <TasaServicioUI>) this.getList();
        
        return new ArrayList();
    }

    public String getNroAleatorio() {
        //la descarga nunca va a tener el mismo nombre
        return String.valueOf(Math.random());
    }

    public void setNroAleatorio(String nroAleatorio) {
        this.nroAleatorio = nroAleatorio;
    }
    
    

    public String getTotalMostrar() {
        return Utilidad.formatearNumero(getTotal());
    }

    public void setTotalMostrar(String totalMostrar) {
        this.totalMostrar = totalMostrar;
    }

    
    
    public List<TasaServicioUI> getListaTasas() {
        return listaTasas;
    }

    public void setListaTasas(List<TasaServicioUI> listaTasas) {
        this.listaTasas = listaTasas;
    }

    public void setNroBoleta(List <Long> nroBoleta) {
        this.nroBoleta = nroBoleta;
    }
    
    public List <Long> getNroBoleta() {
        return nroBoleta;
    }

    public void setCaptcha(JsfCaptcha captcha) {
        this.captcha = captcha;
    }

    public void setCaptchaCode(String captchaCode) {
        this.captchaCode = captchaCode;
    }

    public void setCorrectLabelVisible(boolean correctLabelVisible) {
        this.correctLabelVisible = correctLabelVisible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public JsfCaptcha getCaptcha() {
        return captcha;
    }

    public String getCaptchaCode() {
        return captchaCode;
    }

    public boolean isCorrectLabelVisible() {
        return correctLabelVisible;
    }

    

    @Override
    public void limpiar() {
        this.setCorrectLabelVisible(false);
        this.setCaptcha(null);  
        this.setListaTasas(null);
        this.cargarTasas();
        this.setTotal(0.0);
        this.setTotalMostrar(Utilidad.formatearNumero(getTotal()));
    }
    
    @Override
    public String crear() {
        try {
            boolean isHuman = captcha.validate(captchaCode);
            boolean select = false;
            if(isHuman){
                List <TasaServicioUI> lstTasaAux = this.getListaTasas();
                if(!lstTasaAux.isEmpty()){
                    for(TasaServicioUI tasaServicioUIAux : lstTasaAux){
                        if(tasaServicioUIAux.isSelect()){
                           select = true;
                        }
                    }
                    if(select){
                        this.setNroBoleta(this.boletaFacade.create(lstTasaAux));
                        this.setResultado("successErrorIndex");
                        this.setMsgSuccessError("La boleta nro. " + this.getNroBoleta() + " ha sido generada con éxito");
                        this.setTitle("Proceso Completo...");
                        this.setImages("glyphicon glyphicon-ok-circle");
                        this.setVisible(true);
                        this.limpiar();
                    }else{
                        this.setVisible(false);
                        throw new Exception("Error al intentar crear la boleta, no se seleccionaron tasas");
                    }
                }
            }else{
                this.setCorrectLabelVisible(true);
                this.setResultado("index");
            }            
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorIndex");
            this.limpiar();
        }
        return this.getResultado();
    }
    
    public Resource getImprimirBoleta() throws JRException, FileNotFoundException, IOException {
        Resource miRecurso = null;
        String nroTasas=this.getNroBoleta().toString().replace("[", "").replace("]", "");
        try {
               
                WebManagedBean sessionBean = this.getSessionBean();
               
                JasperPrint jasperResultado = new BoletaReport().imprimirBoleta(nroTasas);
                byte[] bites = JasperExportManager.exportReportToPdf(jasperResultado);
                miRecurso = new DPJResource(bites);
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return miRecurso;
    } 
    
    public void getSeleccionar(){
        this.setTotal(0.0);
        List <TasaServicioUI> lstTasaAux = this.getListaTasas();
        if(!lstTasaAux.isEmpty()){
            for(TasaServicioUI tasaAux : lstTasaAux){
                if (tasaAux.isSelect()){
                    this.setTotal(this.getTotal()+ tasaAux.getMonto());
                }
                
            }
        }
        
    }
    
    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String crearOtro() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void verDetalle() {
        throw new UnsupportedOperationException("Not supported yet.");
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
    
}
