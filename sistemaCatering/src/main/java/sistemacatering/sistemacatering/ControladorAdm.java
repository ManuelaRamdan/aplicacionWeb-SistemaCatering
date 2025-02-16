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
                    System.out.println("Acción mostrarAlta ejecutada");

                    List<Plato> platos = modelo.obtenerPlatosBd();

                    System.out.println("Platos obtenidos:");
                    for (Plato p : platos) {
                        System.out.println(p.getId() + " - " + p.getNombre());
                    }

                    request.setAttribute("platosEntrada", platos);
                    request.setAttribute("platosPrincipal", platos);
                    request.getRequestDispatcher("vistaAdmAlta.jsp").forward(request, response);
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
        }

    }
}
