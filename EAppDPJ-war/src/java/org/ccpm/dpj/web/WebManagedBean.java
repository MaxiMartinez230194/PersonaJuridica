/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.web;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.ccpm.dpj.entity.Grupo;
import org.ccpm.dpj.entity.Menu;
import org.ccpm.dpj.entity.Usuario;
import org.ccpm.dpj.facade.UsuarioFacadeLocal;
import org.ccpm.dpj.utilidad.MiCipher;
/**
 *
 * @author Alvarenga Angel
 */
@ManagedBean
@SessionScoped
public class WebManagedBean {
    @EJB
    private UsuarioFacadeLocal usuarioFacade;    
    private String nombreUsuario;
    private String claveAcceso;  
    private String resultado;
    private String msgSuccessError;   
    private String title;
    private String images;
    private Usuario usuario; 
    private String nombreGrupo;
    private MiCipher cifra=new MiCipher();
    
        
    /** Creates a new instance of WebManagedBean */
    public WebManagedBean() {
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setMsgSuccessError(String msgSuccessError) {
        this.msgSuccessError = msgSuccessError;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public String getImages() {
        return images;
    }

    public String getMsgSuccessError() {
        return msgSuccessError;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getResultado() {
        return resultado;
    }

    public String getTitle() {
        return title;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String login() {       
        try {
                
            cifra.setPassaEncriptar(this.getClaveAcceso());
            String pass=cifra.getEncriptado();
            Usuario usuarioAux = usuarioFacade.find(this.getNombreUsuario().toLowerCase(), pass);
            this.setUsuario(usuarioAux);
            
            this.setNombreGrupo(usuarioAux.getGrupos().get(0).getNombre());
            this.setMsgSuccessError("");
            this.setResultado("administracion");
        } catch (Exception ex) {
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorLogin");
        }
        return resultado;
    }
    
    public void logout() {
        this.setNombreUsuario(null);
        this.setClaveAcceso(null);
        this.setUsuario(null);
    }
       
    public void limpiar() {
        this.setNombreUsuario(null);
        this.setClaveAcceso(null);        
   }
    
   public List<Menu> getMenu() {        
        List<Menu> listRetorno = new ArrayList<>();        
        List<Grupo> listGrupo = this.getUsuario().getGrupos();
        if (!(listGrupo.isEmpty())) {
            for (Grupo grupoAux : listGrupo) {
                List<Menu> listMenu = grupoAux.getMenus();
                listRetorno.addAll(listMenu);
            }
        }
        listRetorno.addAll(this.getUsuario().getMenus());
        return listRetorno;
    }
    
}


