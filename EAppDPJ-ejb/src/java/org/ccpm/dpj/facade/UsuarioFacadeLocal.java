/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.Grupo;
import org.ccpm.dpj.entity.Usuario;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface UsuarioFacadeLocal {

    public void create(Usuario usuario);

    public void edit(Usuario usuario);

    public void remove(Usuario usuario);

    public Usuario find(Object id);

    public List<Usuario> findAll();

    public List<Usuario> findRange(int[] range);

    public int count();

    public Usuario find(String nombreUsuario, String claveAcceso) throws Exception;

    public List<Usuario> findAll(boolean estado);

    public void create(String apellido, String nombre, String nombreUss, 
            String clave, List<Grupo> lstGrupo) throws Exception;

    public void edit(Long id, String apellido, String nombre, 
            String nombreUsuario, String clave, 
                List<Long> listIdGrupos) throws Exception;

    public void edit(Long id, String claveActual, String claveNueva, 
            String claveConfirma, String nombreUsuario) throws Exception;

    public List<Usuario> findAll(String nombreUsuario);

    public void edit(Long idUsuario, List<Long> idMenu) throws Exception;

    public void remove(Long idUsuario, List<Long> idAcciones) throws Exception;

    public void remove(Long idUsuario) throws Exception;

    public void edit(Long id, String claveActual, String claveNueva, String claveConfirma) throws Exception;
    
}
