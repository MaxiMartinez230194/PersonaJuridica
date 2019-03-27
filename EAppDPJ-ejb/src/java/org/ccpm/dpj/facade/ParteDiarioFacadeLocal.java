/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.Boleta;
import org.ccpm.dpj.entity.ParteDiario;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface ParteDiarioFacadeLocal {

   public void create(ParteDiario parteDiario);

    public void edit(ParteDiario parteDiario);

    public void remove(ParteDiario parteDiario);

    public ParteDiario find(Object id);

    public List<ParteDiario> findAll();

    public List<ParteDiario> findRange(int[] range);

    public int count();

    public List<ParteDiario> findAll(Date fechaDesde, Date fechaHasta);

    public List<ParteDiario> findAll(String url);

    public void create(String url, Date fecha, Date fechaPago,Date fechaDeposito,List<Boleta> boletas) throws Exception;

    public void remove(Long idParteDiario) throws Exception;

    public void edit(Long idParteDiario, String url, Date fecha) throws Exception;
    
    public String registrarPagosBoletas(Long nroBoleta,Date fechaPago);
    
    public List <ParteDiario> devuelveParteDiariosPorMes(int mes,int anio) ;
    
    public boolean existeParteDiario(String url) ;
     
    public boolean existeParteDiarioConFechaDeposito(Date fecha);
    
    public void setearFechaPagoUltimoParteDiario(Date fechaPago);
}
