/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.Grupo;
import org.ccpm.dpj.entity.Menu;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface GrupoFacadeLocal {

    public void create(Grupo grupo);

    public void edit(Grupo grupo);

    public void remove(Grupo grupo);

    public Grupo find(Object id);

    public List<Grupo> findAll();

    public List<Grupo> findRange(int[] range);

    public int count();

    public List<Grupo> finGrupos(List<Long> gruposId);

    public List<Menu> findAllMenu(Long idGrupo);

    public List<Grupo> findAll(boolean estado);

    public List<Grupo> findAll(String nombreGrupo);

    public List<Grupo> findAll(List<String> gruposId);

    public void remove(Long idGrupo) throws Exception;

    public void remove(Long idGrupo, List<Long> idAcciones) throws Exception;

    public void create(String nombreGrupo, String descripcionGrupo) throws Exception;

    public void edit(Long idGrupo, String nombreGrupo, String descripcionGrupo) throws Exception;

    public void edit(Long idGrupo, List<Long> idMenu) throws Exception;
    
}
