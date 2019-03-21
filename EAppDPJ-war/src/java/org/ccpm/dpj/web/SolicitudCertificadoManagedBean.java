/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
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
import org.ccpm.dpj.facade.EstadoCertificadoFacadeLocal;
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
    @EJB
    private EstadoCertificadoFacadeLocal estadoCertificadoFacade;
    private Long idEntidad;
    private Long idEstadoCertificado;
    private Long idBoleta1;
    private Long idBoleta2;
    private Long nroBoleta1;
    private Long nroBoleta2;
    private String codigoEntidad;
    private String pathCertificadoEnviar;
    private String codigoSeguridad;

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

    public String getCodigoSeguridad() {
        return codigoSeguridad;
    }

    public void setCodigoSeguridad(String codigoSeguridad) {
        this.codigoSeguridad = codigoSeguridad;
    }

    public String getPathCertificadoEnviar() {
        return pathCertificadoEnviar;
    }

    public void setPathCertificadoEnviar(String pathCertificadoEnviar) {
        this.pathCertificadoEnviar = pathCertificadoEnviar;
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
        this.matarBeans();
    }

    public void limpiarAtributos() {
        this.setCodigoEntidad(null);
        this.setNroBoleta1(null);
        this.setNroBoleta2(null);
    }

    public void matarBeans() {
        this.removeSessionScopedBean("solicitudCertificadoManagedBean");
    }

    public static void removeSessionScopedBean(String beanName) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(beanName);
    }

    public void buscarEntidad(String codigoEntidad) throws Exception {
        List<Entidad> entidadesAux = this.entidadFacade.findByCodigo(codigoEntidad);
        if (entidadesAux.isEmpty()) {
            throw new Exception("No existe una Entidad con el código ingresado.");
        } else {
            for (Entidad entidadAux : entidadesAux) {
                if (entidadAux.getEstadoEntidad().getId() == 2) { //VERIFICA QUE EL ESTADO DE LA ENTIDAD SEA ACTIVO

                    if (entidadAux.getCorreo() != null) { //VERIFICA QUE LA ENTIDAD TENGA UN EMAIL
                        if (isValidEmailAddress(entidadAux.getCorreo())) { //VERIFICA QUE EL EMAIL SEA CORRECTO
                            this.setIdEntidad(entidadAux.getId());
                            this.setEntidad(entidadAux);
                        } else {
                            throw new Exception("La entidad posee un correo electrónico incorrecto, comuníquese con los administradores del sistema.");
                        }

                    } else {
                        throw new Exception("La entidad no tiene registrado un correo electrónico, comuníquese con los administradores del sistema.");
                    }
                } else {
                        throw new Exception("La entidad no se encuentra ACTIVA.");
                }
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

    public void verificarEstadoSolicitud(Long nroBoleta1, Long nroBoleta2) throws Exception {
        List<SolicitudCertificado> solicitudesAux = this.solicitudCertificadoFacade.findByNroBoleta(nroBoleta1, nroBoleta2);
        if (!solicitudesAux.isEmpty()) {
            for (SolicitudCertificado solicitudAux : solicitudesAux) {
                switch (solicitudAux.getEstadoCertificado().getId().intValue()) {
                    case 1:
                        throw new Exception("Ya realizó una SOLICITUD con los datos ingresados. Dentro de las 24 hs. hábiles se enviará el certificado a su correo electrónico.");
                    case 2:
                        throw new Exception("Ya se ha EMITIDO un certificado con esos números de Boletas. Verifique el correo electrónico de su entidad.");
                }
            }
        }
    }

    public boolean verificarPagoBoletas(Long idBoleta1, Long idBoleta2) {
        Boleta boletaCertifiaciones = this.boletaFacade.find(idBoleta1);
        Boleta boletaTodoTramite = this.boletaFacade.find(idBoleta2);
        return boletaCertifiaciones.getEstadoBoleta().getId() == 3 && boletaTodoTramite.getEstadoBoleta().getId() == 3;
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

            /*Verificamos que no se haya emitido ya un certificado para esa entidad con esas boletas*/
            this.verificarEstadoSolicitud(this.getNroBoleta1(), this.getNroBoleta2());

            this.solicitudCertificadoFacade.solicitar(this.getIdEntidad(), this.getNroBoleta1(), this.getNroBoleta2());

            /*Si las boletas 1 y 2 están pagadas envia el email con el certificado. Caso que no se hayan pagado se genera una solicitud*/
            if (this.verificarPagoBoletas(this.getBoleta1().getId(), this.getBoleta2().getId())) {
                this.getImprimirCertificado();
                MailThread hiloMail = new MailThread(true, this.getEntidad().getCorreo(), "CERTIFICADO DE: ".concat(this.getEntidad().getNombre()),
                        "Hola, aquí está su certificado. \nMuchas gracias por cumplir con nuestros requisitos, le deseamos una linda jornada.", this.getPathCertificadoEnviar());

                hiloMail.start();

            }

            this.setMsgSuccessError("La solicitud de certificado ha sido generado con éxito. Dentro de las 24HS. se enviará a su correo electrónico.");
            this.setResultado("successErrorSolicitudCertificado");
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.limpiarAtributos();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorSolicitudCertificado");
        }
        return this.getResultado();
    }

    public String verificarCertificado() throws Exception {
        List<Entidad> entidadesAux = this.entidadFacade.findByCodigo(this.getCodigoEntidad());
        StringBuilder ids = new StringBuilder();
        if (!entidadesAux.isEmpty()) {
            for (Entidad entidadAux : entidadesAux) {
                ids.append(String.valueOf(entidadAux.getId())).append(",");
            }
            List<SolicitudCertificado> solicitudesAux = this.solicitudCertificadoFacade.verificar(ids.toString().subSequence(0, ids.length() - 1).toString(), Long.parseLong(this.getCodigoSeguridad()));
            if (solicitudesAux.isEmpty()) {
                this.setTitle("Resultado del Chequeo...");
                this.setImages("glyphicon glyphicon-remove-circle");
                this.setMsgSuccessError("Datos Incorrectos. El certificado no es válido.");
                this.setResultado("successErrorSolicitudCertificado");
            } else {
                this.setMsgSuccessError("El certificado es válido.");
                this.setResultado("successErrorSolicitudCertificado");
                this.setTitle("Proceso Completo...");
                this.setImages("glyphicon glyphicon-ok-circle");
            }
        } else {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError("Datos Incorrectos. No existe una entidad con ese código.");
            this.setResultado("successErrorSolicitudCertificado");
        }

        return "successErrorSolicitudCertificado";
    }

    public String getNroAleatorio() {
        Random rnd = new Random();
        int numero = (int) (rnd.nextDouble() * 9999 + 1000);
        return String.valueOf(numero);
    }

    public Resource getImprimirCertificado() throws JRException, FileNotFoundException, IOException {
        Resource miRecurso = null;

        try {
            /*Se busca la leyenda del año*/
            Leyenda leyendaAux = this.leyendaFacade.findByAnio(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
            this.setEstadoCertificado(this.estadoCertificadoFacade.find(2L)); //ESTADO EMITIDO
            /*seteo el path para guardar el certificado*/
            this.setPathCertificadoEnviar("/home/DatosDPJ/Certificado " + this.getEntidad().getCodigo() + this.getNroBoleta1() + ".pdf");
            /*agrega codigo de seguridad al reporte*/
            List<SolicitudCertificado> solicitudesAux = this.solicitudCertificadoFacade.findByNroBoleta(this.getNroBoleta1(), this.getNroBoleta2());
            SolicitudCertificado soliAux = solicitudesAux.get(0);
            this.setCodigoSeguridad(this.getNroAleatorio());
            soliAux.setCodigoSeguridad(Long.parseLong(this.getCodigoSeguridad()));
            soliAux.setPathArchivo(this.getPathCertificadoEnviar());
            soliAux.setEstadoCertificado(this.getEstadoCertificado());
            this.solicitudCertificadoFacade.edit(soliAux);

            JasperPrint jasperResultado = new BoletaReport().imprimirCertificado(this.getEntidad().getNombre(), this.getEntidad().getCodigo(), this.getNroBoleta1(), this.getNroBoleta2(), leyendaAux.getNombre(), leyendaAux.getAnio(), this.getCodigoSeguridad());
            byte[] bites = JasperExportManager.exportReportToPdf(jasperResultado);
            /*Guarda certificado en DatosDPJ*/
            OutputStream out = new FileOutputStream(this.getPathCertificadoEnviar());
            out.write(bites);
            out.close();
            miRecurso = new DPJResource(bites);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return miRecurso;
    }

    public boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
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
