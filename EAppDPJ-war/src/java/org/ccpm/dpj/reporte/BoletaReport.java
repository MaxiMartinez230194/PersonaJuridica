/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.reporte;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author Alvarenga Angel
 */
public class BoletaReport {
   private JasperPrint masterPrint = null;
    private static java.net.URL url = null;
    private static JasperReport masterReport = null;
    private static Map parametros = new HashMap();
    private static Connection conexion;
        
    public BoletaReport() {
    }

    public JasperPrint imprimirBoleta(String nroBoleta) throws Exception, JRException {
        try { 
          
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/dpj");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/ccpm/dpj/reporte/reporte.jasper");
            parametros.put("Boleta", nroBoleta); 
            parametros.put("pathimagen", getClass().getResource("escudito.jpg").getPath());
           
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {            
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }
    
    public JasperPrint imprimirReporteDiario(Date fecha,BigDecimal comis) throws Exception, JRException {
        try {     
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/dpj");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/ccpm/dpj/reporte/diario.jasper");
            parametros.put("fecha", fecha); 
            parametros.put("pathimagen", getClass().getResource("escudito.jpg").getPath());
            parametros.put("comis",comis );
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {            
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    
}
    
     public JasperPrint imprimirReporteMensual(int mes,int anio,BigDecimal comis) throws Exception, JRException {
        try {     
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/dpj");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/ccpm/dpj/reporte/mensual.jasper");
            parametros.put("mes", mes); 
            parametros.put("anio", anio);
            parametros.put("comis", comis);
            parametros.put("pathimagen", getClass().getResource("escudito.jpg").getPath());
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {            
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    
}
}