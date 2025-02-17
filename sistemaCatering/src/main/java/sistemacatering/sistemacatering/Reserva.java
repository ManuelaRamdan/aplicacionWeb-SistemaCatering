/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemacatering.sistemacatering;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author Usuario
 */
public class Reserva {

    private int codReserva;
    private int codCliente;
    private Date fechaInicioEvento;
    private Date fechaFinEvento;
    private String restriccionesDieteticas;
    private String preferenciaCliente;
    private String tipoServicio;
    private int cantidadPersonas;
    private int precio;
    private String modoDeReserva;
    private Domicilio direccionDeEntrega;
    private boolean estaEntregado;

    public Reserva(int codReserva, int codCliente, Date fechaInicioEvento, Date fechaFinEvento, String restriccionesDieteticas, String preferenciaCliente, String tipoServicio, int cantidadPersonas, int precio, String modoDeReserva, Domicilio direccionDeEntrega, boolean estaEntregado) {
        this.codReserva = codReserva;
        this.codCliente = codCliente;
        this.fechaInicioEvento = fechaInicioEvento;
        this.fechaFinEvento = fechaFinEvento;
        this.restriccionesDieteticas = restriccionesDieteticas;
        this.preferenciaCliente = preferenciaCliente;
        this.tipoServicio = tipoServicio;
        this.cantidadPersonas = cantidadPersonas;
        this.precio = precio;
        this.modoDeReserva = modoDeReserva;
        this.direccionDeEntrega = direccionDeEntrega;
        this.estaEntregado = estaEntregado;
    }

    public int getCodReserva() {
        return codReserva;
    }

    public void setCodReserva(int codReserva) {
        this.codReserva = codReserva;
    }

    public int getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(int codCliente) {
        this.codCliente = codCliente;
    }

    public Date getFechaInicioEvento() {
        return fechaInicioEvento;
    }

    public void setFechaInicioEvento(Date fechaInicioEvento) {
        this.fechaInicioEvento = fechaInicioEvento;
    }

    public Date getFechaFinEvento() {
        return fechaFinEvento;
    }

    public void setFechaFinEvento(Date fechaFinEvento) {
        this.fechaFinEvento = fechaFinEvento;
    }



    public String getPreferenciaCliente() {
        return preferenciaCliente;
    }

    public void setPreferenciaCliente(String preferenciaCliente) {
        this.preferenciaCliente = preferenciaCliente;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(int cantidadPersonas) {
        this.cantidadPersonas = cantidadPersonas;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getModoDeReserva() {
        return modoDeReserva;
    }

    public void setModoDeReserva(String modoDeReserva) {
        this.modoDeReserva = modoDeReserva;
    }

    public Domicilio getDireccionDeEntrega() {
        return direccionDeEntrega;
    }

    public void setDireccionDeEntrega(Domicilio direccionDeEntrega) {
        this.direccionDeEntrega = direccionDeEntrega;
    }

    public boolean isEstaEntregado() {
        return estaEntregado;
    }

    public void setEstaEntregado(boolean estaEntregado) {
        this.estaEntregado = estaEntregado;
    }

    public String getRestriccionesDieteticas() {
        return restriccionesDieteticas;
    }

    public void setRestriccionesDieteticas(String restriccionesDieteticas) {
        this.restriccionesDieteticas = restriccionesDieteticas;
    }

}
