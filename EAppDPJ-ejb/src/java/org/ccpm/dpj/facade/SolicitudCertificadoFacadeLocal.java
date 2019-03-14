/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.SolicitudCertificado;

@Local
public interface SolicitudCertificadoFacadeLocal {

    public void create(SolicitudCertificado solicitudCertificado);

    public void edit(SolicitudCertificado solicitudCertificado);

    public void remove(SolicitudCertificado solicitudCertificado);

    public SolicitudCertificado find(Object id);

    public List<SolicitudCertificado> findAll();

    public List<SolicitudCertificado> findRange(int[] range);

    public int count();

    public List<SolicitudCertificado> findAll(boolean estado);
    
    public List<SolicitudCertificado> findByNroBoleta(Long nroBoleta1, Long nroBoleta2);

    public List<SolicitudCertificado> findAll(String nombre);

    public void create(Long idEntidad, Long idEstadoCertificado, Long nroBoleta1, Long nroBoleta2) throws Exception;

    public void solicitar(Long idEntidad, Long nroBoleta1, Long nroBoleta2) throws Exception;

    public void remove(Long idSolicitud) throws Exception;

    public void edit(Long idSolicitud, Long idEntidad, Long idEstadoCertificado, Long idBoleta1, Long idBoleta2) throws Exception;

}
