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
                <li><a href="ControladorAdm?accion=mostrarAlta">Alta</a></li>
                <li><a href="ControladorAdm?accion=mostrarBaja">Baja</a></li>
                <li><a href="ControladorAdm?accion=mostrarModificar">Modificación</a></li>
                <li><a href="ControladorAdm?accion=mostrar">Mostrar</a></li>
            </ul>
        </div>

        <div class="contenido-central">
            <h1>Bienvenido</h1>
        </div>

    </body>
</html>


