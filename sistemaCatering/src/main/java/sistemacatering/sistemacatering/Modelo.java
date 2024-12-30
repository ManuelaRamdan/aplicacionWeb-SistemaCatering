/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemacatering.sistemacatering;

/**
 *
 * @author MANUELA
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class Modelo {

    private String jdbcDriver;
    private String dbName;
    private String urlRoot;
    private ArrayList<Persona> resultado;
    private ActionListener listener;

    public Modelo(String url, String dbName) {
        jdbcDriver = "com.mysql.cj.jdbc.Driver";
        urlRoot = "jdbc:mysql://" + url + "/";
        this.dbName = dbName;
        listener = null;
        resultado = new ArrayList<>();
        try {
            Class.forName(jdbcDriver);
        } catch (ClassNotFoundException e) {
            reportException(e.getMessage());
        }
    }

    public ArrayList<Persona> getResultado() {
        return resultado;
    }

    private void reportException(String exception) {
        if (listener != null) {
            ActionEvent evt = new ActionEvent(this, 0, exception);
            listener.actionPerformed(evt);
        }
    }

    public void addExceptionListener(ActionListener listener) {
        this.listener = listener;
    }

    public String verificarUsuario(String usuario, String password) {
        String id = null;
        try {
            Connection con = DriverManager.getConnection(urlRoot + dbName, "", "");
            Statement stmt = con.createStatement();
            stmt.execute("SELECT persona.id FROM Persona WHERE persona.usuario ='" + usuario + "' and persona.password ='" + password + "'");
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                id = rs.getString(1);
            }
            con.close();
        } catch (SQLException e) {
            reportException(e.getMessage());
        }

        return id;
    }

    public String buscarTipoUsuario(String rol) {
        String tipo = null;
        try {
            Connection con = DriverManager.getConnection(urlRoot + dbName, "", "");
            Statement stmt = con.createStatement();
            stmt.execute("SELECT\n"
                    + "    p.id AS persona_id,\n"
                    + "    CASE\n"
                    + "        WHEN a.id IS NOT NULL THEN 'Administrador'\n"
                    + "        WHEN c.id IS NOT NULL THEN 'Cliente'\n"
                    + "        WHEN co.id IS NOT NULL THEN 'Coordinador'\n"
                    + "        ELSE 'Otro'\n"
                    + "    END AS rol\n"
                    + "FROM Persona p\n"
                    + "LEFT JOIN Administrador a ON p.id = a.persona_id\n"
                    + "LEFT JOIN Cliente c ON p.id = c.persona_id\n"
                    + "LEFT JOIN Coordinador co ON p.id = co.persona_id\n"
                    + "WHERE p.id = '"+rol +"';");
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                tipo = rs.getString(2);
            }
            con.close();
        } catch (SQLException e) {
            reportException(e.getMessage());
        }

        return tipo;

    }
}
