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
}

