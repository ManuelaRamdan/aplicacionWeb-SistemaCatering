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

/**
 *
 * @author MANUELA
 */
@WebServlet(name = "Controlador", urlPatterns = {"/Controlador"})
public class Controlador extends HttpServlet {

    HttpServletRequest request;
    HttpServletResponse response;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String password = request.getParameter("contrasenia");
        String ip = request.getParameter("dirIP");
        String bd = request.getParameter("nomBD");

        Modelo modelo = new Modelo(ip, bd);
        String rol = modelo.verificarUsuario(usuario, password);

        if (rol != null) {
            String tipoUsuario = modelo.buscarTipoUsuario(rol);
            switch (tipoUsuario) {
                case "Administrador":
                    RequestDispatcher adminView = request.getRequestDispatcher("vistaAdministrador.jsp");
                    adminView.forward(request, response);
                    break;
                case "Cliente":
                    RequestDispatcher clienteView = request.getRequestDispatcher("vistaCliente.jsp");
                    clienteView.forward(request, response);
                    break;
                case "Coordinador":
                    RequestDispatcher coordView = request.getRequestDispatcher("vistaCoordinador.jsp");
                    coordView.forward(request, response);
                    break;
                case "Otro":
                    request.setAttribute("mensajeError", "Usuario o contraseña incorrectos");
                    RequestDispatcher errorView = request.getRequestDispatcher("vistaError.jsp");
                    errorView.forward(request, response);
                    break;
            }
        } else {
            request.setAttribute("mensajeError", "Usuario o contraseña incorrectos");
            RequestDispatcher errorView = request.getRequestDispatcher("vistaError.jsp");
            errorView.forward(request, response);
        }
    }
}
