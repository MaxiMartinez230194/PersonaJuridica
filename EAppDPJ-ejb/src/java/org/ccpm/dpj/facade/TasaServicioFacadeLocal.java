/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.TasaServicio;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface TasaServicioFacadeLocal {

    public void create(TasaServicio tasaServicio);

    public void edit(TasaServicio tasaServicio);

    public void remove(TasaServicio tasaServicio);

    public TasaServicio find(Object id);

    public List<TasaServicio> findAll();

    public List<TasaServicio> findRange(int[] range);

    public int count();

    public List<TasaServicio> findAll(boolean estado);

    public List<TasaServicio> findAll(String nombre);

    public void create(String nombre, Double monto, boolean visible) throws Exception;

    public void remove(Long idTasaServicio) throws Exception;

    public void edit(Long idTasa, String nombre, Double monto, boolean visible) throws Exception;

    public List<TasaServicio> findAll(boolean estado, boolean visible);
    
}
