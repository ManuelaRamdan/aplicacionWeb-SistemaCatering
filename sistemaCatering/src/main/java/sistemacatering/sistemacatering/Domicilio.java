/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemacatering.sistemacatering;

/**
 *
 * @author Usuario
 */
public class Domicilio {

    private int id;
    private String calle;
    private int altura;
    private String barrio;

    public Domicilio(int id, String calle, int altura, String barrio) {
        this.calle = calle;
        this.altura = altura;
        this.barrio = barrio;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    @Override
    public String toString() {
        return calle + " " + altura + ", " + barrio;
    }

}
