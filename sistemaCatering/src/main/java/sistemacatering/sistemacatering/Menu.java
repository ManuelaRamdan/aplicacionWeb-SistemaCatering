/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemacatering.sistemacatering;

import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class Menu {

    private int id;
    private String nombreMenu;
    private int precio;
    private ArrayList<Plato> platosEntrada;
    private ArrayList<Plato> platosPrincipal;

    public Menu(int id, String nombreMenu, int precio) {
        this.id = id;
        this.nombreMenu = nombreMenu;
        this.precio = precio;
        platosPrincipal = new ArrayList<Plato>();
        platosEntrada = new ArrayList<Plato>();
    }

    public ArrayList<Plato> getPlatosEntrada() {
        return platosEntrada;
    }

    public void setPlatosEntrada(ArrayList<Plato> platosEntrada) {
        this.platosEntrada = platosEntrada;
    }

    public ArrayList<Plato> getPlatosPrincipal() {
        return platosPrincipal;
    }

    public void setPlatosPrincipal(ArrayList<Plato> platosPrincipal) {
        this.platosPrincipal = platosPrincipal;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

}
