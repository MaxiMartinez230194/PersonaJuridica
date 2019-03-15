/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.Banco;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface BancoFacadeLocal {

   public void create(Banco banco);

    public void edit(Banco banco);

    public void remove(Banco banco);

    public Banco find(Object id);

    public List<Banco> findAll();

    public List<Banco> findRange(int[] range);

    public int count();

    public List<Banco> findAll(boolean estado);

    public List<Banco> findAll(String nombre);

    public void create(String nombre, String domicilio) throws Exception;

    public void remove(Long idBanco) throws Exception;

    public void edit(Long idBanco, String nombre, String domicilio) throws Exception;
    
}
