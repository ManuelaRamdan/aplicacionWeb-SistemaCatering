<%-- 
    Document   : vistaAdministrador
    Created on : 29 dic 2024, 17:42:04
    Author     : MANUELA
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Menú Administrador</title>
        <link rel="stylesheet" type="text/css" href="css/estiloAdm.css">
    </head>
    <body>

        <div class="menu-lateral">
            <ul>
                <li>
                    <a href="ControladorAdm?accion=mostrarAlta">Alta</a>
                    <!--<ul class="submenu">
                        <li>Alta Servicio</li>
                        <li>Alta Coordinador</li>
                        <li>Alta Cliente</li>
                        <li>Alta Administrador</li>
                    </ul>-->
                </li>
                <li>
                    <a href="vistaAdmBaja.jsp">Baja</a>
                    <!--
                    <ul class="submenu">
                        <li>Baja Servicio</li>
                        <li>Baja Coordinador</li>
                        <li>Baja Cliente</li>
                    </ul>-->
                </li>
                <li>
                    <a href="vistaAdmModificacion.jsp">Modificación</a>
                    <!--<ul class="submenu">
                        <li>Modificacion Servicio</li>
                        <li>Modificacion Coordinador</li>
                        <li>Modificacion Cliente</li>
                    </ul>-->
                </li>
                <li>
                    <a href="vistaAdmMostrar.jsp">Mostrar</a>
                </li>
            </ul>
        </div>

        <div class="contenido-central">
            <h1>Bienvenido</h1>
        </div>

    </body>
</html>


