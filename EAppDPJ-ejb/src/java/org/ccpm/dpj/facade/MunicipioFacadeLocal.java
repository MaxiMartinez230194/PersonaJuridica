/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.Municipio;

/**
 *
 * @author matias
 */
@Local
public interface MunicipioFacadeLocal {

    void create(Municipio municipio);

    void edit(Municipio municipio);

    void remove(Municipio municipio);

    Municipio find(Object id);

    List<Municipio> findAll();

    List<Municipio> findRange(int[] range);

    int count();

    public List<Municipio> findAll(boolean estado);

    public List<Municipio> findAll(String leyenda);

    public List<Municipio> findAll(List<Long> municipioId);

    public void create(String leyenda) throws Exception;

    public void remove(Long idMunicipio) throws Exception;

    public void edit(Long idMunicipio, String nombre, Integer ur) throws Exception;
    
}
