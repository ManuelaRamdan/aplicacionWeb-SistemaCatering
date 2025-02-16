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
import java.util.List;

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

    public List<Plato> obtenerPlatosBd() {
        List<Plato> listaPlatos = new ArrayList<>();
        String query = "SELECT id, nombre FROM Plato";

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establecemos la conexión y la consulta
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();

            // Procesamos el resultado
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                listaPlatos.add(new Plato(id, nombre));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Mostrar errores en la consola
        } finally {
            // Cerramos los recursos manualmente en el bloque finally
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Mostrar errores en el cierre de recursos
            }
        }

        return listaPlatos;
    }

    public boolean registrarMenu(String nombreMenu, List<Integer> platosEntrada, List<Integer> platosPrincipal) {
        boolean registrado = false;
        Connection con = null;
        PreparedStatement menu = null;
        PreparedStatement menuPlato = null;
        ResultSet idGenerado = null;

        try {
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            con.setAutoCommit(false);

            // 1. Insertar en Menu
            String menuSql = "INSERT INTO Menu (nombreMenu) VALUES (?)"; // Solo el nombre del menú
            menu = con.prepareStatement(menuSql, Statement.RETURN_GENERATED_KEYS);
            menu.setString(1, nombreMenu);
            int menuInserted = menu.executeUpdate();

            if (menuInserted == 0) {
                throw new SQLException("Error al crear el menú, no se insertaron filas.");
            }

            idGenerado = menu.getGeneratedKeys();
            int menuId = 0;
            if (idGenerado.next()) {
                menuId = idGenerado.getInt(1);
            }

            // 2. Asociar platos de entrada
            String menuPlatoSql = "INSERT INTO Menu_Plato (menu_id, plato_id, tipo) VALUES (?, ?, ?)";
            menuPlato = con.prepareStatement(menuPlatoSql);

            for (int platoId : platosEntrada) {
                menuPlato.setInt(1, menuId);
                menuPlato.setInt(2, platoId);
                menuPlato.setString(3, "Entrada");
                menuPlato.addBatch();
            }
            menuPlato.executeBatch();

            // 3. Asociar platos principales
            for (int platoId : platosPrincipal) {
                menuPlato.setInt(1, menuId);
                menuPlato.setInt(2, platoId);
                menuPlato.setString(3, "Principal");
                menuPlato.addBatch();
            }
            menuPlato.executeBatch();

            con.commit();
            registrado = true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    reportException(ex.getMessage());
                }
            }
            reportException(e.getMessage());
        } finally {
            try {
                if (idGenerado != null) {
                    idGenerado.close();
                }
                if (menuPlato != null) {
                    menuPlato.close();
                }
                if (menu != null) {
                    menu.close();
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

    public List<Integer> obtenerIdsSeleccionados(String[] platosId) {
        if (platosId == null || platosId.length == 0) {
            return new ArrayList<>();
        }
        List<Integer> listaIds = new ArrayList<>();
        for (String id : platosId) {
            int idInt = Integer.parseInt(id);
            listaIds.add(idInt);
        }

        return listaIds;
    }

    public boolean registrarServicio(String nombreServicio, List<Integer> menus) {
        boolean registrado = false;
        Connection con = null;
        PreparedStatement servicio = null;
        PreparedStatement servicioMenu = null;
        ResultSet idGenerado = null;

        try {
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            con.setAutoCommit(false);

            // 1. Insertar en Servicio
            String servicioSql = "INSERT INTO Servicio (nombreServicio) VALUES (?)";
            servicio = con.prepareStatement(servicioSql, Statement.RETURN_GENERATED_KEYS);
            servicio.setString(1, nombreServicio);
            int servicioInserted = servicio.executeUpdate();

            if (servicioInserted == 0) {
                throw new SQLException("Error al crear el servicio, no se insertaron filas.");
            }

            idGenerado = servicio.getGeneratedKeys();
            int servicioId = 0;
            if (idGenerado.next()) {
                servicioId = idGenerado.getInt(1);
            }

            // 2. Asociar menús al servicio
            String servicioMenuSql = "INSERT INTO Servicio_Menu (servicio_id, menu_id) VALUES (?, ?)";
            servicioMenu = con.prepareStatement(servicioMenuSql);

            for (int menuId : menus) {
                servicioMenu.setInt(1, servicioId);
                servicioMenu.setInt(2, menuId);
                servicioMenu.addBatch();
            }
            servicioMenu.executeBatch();

            con.commit();
            registrado = true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    reportException(ex.getMessage());
                }
            }
            reportException(e.getMessage());
        } finally {
            try {
                if (idGenerado != null) {
                    idGenerado.close();
                }
                if (servicioMenu != null) {
                    servicioMenu.close();
                }
                if (servicio != null) {
                    servicio.close();
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

    public List<Menu> obtenerMenusBd() {
        List<Menu> listaMenus = new ArrayList<>();
        String query = "SELECT id, nombreMenu, precio FROM Menu";

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establecemos la conexión y ejecutamos la consulta
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();

            // Procesamos el resultado
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombreMenu = rs.getString("nombreMenu");
                int precio = rs.getInt("precio");
                listaMenus.add(new Menu(id, nombreMenu, precio));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Mostrar errores en la consola
        } finally {
            // Cerramos los recursos manualmente en el bloque finally
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Mostrar errores en el cierre de recursos
            }
        }

        return listaMenus;
    }

}
