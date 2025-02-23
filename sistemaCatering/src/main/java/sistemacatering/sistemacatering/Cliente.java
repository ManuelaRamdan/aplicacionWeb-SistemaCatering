/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemacatering.sistemacatering;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class Cliente extends Persona {

    private int id;
    private String nombre;
    private String apellido;
    private String telReferencia;
    private String email;
    private ArrayList<Reserva> reservas;
    private int persona_id;

    // Constructor
    public Cliente(int id, String nombre, String apellido, String telReferencia, String email, int persona_id, String usuario, String password) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telReferencia = telReferencia;
        this.email = email;
        reservas = new ArrayList<Reserva>();
        this.persona_id = persona_id;
        setUsuario(usuario);
        setPassword(password);
    }

    public int getPersona_id() {
        return persona_id;
    }

    public void setPersona_id(int persona_id) {
        this.persona_id = persona_id;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(ArrayList<Reserva> reservas) {
        this.reservas = reservas;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelReferencia() {
        return telReferencia;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTelReferencia(String telReferencia) {
        this.telReferencia = telReferencia;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
