/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemacatering.sistemacatering;

/**
 *
 * @author Usuario
 */
public class Administrador extends Persona {

    private int codAdministrador;

    public Administrador(int codAdministrador, String u, String p) {
        setUsuario(u);
        setPassword(p);
        this.codAdministrador = codAdministrador;
    }
    public int getcodAdministrador() {
        return codAdministrador;
    }

    public void setcodAdministrador(int codCoordinador) {
        this.codAdministrador = codCoordinador;
    }


}
