/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Alvarenga Angel
 */
public abstract class UtilManagedBean {
   private String nombreBuscar = "";
    private boolean alta, baja, modificacion, detalle;
    private List<?> list;
    private Long id;
    private String resultado;
    private String title;
    private String images;
    private Double total=0.0;
    private String msgSuccessError;
    private List<Long> selectedItemsIzquierda;
    private List<Long> selectedItemsDerecha;
    private List<SelectItem> itemsIzquierda;
    private List<SelectItem> itemsDerecha;
    Boolean bandera=true;

    public UtilManagedBean() {
        this.itemsDerecha = new ArrayList<SelectItem>();
        this.itemsIzquierda = new ArrayList<SelectItem>();
        this.selectedItemsIzquierda = new ArrayList<Long>();
        this.selectedItemsDerecha = new ArrayList<Long>();
    }

    public Boolean getBandera() {
        return bandera;
    }

    public void setBandera(Boolean bandera) {
        this.bandera = bandera;
    }

    
    
    public abstract List<?> getListElements();

    public abstract List<SelectItem> getSelectItems();

    public abstract void limpiar();

    public abstract String crear();

    public abstract String crearOtro();

    public abstract void verDetalle();

    public abstract void guardarBorrado();

    public abstract void prepararParaEditar();

    public abstract String guardarEdicion();

    public void setAlta(boolean alta) {
        this.alta = alta;
    }

    public void setBaja(boolean baja) {
        this.baja = baja;
    }

    public void setDetalle(boolean detalle) {
        this.detalle = detalle;
    }

    public void setModificacion(boolean modificacion) {
        this.modificacion = modificacion;
    }

    public void setNombreBuscar(String nombreBuscar) {
        this.nombreBuscar = nombreBuscar;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setItemsDerecha(List<SelectItem> itemsDerecha) {
        this.itemsDerecha = itemsDerecha;
    }

    public void setItemsIzquierda(List<SelectItem> itemsIzquierda) {
        this.itemsIzquierda = itemsIzquierda;
    }

    public void setSelectedItemsDerecha(List<Long> selectedItemsDerecha) {
        this.selectedItemsDerecha = selectedItemsDerecha;
    }

    public void setSelectedItemsIzquierda(List<Long> selectedItemsIzquierda) {
        this.selectedItemsIzquierda = selectedItemsIzquierda;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setMsgSuccessError(String msgSuccessError) {
        this.msgSuccessError = msgSuccessError;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return images;
    }

    public String getMsgSuccessError() {
        return msgSuccessError;
    }

    public String getTitle() {
        return title;
    }

    public String getResultado() {
        return resultado;
    }

    public List<SelectItem> getItemsDerecha() {
        return itemsDerecha;
    }

    public List<SelectItem> getItemsIzquierda() {
        return itemsIzquierda;
    }

    public List<Long> getSelectedItemsDerecha() {
        return selectedItemsDerecha;
    }

    public List<Long> getSelectedItemsIzquierda() {
        return selectedItemsIzquierda;
    }

    public Long getId() {
        return id;
    }

    public List<?> getList() {
        return list;
    }

    protected WebManagedBean getSessionBean() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        return (WebManagedBean) myRequest.getSession().getAttribute("webManagedBean");
    }

    public boolean isAlta() {
        return alta;
    }

    public boolean isBaja() {
        return baja;
    }

    public boolean isDetalle() {
        return detalle;
    }

    public boolean isModificacion() {
        return modificacion;
    }

    public String getNombreBuscar() {
        return nombreBuscar;
    }

    public void aplicarFiltro() {
        this.setBandera(true);
        this.getListElements();
    }

    public void actualizar() {
        this.limpiar();
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    
    public void agregar() {
        Iterator iteratorSelected = getSelectedItemsIzquierda().iterator();
        while (iteratorSelected.hasNext()) {
            SelectItem o;
            Long item = Long.parseLong(iteratorSelected.next().toString());
            Iterator iterator = getItemsIzquierda().iterator();
            while (iterator.hasNext()) {
                o = (SelectItem) iterator.next();
                if (Long.parseLong(o.getValue().toString()) == item) {
                    getItemsIzquierda().remove(o);
                    getItemsDerecha().add(o);
                    break;
                }
            }
        }
        this.selectedItemsIzquierda.clear();
    }

    public void borrar() {
        Iterator iteratorSelected = getSelectedItemsDerecha().iterator();
        while (iteratorSelected.hasNext()) {
            SelectItem o;
            Long item = Long.parseLong(iteratorSelected.next().toString());
            Iterator iterator = getItemsDerecha().iterator();
            while (iterator.hasNext()) {
                o = (SelectItem) iterator.next();
                if (Long.parseLong(o.getValue().toString()) == item) {
                    getItemsDerecha().remove(o);
                    getItemsIzquierda().add(o);
                    break;
                }
            }
        }
        this.selectedItemsDerecha.clear();
    }

    public void agregarTodos() {
        Object object;
        int size = getItemsIzquierda().size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                object = getItemsIzquierda().get(0);
                getItemsIzquierda().remove(object);
                getItemsDerecha().add((SelectItem) object);
            }
            this.selectedItemsIzquierda.clear();
        }
    }

    public void borrarTodos() {
        Object object;
        int size = getItemsDerecha().size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                object = getItemsDerecha().get(0);
                getItemsDerecha().remove(object);
                getItemsIzquierda().add((SelectItem) object);
            }
            this.selectedItemsDerecha.clear();
        }
    }

}
