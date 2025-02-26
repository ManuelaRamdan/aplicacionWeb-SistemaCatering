<%-- 
    Document   : vistaAdministrador
    Created on : 29 dic 2024, 17:42:04
    Author     : MANUELA
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Menú Administrador</title>
        <link rel="stylesheet" type="text/css" href="css/estiloGeneral.css">
    </head>
    <body>
        <div class="container">
            <input type="checkbox" id="menu-toggle" class="menu-checkbox">
            <label for="menu-toggle" class="menu-icon">☰</label>

            <div class="menu-lateral">
                <ul>
                    <li><a href="ControladorAdm?accion=mostrarAlta">Alta</a></li>
                    <li><a href="ControladorAdm?accion=mostrarBaja">Baja</a></li>
                    <li><a href="ControladorAdm?accion=mostrarModificarAdministrador">Modificación</a></li>
                    <li><a href="ControladorAdm?accion=mostrar">Mostrar</a></li> 
                    <li><a href="ControladorAdm?accion=salir">Salir</a></li>


                </ul>
            </div>

            <div class="contenido-central">
                <h1>Bienvenido, ${usuario} </h1>
            </div>
        </div>

    </body>
</html>


