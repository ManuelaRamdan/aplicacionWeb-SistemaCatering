package sistemacatering.sistemacatering;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Usuario
 */
public class Coordinador extends Persona {

    private int codCoordinador;

    public Coordinador(int codCoordinador, String usuario, String password) {
        setUsuario(usuario);
        setPassword(password);
        this.codCoordinador = codCoordinador;
    }
    public int getCodCoordinador() {
        return codCoordinador;
    }

    public void setCodCoordinador(int codCoordinador) {
        this.codCoordinador = codCoordinador;
    }


}
