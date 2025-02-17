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

                case "mostrarModificar":
                    // Obtener el término de búsqueda ingresado
                    String busqueda = request.getParameter("busqueda");

                    // Obtener los resultados para cada tipo de entidad
                    coordinadores = modelo.buscarCoordinador(busqueda);
                    //clientes = modelo.buscarCliente(busqueda);
                    //administradores = modelo.buscarAdministrador(busqueda);
                    //platos = modelo.buscarPlato(busqueda);

                    // Colocar los resultados en el request para la vista
                    request.setAttribute("coordinadores", coordinadores);
                    //request.setAttribute("clientes", clientes);
                    //request.setAttribute("administradores", administradores);
                    //request.setAttribute("platos", platos);

                    // Redirigir a la vista de modificación
                    request.getRequestDispatcher("vistaAdmModificar.jsp").forward(request, response);
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

        if ("registrarCoordinador".equals(action)) {
            /* se fija si la accion que recivio es la que necesitamos*/
            String usuario = request.getParameter("usuario");
            String password = request.getParameter("password");

            boolean registrado = modelo.registrarCoordinador(usuario, password);

            /* dependiendo si se pudo registrar o no , muestra un mensaje */
            if (registrado) {
                request.setAttribute("mensajeCoordinador", "Coordinador registrado correctamente.");
            } else {
                request.setAttribute("mensajeCoordinador", "Error al registrar el coordinador.");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaAdmAlta.jsp");/*Redirige la solicitud a la página */
            dispatcher.forward(request, response);
        } else if ("registrarAdministrador".equals(action)) {
            String usuario = request.getParameter("usuario");
            String password = request.getParameter("password");

            boolean registrado = modelo.registrarAdministrador(usuario, password);

            /* dependiendo si se pudo registrar o no , muestra un mensaje */
            if (registrado) {
                request.setAttribute("mensajeAdm", "Administrador registrado correctamente.");
            } else {
                request.setAttribute("mensajeAdm", "Error al registrar el Administrador.");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaAdmAlta.jsp");/*Redirige la solicitud a la página */
            dispatcher.forward(request, response);
        } else if ("registrarCliente".equals(action)) {
            String usuario = request.getParameter("usuario");
            String password = request.getParameter("password");
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String telefono = request.getParameter("telefono");
            String email = request.getParameter("email");

            boolean registrado = modelo.registrarCliente(usuario, password, nombre, apellido, telefono, email);
            if (registrado) {
                request.setAttribute("mensajeCliente", "Cliente registrado correctamente.");
            } else {
                request.setAttribute("mensajeCliente", "Error al registrar el cliente.");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaAdmAlta.jsp");
            dispatcher.forward(request, response);
        } else if ("registrarPlato".equals(action)) {
            String nombrePlato = request.getParameter("nombrePlato");

            boolean registrado = modelo.registrarPlato(nombrePlato);

            if (registrado) {
                request.setAttribute("mensajePlato", "Plato registrado correctamente.");
            } else {
                request.setAttribute("mensajePlato", "Error al registrar el plato.");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaAdmAlta.jsp");
            dispatcher.forward(request, response);
        } else if ("registrarMenu".equals(action)) {
            String nombreMenu = request.getParameter("nombreMenu");

            // Obtener IDs de platos de entrada seleccionados
            String[] platosEntradaIds = request.getParameterValues("platoEntrada[]");
            List<Integer> platosEntradaSeleccionados = modelo.obtenerIdsSeleccionados(platosEntradaIds);

            // Obtener IDs de platos principales seleccionados
            String[] platosPrincipalIds = request.getParameterValues("platoPrincipal[]");
            List<Integer> platosPrincipalSeleccionados = modelo.obtenerIdsSeleccionados(platosPrincipalIds);

            boolean registrado = modelo.registrarMenu(nombreMenu, platosEntradaSeleccionados, platosPrincipalSeleccionados);

            if (registrado) {
                request.setAttribute("mensajeMenu", "Menu registrado correctamente.");
            } else {
                request.setAttribute("mensajeMenu", "Error al registrar el Menu.");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaAdmAlta.jsp");
            dispatcher.forward(request, response);
        } else if ("registrarServicio".equals(action)) {  // Nueva acción para registrar servicio
            String nombreServicio = request.getParameter("nombreServicio");

            // Obtener IDs de menús seleccionados
            String[] menuIds = request.getParameterValues("menus[]");
            List<Integer> menusSeleccionados = modelo.obtenerIdsSeleccionados(menuIds);

            boolean registrado = modelo.registrarServicio(nombreServicio, menusSeleccionados);

            if (registrado) {
                request.setAttribute("mensajeServicio", "Servicio registrado correctamente.");
            } else {
                request.setAttribute("mensajeServicio", "Error al registrar el Servicio.");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaAdmAlta.jsp");
            dispatcher.forward(request, response);
        } else if ("eliminarCoordinador".equals(action)) {
            int idCoordinador = Integer.parseInt(request.getParameter("idCoordinador"));

            boolean eliminado = modelo.eliminarCoordinador(idCoordinador);

            if (eliminado) {
                request.setAttribute("mensajeBajaCoordinador", "Coordinador dado de baja exitosamente.");
            } else {
                request.setAttribute("mensajeBajaCoordinador", "Error, no se puso eliminar");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaAdmBaja.jsp");
            dispatcher.forward(request, response);

        } else if ("eliminarAdministrador".equals(action)) {
            int idAdministrador = Integer.parseInt(request.getParameter("idAdministrador"));

            boolean eliminado = modelo.eliminarAdministrador(idAdministrador);

            if (eliminado) {
                request.setAttribute("mensajeBajaAdministrador", "Administrador dado de baja exitosamente.");
            } else {
                request.setAttribute("mensajeBajaAdministrador", "Error, no se puso eliminar");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaAdmBaja.jsp");
            dispatcher.forward(request, response);

        } else if ("eliminarCliente".equals(action)) {
            int idCliente = Integer.parseInt(request.getParameter("idCliente"));

            boolean eliminado = modelo.eliminarCliente(idCliente);

            if (eliminado) {
                request.setAttribute("mensajeBajaCliente", "Cliente dado de baja exitosamente.");
            } else {
                request.setAttribute("mensajeBajaCliente", "Error, no se puso eliminar");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaAdmBaja.jsp");
            dispatcher.forward(request, response);
        } else if ("eliminarPlato".equals(action)) {
            int idPlato = Integer.parseInt(request.getParameter("idPlato"));
            boolean eliminado = modelo.eliminarPlato(idPlato);

            if (eliminado) {
                request.setAttribute("mensajeBajaPlato", "Plato dado de baja exitosamente.");
            } else {
                request.setAttribute("mensajeBajaPlato", "Error, no se pudo eliminar");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaAdmBaja.jsp");
            dispatcher.forward(request, response);
        } else if ("eliminarMenu".equals(action)) {
            int idMenu = Integer.parseInt(request.getParameter("idMenu"));
            boolean eliminado = modelo.eliminarMenu(idMenu);

            if (eliminado) {
                request.setAttribute("mensajeBajaMenu", "Menú dado de baja exitosamente.");
            } else {
                request.setAttribute("mensajeBajaMenu", "Error, no se pudo eliminar");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaAdmBaja.jsp");
            dispatcher.forward(request, response);
        } else if ("eliminarServicio".equals(action)) {
            int idServicio = Integer.parseInt(request.getParameter("idServicio"));
            boolean eliminado = modelo.eliminarServicio(idServicio);

            if (eliminado) {
                request.setAttribute("mensajeBajaServicio", "Servicio dado de baja exitosamente.");
            } else {
                request.setAttribute("mensajeBajaServicio", "Error, no se pudo eliminar");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaAdmBaja.jsp");
            dispatcher.forward(request, response);
        }

    }
}
