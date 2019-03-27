/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.web;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.ccpm.dpj.entity.*;
import org.ccpm.dpj.facade.ConfiguracionFacadeLocal;
//import org.ccpm.dpj.facade.MunicipioFacadeLocal;
import org.ccpm.dpj.facade.EntidadFacadeLocal;
import org.ccpm.dpj.facade.EstadoFacadeLocal;
import org.ccpm.dpj.facade.MunicipioFacadeLocal;
import org.ccpm.dpj.facade.TipoEntidadFacadeLocal;
import org.ccpm.dpj.utilidad.DiasHabiles;

/**
 *
 * @author Alvarenga Angel
 */
@ManagedBean
@SessionScoped
public class SociedadManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private EntidadFacadeLocal entidadFacade;
    @EJB
    private MunicipioFacadeLocal municipioFacade;
    @EJB
    private EstadoFacadeLocal estadoFacade;
    @EJB
    private TipoEntidadFacadeLocal tipoEntidadFacade;
    @EJB
    private ConfiguracionFacadeLocal configuracionFacade;
    private String nombre;
    private String codigo;
    private String codigoBuscar;
    private String direccion;
    private String direccionBuscar;
    private Date fechaAlta;
    private Date fechaAltaBuscar;
    private Long idMunicipio;
    private Long idMunicipioBuscar;
    private Long idEstado;
    private Long idEstadoBuscar;
    private String correo;
    private String correoBuscar;
    private String telefono;
    private String telefonoBuscar;
    private String paginaWeb;
    private String paginaWebBuscar;
    private String fechaBaja;
    private String dni;
    private String nombreReserva;
    private Long idTipoEntidad;
    private Long idTipoEntidadBuscar;
    private TipoEntidad tipoEntidad;
    private Estado estado;
    private Municipio municipio;
    private Configuracion configuracion;
    
    private Date fechaVencimiento;
    private String url;
    private Date fechaActual;
    private boolean entidadVencida;

    private List<ActionItem> lstActionItems = new ArrayList<ActionItem>();
    private Object date;
    
    private int tamanioListElement;

    /**
     * Creates a new instance of EntidadManagedBean
     */
    public SociedadManagedBean() {

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
        this.limpiar();
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
                    if (accionAux.getNombre().equalsIgnoreCase("nuevaSociedad")) {
                        this.setAlta(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("editarSociedad")) {
                        this.setModificacion(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("borrarSociedad")) {
                        this.setBaja(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("detalleSociedad")) {
                        this.setDetalle(true);
                    }
                    //System.out.println(accionAux.getNombre());
                }
            }
        } catch (Exception e) {
            throw new Exception("Error al recuperar las acciones del usuario");
        }
    }
    
    public int getTamanioListElement() {
        return this.getListElements().size();
    }

    public void setTamanioListElement(int tamanioListElement) {
        this.tamanioListElement = tamanioListElement;
    }


    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombreReserva() {
        return nombreReserva;
    }

    public void setNombreReserva(String nombreReserva) {
        this.nombreReserva = nombreReserva;
    }
    
    
    
    public Date getFechaVencimiento(Date fechaAlta) {
        DiasHabiles diasHabiles=new DiasHabiles();
        int diasReserva = this.configuracionFacade.findAll().get(0).getTotalDiasReserva();
        System.out.println(diasHabiles.sumarRestarDiasFecha(fechaAlta,diasReserva).toString());
        
        return diasHabiles.sumarRestarDiasFecha(fechaAlta,diasReserva);
    }

    public Configuracion getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(Configuracion configuracion) {
        this.configuracion = configuracion;
    }

    
    
    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
     public boolean isEntidadVencida(Date fechaVencimiento) {
        java.util.Date fecha = new Date();
        
        return !fechaVencimiento.before(fecha);
    }

    public void setEntidadVencida(boolean entidadVencida) {
        this.entidadVencida = entidadVencida;
    }
    
    public Date getFechaActual() {
        java.util.Date fecha = new Date();
        return fecha;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public String getFechaBaja() {
        if (this.fechaAlta != null && this.estado.getLeyenda().equals("RESERVADO")) {
            Format formatter = new SimpleDateFormat("dd/MM/yyyy");
            String s = formatter.format(this.fechaAlta);
            DiasHabiles da = new DiasHabiles();
            String FechaHabil = da.getDatePlusDays(s, 30);
            return FechaHabil;
        }
        return "Estado Activo, sin fecha de baja";
    }

    public String getCodigoBuscar() {
        return codigoBuscar;
    }

    public void setCodigoBuscar(String codigoBuscar) {
        this.codigoBuscar = codigoBuscar;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaAltaBuscar() {
        return fechaAltaBuscar;
    }

    public void setFechaAltaBuscar(Date fechaAltaBuscar) {
        this.fechaAltaBuscar = fechaAltaBuscar;
    }

    public Long getIdMunicipioBuscar() {
        return idMunicipioBuscar;
    }

    public void setIdMunicipioBuscar(Long idMunicipioBuscar) {
        this.idMunicipioBuscar = idMunicipioBuscar;
    }

    public Long getIdEstadoBuscar() {
        return idEstadoBuscar;
    }

    public void setIdEstadoBuscar(Long idEstadoBuscar) {
        this.idEstadoBuscar = idEstadoBuscar;
    }

    public Long getIdTipoEntidadBuscar() {
        return idTipoEntidadBuscar;
    }

    public void setIdTipoEntidadBuscar(Long idTipoEntidadBuscar) {
        this.idTipoEntidadBuscar = idTipoEntidadBuscar;
    }

    public void setFechaBaja(String fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public MunicipioFacadeLocal getMunicipioFacade() {
        return municipioFacade;
    }

    public void setMunicipioFacade(MunicipioFacadeLocal municipioFacade) {
        this.municipioFacade = municipioFacade;
    }

    public Long getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Long idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLstActionItems(List<ActionItem> lstActionItems) {
        this.lstActionItems = lstActionItems;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public List<ActionItem> getLstActionItems() {
        return lstActionItems;
    }

    public String getNombre() {
        return nombre;
    }

    public Long getIdTipoEntidad() {
        return idTipoEntidad;
    }

    public void setIdTipoEntidad(Long idTipoEntidad) {
        this.idTipoEntidad = idTipoEntidad;
    }

    public TipoEntidad getTipoEntidad() {
        return tipoEntidad;
    }

    public void setTipoEntidad(TipoEntidad tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
    }

    public EntidadFacadeLocal getEntidadFacade() {
        return entidadFacade;
    }

    public void setEntidadFacade(EntidadFacadeLocal entidadFacade) {
        this.entidadFacade = entidadFacade;
    }

    public EstadoFacadeLocal getEstadoFacade() {
        return estadoFacade;
    }

    public void setEstadoFacade(EstadoFacadeLocal estadoFacade) {
        this.estadoFacade = estadoFacade;
    }

    public TipoEntidadFacadeLocal getTipoEntidadFacade() {
        return tipoEntidadFacade;
    }

    public void setTipoEntidadFacade(TipoEntidadFacadeLocal tipoEntidadFacade) {
        this.tipoEntidadFacade = tipoEntidadFacade;
    }

    public String getDireccionBuscar() {
        return direccionBuscar;
    }

    public void setDireccionBuscar(String direccionBuscar) {
        this.direccionBuscar = direccionBuscar;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCorreoBuscar() {
        return correoBuscar;
    }

    public void setCorreoBuscar(String correoBuscar) {
        this.correoBuscar = correoBuscar;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefonoBuscar() {
        return telefonoBuscar;
    }

    public void setTelefonoBuscar(String telefonoBuscar) {
        this.telefonoBuscar = telefonoBuscar;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    public String getPaginaWebBuscar() {
        return paginaWebBuscar;
    }

    public void setPaginaWebBuscar(String paginaWebBuscar) {
        this.paginaWebBuscar = paginaWebBuscar;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    @Override
    public List<Entidad> getListElements() {
        if (this.getNombreBuscar().equals("") && this.getCodigoBuscar().equals("") && this.getIdMunicipioBuscar() == 0L && this.getIdEstadoBuscar() == 0L) {
            this.setList(this.entidadFacade.findAll(true, 2L));
        } else {
            this.setList(this.entidadFacade.findAll(this.getNombreBuscar(), this.getCodigoBuscar(), this.getIdMunicipioBuscar(), this.getDireccionBuscar(), this.getTelefonoBuscar(), this.getCorreoBuscar(), this.getPaginaWebBuscar(), this.getIdEstadoBuscar(), 2L));
        }
        return (List<Entidad>) this.getList();
    }

    public List<SelectItem> getSelectItemsEstado() {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        List<Estado> lstEstado = estadoFacade.findAll();
        if (!(lstEstado.isEmpty())) {
            for (Estado estadoAux : lstEstado) {
                selectItems.add(new SelectItem(estadoAux.getId(), estadoAux.getLeyenda()));
            }
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsTipoEntidad() {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        List<TipoEntidad> lstTipoEntidad = tipoEntidadFacade.findAll();
        if (!(lstTipoEntidad.isEmpty())) {
            for (TipoEntidad tipoEntidadAux : lstTipoEntidad) {
                selectItems.add(new SelectItem(tipoEntidadAux.getId(), tipoEntidadAux.getLeyenda()));
            }
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsMunicipio() {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        List<Municipio> lstMunicipio = municipioFacade.findAll();
        if (!(lstMunicipio.isEmpty())) {
            for (Municipio municipioAux : lstMunicipio) {
                selectItems.add(new SelectItem(municipioAux.getId(), municipioAux.getLeyenda()));
            }
        }
        return selectItems;
    }

    @Override
    public void limpiar() {
        this.setNombre("");
        this.setCodigo("");
        this.setIdMunicipio(0L);
        this.setDireccion("");
        this.setTelefono("");
        this.setCorreo("");
        this.setPaginaWeb("");
        
        this.setIdEstado(0L);
        this.setFechaAlta(null);
        
        this.setDni(""); 
        this.setNombreReserva("");
        
        
        this.setNombreBuscar("");
        this.setCodigoBuscar("");
        this.setCorreoBuscar("");
        this.setDireccionBuscar("");
        this.setTelefonoBuscar("");
        this.setPaginaWebBuscar("");
        this.setFechaAlta(null);
        this.setIdMunicipioBuscar(0L);
        this.setIdEstadoBuscar(0L);
        this.setIdTipoEntidad(1L);
    }

    @Override
    public String crear() {
        try {
            this.entidadFacade.create(this.getNombre(), this.getCodigo(), this.getIdMunicipio(), this.getDireccion(), this.getTelefono(), this.getCorreo(), this.getPaginaWeb(), this.getIdEstado(), this.getFechaAlta(), 2L, this.getFechaVencimiento(fechaAlta), this.getDni(), this.getNombreReserva());
            this.limpiar();
            this.setResultado("successErrorSociedad");
            this.setMsgSuccessError("La sociedad ha sido generada con éxito");
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.limpiar();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorSociedad");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.entidadFacade.create(this.getNombre(), this.getCodigo(), this.getIdMunicipio(), this.getDireccion(), this.getTelefono(), this.getCorreo(), this.getPaginaWeb(), this.getIdEstado(), this.getFechaAlta(), 2L, this.getFechaVencimiento(fechaAlta), this.getDni(), this.getNombreReserva());
            this.setResultado("nuevaSociedad");
            this.limpiar();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorSociedad");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idEntidadAux = Long.parseLong(myRequest.getParameter("id"));
        Entidad entidadAux = this.entidadFacade.find(idEntidadAux);
        this.setId(entidadAux.getId());
        this.setNombre(entidadAux.getNombre());
        this.setCodigo(entidadAux.getCodigo());
        this.setFechaAlta(entidadAux.getFechaAlta());
        this.setEstado(entidadAux.getEstadoEntidad());
        this.setTipoEntidad(entidadAux.getTipoEntidad());
        this.setDireccion(entidadAux.getDireccion());
        this.setTelefono(entidadAux.getTelefono());
        this.setCorreo(entidadAux.getCorreo());
        this.setPaginaWeb(entidadAux.getPaginaWeb());
        this.setMunicipio(entidadAux.getMunicipio());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idEntidadAux = Long.parseLong(myRequest.getParameter("id"));
            this.entidadFacade.remove(idEntidadAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("sociedadConf");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorSociedad");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idEntidadAux = Long.parseLong(myRequest.getParameter("id"));
        Entidad entidadAux = this.entidadFacade.find(idEntidadAux);
        this.setId(entidadAux.getId());
        this.setNombre(entidadAux.getNombre());
        this.setCodigo(entidadAux.getCodigo());
        this.setFechaAlta(entidadAux.getFechaAlta());
        this.setEstado(entidadAux.getEstadoEntidad());
        this.setIdEstado(entidadAux.getEstadoEntidad().getId());
        this.setTipoEntidad(entidadAux.getTipoEntidad());
        this.setIdTipoEntidad(entidadAux.getTipoEntidad().getId());
        this.setDireccion(entidadAux.getDireccion());
        this.setTelefono(entidadAux.getTelefono());
        this.setCorreo(entidadAux.getCorreo());
        this.setPaginaWeb(entidadAux.getPaginaWeb());
        this.setMunicipio(entidadAux.getMunicipio());
        this.setIdMunicipio(entidadAux.getMunicipio().getId());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.entidadFacade.edit(this.getId(), this.getNombre(), this.getCodigo(), this.getIdMunicipio(), this.getDireccion(), this.getTelefono(), this.getCorreo(), this.getPaginaWeb(), this.getIdEstado(), this.getFechaAlta(), 2L, this.getFechaVencimiento(fechaAlta), this.getDni(), this.getNombreReserva());
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.setResultado("successErrorSociedad");
            this.setMsgSuccessError("La sociedad ha sido editado con éxito");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorSociedad");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int diasHabiles(Calendar fechaInicial, Calendar fechaFinal, List<Date> listaFechasNoLaborables) {
        int diffDays = 0;
        boolean diaHabil = false;
        //mientras la fecha inicial sea menor o igual que la fecha final se cuentan los dias
        while (fechaInicial.before(fechaFinal) || fechaInicial.equals(fechaFinal)) {

            if (!listaFechasNoLaborables.isEmpty()) {
                for (Date date : listaFechasNoLaborables) {
                    Date fechaNoLaborablecalendar = fechaInicial.getTime();
                    //si el dia de la semana de la fecha minima es diferente de sabado o domingo
                    if (fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && !fechaNoLaborablecalendar.equals(date)) {
                        //se aumentan los dias de diferencia entre min y max
                        diaHabil = true;
                    } else {
                        diaHabil = false;
                        break;
                    }
                }
            } else {
                if (fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                    //se aumentan los dias de diferencia entre min y max
                    diffDays++;
                }
            }
            if (diaHabil == true) {
                diffDays++;
            }
            //se suma 1 dia para hacer la validacion del siguiente dia.
            fechaInicial.add(Calendar.DATE, 1);
        }
        return diffDays;
    }

    @Override
    public void actualizar() {
        this.limpiar();
    }
}
