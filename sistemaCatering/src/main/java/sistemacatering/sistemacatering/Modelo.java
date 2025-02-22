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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            stmt.execute("SELECT persona.id FROM Persona WHERE persona.estado = 1 and persona.usuario ='" + usuario + "' and persona.password ='" + password + "'");
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
            String sqlCodCliente = "SELECT MAX(id) FROM Cliente";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCodCliente);
            int maxCodCliente = 0;
            if (rs.next()) {
                maxCodCliente = rs.getInt(1); // Obtener el máximo valor de codCliente
            }
            int codCliente = maxCodCliente + 1; // Incrementar el valor máximo encontrado

            // Insertar en Cliente
            String sqlCliente = "INSERT INTO Cliente (id, nombre, apellido, telReferencia, email, persona_id,estado) VALUES (?, ?, ?, ?, ?, ?,?)";
            cliente = con.prepareStatement(sqlCliente);
            cliente.setInt(1, codCliente);
            cliente.setString(2, nombre);
            cliente.setString(3, apellido);
            cliente.setString(4, telefono);
            cliente.setString(5, email);
            cliente.setInt(6, personaId);
            cliente.setInt(7, 1);
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
        String query = "SELECT id, nombre FROM Plato WHERE plato.estado = 1";

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

    public boolean registrarMenu(String nombreMenu, List<Integer> platosEntrada, List<Integer> platosPrincipal, int precio) {
        boolean registrado = false;
        Connection con = null;
        PreparedStatement menu = null;
        PreparedStatement menuPlato = null;
        ResultSet idGenerado = null;

        try {
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            con.setAutoCommit(false);

            // 1. Insertar en Menu
            String menuSql = "INSERT INTO Menu (nombreMenu , precio) VALUES (? , ?)"; // Solo el nombre del menú
            menu = con.prepareStatement(menuSql, Statement.RETURN_GENERATED_KEYS);
            menu.setString(1, nombreMenu);
            menu.setInt(2, precio);
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

    public List<Integer> obtenerIdsSeleccionados(String[] ids) {
        if (ids == null || ids.length == 0) {
            return new ArrayList<>();
        }
        List<Integer> listaIds = new ArrayList<>();
        for (String id : ids) {
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
        String query = "SELECT id, nombreMenu, precio FROM Menu WHERE menu.estado = 1";

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

    public List<Coordinador> obtenerCoordinadoresBd() {
        List<Coordinador> listaCoordinadores = new ArrayList<>();
        String query = "SELECT c.id, p.usuario FROM Coordinador c JOIN Persona p ON c.persona_id = p.id WHERE c.estado = 1";

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
                String usuario = rs.getString("usuario");
                listaCoordinadores.add(new Coordinador(id, usuario));
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

        return listaCoordinadores;
    }

    public boolean eliminarCoordinador(int idCoordinador) {
        // Sentencia SQL para actualizar el estado en Coordinador y Persona
        String sqlCoordinador = "UPDATE Coordinador SET estado = 0 WHERE id = ?";
        String sqlPersona = "UPDATE Persona SET estado = 0 WHERE id = (SELECT persona_id FROM Coordinador WHERE id = ?)";

        Connection con = null;
        PreparedStatement coordinador = null;
        PreparedStatement persona = null;

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Actualizar el estado del Coordinador
            coordinador = con.prepareStatement(sqlCoordinador);
            coordinador.setInt(1, idCoordinador);
            int filasAfectadasCoordinador = coordinador.executeUpdate();

            // Actualizar el estado de la Persona asociada
            persona = con.prepareStatement(sqlPersona);
            persona.setInt(1, idCoordinador);
            int filasAfectadasPersona = persona.executeUpdate();

            // Si ambas actualizaciones fueron exitosas, confirmar transacción
            if (filasAfectadasCoordinador > 0 && filasAfectadasPersona > 0) {
                con.commit(); // Confirmar la transacción
                return true;
            } else {
                con.rollback(); // Revertir si algo falla
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback(); // Hacer rollback si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Cerrar los recursos
            try {
                if (coordinador != null) {
                    coordinador.close();
                }
                if (persona != null) {
                    persona.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Administrador> obtenerAdministradoresBd() {
        List<Administrador> listaAdministradores = new ArrayList<>();
        String query = "SELECT a.id, p.usuario FROM Administrador a JOIN Persona p ON a.persona_id = p.id WHERE a.estado = 1"; // Cambié c a a

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
                String usuario = rs.getString("usuario");
                listaAdministradores.add(new Administrador(id, usuario)); // Crear el objeto Administrador
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

        return listaAdministradores;
    }

    public boolean eliminarAdministrador(int idAdministrador) {
        // Sentencias SQL para actualizar el estado en Administrador y Persona
        String sqlAdm = "UPDATE Administrador SET estado = 0 WHERE id = ?";
        String sqlPersona = "UPDATE Persona SET estado = 0 WHERE id = (SELECT persona_id FROM Administrador WHERE id = ?)";

        Connection con = null;
        PreparedStatement stmtAdm = null;
        PreparedStatement stmtPersona = null;

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Actualizar el estado del Administrador
            stmtAdm = con.prepareStatement(sqlAdm);
            stmtAdm.setInt(1, idAdministrador);
            int filasAfectadasAdm = stmtAdm.executeUpdate();

            // Actualizar el estado de la Persona asociada
            stmtPersona = con.prepareStatement(sqlPersona);
            stmtPersona.setInt(1, idAdministrador);
            int filasAfectadasPersona = stmtPersona.executeUpdate();

            // Si ambas actualizaciones fueron exitosas, confirmar transacción
            if (filasAfectadasAdm > 0 && filasAfectadasPersona > 0) {
                con.commit(); // Confirmar la transacción
                return true;
            } else {
                con.rollback(); // Revertir si algo falla
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback(); // Hacer rollback si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Cerrar los recursos
            try {
                if (stmtAdm != null) {
                    stmtAdm.close();
                }
                if (stmtPersona != null) {
                    stmtPersona.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Cliente> obtenerClientesBd() {
        List<Cliente> listaClientes = new ArrayList<>();
        String query = "SELECT c.id, c.nombre, c.apellido, c.telReferencia, c.email, c.persona_id "
                + "FROM Cliente c "
                + "WHERE c.estado = 1";  // Suponiendo que 'estado = 1' indica que el cliente está activo

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
                String apellido = rs.getString("apellido");
                String telReferencia = rs.getString("telReferencia");
                String email = rs.getString("email");
                int persona_id = rs.getInt("persona_id");

                // Crear el objeto Cliente y agregarlo a la lista
                listaClientes.add(new Cliente(id, nombre, apellido, telReferencia, email, persona_id));
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

        return listaClientes;
    }

    public boolean eliminarCliente(int idCliente) {
        // Sentencias SQL para actualizar el estado en Cliente y Persona
        String sqlCliente = "UPDATE Cliente SET estado = 0 WHERE id = ?";
        String sqlPersona = "UPDATE Persona SET estado = 0 WHERE id = (SELECT persona_id FROM Cliente WHERE id = ?)";

        Connection con = null;
        PreparedStatement stmtCliente = null;
        PreparedStatement stmtPersona = null;

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Actualizar el estado del Cliente
            stmtCliente = con.prepareStatement(sqlCliente);
            stmtCliente.setInt(1, idCliente);
            int filasAfectadasCliente = stmtCliente.executeUpdate();

            // Actualizar el estado de la Persona asociada
            stmtPersona = con.prepareStatement(sqlPersona);
            stmtPersona.setInt(1, idCliente);
            int filasAfectadasPersona = stmtPersona.executeUpdate();

            // Si ambas actualizaciones fueron exitosas, confirmar la transacción
            if (filasAfectadasCliente > 0 && filasAfectadasPersona > 0) {
                con.commit(); // Confirmar la transacción
                return true;
            } else {
                con.rollback(); // Revertir si algo falla
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback(); // Hacer rollback si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Cerrar los recursos
            try {
                if (stmtCliente != null) {
                    stmtCliente.close();
                }
                if (stmtPersona != null) {
                    stmtPersona.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean eliminarPlato(int idPlato) {
        // Sentencias SQL para actualizar el estado en Plato, Menu_Plato
        String queryPlato = "UPDATE Plato SET estado = 0 WHERE id = ?";
        String queryMenuPlato = "UPDATE Menu_Plato SET estado = 0 WHERE plato_id = ?";

        Connection con = null;
        PreparedStatement stmtPlato = null;
        PreparedStatement stmtMenuPlato = null;

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Actualizar el estado del Plato
            stmtPlato = con.prepareStatement(queryPlato);
            stmtPlato.setInt(1, idPlato);
            int filasAfectadasPlato = stmtPlato.executeUpdate();

            // Actualizar el estado en la tabla Menu_Plato
            stmtMenuPlato = con.prepareStatement(queryMenuPlato);
            stmtMenuPlato.setInt(1, idPlato);
            int filasAfectadasMenuPlato = stmtMenuPlato.executeUpdate();

            // Si ambas actualizaciones fueron exitosas, confirmar la transacción
            if (filasAfectadasPlato > 0 && filasAfectadasMenuPlato > 0) {
                con.commit(); // Confirmar la transacción
                return true;
            } else {
                con.rollback(); // Revertir si algo falla
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback(); // Hacer rollback si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Cerrar los recursos
            try {
                if (stmtPlato != null) {
                    stmtPlato.close();
                }
                if (stmtMenuPlato != null) {
                    stmtMenuPlato.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean eliminarMenu(int idMenu) {
        // Sentencias SQL para actualizar el estado en Menu, Menu_Plato
        String queryMenu = "UPDATE Menu SET estado = 0 WHERE id = ?";
        String queryMenuPlato = "UPDATE Menu_Plato SET estado = 0 WHERE menu_id = ?";
        String queryServicioMenu = "UPDATE Servicio_Menu SET estado = 0 WHERE menu_id = ?";

        Connection con = null;
        PreparedStatement stmtMenu = null;
        PreparedStatement stmtMenuPlato = null;
        PreparedStatement stmtServicioMenu = null;

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Actualizar el estado del Menu
            stmtMenu = con.prepareStatement(queryMenu);
            stmtMenu.setInt(1, idMenu);
            int filasAfectadasMenu = stmtMenu.executeUpdate();

            // Actualizar el estado en la tabla Menu_Plato
            stmtMenuPlato = con.prepareStatement(queryMenuPlato);
            stmtMenuPlato.setInt(1, idMenu);
            int filasAfectadasMenuPlato = stmtMenuPlato.executeUpdate();

            stmtServicioMenu = con.prepareStatement(queryServicioMenu);
            stmtServicioMenu.setInt(1, idMenu);
            int filasAfectadasServicioMenu = stmtServicioMenu.executeUpdate();

            // Si ambas actualizaciones fueron exitosas, confirmar la transacción
            if (filasAfectadasMenu > 0) {
                con.commit(); // Confirmar la transacción
                return true;
            } else {
                con.rollback(); // Revertir si algo falla
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback(); // Hacer rollback si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Cerrar los recursos
            try {
                if (stmtMenu != null) {
                    stmtMenu.close();
                }
                if (stmtMenuPlato != null) {
                    stmtMenuPlato.close();
                }
                if (con != null) {
                    con.close();
                }
                if (stmtServicioMenu != null) {
                    stmtServicioMenu.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean eliminarServicio(int idServicio) {
        // Sentencias SQL para actualizar el estado en Servicio, Servicio_Menu
        String queryServicio = "UPDATE Servicio SET estado = 0 WHERE id = ?";
        String queryServicioMenu = "UPDATE Servicio_Menu SET estado = 0 WHERE servicio_id = ?";

        Connection con = null;
        PreparedStatement stmtServicio = null;
        PreparedStatement stmtServicioMenu = null;

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Actualizar el estado del Servicio
            stmtServicio = con.prepareStatement(queryServicio);
            stmtServicio.setInt(1, idServicio);
            int filasAfectadasServicio = stmtServicio.executeUpdate();

            // Actualizar el estado en la tabla Servicio_Menu
            stmtServicioMenu = con.prepareStatement(queryServicioMenu);
            stmtServicioMenu.setInt(1, idServicio);
            int filasAfectadasServicioMenu = stmtServicioMenu.executeUpdate();

            // Si ambas actualizaciones fueron exitosas, confirmar la transacción
            if (filasAfectadasServicio > 0 && filasAfectadasServicioMenu > 0) {
                con.commit(); // Confirmar la transacción
                return true;
            } else {
                con.rollback(); // Revertir si algo falla
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback(); // Hacer rollback si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Cerrar los recursos
            try {
                if (stmtServicio != null) {
                    stmtServicio.close();
                }
                if (stmtServicioMenu != null) {
                    stmtServicioMenu.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Servicio> obtenerServiciosBd() {
        List<Servicio> listaServicios = new ArrayList<>();
        String query = "SELECT id, nombreServicio FROM Servicio WHERE servicio.estado = 1";

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
                String nombre = rs.getString("nombreServicio");
                listaServicios.add(new Servicio(id, nombre));
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

        return listaServicios;
    }

    public List<Reserva> obtenerReservaBd() {
        List<Reserva> listaReservas = new ArrayList<>();
        String query = "SELECT r.id, r.codCliente, r.fechaInicioEvento, r.fechaFinEvento, r.restriccionesDieteticas, "
                + "r.preferenciaCliente, r.tipoServicio, r.cantidadPersonas, r.precio, r.modoDeReserva, "
                + "r.direccionDeEntrega_id, r.estaEntregado, d.calle, d.altura, d.barrio, "
                + "s.id AS servicio_id "
                + "FROM Reserva r "
                + "JOIN Domicilio d ON r.direccionDeEntrega_id = d.id "
                + "LEFT JOIN reserva_servicio rs ON r.id = rs.reserva_id "
                + "LEFT JOIN servicio s ON rs.servicio_id = s.id "
                + "WHERE r.estado = 1"; // Filtrar por reservas activas

        try (Connection con = DriverManager.getConnection(urlRoot + dbName, "", ""); PreparedStatement stmt = con.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            // Creamos una lista para almacenar las reservas
            Reserva reservaActual = null;
            List<Servicio> servicios = new ArrayList<>();

            while (rs.next()) {
                int codReserva = rs.getInt("id");
                int codCliente = rs.getInt("codCliente");

                // Obtener los valores de tipo Timestamp desde la base de datos
                Timestamp timestampInicio = rs.getTimestamp("fechaInicioEvento");
                Timestamp timestampFin = rs.getTimestamp("fechaFinEvento");

                // Convertir los Timestamps a java.util.Date
                Date fechaInicioEvento = new Date(timestampInicio.getTime());
                Date fechaFinEvento = new Date(timestampFin.getTime());

                String restriccionesDieteticas = rs.getString("restriccionesDieteticas");
                String preferenciaCliente = rs.getString("preferenciaCliente");
                String tipoServicio = rs.getString("tipoServicio");
                int cantidadPersonas = rs.getInt("cantidadPersonas");
                int precio = rs.getInt("precio");
                String modoDeReserva = rs.getString("modoDeReserva");
                int direccionDeEntregaId = rs.getInt("direccionDeEntrega_id");
                boolean estaEntregado = rs.getBoolean("estaEntregado");

                // Crear el objeto Domicilio con los datos de la dirección
                String calle = rs.getString("calle");
                int altura = rs.getInt("altura");
                String barrio = rs.getString("barrio");

                Domicilio direccionDeEntrega = new Domicilio(direccionDeEntregaId, calle, altura, barrio);

                // Si estamos en una nueva reserva (con un nuevo codReserva)
                if (reservaActual == null || reservaActual.getCodReserva() != codReserva) {
                    // Si ya hemos encontrado una reserva y la hemos procesado, agregamos la reserva y la lista de servicios
                    if (reservaActual != null) {
                        reservaActual.setServicios(servicios);
                        listaReservas.add(reservaActual);
                    }

                    // Creamos una nueva reserva
                    reservaActual = new Reserva(
                            codReserva, codCliente, fechaInicioEvento, fechaFinEvento, restriccionesDieteticas,
                            preferenciaCliente, tipoServicio, cantidadPersonas, precio, modoDeReserva,
                            direccionDeEntrega, estaEntregado
                    );

                    // Limpiamos la lista de servicios para esta nueva reserva
                    servicios = new ArrayList<>();
                }

                // Cargar servicio si existe
                int servicioId = rs.getInt("servicio_id");
                if (servicioId > 0) {
                    Servicio servicio = obtenerServicioConId(servicioId);
                    servicios.add(servicio);
                }
            }

            // Añadir la última reserva procesada
            if (reservaActual != null) {
                reservaActual.setServicios(servicios);
                listaReservas.add(reservaActual);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Mostrar errores en la consola
        }

        return listaReservas;
    }

    public Cliente obtenerClientePorId(String idCliente) {
        Cliente cliente = null;
        String query = "SELECT c.id, c.nombre, c.apellido, c.telReferencia, c.email, c.persona_id "
                + "FROM Cliente c "
                + "WHERE c.estado = 1 and c.persona_id = ?";  // Suponiendo que 'estado = 1' indica que el cliente está activo

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establecemos la conexión y la consulta
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            stmt = con.prepareStatement(query);

            // Establecemos el valor del parámetro (idCliente) en el PreparedStatement
            stmt.setString(1, idCliente);  // Si 'idCliente' es un String
            // O si 'idCliente' es un número, usa stmt.setInt(1, Integer.parseInt(idCliente));

            rs = stmt.executeQuery();

            // Procesamos el resultado
            if (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String telReferencia = rs.getString("telReferencia");
                String email = rs.getString("email");
                int persona_id = rs.getInt("persona_id");

                // Crear el objeto Cliente con los datos obtenidos
                cliente = new Cliente(id, nombre, apellido, telReferencia, email, persona_id);
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

        return cliente;
    }

    // Método para obtener las reservas de un cliente
    public List<Reserva> obtenerReservasPorCliente(String persona_id) {
        List<Reserva> reservas = new ArrayList<>();
        String query = "SELECT r.id, r.codCliente, r.fechaInicioEvento, r.fechaFinEvento, "
                + "r.restriccionesDieteticas, r.preferenciaCliente, r.tipoServicio, r.cantidadPersonas, "
                + "r.precio, r.modoDeReserva, r.direccionDeEntrega_id, r.estaEntregado, "
                + "d.calle, d.altura, d.barrio "
                + "FROM Reserva r "
                + "LEFT JOIN Domicilio d ON r.direccionDeEntrega_id = d.id "
                + "WHERE r.codCliente = (SELECT c.id FROM Cliente c WHERE c.persona_id = ?) "
                + "  AND r.estado = 1";

        try (Connection con = DriverManager.getConnection(urlRoot + dbName, "", ""); PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, persona_id);  // Establecer el parámetro para buscar por cliente
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Obtener los valores de la reserva
                    int codReserva = rs.getInt("id");
                    int codCliente = rs.getInt("codCliente");
                    Date fechaInicioEvento = rs.getDate("fechaInicioEvento");
                    Date fechaFinEvento = rs.getDate("fechaFinEvento");
                    String restriccionesDieteticas = rs.getString("restriccionesDieteticas");
                    String preferenciaCliente = rs.getString("preferenciaCliente");
                    String tipoServicio = rs.getString("tipoServicio");
                    int cantidadPersonas = rs.getInt("cantidadPersonas");
                    int precio = rs.getInt("precio");
                    String modoDeReserva = rs.getString("modoDeReserva");
                    boolean estaEntregado = rs.getBoolean("estaEntregado");

                    // Obtener los valores de la dirección de entrega
                    int direccionDeEntregaId = rs.getInt("direccionDeEntrega_id");
                    String calle = rs.getString("calle");
                    int altura = rs.getInt("altura");
                    String barrio = rs.getString("barrio");

                    // Crear el objeto Domicilio con los datos de la dirección
                    Domicilio direccionDeEntrega = new Domicilio(direccionDeEntregaId, calle, altura, barrio);

                    // Crear y añadir la reserva a la lista
                    Reserva reserva = new Reserva(
                            codReserva, codCliente, fechaInicioEvento, fechaFinEvento, restriccionesDieteticas,
                            preferenciaCliente, tipoServicio, cantidadPersonas, precio, modoDeReserva,
                            direccionDeEntrega, estaEntregado
                    );

                    reservas.add(reserva);
                }
            }

        } catch (SQLException e) {
            reportException(e.getMessage());
        }
        return reservas;
    }

    public List<Cliente> obtenerClientesConReservas() {
        List<Cliente> clientes = new ArrayList<>();
        String query = "SELECT c.id, c.nombre, c.apellido, c.telReferencia, c.email, c.persona_id, r.id AS codReserva, r.fechaInicioEvento, r.fechaFinEvento, "
                + " r.restriccionesDieteticas, r.preferenciaCliente, r.tipoServicio, r.cantidadPersonas, "
                + " r.precio, r.modoDeReserva, r.direccionDeEntrega_id, r.estaEntregado, "
                + " d.calle, d.altura, d.barrio "
                + " FROM Cliente c "
                + "LEFT JOIN Reserva r ON c.id = r.codCliente "
                + "LEFT JOIN Domicilio d ON r.direccionDeEntrega_id = d.id "
                + "WHERE c.estado = 1";

        try (Connection con = DriverManager.getConnection(urlRoot + dbName, "", ""); PreparedStatement stmt = con.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String telReferencia = rs.getString("telReferencia");
                String email = rs.getString("email");
                int persona_id = rs.getInt("persona_id");
                // Buscar si el cliente ya existe en la lista
                Cliente cliente = null;
                for (Cliente c : clientes) {
                    if (c.getId() == id) {
                        cliente = c;
                        break;
                    }
                }

                // Si el cliente no existe, crear uno nuevo
                if (cliente == null) {
                    cliente = new Cliente(id, nombre, apellido, telReferencia, email, persona_id);
                    clientes.add(cliente);  // Agregar el nuevo cliente a la lista
                }

                // Obtener las reservas para el cliente (si existen)
                int codReserva = rs.getInt("codReserva");
                if (codReserva != 0) {  // Asegurarse de que haya una reserva
                    Date fechaInicioEvento = rs.getDate("fechaInicioEvento");
                    Date fechaFinEvento = rs.getDate("fechaFinEvento");
                    String restriccionesDieteticas = rs.getString("restriccionesDieteticas");
                    String preferenciaCliente = rs.getString("preferenciaCliente");
                    String tipoServicio = rs.getString("tipoServicio");
                    int cantidadPersonas = rs.getInt("cantidadPersonas");
                    int precio = rs.getInt("precio");
                    String modoDeReserva = rs.getString("modoDeReserva");
                    boolean estaEntregado = rs.getBoolean("estaEntregado");

                    // Obtener los valores de la dirección de entrega
                    int direccionDeEntregaId = rs.getInt("direccionDeEntrega_id");
                    String calle = rs.getString("calle");
                    int altura = rs.getInt("altura");
                    String barrio = rs.getString("barrio");

                    // Crear el objeto Domicilio con los datos de la dirección
                    Domicilio direccionDeEntrega = new Domicilio(direccionDeEntregaId, calle, altura, barrio);

                    // Crear y añadir la reserva al cliente
                    Reserva reserva = new Reserva(
                            codReserva, id, fechaInicioEvento, fechaFinEvento, restriccionesDieteticas,
                            preferenciaCliente, tipoServicio, cantidadPersonas, precio, modoDeReserva,
                            direccionDeEntrega, estaEntregado
                    );
                    cliente.getReservas().add(reserva);  // Añadir la reserva al cliente
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Manejo de errores
        }
        return clientes;
    }

    public boolean eliminarReserva(int idReserva) {
        // Sentencias SQL para actualizar el estado en Servicio, Servicio_Menu
        String queryReserva = "UPDATE Reserva SET estado = 0 WHERE id = ?";
        String queryReservaServicio = "UPDATE reserva_servicio SET estado = 0 WHERE servicio_id = ?";

        Connection con = null;
        PreparedStatement stmtReserva = null;
        PreparedStatement stmtReservaServicio = null;

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Actualizar el estado del Servicio
            stmtReserva = con.prepareStatement(queryReserva);
            stmtReserva.setInt(1, idReserva);
            int filasAfectadasServicio = stmtReserva.executeUpdate();

            // Actualizar el estado en la tabla Servicio_Menu
            stmtReservaServicio = con.prepareStatement(queryReservaServicio);
            stmtReservaServicio.setInt(1, idReserva);
            int filasAfectadasServicioMenu = stmtReservaServicio.executeUpdate();

            // Si ambas actualizaciones fueron exitosas, confirmar la transacción
            if (filasAfectadasServicio > 0) {
                con.commit(); // Confirmar la transacción
                return true;
            } else {
                con.rollback(); // Revertir si algo falla
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback(); // Hacer rollback si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Cerrar los recursos
            try {
                if (stmtReserva != null) {
                    stmtReserva.close();
                }
                if (stmtReservaServicio != null) {
                    stmtReservaServicio.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Servicio> obtenerServiciosDisponibles(String fechaInicio, String fechaFin) {
        List<Servicio> serviciosDisponibles = new ArrayList<>();
        String query = "SELECT s.id "
                + "FROM servicio s "
                + "WHERE NOT EXISTS ( "
                + "    SELECT 1 "
                + "    FROM reserva_servicio rs "
                + "    JOIN reserva r ON rs.reserva_id = r.id "
                + "    WHERE (r.fechaInicioEvento < ? AND r.fechaFinEvento > ?) "
                + "    AND rs.servicio_id = s.id and r.estado = 1"
                + ") and s.estado = 1";

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establecemos la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Preparamos la consulta
            stmt = con.prepareStatement(query);

            // Establecemos los parámetros (fechaFin y fechaInicio)
            stmt.setString(1, fechaFin);  // Fecha de fin del rango
            stmt.setString(2, fechaInicio); // Fecha de inicio del rango

            // Ejecutamos la consulta
            rs = stmt.executeQuery();

            // Procesamos el resultado
            while (rs.next()) {
                int id = rs.getInt("id");

                // Creamos el objeto Servicio y lo agregamos a la lista
                Servicio servicio = obtenerServicioConId(id);
                serviciosDisponibles.add(servicio);
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

        return serviciosDisponibles;
    }

    public boolean verificarCliente(int codCliente) {
        String query = "SELECT COUNT(*) FROM Cliente WHERE id = ? AND estado = 1";
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean clienteExiste = false; // Valor por defecto

        try {
            // Establecemos la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Preparamos la consulta
            stmt = con.prepareStatement(query);
            stmt.setInt(1, codCliente); // Usamos setInt ya que codCliente es un número

            // Ejecutamos la consulta
            rs = stmt.executeQuery();

            // Procesamos el resultado
            if (rs.next()) {
                int count = rs.getInt(1); // El resultado de COUNT(*) está en la primera columna
                if (count > 0) {
                    clienteExiste = true; // Cliente encontrado con estado 1
                }
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

        return clienteExiste;
    }

    boolean registrarReserva(Reserva reserva, Domicilio domicilio) {
        boolean registrado = false;
        Connection con = null;
        PreparedStatement reservaStmt = null;
        PreparedStatement reservaServicioStmt = null;
        PreparedStatement domicilioStmt = null;
        ResultSet generatedKeys = null;

        int precio = obtenerPrecioReserva(reserva.getServicios(), reserva.getCantidadPersonas());

        try {
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            con.setAutoCommit(false);

            // 1. Insertar domicilio primero
            String domicilioSql = "INSERT INTO domicilio (calle, altura, barrio, estado) VALUES (?, ?, ?, ?)";
            domicilioStmt = con.prepareStatement(domicilioSql, Statement.RETURN_GENERATED_KEYS);
            domicilioStmt.setString(1, domicilio.getCalle());
            domicilioStmt.setInt(2, domicilio.getAltura());
            domicilioStmt.setString(3, domicilio.getBarrio());
            domicilioStmt.setInt(4, 1);

            int filasDomicilio = domicilioStmt.executeUpdate();
            if (filasDomicilio == 0) {
                throw new SQLException("Error al crear el domicilio, no se insertaron filas.");
            }

// Obtener el ID generado para Domicilio
            generatedKeys = domicilioStmt.getGeneratedKeys();
            int idDomicilio = 0;
            if (generatedKeys.next()) {
                idDomicilio = generatedKeys.getInt(1);
            } else {
                throw new SQLException("No se pudo obtener el ID del domicilio insertado.");
            }

            // 1. Insertar en la tabla 'reserva'
            String reservaSql = "INSERT INTO reserva (codCliente, fechaInicioEvento, fechaFinEvento, restriccionesDieteticas, "
                    + "preferenciaCliente, tipoServicio, cantidadPersonas, precio, modoDeReserva, direccionDeEntrega_id, estaEntregado, estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            reservaStmt = con.prepareStatement(reservaSql, Statement.RETURN_GENERATED_KEYS);// Statement.RETURN_GENERATED_KEYS para recuperar el ID de la reserva recién insertada.

            if (reserva.getCodCliente() == 0) {
                throw new SQLException("Error: codCliente no puede ser 0 o nulo.");
            }
            if (reserva.getFechaInicioEvento() == null || reserva.getFechaFinEvento() == null) {
                throw new SQLException("Error: Fecha de inicio o fin es nula.");
            }

            reservaStmt.setInt(1, reserva.getCodCliente());
// Convertir Date a LocalDateTime
            LocalDateTime fechaInicioLocalDateTime = reserva.getFechaInicioEvento().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime fechaFinLocalDateTime = reserva.getFechaFinEvento().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

// Convertir LocalDateTime a Timestamp
            Timestamp fechaInicioTimestamp = Timestamp.valueOf(fechaInicioLocalDateTime);
            Timestamp fechaFinTimestamp = Timestamp.valueOf(fechaFinLocalDateTime);

// Establecer en el PreparedStatement
            reservaStmt.setTimestamp(2, fechaInicioTimestamp);
            reservaStmt.setTimestamp(3, fechaFinTimestamp);

            reservaStmt.setString(4, reserva.getRestriccionesDieteticas());
            reservaStmt.setString(5, reserva.getPreferenciaCliente());
            reservaStmt.setString(6, reserva.getTipoServicio());
            reservaStmt.setInt(7, reserva.getCantidadPersonas());
            reservaStmt.setInt(8, reserva.getPrecio());
            reservaStmt.setString(9, reserva.getModoDeReserva()); // Suponiendo que 'ModoDeReserva' es un enum
            reservaStmt.setInt(10, idDomicilio);
            reservaStmt.setInt(11, 0);
            reservaStmt.setInt(12, 1);

            int filasAfectadas = reservaStmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("Error al crear la reserva, no se insertaron filas.");
            }

            // Obtener el ID generado para la nueva reserva
            generatedKeys = reservaStmt.getGeneratedKeys();
            int reservaId = 0;
            if (generatedKeys.next()) {
                reservaId = generatedKeys.getInt(1);
            }

            // 2. Insertar en la tabla 'reserva_servicio' (asociar la reserva con los servicios)
            String reservaServicioSql = "INSERT INTO reserva_servicio (reserva_id, servicio_id, estado) VALUES (?, ?, ?)";
            reservaServicioStmt = con.prepareStatement(reservaServicioSql);

            if (reserva.getServicios().isEmpty()) {
                throw new SQLException("Error: No hay servicios asociados a la reserva.");
            }

            // Suponiendo que reserva.getServicios() es una lista de ID de servicios
            for (Servicio servicio : reserva.getServicios()) {
                // Aquí tienes acceso al objeto 'servicio' completo
                reservaServicioStmt.setInt(1, reservaId);
                reservaServicioStmt.setInt(2, servicio.getId()); // Obtener el id del servicio
                reservaServicioStmt.setInt(3, 1);  // Suponiendo que 'estado' es 1 para activo
                reservaServicioStmt.addBatch();
            }
            reservaServicioStmt.executeBatch();

            // Confirmar la transacción
            con.commit();
            registrado = true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();  // En caso de error, revertir los cambios
                } catch (SQLException ex) {
                    reportException(ex.getMessage());
                }
            }
            reportException(e.getMessage());
        } finally {
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (reservaStmt != null) {
                    reservaStmt.close();
                }
                if (reservaServicioStmt != null) {
                    reservaServicioStmt.close();
                }
                if (domicilioStmt != null) {
                    domicilioStmt.close();
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

    public boolean actualizarCliente(int idCliente, String nombre, String apellido, String telefono, String email) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Consulta SQL para actualizar los datos del cliente
            String sql = "UPDATE cliente SET nombre = ?, apellido = ?, telReferencia = ?, email = ? WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, telefono);
            ps.setString(4, email);
            ps.setInt(5, idCliente);

            // Ejecutar la actualización
            int filasAfectadas = ps.executeUpdate();

            // Confirmar o revertir transacción según el resultado
            if (filasAfectadas > 0) {
                con.commit();  // Confirmar la transacción
                return true;
            } else {
                con.rollback();  // Revertir cambios si no se actualizó nada
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();  // Hacer rollback si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Cerrar los recursos
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Administrador obtenerAdministradorPorId(int idAdm) {
        /*SELECT administrador.id AS id_administrador, persona.usuario 
FROM administrador
JOIN persona ON administrador.persona_id = persona.id
WHERE administrador.id = 3 and administrador.estado = 1;*/

        Administrador adm = null;
        String query = "SELECT administrador.id, persona.usuario \n"
                + "FROM administrador\n"
                + "JOIN persona ON administrador.persona_id = persona.id\n"
                + "WHERE administrador.id = ? and administrador.estado = 1";  // Suponiendo que 'estado = 1' indica que el cliente está activo

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establecemos la conexión y la consulta
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            stmt = con.prepareStatement(query);

            // Establecemos el valor del parámetro (idCliente) en el PreparedStatement
            stmt.setInt(1, idAdm);  // Si 'idCliente' es un String
            // O si 'idCliente' es un número, usa stmt.setInt(1, Integer.parseInt(idCliente));

            rs = stmt.executeQuery();

            // Procesamos el resultado
            if (rs.next()) {
                int id = rs.getInt("id");
                String usuario = rs.getString("usuario");

                // Crear el objeto Cliente con los datos obtenidos
                adm = new Administrador(id, usuario);
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

        return adm;
    }

    public Coordinador obtenerCoordinadorPorId(int idCoord) {
        /*SELECT administrador.id AS id_administrador, persona.usuario 
FROM administrador
JOIN persona ON administrador.persona_id = persona.id
WHERE administrador.id = 3 and administrador.estado = 1;*/

        Coordinador coord = null;
        String query = "SELECT coordinador.id, persona.usuario \n"
                + "FROM coordinador\n"
                + "JOIN persona ON coordinador.persona_id = persona.id\n"
                + "WHERE coordinador.id = ? and coordinador.estado = 1";  // Suponiendo que 'estado = 1' indica que el cliente está activo

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establecemos la conexión y la consulta
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            stmt = con.prepareStatement(query);

            // Establecemos el valor del parámetro (idCliente) en el PreparedStatement
            stmt.setInt(1, idCoord);  // Si 'idCliente' es un String
            // O si 'idCliente' es un número, usa stmt.setInt(1, Integer.parseInt(idCliente));

            rs = stmt.executeQuery();

            // Procesamos el resultado
            if (rs.next()) {
                int id = rs.getInt("id");
                String usuario = rs.getString("usuario");

                // Crear el objeto Cliente con los datos obtenidos
                coord = new Coordinador(id, usuario);
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

        return coord;
    }

    public boolean actualizarAdministrador(int idAdm, String usuario) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Consulta SQL para actualizar los datos del cliente
            String sql = "UPDATE persona \n"
                    + "SET usuario = ? \n"
                    + "WHERE id = (SELECT persona_id FROM administrador WHERE id = ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setInt(2, idAdm);

            // Ejecutar la actualización
            int filasAfectadas = ps.executeUpdate();

            // Confirmar o revertir transacción según el resultado
            if (filasAfectadas > 0) {
                con.commit();  // Confirmar la transacción
                return true;
            } else {
                con.rollback();  // Revertir cambios si no se actualizó nada
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();  // Hacer rollback si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Cerrar los recursos
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean actualizarCoordinador(int idCoord, String usuario) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Consulta SQL para actualizar los datos del cliente
            String sql = "UPDATE persona \n"
                    + "SET usuario = ? \n"
                    + "WHERE id = (SELECT persona_id FROM coordinador WHERE id = ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setInt(2, idCoord);

            // Ejecutar la actualización
            int filasAfectadas = ps.executeUpdate();

            // Confirmar o revertir transacción según el resultado
            if (filasAfectadas > 0) {
                con.commit();  // Confirmar la transacción
                return true;
            } else {
                con.rollback();  // Revertir cambios si no se actualizó nada
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();  // Hacer rollback si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Cerrar los recursos
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Plato obtenerPlatoPorId(int idPlato) {
        Plato plato = null;
        String query = "SELECT plato.id, plato.nombre \n"
                + "FROM plato\n"
                + "WHERE plato.id = ? and plato.estado = 1";  // Suponiendo que 'estado = 1' indica que el cliente está activo

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establecemos la conexión y la consulta
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            stmt = con.prepareStatement(query);

            // Establecemos el valor del parámetro (idCliente) en el PreparedStatement
            stmt.setInt(1, idPlato);  // Si 'idCliente' es un String
            // O si 'idCliente' es un número, usa stmt.setInt(1, Integer.parseInt(idCliente));

            rs = stmt.executeQuery();

            // Procesamos el resultado
            if (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");

                // Crear el objeto Cliente con los datos obtenidos
                plato = new Plato(id, nombre);
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

        return plato;
    }

    public boolean actualizarPlato(int idPlato, String nombre) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Consulta SQL para actualizar los datos del cliente
            String sql = "UPDATE plato \n"
                    + "SET nombre = ? \n"
                    + "WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setInt(2, idPlato);

            // Ejecutar la actualización
            int filasAfectadas = ps.executeUpdate();

            // Confirmar o revertir transacción según el resultado
            if (filasAfectadas > 0) {
                con.commit();  // Confirmar la transacción
                return true;
            } else {
                con.rollback();  // Revertir cambios si no se actualizó nada
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();  // Hacer rollback si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Cerrar los recursos
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Menu> obtenerMenusConPlatos() {
        List<Menu> menus = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT m.id AS menu_id, m.nombreMenu, m.precio, mp.tipo, p.id AS plato_id, p.nombre "
                + "FROM menu m "
                + "JOIN menu_plato mp ON m.id = mp.menu_id "
                + "JOIN plato p ON mp.plato_id = p.id "
                + "WHERE m.estado = 1 AND mp.estado = 1";

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Preparar la consulta
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            Map<Integer, Menu> menuMap = new HashMap<>();

            while (rs.next()) {
                int menuId = rs.getInt("menu_id");
                Menu menu = menuMap.get(menuId);

                // Si el menú no ha sido creado aún, crear uno nuevo usando el constructor adecuado
                if (menu == null) {
                    menu = new Menu(menuId, rs.getString("nombreMenu"), rs.getInt("precio"));
                    menuMap.put(menuId, menu);
                }

                String tipo = rs.getString("tipo");
                int platoId = rs.getInt("plato_id"); // Ahora con el alias correcto 'plato_id'
                Plato plato = new Plato(platoId, rs.getString("nombre")); // Crear el plato usando el constructor con id y nombre

                // Según el tipo de plato, agregarlo a la lista correspondiente
                if ("Entrada".equals(tipo)) {
                    menu.getPlatosEntrada().add(plato); // Agregar a platosEntrada
                } else if ("Principal".equals(tipo)) {
                    menu.getPlatosPrincipal().add(plato); // Agregar a platosPrincipal
                }
            }

            // Confirmar la transacción, ya que no hubo errores al procesar los resultados
            con.commit();

            // Agregar todos los menús al listado
            menus.addAll(menuMap.values());

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();  // Revertir cambios si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            // Cerrar los recursos
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return menus;
    }

    public List<Servicio> obtenerServiciosConMenusYPlatos() {
        List<Servicio> servicios = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT s.id AS servicio_id, s.nombreServicio AS servicio_nombre, "
                + "m.id AS menu_id "
                + "FROM servicio s "
                + "JOIN servicio_menu sm ON s.id = sm.servicio_id "
                + "JOIN menu m ON sm.menu_id = m.id "
                + "WHERE s.estado = 1 AND m.estado = 1";

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Preparar la consulta
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            // Obtener todos los menús con platos
            List<Menu> menusConPlatos = obtenerMenusConPlatos(); // Llamamos al método obtenerMenusConPlatos()

            // Crear un mapa de menús por ID para facilitar la búsqueda
            Map<Integer, Menu> menuMap = new HashMap<>();
            for (Menu menu : menusConPlatos) {
                menuMap.put(menu.getId(), menu); // Guardar los menús por su ID
            }

            Map<Integer, Servicio> servicioMap = new HashMap<>();

            while (rs.next()) {
                int servicioId = rs.getInt("servicio_id");
                Servicio servicio = servicioMap.get(servicioId);

                // Si el servicio no ha sido creado aún, crear uno nuevo
                if (servicio == null) {
                    servicio = new Servicio(servicioId, rs.getString("servicio_nombre"));
                    servicioMap.put(servicioId, servicio);
                }

                int menuId = rs.getInt("menu_id");

                // Buscar el menú correspondiente en el mapa de menús
                Menu menu = menuMap.get(menuId);

                if (menu != null) {
                    // Asociar el menú al servicio
                    servicio.getMenus().add(menu);
                }
            }

            // Confirmar la transacción
            con.commit();

            // Agregar todos los servicios al listado
            servicios.addAll(servicioMap.values());

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();  // Revertir cambios si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            // Cerrar los recursos
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return servicios;
    }

    public Menu obtenerMenuConId(int idMenu) {
        Menu menu = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Consulta SQL ajustada para filtrar por idMenu
        String sql = "SELECT m.id AS menu_id, m.nombreMenu, m.precio, mp.tipo, p.id AS plato_id, p.nombre "
                + "FROM menu m "
                + "JOIN menu_plato mp ON m.id = mp.menu_id "
                + "JOIN plato p ON mp.plato_id = p.id "
                + "WHERE m.estado = 1 AND mp.estado = 1 AND m.id = ?";

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Preparar la consulta con el parámetro idMenu
            ps = con.prepareStatement(sql);
            ps.setInt(1, idMenu); // Establecemos el valor de idMenu en la consulta
            rs = ps.executeQuery();

            // Procesar los resultados de la consulta
            if (rs.next()) {
                // Si no se ha creado el menú aún, crearlo con los datos de la primera fila
                menu = new Menu(idMenu, rs.getString("nombreMenu"), rs.getInt("precio"));

                // Recorrer todos los platos relacionados con este menú
                do {
                    String tipo = rs.getString("tipo");
                    int platoId = rs.getInt("plato_id");
                    Plato plato = new Plato(platoId, rs.getString("nombre"));

                    // Según el tipo de plato, agregarlo a la lista correspondiente
                    if ("Entrada".equals(tipo)) {
                        menu.getPlatosEntrada().add(plato); // Agregar a platosEntrada
                    } else if ("Principal".equals(tipo)) {
                        menu.getPlatosPrincipal().add(plato); // Agregar a platosPrincipal
                    }
                } while (rs.next());  // Continuar recorriendo los resultados si hay más platos
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar los recursos
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return menu; // Retornar el único menú encontrado o null si no existe
    }

    public Servicio obtenerServicioConId(int idServicio) {
        Servicio servicio = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Consulta SQL ajustada para filtrar por idServicio
        String sql = "SELECT s.id AS servicio_id, s.nombreServicio AS servicio_nombre, "
                + "m.id AS menu_id "
                + "FROM servicio s "
                + "JOIN servicio_menu sm ON s.id = sm.servicio_id "
                + "JOIN menu m ON sm.menu_id = m.id "
                + "WHERE s.estado = 1 AND m.estado = 1 AND s.id = ?"; // Filtramos por idServicio

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Preparar la consulta con el parámetro idServicio
            ps = con.prepareStatement(sql);
            ps.setInt(1, idServicio); // Establecemos el valor de idServicio en la consulta
            rs = ps.executeQuery();

            // Obtener todos los menús con platos
            List<Menu> menusConPlatos = obtenerMenusConPlatos(); // Llamamos al método obtenerMenusConPlatos()

            // Crear un mapa de menús por ID para facilitar la búsqueda
            Map<Integer, Menu> menuMap = new HashMap<>();
            for (Menu menu : menusConPlatos) {
                menuMap.put(menu.getId(), menu); // Guardar los menús por su ID
            }

            // Procesar el resultado y asociar menús al servicio
            if (rs.next()) {
                // Crear el servicio con el id y nombre obtenido
                servicio = new Servicio(idServicio, rs.getString("servicio_nombre"));

                // Buscar los menús asociados al servicio
                do {
                    int menuId = rs.getInt("menu_id");

                    // Buscar el menú correspondiente en el mapa de menús
                    Menu menu = menuMap.get(menuId);

                    if (menu != null) {
                        // Asociar el menú al servicio
                        servicio.getMenus().add(menu);
                    }
                } while (rs.next()); // Continuar recorriendo los resultados si hay más menús
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar los recursos
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return servicio; // Retornar el servicio encontrado o null si no existe
    }

    public boolean actualizarMenuNombrePrecio(int idMenu, String nombre, int precio) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Consulta SQL para actualizar los datos del cliente
            String sql = "UPDATE menu \n"
                    + "SET nombreMenu = ? , precio = ?\n"
                    + "WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setInt(2, precio);
            ps.setInt(3, idMenu);

            // Ejecutar la actualización
            int filasAfectadas = ps.executeUpdate();

            // Confirmar o revertir transacción según el resultado
            if (filasAfectadas > 0) {
                con.commit();  // Confirmar la transacción
                return true;
            } else {
                con.rollback();  // Revertir cambios si no se actualizó nada
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();  // Hacer rollback si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Cerrar los recursos
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean agregarPlatosMenu(int idMenu, List<Integer> platosEntradaSeleccionados, List<Integer> platosPrincipalSeleccionados) {
        boolean registrado = false;
        Connection con = null;
        PreparedStatement menuPlato = null;

        try {
            // Establecer conexión con la base de datos
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            con.setAutoCommit(false);

            // 1. Asociar platos de entrada
            String menuPlatoSql = "INSERT INTO Menu_Plato (menu_id, plato_id, tipo) VALUES (?, ?, ?)";
            menuPlato = con.prepareStatement(menuPlatoSql);

            // Insertar los platos de entrada seleccionados
            for (int platoId : platosEntradaSeleccionados) {
                menuPlato.setInt(1, idMenu);
                menuPlato.setInt(2, platoId);
                menuPlato.setString(3, "Entrada");  // Tipo de plato: Entrada
                menuPlato.addBatch();
            }

            // Ejecutar el batch de platos de entrada
            menuPlato.executeBatch();

            // 2. Asociar platos principales
            for (int platoId : platosPrincipalSeleccionados) {
                menuPlato.setInt(1, idMenu);
                menuPlato.setInt(2, platoId);
                menuPlato.setString(3, "Principal");  // Tipo de plato: Principal
                menuPlato.addBatch();
            }

            // Ejecutar el batch de platos principales
            menuPlato.executeBatch();

            // Si todo va bien, hacer commit
            con.commit();
            registrado = true;

        } catch (SQLException e) {
            // Si ocurre una excepción, hacer rollback
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    reportException(ex.getMessage());
                }
            }
            reportException(e.getMessage());
        } finally {
            // Cerrar recursos
            try {
                if (menuPlato != null) {
                    menuPlato.close();
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

    public boolean eliminarPlatosMenu(int idMenu, List<Integer> platosEntradaEliminar, List<Integer> platosPrincipalEliminar) {
        boolean eliminado = false;
        Connection con = null;
        PreparedStatement menuPlato = null;

        try {
            // Establecer conexión con la base de datos
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            con.setAutoCommit(false);

            // 1. Eliminar los platos de entrada
            String menuPlatoSql = "DELETE FROM Menu_Plato WHERE menu_id = ? AND plato_id = ? AND tipo = ?";
            menuPlato = con.prepareStatement(menuPlatoSql);

            // Eliminar los platos de entrada seleccionados
            for (int platoId : platosEntradaEliminar) {
                menuPlato.setInt(1, idMenu);
                menuPlato.setInt(2, platoId);
                menuPlato.setString(3, "Entrada");  // Tipo de plato: Entrada
                menuPlato.addBatch();
            }

            // Ejecutar el batch de eliminación de platos de entrada
            menuPlato.executeBatch();

            // 2. Eliminar los platos principales
            for (int platoId : platosPrincipalEliminar) {
                menuPlato.setInt(1, idMenu);
                menuPlato.setInt(2, platoId);
                menuPlato.setString(3, "Principal");  // Tipo de plato: Principal
                menuPlato.addBatch();
            }

            // Ejecutar el batch de eliminación de platos principales
            menuPlato.executeBatch();

            // Si todo va bien, hacer commit
            con.commit();
            eliminado = true;

        } catch (SQLException e) {
            // Si ocurre una excepción, hacer rollback
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    reportException(ex.getMessage());
                }
            }
            reportException(e.getMessage());
        } finally {
            // Cerrar recursos
            try {
                if (menuPlato != null) {
                    menuPlato.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                reportException(e.getMessage());
            }
        }

        return eliminado;
    }

    public List<Plato> obtenerPlatosNoSeleccionados(int idMenu, String tipo) {
        List<Plato> platos = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Definir la consulta SQL para obtener los platos no asociados
        String sql = "SELECT p.id, p.nombre "
                + "FROM plato p "
                + "LEFT JOIN menu_plato mp ON p.id = mp.plato_id AND mp.menu_id = ? AND mp.tipo = ? "
                + "WHERE mp.plato_id IS NULL";

        try {
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            ps = con.prepareStatement(sql);
            ps.setInt(1, idMenu);  // Establecemos el id del menú
            ps.setString(2, tipo);  // Establecemos el tipo de plato ('Entrada' o 'Principal')
            rs = ps.executeQuery();

            // Procesar los resultados de la consulta
            while (rs.next()) {
                Plato plato = new Plato(rs.getInt("id"), rs.getString("nombre"));
                platos.add(plato);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return platos;
    }

    public List<Menu> obtenerMenuNoSeleccionado(int idServicio) {
        List<Menu> menus = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Consulta SQL para obtener los menús no seleccionados en el servicio dado
        String sql = "SELECT m.id "
                + "FROM menu m "
                + "LEFT JOIN servicio_menu sm ON m.id = sm.menu_id AND sm.servicio_id = ? "
                + "WHERE sm.menu_id IS NULL AND m.estado = 1";

        try {
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            ps = con.prepareStatement(sql);
            ps.setInt(1, idServicio); // Establecemos el id del servicio
            rs = ps.executeQuery();

            // Procesar los resultados de la consulta
            while (rs.next()) {
                Menu menu = obtenerMenuConId(rs.getInt("id"));
                menus.add(menu);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return menus;
    }

    public boolean actualizarServicioMenu(int idServicio, String nombre) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Consulta SQL para actualizar los datos del cliente
            String sql = "UPDATE servicio \n"
                    + "SET nombreServicio = ? \n"
                    + "WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setInt(2, idServicio);

            // Ejecutar la actualización
            int filasAfectadas = ps.executeUpdate();

            // Confirmar o revertir transacción según el resultado
            if (filasAfectadas > 0) {
                con.commit();  // Confirmar la transacción
                return true;
            } else {
                con.rollback();  // Revertir cambios si no se actualizó nada
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();  // Hacer rollback si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Cerrar los recursos
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean agregarMenuServicio(int idServicio, List<Integer> menusSeleccionados) {
        boolean registrado = false;
        Connection con = null;
        PreparedStatement servicioMenu = null;

        try {
            // Establecer conexión con la base de datos
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            con.setAutoCommit(false);

            // SQL para insertar la relación entre los menús y el servicio
            String servicioMenuSql = "INSERT INTO Servicio_Menu (servicio_id, menu_id) VALUES (?, ?)";
            servicioMenu = con.prepareStatement(servicioMenuSql);

            // Insertar los menús seleccionados para el servicio
            for (int menuId : menusSeleccionados) {
                servicioMenu.setInt(1, idServicio); // idServicio
                servicioMenu.setInt(2, menuId);     // menu_id
                servicioMenu.addBatch();
            }

            // Ejecutar el batch de inserciones
            servicioMenu.executeBatch();

            // Si todo va bien, hacer commit
            con.commit();
            registrado = true;

        } catch (SQLException e) {
            // Si ocurre una excepción, hacer rollback
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    reportException(ex.getMessage());
                }
            }
            reportException(e.getMessage());
        } finally {
            // Cerrar recursos
            try {
                if (servicioMenu != null) {
                    servicioMenu.close();
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

    public boolean eliminarMenuServicio(int idServicio, List<Integer> menusSeleccionados) {
        boolean eliminado = false;
        Connection con = null;
        PreparedStatement servicioMenu = null;

        try {
            // Establecer conexión con la base de datos
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            con.setAutoCommit(false);

            // SQL para eliminar la relación entre los menús y el servicio
            String servicioMenuSql = "DELETE FROM servicio_menu WHERE servicio_id = ? AND menu_id = ?";
            servicioMenu = con.prepareStatement(servicioMenuSql);

            // Eliminar los menús seleccionados para el servicio
            for (int idMenu : menusSeleccionados) {
                servicioMenu.setInt(1, idServicio); // idServicio
                servicioMenu.setInt(2, idMenu);     // menu_id
                servicioMenu.addBatch();
            }

            // Ejecutar el batch de eliminaciones
            servicioMenu.executeBatch();

            // Si todo va bien, hacer commit
            con.commit();
            eliminado = true;

        } catch (SQLException e) {
            // Si ocurre una excepción, hacer rollback
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    reportException(ex.getMessage());
                }
            }
            reportException(e.getMessage());
        } finally {
            // Cerrar recursos
            try {
                if (servicioMenu != null) {
                    servicioMenu.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                reportException(e.getMessage());
            }
        }

        return eliminado;
    }

    public List<Servicio> obtenerServiciosNoSeleccionados(int idReserva, String fechaInicio, String fechaFin) {
        List<Servicio> servicios = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Consulta SQL para obtener los servicios no seleccionados y disponibles en las fechas
        String sql = "SELECT s.id "
                + "FROM servicio s "
                + "LEFT JOIN reserva_servicio rs ON s.id = rs.servicio_id AND rs.reserva_id = ? "
                + "WHERE rs.servicio_id IS NULL AND s.estado = 1 "
                + "AND NOT EXISTS ( "
                + "    SELECT 1 "
                + "    FROM reserva_servicio rs2 "
                + "    JOIN reserva r ON rs2.reserva_id = r.id "
                + "    WHERE (r.fechaInicioEvento < ? AND r.fechaFinEvento > ?) "
                + "    AND rs2.servicio_id = s.id and r.estado = 1"
                + ")";

        try {
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            ps = con.prepareStatement(sql);
            ps.setInt(1, idReserva); // Establecemos el id de la reserva
            ps.setString(2, fechaFin);  // Fecha de fin
            ps.setString(3, fechaInicio); // Fecha de inicio
            rs = ps.executeQuery();

            // Procesar los resultados de la consulta
            while (rs.next()) {
                Servicio servicio = obtenerServicioConId(rs.getInt("id"));
                servicios.add(servicio);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return servicios;
    }

    public Reserva obtenerReservaConId(int idReserva) {
        Reserva reserva = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Consulta SQL ajustada para obtener la reserva con servicios y menús, y la dirección
        String sql = "SELECT r.id AS reserva_id, r.codCliente, r.fechaInicioEvento, r.fechaFinEvento, r.restriccionesDieteticas, "
                + "r.preferenciaCliente, r.tipoServicio, r.cantidadPersonas, r.precio, r.modoDeReserva, r.direccionDeEntrega_id, r.estaEntregado, "
                + "r.estado AS reserva_estado, "
                + "d.calle, d.altura, d.barrio, "
                + "s.id AS servicio_id "
                + "FROM reserva r "
                + "JOIN domicilio d ON r.direccionDeEntrega_id = d.id "
                + "LEFT JOIN reserva_servicio rs ON r.id = rs.reserva_id "
                + "LEFT JOIN servicio s ON rs.servicio_id = s.id "
                + "WHERE r.id = ? AND r.estado = 1 AND s.estado = 1"; // Filtramos por estado activo de reserva y servicio

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Preparar la consulta con el parámetro idReserva
            ps = con.prepareStatement(sql);
            ps.setInt(1, idReserva); // Establecemos el valor de idReserva en la consulta
            rs = ps.executeQuery();

            // Inicializar las listas de menús y servicios para la reserva
            List<Servicio> servicios = new ArrayList<>();

            // Procesar los resultados
            while (rs.next()) {
                // Crear o asignar la reserva
                if (reserva == null) {
                    // Obtener los valores de la dirección
                    String calle = rs.getString("calle");
                    int altura = rs.getInt("altura");
                    String barrio = rs.getString("barrio");
                    Domicilio direccionDeEntrega = new Domicilio(rs.getInt("direccionDeEntrega_id"), calle, altura, barrio);

                    // Convertir el valor de estaEntregado de int a boolean
                    boolean estaEntregado = rs.getInt("estaEntregado") == 1;

                    // Crear la reserva con los datos obtenidos
                    reserva = new Reserva(
                            rs.getInt("reserva_id"),
                            rs.getInt("codCliente"),
                            rs.getTimestamp("fechaInicioEvento"),
                            rs.getTimestamp("fechaFinEvento"),
                            rs.getString("restriccionesDieteticas"),
                            rs.getString("preferenciaCliente"),
                            rs.getString("tipoServicio"),
                            rs.getInt("cantidadPersonas"),
                            rs.getInt("precio"),
                            rs.getString("modoDeReserva"),
                            direccionDeEntrega,
                            estaEntregado
                    );
                }

                // Crear el servicio asociado si existe
                int servicioId = rs.getInt("servicio_id");
                if (servicioId > 0) {
                    Servicio servicio = obtenerServicioConId(servicioId);
                    servicios.add(servicio);
                }
            }

            // Establecer los servicios en la reserva
            if (reserva != null) {
                reserva.setServicios(servicios);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar los recursos
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return reserva; // Retornar la reserva con los servicios y menús asociados, o null si no existe
    }

    public boolean actualizarReserva(int idReserva, int codCliente, java.util.Date fechaInicioEvento, java.util.Date fechaFinEvento, String restriccionesDieteticas, String preferenciaCliente, String tipoServicio, int cantidadPersonas, String modoDeReserva, String calle, int altura, String barrio, boolean estaEntregado) {

        Connection con = null;
        PreparedStatement ps = null;

        Reserva r = obtenerReservaConId(idReserva);
        int precio = obtenerPrecioReserva(r.getServicios(), cantidadPersonas);

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Actualizar datos de la reserva
            String sqlReserva = "UPDATE reserva "
                    + "SET codCliente = ?, fechaInicioEvento = ?, fechaFinEvento = ?, "
                    + "restriccionesDieteticas = ?, preferenciaCliente = ?, tipoServicio = ?, "
                    + "cantidadPersonas = ?, precio = ?, modoDeReserva = ?, estaEntregado = ? "
                    + "WHERE id = ?";
            ps = con.prepareStatement(sqlReserva);
            ps.setInt(1, codCliente);
            ps.setTimestamp(2, new java.sql.Timestamp(fechaInicioEvento.getTime()));
            ps.setTimestamp(3, new java.sql.Timestamp(fechaFinEvento.getTime()));
            ps.setString(4, restriccionesDieteticas);
            ps.setString(5, preferenciaCliente);
            ps.setString(6, tipoServicio);
            ps.setInt(7, cantidadPersonas);
            ps.setInt(8, precio);
            ps.setString(9, modoDeReserva);
            ps.setBoolean(10, estaEntregado);
            ps.setInt(11, idReserva);

            // Ejecutar la actualización de la reserva
            int filasAfectadasReserva = ps.executeUpdate();

            // Actualizar dirección de entrega (si es necesario)
            String sqlDireccion = "UPDATE domicilio "
                    + "SET calle = ?, altura = ?, barrio = ? "
                    + "WHERE id = (SELECT direccionDeEntrega_id FROM reserva WHERE id = ?)";
            ps = con.prepareStatement(sqlDireccion);
            ps.setString(1, calle);
            ps.setInt(2, altura);
            ps.setString(3, barrio);
            ps.setInt(4, idReserva);

            // Ejecutar la actualización de la dirección de entrega
            int filasAfectadasDireccion = ps.executeUpdate();

            // Confirmar la transacción si se actualizaron las filas correctamente
            if (filasAfectadasReserva > 0 && filasAfectadasDireccion > 0) {
                con.commit();  // Confirmar la transacción
                return true;
            } else {
                con.rollback();  // Revertir los cambios si no se actualizó nada
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();  // Hacer rollback si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Cerrar los recursos
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean agregarServicioReserva(int idReserva, List<Integer> serviciosSeleccionados) {
        boolean registrado = false;
        Connection con = null;
        PreparedStatement reservaServicio = null;
        List<Servicio> servicios = new ArrayList<>();

        Reserva r = obtenerReservaConId(idReserva);
        for (Integer id : serviciosSeleccionados) {
            Servicio s = obtenerServicioConId(id);
            servicios.add(s);
        }

        int precioNuevosServicios = obtenerPrecioReserva(servicios, r.getCantidadPersonas());
        int precioTotalActualizado = r.getPrecio() + precioNuevosServicios;

        boolean actualizado = actualizarPrecioReserva(idReserva, precioTotalActualizado);
        if (actualizado) {
            try {
                // Establecer conexión con la base de datos
                con = DriverManager.getConnection(urlRoot + dbName, "", "");
                con.setAutoCommit(false); // Desactivar el auto-commit para asegurar la atomicidad

                // SQL para insertar la relación entre la reserva y los servicios
                String reservaServicioSql = "INSERT INTO reserva_servicio (reserva_id, servicio_id) VALUES (?, ?)";
                reservaServicio = con.prepareStatement(reservaServicioSql);

                // Insertar los servicios seleccionados para la reserva
                for (int servicioId : serviciosSeleccionados) {
                    reservaServicio.setInt(1, idReserva);     // reserva_id
                    reservaServicio.setInt(2, servicioId);    // servicio_id
                    reservaServicio.addBatch(); // Añadir al batch
                }

                // Ejecutar el batch de inserciones
                reservaServicio.executeBatch();

                // Si todo va bien, hacer commit
                con.commit();
                registrado = true;

            } catch (SQLException e) {
                // Si ocurre una excepción, hacer rollback
                if (con != null) {
                    try {
                        con.rollback(); // Revertir las inserciones en caso de error
                    } catch (SQLException ex) {
                        reportException(ex.getMessage()); // Manejar el rollback
                    }
                }
                reportException(e.getMessage()); // Manejar el error original
            } finally {
                // Cerrar recursos
                try {
                    if (reservaServicio != null) {
                        reservaServicio.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    reportException(e.getMessage()); // Manejar el cierre de recursos
                }
            }

        }
        return registrado;
    }

    public boolean eliminarServicioReserva(int idReserva, List<Integer> serviciosSeleccionados) {
        boolean eliminado = false;
        Connection con = null;
        PreparedStatement stmt = null;
        List<Servicio> servicios = new ArrayList<>();
        Reserva r = obtenerReservaConId(idReserva);
        for (Integer id : serviciosSeleccionados) {
            Servicio s = obtenerServicioConId(id);
            servicios.add(s);
        }

        int precioNuevosServicios = obtenerPrecioReserva(servicios, r.getCantidadPersonas());
        int precioTotalActualizado = r.getPrecio() - precioNuevosServicios;

        boolean actualizado = actualizarPrecioReserva(idReserva, precioTotalActualizado);
        if (actualizado) {
            try {
                // Establecer conexión con la base de datos
                con = DriverManager.getConnection(urlRoot + dbName, "", "");
                con.setAutoCommit(false); // Desactivamos el autocommit para hacer el commit manual

                // SQL para eliminar la relación entre la reserva y los servicios seleccionados
                String sql = "DELETE FROM reserva_servicio WHERE reserva_id = ? AND servicio_id = ?";

                stmt = con.prepareStatement(sql);

                // Eliminar los servicios seleccionados para la reserva
                for (int idServicio : serviciosSeleccionados) {
                    stmt.setInt(1, idReserva); // Establecemos el id de la reserva
                    stmt.setInt(2, idServicio); // Establecemos el id del servicio
                    stmt.addBatch(); // Añadimos la eliminación al batch
                }

                // Ejecutar el batch de eliminaciones
                stmt.executeBatch();

                // Si todo va bien, hacer commit
                con.commit();
                eliminado = true;

            } catch (SQLException e) {
                // Si ocurre una excepción, hacemos rollback
                if (con != null) {
                    try {
                        con.rollback();
                    } catch (SQLException ex) {
                        reportException(ex.getMessage());
                    }
                }
                reportException(e.getMessage());
            } finally {
                // Cerrar recursos
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    reportException(e.getMessage());
                }
            }
        }

        return eliminado;
    }

    private int obtenerPrecioReserva(List<Servicio> servicios, int cantPersonas) {

        if (servicios == null || servicios.isEmpty()) {
            return 0; // No hay servicios, el precio es 0
        }
        int montoTotal = 0;

        for (Servicio s : servicios) {
            for (Menu m : s.getMenus()) {
                montoTotal += m.getPrecio();
            }
        }

        return montoTotal * cantPersonas;
    }

    private boolean actualizarPrecioReserva(int idReserva, int precioTotalActualizado) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Establecer la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Iniciar transacción
            con.setAutoCommit(false);

            // Actualizar datos de la reserva
            String sqlReserva = "UPDATE reserva "
                    + "SET  precio = ? "
                    + "WHERE id = ?";
            ps = con.prepareStatement(sqlReserva);
            ps.setInt(1, precioTotalActualizado);
            ps.setInt(2, idReserva);

            // Ejecutar la actualización de la reserva
            int filasAfectadasReserva = ps.executeUpdate();

            // Confirmar la transacción si se actualizaron las filas correctamente
            if (filasAfectadasReserva > 0) {
                con.commit();  // Confirmar la transacción
                return true;
            } else {
                con.rollback();  // Revertir los cambios si no se actualizó nada
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();  // Hacer rollback si ocurre un error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Cerrar los recursos
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean verificarPersona(String usuario, String password) {
        String query = "SELECT COUNT(*) FROM Persona WHERE usuario = ? and  AND password = ? estado = 1";
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean personaExiste = false; // Valor por defecto

        try {
            // Establecemos la conexión
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Preparamos la consulta
            stmt = con.prepareStatement(query);
            stmt.setString(1, usuario);
            stmt.setString(2, password);

            // Ejecutamos la consulta
            rs = stmt.executeQuery();

            // Procesamos el resultado
            if (rs.next()) {
                int count = rs.getInt(1); // El resultado de COUNT(*) está en la primera columna
                if (count > 0) {
                    personaExiste = true; // Cliente encontrado con estado 1
                }
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

        return personaExiste;
    }

}
