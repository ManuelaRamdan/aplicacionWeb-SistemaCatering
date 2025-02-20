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

    public List<Coordinador> buscarCoordinador(String busqueda) {
        List<Coordinador> coordinadores = new ArrayList<>();

        String query = "SELECT * FROM Coordinador c "
                + "INNER JOIN Persona p ON c.persona_id = p.id "
                + "WHERE p.usuario LIKE ?";  // Buscar solo por usuario en la tabla Persona

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(urlRoot + dbName, "", "");
            stmt = con.prepareStatement(query);

            // Establecer el parámetro para la búsqueda
            stmt.setString(1, "%" + busqueda + "%");

            // Ejecutar la consulta y obtener los resultados
            rs = stmt.executeQuery();

            // Procesar el resultado
            while (rs.next()) {
                int cod = rs.getInt("id");
                String usuario = rs.getString("usuario");
                Coordinador coordinador = new Coordinador(cod, usuario);
                coordinadores.add(coordinador);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de errores
        } finally {
            // Cerrar recursos en el bloque finally
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
                e.printStackTrace();
            }
        }

        return coordinadores;
    }

    public List<Reserva> obtenerReservaBd() {
        List<Reserva> listaReservas = new ArrayList<>();
        String query = "SELECT r.id, r.codCliente, r.fechaInicioEvento, r.fechaFinEvento, r.restriccionesDieteticas, "
                + "r.preferenciaCliente, r.tipoServicio, r.cantidadPersonas, r.precio, r.modoDeReserva, "
                + "r.direccionDeEntrega_id, r.estaEntregado, d.calle, d.altura, d.barrio "
                + "FROM Reserva r "
                + "JOIN Domicilio d ON r.direccionDeEntrega_id = d.id "
                + "WHERE r.estado = 1"; // Asegúrate de filtrar por reservas activas

        try (Connection con = DriverManager.getConnection(urlRoot + dbName, "", ""); PreparedStatement stmt = con.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            // Procesamos el resultado
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

                // Crear y añadir la reserva a la lista
                Reserva reserva = new Reserva(
                        codReserva, codCliente, fechaInicioEvento, fechaFinEvento, restriccionesDieteticas,
                        preferenciaCliente, tipoServicio, cantidadPersonas, precio, modoDeReserva,
                        direccionDeEntrega, estaEntregado
                );

                listaReservas.add(reserva);
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
        String query = "SELECT s.id, s.nombreServicio "
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
                String nombreServicio = rs.getString("nombreServicio");

                // Creamos el objeto Servicio y lo agregamos a la lista
                Servicio servicio = new Servicio(id, nombreServicio);
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

    boolean verificarCliente(int codCliente) {
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

    public Servicio obtenerServicioPorId(int idServicio) {
        // Consulta SQL para obtener el servicio por su ID
        String query = "SELECT * FROM Servicio WHERE id = ?";

        // Variables para manejar la conexión y la consulta
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Servicio servicio = null; // Inicializamos la variable de tipo Servicio

        try {
            // Establecemos la conexión a la base de datos
            con = DriverManager.getConnection(urlRoot + dbName, "", "");

            // Preparamos la consulta
            stmt = con.prepareStatement(query);
            stmt.setInt(1, idServicio); // Establecemos el parámetro idServicio en la consulta

            // Ejecutamos la consulta
            rs = stmt.executeQuery();

            // Verificamos si hay resultados
            if (rs.next()) {
                // Recuperamos los valores de la base de datos y los asignamos al objeto Servicio
                int id = rs.getInt("id");
                String nombre = rs.getString("nombreServicio");
                // Creamos el objeto Servicio con los valores recuperados
                servicio = new Servicio(id, nombre);  // Asegúrate de que el constructor exista
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Manejo de errores
        } finally {
            // Cerramos los recursos manualmente
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
                e.printStackTrace();  // Manejo de errores al cerrar recursos
            }
        }

        return servicio;  // Retornamos el objeto Servicio o null si no se encontró
    }

    boolean registrarReserva(Reserva reserva, Domicilio domicilio) {
        boolean registrado = false;
        Connection con = null;
        PreparedStatement reservaStmt = null;
        PreparedStatement reservaServicioStmt = null;
        PreparedStatement domicilioStmt = null;
        ResultSet generatedKeys = null;

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
}
