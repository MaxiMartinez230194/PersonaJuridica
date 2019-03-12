/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.ccpm.dpj.entity.ActionItem;
import org.ccpm.dpj.entity.Boleta;
import org.ccpm.dpj.entity.Entidad;
import org.ccpm.dpj.entity.EstadoCertificado;
import org.ccpm.dpj.entity.SolicitudCertificado;
import org.ccpm.dpj.entity.Grupo;
import org.ccpm.dpj.entity.ItemBoleta;
import org.ccpm.dpj.entity.Leyenda;
import org.ccpm.dpj.entity.Usuario;
import org.ccpm.dpj.facade.BoletaFacadeLocal;
import org.ccpm.dpj.facade.EntidadFacadeLocal;
import org.ccpm.dpj.facade.LeyendaFacadeLocal;
import org.ccpm.dpj.facade.SolicitudCertificadoFacadeLocal;
import org.ccpm.dpj.reporte.BoletaReport;
import org.ccpm.dpj.utilidad.DPJResource;
import org.ccpm.dpj.utilidad.MailThread;

@ManagedBean
@SessionScoped
public class SolicitudCertificadoManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private SolicitudCertificadoFacadeLocal solicitudCertificadoFacade;
    @EJB
    private BoletaFacadeLocal boletaFacade;
    @EJB
    private EntidadFacadeLocal entidadFacade;
    @EJB
    private LeyendaFacadeLocal leyendaFacade;
    private Long idEntidad;
    private Long idEstadoCertificado;
    private Long idBoleta1;
    private Long idBoleta2;
    private Long nroBoleta1;
    private Long nroBoleta2;
    private String codigoEntidad;

    private Date fecha;
    private Entidad entidad;
    private EstadoCertificado estadoCertificado;
    /*ESTAS BOLETAS DEBEN ESTAR PAGADAS PARA EMITIR EL CERTIFICADO*/
    private Boleta boleta1; //BOLETA DE TASA CERTIFICACIONES
    private Boleta boleta2; //BOLETA DE TASA POR TODO TRAMITE
    private List<ActionItem> lstActionItems = new ArrayList<>();

    /**
     * Creates a new instance of SolicitudCertificadoManagedBean
     */
    public SolicitudCertificadoManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.prepararAcciones(sessionBean.getUsuario());
            } catch (Exception e) {
            }
        }
    }

    public void prepararAcciones(Usuario usuario) throws Exception {
        try {
            List<Grupo> listGrupo = usuario.getGrupos();
            if (!(listGrupo.isEmpty())) {
                for (Grupo grupoAux : listGrupo) {
                    this.lstActionItems.addAll(grupoAux.getAcciones());
                }
            }
            this.lstActionItems.addAll(usuario.getAcciones());
            if (!(lstActionItems.isEmpty())) {
                for (ActionItem accionAux : lstActionItems) {
                    if (accionAux.getNombre().equalsIgnoreCase("nuevaSolicitudCertificado")) {
                        this.setAlta(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("editarSolicitudCertificado")) {
                        this.setModificacion(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("borrarSolicitudCertificado")) {
                        this.setBaja(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("detalleSolicitudCertificado")) {
                        this.setDetalle(true);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Error al recuperar las acciones del usuario");
        }
    }

    public String getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(String codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    public Long getNroBoleta1() {
        return nroBoleta1;
    }

    public void setNroBoleta1(Long nroBoleta1) {
        this.nroBoleta1 = nroBoleta1;
    }

    public Long getNroBoleta2() {
        return nroBoleta2;
    }

    public void setNroBoleta2(Long nroBoleta2) {
        this.nroBoleta2 = nroBoleta2;
    }

    public void setLstActionItems(List<ActionItem> lstActionItems) {
        this.lstActionItems = lstActionItems;
    }

    public List<ActionItem> getLstActionItems() {
        return lstActionItems;
    }

    public Long getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(Long idEntidad) {
        this.idEntidad = idEntidad;
    }

    public Long getIdEstadoCertificado() {
        return idEstadoCertificado;
    }

    public void setIdEstadoCertificado(Long idEstadoCertificado) {
        this.idEstadoCertificado = idEstadoCertificado;
    }

    public Long getIdBoleta1() {
        return idBoleta1;
    }

    public void setIdBoleta1(Long idBoleta1) {
        this.idBoleta1 = idBoleta1;
    }

    public Long getIdBoleta2() {
        return idBoleta2;
    }

    public void setIdBoleta2(Long idBoleta2) {
        this.idBoleta2 = idBoleta2;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    public EstadoCertificado getEstadoCertificado() {
        return estadoCertificado;
    }

    public void setEstadoCertificado(EstadoCertificado estadoCertificado) {
        this.estadoCertificado = estadoCertificado;
    }

    public Boleta getBoleta1() {
        return boleta1;
    }

    public void setBoleta1(Boleta boleta1) {
        this.boleta1 = boleta1;
    }

    public Boleta getBoleta2() {
        return boleta2;
    }

    public void setBoleta2(Boleta boleta2) {
        this.boleta2 = boleta2;
    }

    @Override
    public List<SolicitudCertificado> getListElements() {
        if (this.getNombreBuscar().equals("")) {
            this.setList(this.solicitudCertificadoFacade.findAll(true));
        } else {
            this.setList(this.solicitudCertificadoFacade.findAll(this.getNombreBuscar().toUpperCase()));
        }
        return (List<SolicitudCertificado>) this.getList();
    }

    @Override
    public void limpiar() {
        this.setCodigoEntidad(null);
        this.setNroBoleta1(null);
        this.setNroBoleta2(null);
    }

    public void buscarEntidad(String codigoEntidad) throws Exception {
        List<Entidad> entidadesAux = this.entidadFacade.findByCodigo(codigoEntidad);
        if (entidadesAux.isEmpty()) {
            throw new Exception("No existe una Entidad con el código ingresado.");
        } else {
            for (Entidad entidadAux : entidadesAux) {
                this.setIdEntidad(entidadAux.getId());
                this.setEntidad(entidadAux);
                System.out.println(entidadAux.getNombre());
            }
        }
    }

    public void buscarBoletaCertificaciones(Long nroCorrelativo) throws Exception {
        Boleta boletaAux = this.boletaFacade.findAllIn(true, nroCorrelativo);
        if (boletaAux != null) {
            ItemBoleta itemAux = boletaAux.getItems().get(0);
            if (!itemAux.getNombreTasa().equals("CERTIFICACIONES (C/U)")) {
                throw new Exception("El número de la boleta ingresada no pertenece a la TASA CERTIFICACIONES (C/U).");
            } else {
                this.setBoleta1(boletaAux);
            }
        } else {
            throw new Exception("El número de la boleta de CERTIFICACIONES (C/U) no es correcto.");
        }
    }

    public void buscarBoletaTodoTramite(Long nroCorrelativo) throws Exception {
        Boleta boletaAux = this.boletaFacade.findAllIn(true, nroCorrelativo);
        if (boletaAux != null) {
            ItemBoleta itemAux2 = boletaAux.getItems().get(0);
            if (!itemAux2.getNombreTasa().equals("TASA POR TODO TRAMITE")) {
                throw new Exception("El número de la boleta ingresada no pertenece a la TASA POR TODO TRAMITE.");
            } else {
                this.setBoleta2(boletaAux);
            }
        } else {
            throw new Exception("El número de la boleta de TASA POR TODO TRAMITE no es correcto.");
        }
    }

    public void verificarEstadoSolicitud(Long idEntidad, Long nroBoleta1) throws Exception {
        List<SolicitudCertificado> solicitudesAux = this.solicitudCertificadoFacade.findByEntidadAndNroBoleta(idEntidad, nroBoleta1);
        if (!solicitudesAux.isEmpty()) {
            for (SolicitudCertificado solicitudAux : solicitudesAux) {
                switch (solicitudAux.getEstadoCertificado().getId().intValue()) {
                    case 1:
                        throw new Exception("Ya realizó una SOLICITUD con los datos ingresados. Dentro de las 24 hs. hábiles se enviará el certificado a su correo electrónico.");
                    case 2:
                        throw new Exception("Ya se ha EMITIDO el certificado para esa Entidad con esos números de Boletas. Verifique su correo electrónico.");
                }
            }
        }
    }

    public String solicitar() {
        try {

            //BOLETA 1 = CERTIFICACIONES
            //BOLETA 2 = TODO TRAMITE
            /*Busca la entidad con el codigo ingresado*/
            this.buscarEntidad(this.getCodigoEntidad());

            /*Busca la boleta de certificaciones con el numero ingresado, si encuentra setea en Boleta1*/
            this.buscarBoletaCertificaciones(this.getNroBoleta1());

            /*Busca la boleta de todo tramite con el numero ingresado, si encuentra setea en Boleta2*/
            this.buscarBoletaTodoTramite(this.getNroBoleta2());

            /*Verificamos que no se haya emitido ya un certificado para esa entidad con esa boleta*/
            this.verificarEstadoSolicitud(this.getIdEntidad(), this.getNroBoleta1());

            /*Si las boletas 1 y 2 están pagadas envia el email con el certificado.*/
            if (this.getBoleta1().getEstadoBoleta().getId() == 3 && this.getBoleta2().getEstadoBoleta().getId() == 3) {

                System.out.println("Ya pagó");
//                MailThread hiloMail = new MailThread(true, this.getEntidad().getCorreo(), "CERTIFICADO DE: ".concat(this.getEntidad().getNombre()),
//                        "Hola, aquí está su certificado. ", "urlArchivo");
//
//                hiloMail.start();
            } else {
                System.out.println("No pagó todavia");
                this.solicitudCertificadoFacade.solicitar(this.getIdEntidad(), this.getNroBoleta1(), this.getNroBoleta2());
            }

            this.setResultado("successErrorSolicitudCertificado");
            this.setMsgSuccessError("La solicitud de certificado ha sido generado con éxito");
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.limpiar();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorSolicitudCertificado");
        }
        return this.getResultado();
    }
    
    public String getNroAleatorio() {
        return String.valueOf(Math.random());
    }

    public Resource getImprimirCertificado() throws JRException, FileNotFoundException, IOException {
        Resource miRecurso = null;

        try {                
            System.out.println(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
            Leyenda leyendaAux = this.leyendaFacade.findByAnio(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
            this.buscarEntidad("4492");
            JasperPrint jasperResultado = new BoletaReport().imprimirCertificado(this.getEntidad().getNombre(), this.getEntidad().getCodigo(), 108979L, 98114L, leyendaAux.getNombre());
            byte[] bites = JasperExportManager.exportReportToPdf(jasperResultado);
            miRecurso = new DPJResource(bites);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return miRecurso;
    }

    @Override
    public String crear() {
        try {
            this.solicitudCertificadoFacade.create(this.getIdEntidad(), this.getIdEstadoCertificado(), this.getIdBoleta1(), this.getIdBoleta2());
            this.setResultado("successErrorSolicitudCertificado");
            this.setMsgSuccessError("La solicitud de certificado ha sido generado con éxito");
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.limpiar();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorSolicitudCertificado");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idSolicitudAux = Long.parseLong(myRequest.getParameter("id"));
        SolicitudCertificado solicitudCertificadoAux = this.solicitudCertificadoFacade.find(idSolicitudAux);
        this.setId(solicitudCertificadoAux.getId());

    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idSolicitudAux = Long.parseLong(myRequest.getParameter("id"));
            this.solicitudCertificadoFacade.remove(idSolicitudAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("solicitudConf");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorSolicitudCertificado");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idSolicitudAux = Long.parseLong(myRequest.getParameter("id"));
        SolicitudCertificado solicitudAux = this.solicitudCertificadoFacade.find(idSolicitudAux);
        this.setId(solicitudAux.getId());

    }

    @Override
    public String guardarEdicion() {
        try {
            this.solicitudCertificadoFacade.edit(this.getId(), this.getIdEntidad(), this.getIdEstadoCertificado(), this.getIdBoleta1(), this.getIdBoleta2());
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.setResultado("successErrorSolicitudCertificado");
            this.setMsgSuccessError("La solicitud de certificado ha sido editado con éxito");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorSolicitudCertificado");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String crearOtro() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
