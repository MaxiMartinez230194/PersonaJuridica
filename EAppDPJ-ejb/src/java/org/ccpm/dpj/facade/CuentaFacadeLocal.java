/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.Cuenta;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface CuentaFacadeLocal {

    public void create(Cuenta cuenta);

    public void edit(Cuenta cuenta);

    public void remove(Cuenta cuenta);

    public Cuenta find(Object id);

    public List<Cuenta> findAll();

    public List<Cuenta> findRange(int[] range);

    public int count();

    public List<Cuenta> findAll(boolean estado);

    public List<Cuenta> findAll(String denominacion);

    public void create(String denominacion, String nro, Long idTipoCuenta, Long idBanco) throws Exception;

    public void remove(Long idCuenta) throws Exception;

    public void edit(Long idCuenta, String denominacion) throws java.lang.Exception;
    
}
