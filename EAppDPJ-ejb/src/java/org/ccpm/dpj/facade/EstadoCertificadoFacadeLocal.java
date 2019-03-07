/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.EstadoCertificado;


@Local
public interface EstadoCertificadoFacadeLocal {

    public void create(EstadoCertificado estadoCertificado);

    public void edit(EstadoCertificado estadoCertificado);

    public void remove(EstadoCertificado estadoCertificado);

    public EstadoCertificado find(Object id);

    public List<EstadoCertificado> findAll();

    public List<EstadoCertificado> findRange(int[] range);

    public int count();

    public List<EstadoCertificado> findAll(boolean estado);

    public List<EstadoCertificado> findAll(String nombre);

    public void create(String nombre) throws Exception;

    public void remove(Long idEstado) throws Exception;

    public void edit(Long idEstado, String nombre) throws Exception;
    
}
