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
                    + "WHERE p.id = '" + rol + "';");
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

    public String obtenerNombreAdministrador(String idAdministrador) {
        String nombre = "Administrador";
        try {
            Connection con = DriverManager.getConnection(urlRoot + dbName, "", "");
            String query = "SELECT nombre FROM Persona WHERE id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, idAdministrador);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nombre = rs.getString("nombre");
            }

            con.close();
        } catch (SQLException e) {
            reportException(e.getMessage());
        }
        return nombre;
    }

    public boolean registrarCoordinador(String usuario, String password) {
        boolean registrado = false;
        Connection con = null;
        PreparedStatement persona = null;
        PreparedStatement coordinador = null;
        ResultSet idGenerado = null;

        try {
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            con.setAutoCommit(false); // Iniciar transacción, puse false para que los cambios que se le hace a la bd no sea de inmediato

            // Insertar en Persona
            String sqlPersona = "INSERT INTO Persona (usuario, password) VALUES (?, ?)";
            persona = con.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);
            persona.setString(1, usuario);
            persona.setString(2, password);
            int seInserto = persona.executeUpdate();

            if (seInserto == 0) {
                throw new SQLException("No se pudo insertar en Persona.");
            }

            // Obtener el ID generado
            idGenerado = persona.getGeneratedKeys();
            int personaId = 0;
            if (idGenerado.next()) {
                personaId = idGenerado.getInt(1);
            }

            // Insertar en Coordinador
            String sqlCoordinador = "INSERT INTO Coordinador (persona_id) VALUES (?)";
            coordinador = con.prepareStatement(sqlCoordinador);
            coordinador.setInt(1, personaId);
            coordinador.executeUpdate();

            con.commit(); // Confirmar transacción
            registrado = true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // Revertir cambios en caso de error
                } catch (SQLException ex) {
                    reportException(ex.getMessage());
                }
            }
            reportException(e.getMessage());
        } finally {
            try {
                if (persona != null) {
                    persona.close();
                }
                if (coordinador != null) {
                    coordinador.close();
                }
                if (idGenerado != null) {
                    idGenerado.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                reportException(e.getMessage());
            }
        }
        return registrado;
    }

    public boolean registrarAdministrador(String usuario, String password) {
        boolean registrado = false;
        Connection con = null;
        PreparedStatement persona = null;
        PreparedStatement administrador = null;
        ResultSet idGenerado = null;

        try {
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            con.setAutoCommit(false); // Iniciar transacción, puse false para que los cambios que se le hace a la bd no sea de inmediato

            // Insertar en Persona
            String sqlPersona = "INSERT INTO Persona (usuario, password) VALUES (?, ?)";
            persona = con.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);
            persona.setString(1, usuario);
            persona.setString(2, password);
            int seInserto = persona.executeUpdate();

            if (seInserto == 0) {
                throw new SQLException("No se pudo insertar en Persona.");
            }

            // Obtener el ID generado
            idGenerado = persona.getGeneratedKeys();
            int personaId = 0;
            if (idGenerado.next()) {
                personaId = idGenerado.getInt(1);
            }

            // Insertar en Coordinador
            String sqlAdministrador = "INSERT INTO Administrador (persona_id) VALUES (?)";
            administrador = con.prepareStatement(sqlAdministrador);
            administrador.setInt(1, personaId);
            administrador.executeUpdate();

            con.commit(); // Confirmar transacción
            registrado = true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // Revertir cambios en caso de error
                } catch (SQLException ex) {
                    reportException(ex.getMessage());
                }
            }
            reportException(e.getMessage());
        } finally {
            try {
                if (persona != null) {
                    persona.close();
                }
                if (administrador != null) {
                    administrador.close();
                }
                if (idGenerado != null) {
                    idGenerado.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                reportException(e.getMessage());
            }
        }
        return registrado;
    }

    public boolean registrarCliente(String usuario, String password, String nombre, String apellido, String telefono, String email) {
        boolean registrado = false;
        Connection con = null;
        PreparedStatement persona = null;
        PreparedStatement cliente = null;
        ResultSet idGenerado = null;

        try {
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            con.setAutoCommit(false); // Iniciar transacción

            // Insertar en Persona
            String sqlPersona = "INSERT INTO Persona (usuario, password) VALUES (?, ?)";
            persona = con.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);
            persona.setString(1, usuario);
            persona.setString(2, password);
            int seRegistro = persona.executeUpdate();

            if (seRegistro == 0) {
                throw new SQLException("No se pudo insertar en Persona.");
            }

            // Obtener el ID generado
            idGenerado = persona.getGeneratedKeys();
            int personaId = 0;
            if (idGenerado.next()) {
                personaId = idGenerado.getInt(1);
            }

            String sqlCodCliente = "SELECT MAX(codCliente) FROM Cliente";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCodCliente);
            int maxCodCliente = 0;
            if (rs.next()) {
                maxCodCliente = rs.getInt(1); // Obtener el máximo valor de codCliente
            }
            int codCliente = maxCodCliente + 1; // Incrementar el valor máximo encontrado

            // Insertar en Cliente
            String sqlCliente = "INSERT INTO Cliente (codCliente, nombre, apellido, telReferencia, email, persona_id) VALUES (?, ?, ?, ?, ?, ?)";
            cliente = con.prepareStatement(sqlCliente);
            cliente.setInt(1, codCliente);
            cliente.setString(2, nombre);
            cliente.setString(3, apellido);
            cliente.setString(4, telefono);
            cliente.setString(5, email);
            cliente.setInt(6, personaId);
            cliente.executeUpdate();

            con.commit(); // Confirmar transacción
            registrado = true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // Revertir cambios en caso de error
                } catch (SQLException ex) {
                    reportException(ex.getMessage());
                }
            }
            reportException(e.getMessage());
        } finally {
            try {
                if (persona != null) {
                    persona.close();
                }
                if (cliente != null) {
                    cliente.close();
                }
                if (idGenerado != null) {
                    idGenerado.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                reportException(e.getMessage());
            }
        }
        return registrado;
    }

    public boolean registrarPlato(String nombrePlato) {
        boolean registrado = false;
        Connection con = null;
        PreparedStatement plato = null;

        try {
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Insertar en Plato (el ID se genera automáticamente por ser AUTO_INCREMENT)
            String sqlPlato = "INSERT INTO Plato (nombre) VALUES (?)";
            plato = con.prepareStatement(sqlPlato);
            plato.setString(1, nombrePlato);

            int seRegistro = plato.executeUpdate();
            registrado = (seRegistro > 0);

        } catch (SQLException e) {
            reportException(e.getMessage());
        } finally {
            try {
                if (plato != null) {
                    plato.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                reportException(e.getMessage());
            }
        }
        return registrado;
    }

}
