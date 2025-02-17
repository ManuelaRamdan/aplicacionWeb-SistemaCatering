<%-- 
    Document   : vistaClienteDatos
    Created on : 17 feb 2025, 09:41:04
    Author     : Usuario
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Menú Cliente</title>
        <link rel="stylesheet" type="text/css" href="css/estilos.css">
        <link rel="stylesheet" type="text/css" href="css/estiloCliente.css">

    </head>
    <body>
        <div class="menu-lateral">
            <ul>
                <!-- Botones para cambiar entre las secciones -->
                <li><a href="ControladorCliente?accion=misDatos">Mis Datos</a></li>
                <li><a href="ControladorCliente?accion=misReservas">Mis Reservas</a></li>
            </ul>
        </div>

        <div class="contenido-central">
            <h1>Bienvenido, ${cliente.nombre} ${cliente.apellido}</h1>

            <div>
                <h2>Mis Datos</h2>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Apellido</th>
                            <th>Email</th>
                            <th>Teléfono</th>
                        </tr>
                    </thead>
                    <tbody>
                        <!-- Mostrar los datos del cliente que se pasan desde el servlet -->
                        <tr>
                            <td>${cliente.id}</td>
                            <td>${cliente.nombre}</td>
                            <td>${cliente.apellido}</td>
                            <td>${cliente.email}</td>
                            <td>${cliente.telReferencia}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>


