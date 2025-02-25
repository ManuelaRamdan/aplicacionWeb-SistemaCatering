/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemacatering.sistemacatering;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author MANUELA
 */
@WebServlet(name = "Controlador", urlPatterns = {"/Controlador"})
public class Controlador extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String password = request.getParameter("contrasenia");
        //String ip = request.getParameter("dirIP");
        //String bd = request.getParameter("nomBD");

        Modelo modelo = new Modelo();
        String rol = modelo.verificarUsuario(usuario, password);

        if (rol != null) {
            String tipoUsuario = modelo.buscarTipoUsuario(rol);
            
            // Crear sesión y guardar ID del usuario autenticado
            HttpSession session = request.getSession();
            session.setAttribute("userId", rol); // Guardar ID del usuario

            // Redirigir según el tipo de usuario
            switch (tipoUsuario) {
                case "Administrador":
                    response.sendRedirect("ControladorAdm");
                    break;
                case "Cliente":
                    response.sendRedirect("ControladorCliente");
                    break;
                case "Coordinador":
                    response.sendRedirect("ControladorCoordinador");
                    break;
                default:
                    request.setAttribute("mensajeError", "Usuario o contraseña incorrectos");
                    request.getRequestDispatcher("vistaError.jsp").forward(request, response);
                    break;
            }
        } else {
            request.setAttribute("mensajeError", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("vistaError.jsp").forward(request, response);
        }
    }
}

