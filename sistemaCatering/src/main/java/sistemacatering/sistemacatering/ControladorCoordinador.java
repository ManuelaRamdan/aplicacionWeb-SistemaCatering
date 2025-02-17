/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemacatering.sistemacatering;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Usuario
 */
@WebServlet(name = "ControladorCoordinador", urlPatterns = {"/ControladorCoordinador"})
public class ControladorCoordinador extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) // obtener datos para la vista
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String idCoordinador = (String) session.getAttribute("userId");

        if (idCoordinador != null) {
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
                    RequestDispatcher dispatcherMenu = request.getRequestDispatcher("vistaCoordinadorMenu.jsp");
                    dispatcherMenu.forward(request, response);
                    break;

                case "mostrarAlta":

                    break;

                case "mostrarBaja":

                    break;

                case "mostrarModificar":

                    break;

                case "mostrarReservas":
                    List<Reserva> reservas = modelo.obtenerReservaBd();
                    request.setAttribute("reservas", reservas);
                    request.getRequestDispatcher("vistaCoordMostrarReserva.jsp").forward(request, response);

                    break;
                case "mostrarCliente":
                    List<Cliente> clientes = modelo.obtenerClientesConReservas(); // Obtener clientes con reservas
                    request.setAttribute("clientes", clientes);  // Pasar la lista de clientes al JSP
                    request.getRequestDispatcher("vistaCoordMostrarCliente.jsp").forward(request, response);

                    break;
                case "consultarCliente":

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

    }
}
