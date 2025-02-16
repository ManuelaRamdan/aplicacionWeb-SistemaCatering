<%-- 
    Document   : vistaAdmBaja
    Created on : 13 feb 2025, 20:29:22
    Author     : MANUELA
--%>

<%@page import="sistemacatering.sistemacatering.Administrador"%>
<%@page import="sistemacatering.sistemacatering.Coordinador"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Baja de Datos</title>
        <link rel="stylesheet" type="text/css" href="css/estiloAdm.css">
        <link rel="stylesheet" type="text/css" href="css/estiloBaja.css">
    </head>
    <body>

        <div class="menu-lateral">
            <ul>
                <li><a href="ControladorAdm?accion=mostrarAlta">Alta</a></li>
                <li><a href="vistaAdmBaja.jsp">Baja</a></li>
                <li><a href="vistaAdmModificacion.jsp">Modificación</a></li>
                <li><a href="vistaAdmMostrar.jsp">Mostrar</a></li>
            </ul>
        </div>

        <div class="contenido-central">
            <!-- Formulario Baja Coordinador -->
            <div class="formulario-contenedor">
                <h2>Eliminar Coordinador</h2>

                <p>Seleccione el ID del Coordinador que desea eliminar:</p>

                <table border="1">
                    <tr>
                        <th>ID</th>
                        <th>Usuario</th>
                    </tr>
                    <%
                        List<Coordinador> coordinadores = (List<Coordinador>) request.getAttribute("coordinadores");
                        if (coordinadores != null && !coordinadores.isEmpty()) {
                            for (Coordinador coordinador : coordinadores) {
                    %>
                    <tr>
                        <td><%= coordinador.getCodCoordinador()%></td>
                        <td><%= coordinador.getUsuario()%></td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="2">No hay coordinadores disponibles.</td>
                    </tr>
                    <% }%>
                </table>

                <form action="ControladorAdm" method="post">
                    <input type="hidden" name="action" value="eliminarCoordinador">

                    <label for="idCoordinador">ID del Coordinador:</label>
                    <input type="number" id="idCoordinador" name="idCoordinador" required>

                    <input type="submit" value="Eliminar Coordinador">
                </form>
            </div>

            <hr>
            <!-- Formulario Baja Cliente -->
            <div class="formulario-contenedor">
                <h2>Eliminar Cliente</h2>
                <form action="ControladorAdm" method="POST">
                    <input type="hidden" name="action" value="eliminarCliente">

                    <label for="usuario">Usuario:</label>
                    <input type="text" id="usuario" name="usuario" required>

                    <input type="submit" value="Eliminar Cliente">
                </form>
            </div>
            <hr>
            <!-- Formulario Baja Administrador -->
            <div class="formulario-contenedor">
                <h2>Eliminar Administrador</h2>

                <p>Seleccione el ID del Administrador que desea eliminar:</p>

                <table border="1">
                    <tr>
                        <th>ID</th>
                        <th>Usuario</th>
                    </tr>
                    <%
                        List<Administrador> administradores = (List<Administrador>) request.getAttribute("administradores");
                        if (administradores != null && !administradores.isEmpty()) {
                            for (Administrador administrador : administradores) {
                    %>
                    <tr>
                        <td><%= administrador.getcodAdministrador()%></td>
                        <td><%= administrador.getUsuario()%></td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="2">No hay Administradores disponibles.</td>
                    </tr>
                    <% }%>
                </table>

                <form action="ControladorAdm" method="post">
                    <input type="hidden" name="action" value="eliminarAdministrador">

                    <label for="idAdministrador">ID del Administrador:</label>
                    <input type="number" id="idAdministrador" name="idAdministrador" required>

                    <input type="submit" value="Eliminar Administrador">
                </form>

            </div>
            <hr>
            <!-- Formulario Baja de Platos -->
            <div class="formulario-contenedor">
                <h2>Eliminar Plato</h2>
                <form class="formulario" action="ControladorAdm" method="POST">
                    <input type="hidden" name="action" value="eliminarPlato">
                    <label>Nombre del Plato:</label>
                    <input type="text" name="nombrePlato" required>
                    <input type="submit" value="Eliminar Plato">
                </form>
            </div>
            <hr>
            <!-- Formulario Baja de Menú -->
            <div class="formulario-contenedor">
                <h2>Eliminar Menú</h2>
                <form action="ControladorAdm" method="POST">
                    <input type="hidden" name="action" value="eliminarMenu">
                    <label>Nombre del Menú:</label>
                    <input type="text" name="nombreMenu" required>
                    <input type="submit" value="Eliminar Menú">
                </form>
            </div>
            <hr>
            <!-- Formulario Baja Servicio -->
            <div class="formulario-contenedor">
                <h2>Eliminar Servicio</h2>
                <form action="ControladorAdm" method="POST">
                    <input type="hidden" name="action" value="eliminarServicio">
                    <label>Nombre del Servicio:</label>
                    <input type="text" name="nombreServicio" required>
                    <input type="submit" value="Eliminar Servicio">
                </form>
            </div>
        </div>
    </body>
</html>


