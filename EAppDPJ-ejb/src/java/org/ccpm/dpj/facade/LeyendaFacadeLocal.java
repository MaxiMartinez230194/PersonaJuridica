/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.Leyenda;


@Local
public interface LeyendaFacadeLocal {

    public void create(Leyenda leyenda);

    public void edit(Leyenda leyenda);

    public void remove(Leyenda leyenda);

    public Leyenda find(Object id);

    public List<Leyenda> findAll();

    public List<Leyenda> findRange(int[] range);

    public int count();

    public List<Leyenda> findAll(boolean estado);

    public List<Leyenda> findAll(String nombre);
    
    public Leyenda findByAnio(String anio);

    public void create(String anio, String nombre) throws Exception;

    public void remove(Long idLeyenda) throws Exception;

    public void edit(Long idLeyenda, String anio, String nombre) throws Exception;
    
}
