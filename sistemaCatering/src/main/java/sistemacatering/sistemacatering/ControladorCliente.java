/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemacatering.sistemacatering;

import java.io.IOException;
import java.util.ArrayList;
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
@WebServlet(name = "ControladorCliente", urlPatterns = {"/ControladorCliente"})
public class ControladorCliente extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) // obtener datos para la vista
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String idCliente = (String) session.getAttribute("userId");

        if (idCliente != null) {
            Modelo modelo = new Modelo(); // Conexión con la base de datos

            // Obtener la acción que el administrador desea realizar (en caso de que haya una acción específica)
            String accion = request.getParameter("accion");

            // Si no hay acción específica, por defecto se carga la vista de menú
            if (accion == null) {
                accion = "misDatos"; // Acción por defecto
            }

            switch (accion) {
                case "salir":
                    // Redirigir a la vista de menú
                    RequestDispatcher dispatcherMenu = request.getRequestDispatcher("index.jsp");
                    dispatcherMenu.forward(request, response);
                    break;
                case "misDatos":

                    Cliente cliente = modelo.obtenerClientePorId(idCliente);

                    request.setAttribute("cliente", cliente);
                    request.getRequestDispatcher("vistaClienteDatos.jsp").forward(request, response);

                    break;

                case "misReservas":
                    //System.out.println("Entrando a misReservas");

                    ArrayList<Reserva> reservas = modelo.obtenerReservasPorCliente(idCliente);
                    //System.out.println("Reservas obtenidas: " + reservas.size());  // Imprimir el tamaño de la lista de reservas

                    request.setAttribute("reservas", reservas);

 

                    request.getRequestDispatcher("vistaClienteReservas.jsp").forward(request, response);
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
}
