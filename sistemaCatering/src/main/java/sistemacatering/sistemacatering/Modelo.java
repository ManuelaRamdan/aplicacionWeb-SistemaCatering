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
        String query = "SELECT c.id, c.nombre, c.apellido, c.telReferencia, c.email "
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

                // Crear el objeto Cliente y agregarlo a la lista
                listaClientes.add(new Cliente(id, nombre, apellido, telReferencia, email));
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

                // Imprimir los valores recuperados para verificar
                System.out.println("Reserva: " + reserva);  // Verifica si todos los campos tienen valores correctos.

                listaReservas.add(reserva);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Mostrar errores en la consola
        }

        return listaReservas;
    }

    public Cliente obtenerClientePorId(String idCliente) {
        Cliente cliente = null;
        String query = "SELECT c.id, c.nombre, c.apellido, c.telReferencia, c.email "
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

                // Crear el objeto Cliente con los datos obtenidos
                cliente = new Cliente(id, nombre, apellido, telReferencia, email);
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
    public ArrayList<Reserva> obtenerReservasPorCliente(String idCliente) {
        ArrayList<Reserva> reservas = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(urlRoot + dbName, "", ""); PreparedStatement stmt = con.prepareStatement(
                "SELECT r.codReserva, r.codCliente, r.fechaInicioEvento, r.fechaFinEvento, "
                + "r.restriccionesDieteticas, r.preferenciaCliente, r.tipoServicio, r.cantidadPersonas, "
                + "r.precio, r.modoDeReserva, r.direccionDeEntrega_id, r.estaEntregado, "
                + "d.calle, d.altura, d.barrio "
                + "FROM Reserva r "
                + "LEFT JOIN Domicilio d ON r.direccionDeEntrega_id = d.id "
                + "WHERE r.codCliente = ? and r.estado = 1")) {

            stmt.setString(1, idCliente);  // Establecer el parámetro para buscar por cliente
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Obtener los valores de la reserva
                int codReserva = rs.getInt("codReserva");
                int codCliente = rs.getInt("codCliente");

                // Obtener los valores de tipo Timestamp desde la base de datos
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
        } catch (SQLException e) {
            reportException(e.getMessage());
        }
        return reservas;
    }

}
