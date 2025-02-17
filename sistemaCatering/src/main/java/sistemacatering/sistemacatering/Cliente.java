/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemacatering.sistemacatering;

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

    // Constructor
    public Cliente(int id, String nombre, String apellido, String telReferencia, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telReferencia = telReferencia;
        this.email = email;
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


    

