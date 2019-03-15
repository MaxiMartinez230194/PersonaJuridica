/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.web;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.ccpm.dpj.entity.*;
import org.ccpm.dpj.facade.BoletaFacadeLocal;
import org.ccpm.dpj.facade.ParteDiarioFacadeLocal;
import org.ccpm.dpj.reporte.BoletaReport;
import org.ccpm.dpj.utilidad.DPJResource;
import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;

/**
 *
 * @author Facundo Gonzalez
 */
@ManagedBean
@SessionScoped
public class ParteDiarioManagedBean extends UtilManagedBean implements Serializable{
    @EJB
    private ParteDiarioFacadeLocal parteDiarioFacade;
    @EJB
    private BoletaFacadeLocal boletaFacade;
    private List<ParteDiario> ListparteDiario;
    private ParteDiario parteDiario;
    private Date fechaParteDiario;
    private String adjunto;
    private String contenidoArchivo;
    private boolean mostrarTextArea=true;
    private String error;
    private String totalReg;
    private Date fechaDesdeBusqueda;
    private Date fechaHastaBusqueda;
    private List <ActionItem> lstActionItems = new ArrayList <ActionItem>();
    private Resource resource,resource2;
    private String path;
    private boolean verLink=false;
    private String archivo;
    private List<ParteDiario> listaMesParteDiario;
    private int Mes;
    private int Anio=Integer.parseInt(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
    private Boolean verTabla=false;
    private Boolean verLinkReporte=false;
    private Boolean verLinkReporteMensual=false;
    private Date fechaPagoReporte;
    private Date fechaPago;
    private Date fechaDeposito;
    private String nroAleatorio;
    private int numero;
    //cambios 21 feb 2019
    private BigDecimal comisionBanco;
    private BigDecimal comisionBancoMensual;
    
    private Double comision;
    private Double comisionParteMensual;
    List<String> Meses = new ArrayList<String>();
      
    
   
    
    /** Creates a new instance of ParteDiarioTasaManagedBean */
    public ParteDiarioManagedBean() {
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
                    if (accionAux.getNombre().equalsIgnoreCase("nuevaParteDiario")) {
                        this.setAlta(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("editarParteDiario")) {
                        this.setModificacion(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("borrarParteDiario")) {
                        this.setBaja(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("detalleParteDiario")) {
                        this.setDetalle(true);
                    }
                   
                }
                
            }
        } catch (Exception e) {
            throw new Exception("Error al recuperar las acciones del usuario");
        }
    }

    public Double getComisionParteMensual() {
        return comisionParteMensual;
    }

    public void setComisionParteMensual(Double comisionParteMensual) {
        this.comisionParteMensual = comisionParteMensual;
    }

    public BigDecimal getComisionBancoMensual() {
        BigDecimal com = new BigDecimal(comisionParteMensual==null?0.00673:comisionParteMensual);//controla el null
        com = com.setScale(6, RoundingMode.HALF_UP);
        return com;
    }

    public void setComisionBancoMensual(BigDecimal comisionBancoMensual) {
        this.comisionBancoMensual = comisionBancoMensual;
    }

    
    
    
   

    public void setComision(Double comision) {
        this.comision = comision;
    }
    
     public Double getComision() {
        
        return comision;
    }
    

    public BigDecimal getComisionBanco() {
        BigDecimal com = new BigDecimal(comision==null?0.00673:comision);//controla el null
        com = com.setScale(6, RoundingMode.HALF_UP);
        return com;
    }

    
    
    
    public void setComisionBanco(BigDecimal comisionBanco) {
        this.comisionBanco = comisionBanco;
    }
    
    

    public String getNroAleatorio() {
        return String.valueOf(Math.random());
    }

    public void setNroAleatorio(String nroAleatorio) {
        this.nroAleatorio = nroAleatorio;
    }

    
    
    
    
    public Date getFechaDeposito() {
        return fechaDeposito;
    }

    public void setFechaDeposito(Date fechaDeposito) {
        
        
        this.fechaDeposito = fechaDeposito;
    }

    
    
    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }
    
    

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    
    
    
    public Boolean getVerLinkReporteMensual() {
        
        return verLinkReporteMensual;
    }

    public void setVerLinkReporteMensual(Boolean verLinkReporteMensual) {
        this.verLinkReporteMensual = verLinkReporteMensual;
    }

    
    public Date getFechaPagoReporte() {
        return fechaPagoReporte;
    }

    public void setFechaPagoReporte(Date fechaPagoReporte) {
        this.fechaPagoReporte = fechaPagoReporte;
    }
    
    

    public Boolean getVerLinkReporte() {
        return verLinkReporte;
    }

    public void setVerLinkReporte(Boolean verLinkReporte) {
        this.verLinkReporte = verLinkReporte;
    }

    
    
    public Boolean getVerTabla() {
        return verTabla;
    }

    public void setVerTabla(Boolean verTabla) {
        this.verTabla = verTabla;
    }

    public int getAnio() {
        return Anio;
    }

    public void setAnio(int Anio) {
        this.setVerTabla(true);
        this.Anio = Anio;
    }

    
    public int getMes() {
        return Mes;
    }

    public void setMes(int Mes) {
        
        this.Mes = Mes;
        
        //System.out.println("mess "+Mes);
    }
    

    public List<ParteDiario> getListaMesParteMensual() { //lista del datatable MensualConf
       
        return !this.parteDiarioFacade.devuelveParteDiariosPorMes(this.getMes(),this.getAnio()).isEmpty()?
               this.parteDiarioFacade.devuelveParteDiariosPorMes(this.getMes(),this.getAnio()):null;
       
       
    }

    public void setListaMesParteMensual(List<ParteDiario> listaMesParteDiario) {
        this.listaMesParteDiario = listaMesParteDiario;
    }
    
    

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    
    public boolean isVerLink() {
        return verLink;
    }

    public void setVerLink(boolean verLink) {
        this.verLink = verLink;
    }

    
    
    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource2() {
        return resource2;
    }

    public void setResource2(Resource resource2) {
        this.resource2 = resource2;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    
    
    
    
    public Date getFechaDesdeBusqueda() {
        return fechaDesdeBusqueda;
    }

    public void setFechaDesdeBusqueda(Date fechaDesdeBusqueda) {
        this.fechaDesdeBusqueda = fechaDesdeBusqueda;
    }

    public Date getFechaHastaBusqueda() {
        return fechaHastaBusqueda;
    }

    public void setFechaHastaBusqueda(Date fechaHastaBusqueda) {
        this.fechaHastaBusqueda = fechaHastaBusqueda;
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

    public ParteDiarioFacadeLocal getParteDiarioFacade() {
        return parteDiarioFacade;
    }

    public void setParteDiarioFacade(ParteDiarioFacadeLocal parteDiarioFacade) {
        this.parteDiarioFacade = parteDiarioFacade;
    }

    public List<ParteDiario> getListparteDiario() {
        return ListparteDiario;
    }

    public void setListparteDiario(List<ParteDiario> ListparteDiario) {
        this.ListparteDiario = ListparteDiario;
    }

    public ParteDiario getParteDiario() {
        return parteDiario;
    }

    public void setParteDiario(ParteDiario parteDiario) {
        this.parteDiario = parteDiario;
    }
    
    
    public void verReporteMensual(){
        this.setVerLinkReporteMensual(true);
    }
    
   

    public List<ActionItem> getLstActionItems() {
        return lstActionItems;
    }

    @Override
    public List<ParteDiario> getListElements() {
        this.setList(this.parteDiarioFacade.findAll(this.getFechaDesdeBusqueda(),this.getFechaHastaBusqueda()));
        this.limpiar();
        return (List<ParteDiario>) this.getList();
    }

    

    @Override
    public void actualizar() {
        
        this.setFechaDesdeBusqueda(null);
        this.setFechaHastaBusqueda(null);
        
    }
    
    @Override
    public void limpiar() {
        this.setFechaParteDiario(null);
        this.setAdjunto("");
        this.setPath("");
        this.setFechaPago(null);
        
        
        
        
    }

    

    @Override
    public String crearOtro() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idParteDiarioAux = Long.parseLong(myRequest.getParameter("id"));
        this.setListparteDiario(this.boletaFacade.findAllBoletas(idParteDiarioAux));
       
        this.setParteDiario(this.parteDiarioFacade.find(idParteDiarioAux));
        //System.out.println("desde partediarioMB list boleta--> "+this.getListparteDiario().size());
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
    
    
    //modificaciones 27 mayo 2017, parte diario by facu
    public void customValidator(FileEntryEvent entryEvent) { 
        FileEntryResults results = ((FileEntry) entryEvent.getComponent()).getResults();
        for (FileEntryResults.FileInfo file : results.getFiles()) {
            this.setAdjunto(file.getFile().getAbsolutePath());
            if (file.isSaved()) {
//                System.out.println(file.getFileName());
//                System.out.println(file.getContentType());
//                System.out.println(file.getFile().getAbsolutePath());
                this.setAdjunto(file.getFile().getAbsolutePath());
                
            }
        }
     }
    //lee el archivo segun url
    public void subeArchivoActualizaEstadoBoletas(String archivo) throws FileNotFoundException, IOException, ParseException {
        String cadena;
        String cadena1="",auxCadena1="";
        String auxCadena="";
        String fecha="",nroParteDiario;
        int totalRegistrosActualizados=0;
        int i=1;
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        
        while((cadena = b.readLine())!=null) {
           
            //System.out.println(cadena);
            
            if (!cadena.substring(20, 28).contains("COBRADO")){ //saltea la primera linea del titulo
                
               
                nroParteDiario=cadena.substring(20, 28);
                 //fecha de pago
                Long nro=Long.parseLong(nroParteDiario);
                cadena1=parteDiarioFacade.registrarPagosBoletas(nro); //edita las boletas
                
                totalRegistrosActualizados=totalRegistrosActualizados+1;
                
                
                }
            fecha=cadena.substring(0, 8);
                
            }
            if (!fecha.isEmpty()){
                    this.setFechaPago(this.textoAfecha(fecha));
            }

            
            
            
           
            
        
        b.close();
        
       
        
    }
     public void mostrarInfo() throws IOException{
        
        this.setMostrarTextArea(true);
    }
     
    @Override
    public String crear() {
        
            

            
        try {         
             
           
                    this.setVerLinkReporte(false);
                    this.setVerLinkReporteMensual(false);
                    //System.out.println(this.parteDiarioFacade.existeParteDiario(this.adjunto)?"existe":"noexiste");
                    System.out.println("que pasooooo:"+this.adjunto.substring(34,42));
                    //uso la fecha del nombre del archivo como fecha de deposito.
                    this.setFechaDeposito(this.textoAfechaDeposito(this.adjunto.substring(34, 42)));
                    //control fecha de deposito.
                    if (!this.parteDiarioFacade.existeParteDiarioConFechaDeposito(this.getFechaDeposito())){
                        this.subeArchivoActualizaEstadoBoletas(adjunto);
                        this.parteDiarioFacade.create(this.getAdjunto(),this.getFechaParteDiario(),this.getFechaPago(),this.getFechaDeposito());
                        this.subeArchivoActualizaEstadoBoletas(adjunto);
                        //sube el archivo y actualiza las boletas generadas a pagadas segun el archivo del banco

                        this.setResultado("successErrorParteDiario");
                         //System.out.println("anduvooo");
                        this.setMsgSuccessError("El parte diario ha sido generado con éxito ");
                        this.setTitle("Proceso Completo...");
                        this.setImages("glyphicon glyphicon-ok-circle");
                        this.limpiar();
                    }else{
                        this.setTitle("Resultado del Chequeo...");
                        this.setImages("glyphicon glyphicon-remove-circle");
                        this.setMsgSuccessError("Ya existe un parte diario con la fecha de depósito");
                        this.setResultado("successErrorParteDiario");
                        this.limpiar();
                    }
          
                   
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            System.out.println("errorrrr:"+ex.getMessage());
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorParteDiario");
            this.limpiar();
        }
        return this.getResultado();
        
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //muestra la información del archivo
   

    //martes 6 de junio de 2017
    
    public class MyResource extends Resource implements java.io.Serializable {

        private String path = "";
        private HashMap<String, String> headers;
        private byte[] bytes;
        
        public MyResource(byte[] bytes) {
            this.bytes = bytes;
            this.headers = new HashMap<String, String>();
        }
        
        public InputStream getInputStream() {
            return new ByteArrayInputStream(this.bytes);
        }

        public String getRequestPath() {
            return path;
        }
        
        public void setRequestPath(String path) {
            this.path = path;
        }

        public Map<String, String> getResponseHeaders() {
            return headers;
        }

        public URL  getURL() {
            return null;
        }

        public boolean userAgentNeedsUpdate(FacesContext context) {
            return false;
        }
    }
    
   
    
    public void initMetaData() { //devuelve el archivo adjunto segun el path que se le pase
        String resourcePath = path;
        this.setPath(resourcePath);
        File file = new File(resourcePath);
        try {
            this.resource = new ParteDiarioManagedBean.MyResource(readIntoByteArray(new FileInputStream(file)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            this.resource = new ParteDiarioManagedBean.MyResource(new byte[0]);
        }
        
    }
    
      private static byte[] readIntoByteArray(InputStream in) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        out.flush();

        return out.toByteArray();
    }
    
      
      
    //CAMBIOS 13 JUNIO 2017
      
     public void setearFechaParaReporte(Date fechaPago){
        this.setFechaPagoReporte(fechaPago);
        this.setVerLinkReporte(true);
    }
     
     //reporte diario
     public Resource getImprimirBoleta() throws JRException, FileNotFoundException, IOException {
        Resource miRecurso = null;
        
        try {
               
                JasperPrint jasperResultado = new BoletaReport().imprimirReporteDiario(this.fechaPagoReporte,this.getComisionBanco());
                byte[] bites = JasperExportManager.exportReportToPdf(jasperResultado);
                miRecurso = new DPJResource(bites);
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return miRecurso;
    } 
      //reporte mensual
     
     
     
      public Resource getImprimirBoletaMensual() throws JRException, FileNotFoundException, IOException {
        Resource miRecurso = null;
        
        try {
               
                JasperPrint jasperResultado = new BoletaReport().imprimirReporteMensual(this.Mes,this.Anio,this.getComisionBancoMensual());
                byte[] bites = JasperExportManager.exportReportToPdf(jasperResultado);
                miRecurso = new DPJResource(bites);
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return miRecurso;
    }
      
      
      public void setearReporteMensual(){
          this.setVerLinkReporteMensual(true);
      }
      
      
      public String devolverMes(int nroMes){  
      Meses.add("ENERO");
      Meses.add("FEBRERO");
      Meses.add("MARZO");
      Meses.add("ABRIL");
      Meses.add("MAYO");
      Meses.add("JUNIO");
      Meses.add("JULIO");
      Meses.add("AGOSTO");
      Meses.add("SEPTIEMBRE");
      Meses.add("OCTUBRE");
      Meses.add("NOVIEMBRE");
      Meses.add("DICIEMBRE");
      return Meses.get(nroMes-1);
                                        
      
      }
    
      public String generarBoletaMensual(){
          
           try {
                    if (this.getListaMesParteMensual() != null)  {  
                        this.setVerLinkReporteMensual(true);
                        this.setResultado("successErrorParteMensual");
                        this.setMsgSuccessError("El reporte ha sido generada con éxito");
                        this.setTitle("Proceso Completo...");
                        this.setImages("glyphicon glyphicon-ok-circle");
                       
                        this.limpiar();
                    }else{
                        this.setVerLinkReporteMensual(false);
                        throw new Exception("Error al generar el reporte, no existe el datos del mes solicitado");
                        
                    }
                }catch (Exception ex) {
                        this.setTitle("Resultado del Chequeo...");
                        this.setImages("glyphicon glyphicon-remove-circle");
                        this.setMsgSuccessError(ex.getMessage());
                        this.setResultado("successErrorParteMensual");
                        this.limpiar();
        }
        return this.getResultado();
          
      }
      
      public String generarReporteDiario(){
          
           try {
                        this.setVerLinkReporte(true);
                        this.setResultado("successErrorParteDiario");
                        this.setMsgSuccessError("El reporte ha sido generada con éxito");
                        this.setTitle("Proceso Completo...");
                        this.setImages("glyphicon glyphicon-ok-circle");
                        //this.setVerLinkReporte(true);
                        this.limpiar();
                   
                        
                        
                    
                }catch (Exception ex) {
                        this.setTitle("Resultado del Chequeo...");
                        this.setImages("glyphicon glyphicon-remove-circle");
                        this.setMsgSuccessError(ex.getMessage());
                        this.setResultado("successErrorParteDiario");
                        this.limpiar();
        }
        return this.getResultado();
          
      }
    
      
     private Date textoAfecha(String fecha) throws ParseException{
       
        String texto=fecha.substring(0,2)+"/"+fecha.substring(2,4)+"/"+fecha.substring(4,8);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        

       
           // System.out.println("holaaaaaa "+dateInString);
           Date date = formatter.parse(texto);
            //this.setFechaPago(date);
            return date;

        
    
         
     }
     
     //obtiene la fecha de deposito desde el nombre del archivo ej:FdoEspPerJur1164_20170726, devuelve-> 26-07-2017
     private Date textoAfechaDeposito(String fecha) throws ParseException{
       
        String texto=fecha.substring(6,8)+"/"+fecha.substring(4,6)+"/"+fecha.substring(0,4);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        

       
           // System.out.println("holaaaaaa "+dateInString);
           Date date = formatter.parse(texto);
            //this.setFechaPago(date);
            return date;

        
    
         
     }
    
    

    }

