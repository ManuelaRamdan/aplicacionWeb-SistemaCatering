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
public class Servicio {
    private int id;
    private String nombreServicio;
    private ArrayList<Menu> menus;

    public Servicio(int id, String nombreServicio) {
        this.id = id;
        this.nombreServicio = nombreServicio;
        this.menus = new ArrayList<Menu>();
    }

    public ArrayList<Menu> getMenus() {
        return menus;
    }

    public void setMenus(ArrayList<Menu> menus) {
        this.menus = menus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    
}
