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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ControladorAdm", urlPatterns = {"/ControladorAdm"})
public class ControladorAdm extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String idAdministrador = (String) session.getAttribute("userId");

        if (idAdministrador != null) {
            Modelo modelo = new Modelo("localhost", "catering"); // Conexión con la BD
            String nombreAdmin = modelo.obtenerNombreAdministrador(idAdministrador);
            request.setAttribute("nombreAdmin", nombreAdmin);
        } else {
            request.setAttribute("nombreAdmin", "Administrador Desconocido");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("vistaAdministradorMenu.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
        }else if ("registrarPlato".equals(action)) {
            String nombrePlato = request.getParameter("nombrePlato");

            boolean registrado = modelo.registrarPlato(nombrePlato);

            if (registrado) {
                request.setAttribute("mensajePlato", "Plato registrado correctamente.");
            } else {
                request.setAttribute("mensajePlato", "Error al registrar el plato.");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaAdmAlta.jsp");
            dispatcher.forward(request, response);
        }

    }

}
