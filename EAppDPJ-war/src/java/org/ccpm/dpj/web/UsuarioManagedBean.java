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
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.ccpm.dpj.entity.ActionItem;
import org.ccpm.dpj.entity.Grupo;
import org.ccpm.dpj.entity.Usuario;
import org.ccpm.dpj.facade.GrupoFacadeLocal;
import org.ccpm.dpj.facade.UsuarioFacadeLocal;
import org.ccpm.dpj.utilidad.MiCipher;


/**
 *
 * @author Alvarenga Angel
 */
@ManagedBean
@SessionScoped
public class UsuarioManagedBean extends UtilManagedBean implements Serializable {
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private GrupoFacadeLocal grupoFacade;
    private Usuario usuario;
    private String nombre;
    private String claveAcceso;
    private String apellido;
    private String nombreP;
    private String passActual;
    private String passConfirmar;
    private String email;
    private String sexo;
    private String foto;
    private List <Long> idItems = new ArrayList<>();
    private List <ActionItem> lstActionItems = new ArrayList <>();
    private List <Long> idItemsIzquierda;
    private List <Long> idItemsDerecha;
    private List <SelectItem> selecteditemsIzquierda;
    private List <SelectItem> selecteditemsDerecha;
    private MiCipher cifra=new MiCipher();
    
        
    
    
    /** Creates a new instance of UsuarioManagedBean */
    public UsuarioManagedBean() {
        this.selecteditemsDerecha = new ArrayList <>();
        this.selecteditemsIzquierda = new ArrayList <>();
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
                    if (accionAux.getNombre().equalsIgnoreCase("nuevoUsuario")) {
                        this.setAlta(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("editarUsuario")) {
                        this.setModificacion(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("borrarUsuario")) {
                        this.setBaja(true);
                    }
                    if (accionAux.getNombre().equalsIgnoreCase("detalleUsuario")) {
                        this.setDetalle(true);
                    }
                    
                    //System.out.println("contraseñaa-->"+accionAux.getNombre());
                    
                    
                }
            }
        } catch (Exception e) {
            throw new Exception("Error al recuperar las acciones del usuario");
        }
    }

    

    
    
    public MiCipher getCifra() {
        return cifra;
    }

    public void setCifra(MiCipher cifra) {
        this.cifra = cifra;
    }
    
    

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setClaveAcceso(String claveAcceso) {

      
      this.claveAcceso = claveAcceso;
    }

    public void setLstActionItems(List<ActionItem> lstActionItems) {
        this.lstActionItems = lstActionItems;
    }

    public void setIdItems(List<Long> idItems) {
        this.idItems = idItems;
    }

    public void setIdItemsIzquierda(List<Long> idItemsIzquierda) {
        this.idItemsIzquierda = idItemsIzquierda;
    }

    public void setIdItemsDerecha(List<Long> idItemsDerecha) {
        this.idItemsDerecha = idItemsDerecha;
    }

    public void setNombreP(String nombreP) {
        this.nombreP = nombreP;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setSelecteditemsIzquierda(List<SelectItem> selecteditemsIzquierda) {
        this.selecteditemsIzquierda = selecteditemsIzquierda;
    }

    public void setSelecteditemsDerecha(List<SelectItem> selecteditemsDerecha) {
        this.selecteditemsDerecha = selecteditemsDerecha;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNombreP() {
        return nombreP;
    }

    public List<SelectItem> getSelecteditemsIzquierda() {
        return selecteditemsIzquierda;
    }

    public List<SelectItem> getSelecteditemsDerecha() {
        return selecteditemsDerecha;
    }

    public List<ActionItem> getLstActionItems() {
        return lstActionItems;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<Long> getIdItems() {
        return idItems;
    }

    public List<Long> getIdItemsIzquierda() {
        return idItemsIzquierda;
    }

    public List<Long> getIdItemsDerecha() {
        return idItemsDerecha;
    }

    public String getClaveAcceso() {
//        sr.nextBytes(iv);
//        String passDescifrada=cifra.decriptar(clave, iv, claveAcceso);
//        System.out.println("des "+passDescifrada);
        return claveAcceso;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getPassConfirmar() {
        return passConfirmar;
    }

    public void setPassConfirmar(String passConfirmar) {
        this.passConfirmar = passConfirmar;
    }

    public String getPassActual() {
        return passActual;
    }

    public void setPassActual(String passActual) {
        this.passActual = passActual;
    }
    
    
    
    @Override
    public List <Usuario> getListElements() {
        if (this.getNombreBuscar().equals("")) {
            this.setList(usuarioFacade.findAll(true));
        } else {
            this.setList(usuarioFacade.findAll(this.getNombreBuscar()));
        }
        return (List <Usuario>) this.getList();
    }

    @Override
    public void limpiar() {
        this.setApellido(null);
        this.setNombreP(null);
        this.setNombre(null);
        this.setClaveAcceso(null);
        this.getSelecteditemsIzquierda().clear();
        this.getSelecteditemsDerecha().clear();
    }

    @Override
    public String crear() {
        try {
            List <Grupo> lstAux = this.grupoFacade.finGrupos(idItems); 
            cifra.setPassaEncriptar(claveAcceso);
            String passCifrada=cifra.getEncriptado();
            System.out.println("cif    "+passCifrada);
            this.usuarioFacade.create(this.getApellido(), this.getNombreP(),
            this.getNombre(), passCifrada, lstAux);
            this.setTitle("Proceso Completo ...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.setResultado("successErrorUsuario");
            this.setMsgSuccessError("El usuario ha sido generado con éxito");
            this.limpiar();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorUsuario");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            List <Grupo> lstAux = this.grupoFacade.finGrupos(idItems);   
            cifra.setPassaEncriptar(claveAcceso);   // cifrado
            String passCifrada=cifra.getEncriptado(); //cifrado
            this.usuarioFacade.create(this.getApellido(), this.getNombreP(),
            this.getNombre(),passCifrada, lstAux);
            this.setResultado("nuevoUsuario");
            this.limpiar();
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorUsuario");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idUsuarioAux = Long.parseLong(myRequest.getParameter("id"));
        Usuario usuarioAux = this.usuarioFacade.find(idUsuarioAux);
        this.setUsuario(usuarioAux);
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idUsuarioAux = Long.parseLong(myRequest.getParameter("id"));
            this.usuarioFacade.remove(idUsuarioAux);
            this.limpiar();
            this.setResultado("usuarioConf");
        } catch (Exception ex) {
            this.limpiar();
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorUsuario");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idUsuarioAux = Long.parseLong(myRequest.getParameter("id"));
        Usuario usuarioAux = this.usuarioFacade.find(idUsuarioAux);
        this.setId(usuarioAux.getId());
        this.setNombreP(usuarioAux.getNombreUsuario());
        this.setClaveAcceso(usuarioAux.getClaveAcceso());
        this.setApellido(usuarioAux.getApellido());
        this.setNombre(usuarioAux.getNombre());
        this.gruposDisponibles(usuarioAux);
        this.gruposSeleccionados(usuarioAux);
    }
    
    public void gruposDisponibles(Usuario usuario) {     
        this.selecteditemsDerecha.clear();
        List <Grupo> listGruposUsuario = usuario.getGrupos();
        List <Grupo> listGrupos = grupoFacade.findAll(true);
        listGrupos.removeAll(listGruposUsuario);
        if (!(listGrupos.isEmpty())) {
            for (Grupo grupoAux : listGrupos) {
                this.selecteditemsDerecha.add(new SelectItem(grupoAux.getId(), grupoAux.getNombre()));
            }
        }        
    }
    
    public void gruposSeleccionados(Usuario usuario) {
        this.selecteditemsIzquierda.clear();
        List <Grupo> listGrupos = usuario.getGrupos();
        if (!(listGrupos.isEmpty())) {
            for (Grupo grupoAux : listGrupos) {
                this.selecteditemsIzquierda.add(new SelectItem(grupoAux.getId(), grupoAux.getNombre()));
            }
        }
    }
    
    @Override
    public String guardarEdicion() {
        try {            
            List <Long> lstAux = new ArrayList <>();
            for (SelectItem object : this.selecteditemsIzquierda) {
                lstAux.add((Long)object.getValue());
            }            
            cifra.setPassaEncriptar(claveAcceso);
            String passCifrada=cifra.getEncriptado();
            usuarioFacade.edit(this.getId(), this.getApellido(), this.getNombre(),
                    this.getNombre(), passCifrada, lstAux);
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.setResultado("successErrorUsuario");
            this.setMsgSuccessError("El usuario ha sido editado con éxito");
        } catch (Exception ex) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorUsuario");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        List <SelectItem> selectItems = new ArrayList <>();
        List <Grupo> listGrupo = grupoFacade.findAll(true);
        if (!(listGrupo.isEmpty())) {
            for (Grupo grupoAux : listGrupo) {
                selectItems.add(new SelectItem(grupoAux.getId(), grupoAux.getNombre()));
            }
        }
        return selectItems;
    }
    
    public void prepararParaEditarContrasena(){
        WebManagedBean sessionBean = this.getSessionBean();
        Usuario usuarioAux = sessionBean.getUsuario();
        this.setId(usuarioAux.getId());
        this.setApellido(usuarioAux.getApellido());
        this.setNombre(usuarioAux.getNombre());
        this.setNombreP(usuarioAux.getNombreUsuario());
        this.setPassActual(usuarioAux.getClaveAcceso());
        this.gruposDisponibles(usuarioAux);
        this.gruposSeleccionados(usuarioAux);
    
    }
    
    public String guardarConfiguracion(){
        try {
            usuarioFacade.edit(this.getId(), this.getPassActual(), this.getClaveAcceso(), this.getPassConfirmar());
            this.setTitle("Proceso Completo...");
            this.setImages("glyphicon glyphicon-ok-circle");
            this.setResultado("successErrorConfiguracion");
            this.setMsgSuccessError("El usuario ha sido editado con éxito");
            this.limpiar();
        } catch (Exception e) {
            this.setTitle("Resultado del Chequeo...");
            this.setImages("glyphicon glyphicon-remove-circle");
            this.setMsgSuccessError(e.getMessage());
            this.setResultado("successErrorConfiguracion");
            this.limpiar();
            
        }
        
        return this.getResultado();
    
    }

}