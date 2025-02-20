/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemacatering.sistemacatering;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ControladorAdm", urlPatterns = {"/ControladorAdm"})
public class ControladorAdm extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) // obtener datos para la vista
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String idAdministrador = (String) session.getAttribute("userId");

        if (idAdministrador != null) {
            Modelo modelo = new Modelo("localhost", "catering"); // Conexión con la base de datos

            // Obtener la acción que el administrador desea realizar (en caso de que haya una acción específica)
            String accion = request.getParameter("accion");

            // Si no hay acción específica, por defecto se carga la vista de menú
            if (accion == null) {
                accion = "menu"; // Acción por defecto
            }

            switch (accion) {
                case "menu":
                    // Redirigir a la vista de menú
                    RequestDispatcher dispatcherMenu = request.getRequestDispatcher("vistaAdministradorMenu.jsp");
                    dispatcherMenu.forward(request, response);
                    break;

                case "mostrarAlta":

                    List<Plato> platos = modelo.obtenerPlatosBd();
                    List<Menu> menus = modelo.obtenerMenusBd();

                    request.setAttribute("platosEntrada", platos);
                    request.setAttribute("platosPrincipal", platos);
                    request.setAttribute("menus", menus);
                    request.getRequestDispatcher("vistaAdmAlta.jsp").forward(request, response);
                    break;

                case "mostrarBaja":
                    List<Coordinador> coordinadores = modelo.obtenerCoordinadoresBd();
                    List<Administrador> administradores = modelo.obtenerAdministradoresBd();

                    request.setAttribute("coordinadores", coordinadores);
                    request.setAttribute("administradores", administradores);

                    List<Cliente> clientes = modelo.obtenerClientesBd();
                    request.setAttribute("clientes", clientes);

                    platos = modelo.obtenerPlatosBd();
                    menus = modelo.obtenerMenusBd();

                    request.setAttribute("platos", platos);

                    request.setAttribute("menus", menus);

                    List<Servicio> servicios = modelo.obtenerServiciosBd();
                    request.setAttribute("servicios", servicios);

                    request.getRequestDispatcher("vistaAdmBaja.jsp").forward(request, response);
                    break;

                case "mostrarModificarAdministrador":

                    administradores = modelo.obtenerAdministradoresBd();

                    request.setAttribute("administradores", administradores);

                    request.getRequestDispatcher("vistaAdmModificarAdm.jsp").forward(request, response);
                    break;

                case "mostrarModificarCoordinador":
                    coordinadores = modelo.obtenerCoordinadoresBd();

                    request.setAttribute("coordinadores", coordinadores);
                    request.getRequestDispatcher("vistaAdmModificarCoordinador.jsp").forward(request, response);
                    break;
                case "mostrarModificarCliente":

                    clientes = modelo.obtenerClientesBd();
                    request.setAttribute("clientes", clientes);

                    request.getRequestDispatcher("vistaAdmModificarCliente.jsp").forward(request, response);
                    break;

                case "mostrarModificarPlato":

                    platos = modelo.obtenerPlatosBd();

                    request.setAttribute("platos", platos);

                    request.getRequestDispatcher("vistaAdmModificarPlato.jsp").forward(request, response);
                    break;
                case "mostrar":
                    // Fetch all data
                    administradores = modelo.obtenerAdministradoresBd();
                    clientes = modelo.obtenerClientesBd();
                    coordinadores = modelo.obtenerCoordinadoresBd();
                    platos = modelo.obtenerPlatosBd();
                    menus = modelo.obtenerMenusBd();

                    servicios = modelo.obtenerServiciosBd();

                    // Set the data as request attributes
                    request.setAttribute("administradores", administradores);
                    request.setAttribute("clientes", clientes);
                    request.setAttribute("coordinadores", coordinadores);
                    request.setAttribute("platos", platos);
                    request.setAttribute("menus", menus);
                    request.setAttribute("servicios", servicios);

                    List<Reserva> reservas = modelo.obtenerReservaBd();
                    request.setAttribute("reservas", reservas);

                    // Forward to the JSP page
                    request.getRequestDispatcher("vistaAdmMostrar.jsp").forward(request, response);
                    break;

                default:
                    // Si la acción no es reconocida, redirige a una página de error
                    request.setAttribute("mensajeError", "Acción no válida");
                    request.getRequestDispatcher("vistaError.jsp").forward(request, response);
                    break;
            }

        } else {
            // Si el usuario no está logueado, redirigir al login
            RequestDispatcher dispatcherLogin = request.getRequestDispatcher("login.jsp");
            dispatcherLogin.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) // enviar datos necesarios
            throws ServletException, IOException {

        String action = request.getParameter("action");

        /* toma lo que le envio el formulario */
        Modelo modelo = new Modelo("localhost", "catering");

        switch (action) {
            case "registrarCoordinador":
                String usuario = request.getParameter("usuario");
                String password = request.getParameter("password");

                boolean registrado = modelo.registrarCoordinador(usuario, password);

                /* dependiendo si se pudo registrar o no , muestra un mensaje */
                if (registrado) {
                    request.setAttribute("mensajeCoordinador", "Coordinador registrado correctamente.");
                } else {
                    request.setAttribute("mensajeCoordinador", "Error al registrar el coordinador.");
                }

               response.sendRedirect("ControladorAdm?accion=mostrarAlta");
                break;

            case "registrarAdministrador":

                usuario = request.getParameter("usuario");
                password = request.getParameter("password");

                registrado = modelo.registrarAdministrador(usuario, password);

                /* dependiendo si se pudo registrar o no , muestra un mensaje */
                if (registrado) {
                    request.setAttribute("mensajeAdm", "Administrador registrado correctamente.");
                } else {
                    request.setAttribute("mensajeAdm", "Error al registrar el Administrador.");
                }

               response.sendRedirect("ControladorAdm?accion=mostrarAlta");

                break;

            case "registrarCliente":
                usuario = request.getParameter("usuario");
                password = request.getParameter("password");
                String nombre = request.getParameter("nombre");
                String apellido = request.getParameter("apellido");
                String telefono = request.getParameter("telefono");
                String email = request.getParameter("email");

                registrado = modelo.registrarCliente(usuario, password, nombre, apellido, telefono, email);
                if (registrado) {
                    request.setAttribute("mensajeCliente", "Cliente registrado correctamente.");
                } else {
                    request.setAttribute("mensajeCliente", "Error al registrar el cliente.");
                }

               response.sendRedirect("ControladorAdm?accion=mostrarAlta");

                break;

            case "registrarPlato":

                String nombrePlato = request.getParameter("nombrePlato");

                registrado = modelo.registrarPlato(nombrePlato);

                if (registrado) {
                    request.setAttribute("mensajePlato", "Plato registrado correctamente.");
                } else {
                    request.setAttribute("mensajePlato", "Error al registrar el plato.");
                }

               response.sendRedirect("ControladorAdm?accion=mostrarAlta");

                break;

            case "registrarMenu":
                String nombreMenu = request.getParameter("nombreMenu");

                // Obtener IDs de platos de entrada seleccionados
                String[] platosEntradaIds = request.getParameterValues("platoEntrada[]");
                List<Integer> platosEntradaSeleccionados = modelo.obtenerIdsSeleccionados(platosEntradaIds);

                // Obtener IDs de platos principales seleccionados
                String[] platosPrincipalIds = request.getParameterValues("platoPrincipal[]");
                List<Integer> platosPrincipalSeleccionados = modelo.obtenerIdsSeleccionados(platosPrincipalIds);

                registrado = modelo.registrarMenu(nombreMenu, platosEntradaSeleccionados, platosPrincipalSeleccionados);

                if (registrado) {
                    request.setAttribute("mensajeMenu", "Menu registrado correctamente.");
                } else {
                    request.setAttribute("mensajeMenu", "Error al registrar el Menu.");
                }

               response.sendRedirect("ControladorAdm?accion=mostrarAlta");

                break;
            case "registrarServicio":
                String nombreServicio = request.getParameter("nombreServicio");

                // Obtener IDs de menús seleccionados
                String[] menuIds = request.getParameterValues("menus[]");
                List<Integer> menusSeleccionados = modelo.obtenerIdsSeleccionados(menuIds);

                registrado = modelo.registrarServicio(nombreServicio, menusSeleccionados);

                if (registrado) {
                    request.setAttribute("mensajeServicio", "Servicio registrado correctamente.");
                } else {
                    request.setAttribute("mensajeServicio", "Error al registrar el Servicio.");
                }

               response.sendRedirect("ControladorAdm?accion=mostrarAlta");

                break;
            case "eliminarCoordinador":
                int idCoordinador = Integer.parseInt(request.getParameter("idCoordinador"));

                boolean eliminado = modelo.eliminarCoordinador(idCoordinador);

                if (eliminado) {
                    request.setAttribute("mensajeBajaCoordinador", "Coordinador dado de baja exitosamente.");
                } else {
                    request.setAttribute("mensajeBajaCoordinador", "Error, no se puso eliminar");
                }

               response.sendRedirect("ControladorAdm?accion=mostrarBaja");


                break;
            case "eliminarAdministrador":
                int idAdministrador = Integer.parseInt(request.getParameter("idAdministrador"));

                eliminado = modelo.eliminarAdministrador(idAdministrador);

                if (eliminado) {
                    request.setAttribute("mensajeBajaAdministrador", "Administrador dado de baja exitosamente.");
                } else {
                    request.setAttribute("mensajeBajaAdministrador", "Error, no se puso eliminar");
                }

               response.sendRedirect("ControladorAdm?accion=mostrarBaja");


                break;

            case "eliminarCliente":
                int idCliente = Integer.parseInt(request.getParameter("idCliente"));

                eliminado = modelo.eliminarCliente(idCliente);

                if (eliminado) {
                    request.setAttribute("mensajeBajaCliente", "Cliente dado de baja exitosamente.");
                } else {
                    request.setAttribute("mensajeBajaCliente", "Error, no se puso eliminar");
                }

                   response.sendRedirect("ControladorAdm?accion=mostrarBaja");


                break;

            case "eliminarPlato":
                int idPlato = Integer.parseInt(request.getParameter("idPlato"));
                eliminado = modelo.eliminarPlato(idPlato);

                if (eliminado) {
                    request.setAttribute("mensajeBajaPlato", "Plato dado de baja exitosamente.");
                } else {
                    request.setAttribute("mensajeBajaPlato", "Error, no se pudo eliminar");
                }

               response.sendRedirect("ControladorAdm?accion=mostrarBaja");


                break;

            case "eliminarMenu":
                int idMenu = Integer.parseInt(request.getParameter("idMenu"));
                eliminado = modelo.eliminarMenu(idMenu);

                if (eliminado) {
                    request.setAttribute("mensajeBajaMenu", "Menú dado de baja exitosamente.");
                } else {
                    request.setAttribute("mensajeBajaMenu", "Error, no se pudo eliminar");
                }

               response.sendRedirect("ControladorAdm?accion=mostrarBaja");

                break;

            case "eliminarServicio":
                int idServicio = Integer.parseInt(request.getParameter("idServicio"));
                eliminado = modelo.eliminarServicio(idServicio);

                if (eliminado) {
                    request.setAttribute("mensajeBajaServicio", "Servicio dado de baja exitosamente.");
                } else {
                    request.setAttribute("mensajeBajaServicio", "Error, no se pudo eliminar");
                }

               response.sendRedirect("ControladorAdm?accion=mostrarBaja");

                break;

            case "modificarCliente":
                String personaId = request.getParameter("persona_id");  // Obtener persona_id
                Cliente cliente = modelo.obtenerClientePorId(personaId);  // Obtener reservas usando persona_id
                request.setAttribute("cliente", cliente);  // Establecer las reservas en el request
               response.sendRedirect("ControladorAdm?accion=mostrarModificarCliente");

                break;

            case "actualizarCliente":

                try {
                    idCliente = Integer.parseInt(request.getParameter("idCliente"));
                    nombre = request.getParameter("nombre");
                    apellido = request.getParameter("apellido");
                    telefono = request.getParameter("telefono");
                    email = request.getParameter("email");
                    //System.out.println("controlador cliente con ID: " + idCliente);
                    //System.out.println("Nuevos valores - Nombre: " + nombre + ", Apellido: " + apellido + ", Teléfono: " + telefono + ", Email: " + email);
                    boolean actualizado = modelo.actualizarCliente(idCliente, nombre, apellido, telefono, email);

                    if (actualizado) {
                        request.setAttribute("mensajeActualizarCliente", "Cliente actualizado exitosamente.");
                    } else {
                        request.setAttribute("mensajeActualizarCliente", "Error, no se pudo actualizar.");
                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("mensajeActualizarCliente", "Error: ID de cliente no válido.");
                }

               response.sendRedirect("ControladorAdm?accion=mostrarModificarCliente");

                break;

            case "modificarAdministrador":
                idAdministrador = Integer.parseInt(request.getParameter("idAdministrador"));
                Administrador administrador = modelo.obtenerAdministradorPorId(idAdministrador);  // Obtener reservas usando persona_id
                request.setAttribute("administrador", administrador);  // Establecer las reservas en el request
               response.sendRedirect("ControladorAdm?accion=mostrarModificarAdministrador");

                break;

            case "modificarCoordinador":
                idCoordinador = Integer.parseInt(request.getParameter("idCoordinador"));

                Coordinador coordinador = modelo.obtenerCoordinadorPorId(idCoordinador);  // Obtener coordinador usando persona_id

                request.setAttribute("coordinador", coordinador);  // Establecer los datos del coordinador en el request
               response.sendRedirect("ControladorAdm?accion=mostrarModificarCoordinador");

                break;

            case "modificarPlato":
                idPlato = Integer.parseInt(request.getParameter("idPlato"));
                Plato plato = modelo.obtenerPlatoPorId(idPlato);  // Obtener reservas usando persona_id
                request.setAttribute("plato", plato);  // Establecer las reservas en el request
               response.sendRedirect("ControladorAdm?accion=mostrarModificarPlato");

                break;

            case "actualizarAdm":

                try {
                    idAdministrador = Integer.parseInt(request.getParameter("idAdministrador"));
                    usuario = request.getParameter("usuario");

                    System.out.println(idAdministrador);
                    boolean actualizado = modelo.actualizarAdministrador(idAdministrador, usuario);

                    if (actualizado) {
                        request.setAttribute("mensajeActualizarAdministrador", "Administrador actualizado exitosamente.");
                    } else {
                        request.setAttribute("mensajeActualizarAdministrador", "Error, no se pudo actualizar.");
                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("mensajeActualizarAdministrador", "Error: ID de cliente no válido.");
                }

               response.sendRedirect("ControladorAdm?accion=mostrarModificarAdministrador");

                break;

            case "actualizarCoordinador":
                try {
                    idCoordinador = Integer.parseInt(request.getParameter("idCoordinador"));
                    usuario = request.getParameter("usuario");

                    boolean actualizado = modelo.actualizarCoordinador(idCoordinador, usuario);

                    if (actualizado) {
                        request.setAttribute("mensajeActualizarCoordinador", "Coordinador actualizado exitosamente.");
                    } else {
                        request.setAttribute("mensajeActualizarCoordinador", "Error, no se pudo actualizar.");
                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("mensajeActualizarCoordinador", "Error: ID de Coordinador no válido.");
                }

               response.sendRedirect("ControladorAdm?accion=mostrarModificarCoordinador");

                break;

            case "actualizarPlato":

                try {
                    idPlato = Integer.parseInt(request.getParameter("idPlato"));
                    nombre = request.getParameter("nombre");

                    boolean actualizado = modelo.actualizarPlato(idPlato, nombre);

                    if (actualizado) {
                        request.setAttribute("mensajeActualizarPlato", "Plato actualizado exitosamente.");
                    } else {
                        request.setAttribute("mensajeActualizarPlato", "Error, no se pudo actualizar.");
                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("mensajeActualizarPlato", "Error: ID de Plato no válido.");
                }

               response.sendRedirect("ControladorAdm?accion=mostrarModificarPlato");

                break;
            default:
                // Si la acción no es reconocida, redirige a una página de error
                request.setAttribute("mensajeError", "Acción no válida");
                request.getRequestDispatcher("vistaError.jsp").forward(request, response);
                break;
        }

    }
}
