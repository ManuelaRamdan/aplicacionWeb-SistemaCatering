<%-- 
    Document   : index
    Created on : 28 dic 2024, 20:53:08
    Author     : MANUELA
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inicio de sesion</title>
        <link rel="stylesheet" type="text/css" href="css/estiloUnificado.css">
    </head>
    <body>
        <h1>Servicio de Catering</h1>
        <div class="contenedor-centrado">
            <form method="post" action="Controlador">
                <h1>Inicio de sesión</h1>
                <p>
                    <label for="usuario">Usuario:</label>
                    <input type="text" id="usuario" name="usuario" required>
                </p>
                <p>
                    <label for="contrasenia">Contraseña:</label>
                    <input type="password" id="contrasenia" name="contrasenia" required>
                </p>
                <input type="hidden" name="dirIP" value="localhost">
                <input type="hidden" name="nomBD" value="catering">
                <input type="submit" value="Iniciar sesión">
            </form>
        </div>

    </body>
</html>
