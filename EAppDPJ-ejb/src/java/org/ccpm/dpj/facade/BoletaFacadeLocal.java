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
import org.ccpm.dpj.utilidad.TasaServicioUI;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface BoletaFacadeLocal {

    public void create(Boleta boleta);

    public void edit(Boleta boleta);

    public void remove(Boleta boleta);

    public Boleta find(Object id);

    public List<Boleta> findAll();

    public List<Boleta> findRange(int[] range);
    
    public boolean verificarPago(Long nroCorrelativo1, Long nroCorrelativo2);

    public int count();

    public List <Long> create(List<TasaServicioUI> lstTasaUI) throws Exception;

    public List<Boleta> findAll(boolean estado);

    public List<Boleta> findAll(Long nroCorrelativo, Date fechaEmDesde, 
            Date fechaEmHasta, Date fechaPagoDesde, 
            Date fechaPagoHasta, Long idEstado);
    
    
    public Boleta findAllIn(boolean estado, Long idNroCorrelativo);
    
    public List <ParteDiario> findAllBoletas(Long idParteDiario);
    
    
    
}
