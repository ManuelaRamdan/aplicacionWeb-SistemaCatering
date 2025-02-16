<%-- 
    Document   : vistaAdmAlta
    Created on : 13 feb 2025, 20:16:20
    Author     : MANUELA
--%>

<%@page import="sistemacatering.sistemacatering.Plato"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Alta de Datos</title>
        <link rel="stylesheet" type="text/css" href="css/estiloAdm.css">
        <link rel="stylesheet" type="text/css" href="css/estiloAlta.css">
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




            <!-- Formulario Alta Coordinador -->
            <div class="formulario-contenedor">
                <form action="ControladorAdm" method="post">
                    <h2>Registrar Coordinador</h2>
                    <input type="hidden" name="action" value="registrarCoordinador">

                    <label for="usuario">Usuario:</label>
                    <input type="text" id="usuario" name="usuario" required>

                    <label for="password">Contraseña:</label>
                    <input type="password" id="password" name="password" required>

                    <input type="submit" value="Registrar Coordinador">


                    <% String mensaje = (String) request.getAttribute("mensajeCoordinador"); %> <!--! se le manda al controlador los datos-->
                    <% if (mensaje != null) {%> ><!-- verifica que no sea nulo -->
                    <p><%= mensaje%></p><--<!-- si es nulo muestra el mensaje -->
                    <% }%>
                </form>
                <!--! se envia al controlador adm el usuario y contraseña con el campo action = registarCoordinador-->
            </div>

            <hr>

            <!-- Formulario Alta Cliente -->
            <div class="formulario-contenedor">
                <h2>Alta de Cliente</h2>
                <form action="ControladorAdm" method="POST">
                    <input type="hidden" name="action" value="registrarCliente"> <!-- Acción que se envía al controlador -->

                    <label for="usuario">Usuario:</label>
                    <input type="text" id="usuario" name="usuario" required>

                    <label for="password">Contraseña:</label>
                    <input type="password" id="password" name="password" required>

                    <label for="nombre">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" required>

                    <label for="apellido">Apellido:</label>
                    <input type="text" id="apellido" name="apellido" required>

                    <label for="telefono">Teléfono:</label>
                    <input type="number" id="telefono" name="telefono" required>

                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" required>

                    <input type="submit" value="Registrar Cliente">


                    <% String mensajeCliente = (String) request.getAttribute("mensajeCliente"); %>
                    <% if (mensajeCliente != null) {%>
                    <p><%= mensajeCliente%></p>
                    <% } %>
                </form>
            </div>
            <hr>

            <!-- Formulario Alta Administrador -->
            <div class="formulario-contenedor">
                <form action="ControladorAdm" method="POST">
                    <h2>Registrar Administrador</h2>
                    <input type="hidden" name="action" value="registrarAdministrador">

                    <label for="usuario">Usuario:</label>
                    <input type="text" id="usuario" name="usuario" required>

                    <label for="password">Contraseña:</label>
                    <input type="password" id="password" name="password" required>

                    <input type="submit" value="Registrar Administrador">


                    <% String mensajeAdm = (String) request.getAttribute("mensajeAdm"); %> <!--! se le manda al controlador los datos-->
                    <% if (mensajeAdm != null) {%> ><!-- verifica que no sea nulo -->
                    <p><%= mensajeAdm%></p><--<!-- si es nulo muestra el mensaje -->
                    <% }%>
                </form>
            </div>



            <hr>

            <!-- Formulario Alta de Platos -->
            <div class="formulario-contenedor">
                <h2>Alta de Plato</h2>
                <form class="formulario" action="ControladorAdm" method="POST">
                    <input type="hidden" name="action" value="registrarPlato"> <!-- Acción que se envía al controlador -->
                    <label>Nombre del Plato:</label>
                    <input type="text" name="nombrePlato" required>

                    <input type="submit" value="Registrar Plato">
                </form>

                <% String mensajePlato = (String) request.getAttribute("mensajePlato"); %>
                <% if (mensajePlato != null) {%>
                <p><%= mensajePlato%></p>
                <% }%>
            </div>

            <hr>

            <!-- Formulario Alta de Menú -->
            <div class="formulario-contenedor">
                <h2>Alta de Menú</h2>
                <form action="ControladorAdm" method="POST">
                    <input type="hidden" name="action" value="registrarMenu">

                    <label>Nombre del Menú:</label>
                    <input type="text" name="nombreMenu" required>

                    <p>Selecciona los Platos de la entrada:</p>
                    <%
                        List<Plato> platosEntrada = (List<Plato>) request.getAttribute("platosEntrada");
                        if (platosEntrada == null || platosEntrada.isEmpty()) {
                    %> <!-- se oltiene la lista de platos de la entrada de la solisitud request.getAttribute("platosEntrada")  -->
                    <p>No hay platos disponibles.</p>
                   <!-- si no hay platos se muestra -->
                    <% } else {
                        for (Plato plato : platosEntrada) {
                    %>
                    <!-- Pero si hay platos se muestran  -->
                    <label>
                        <input type="checkbox" name="platoEntrada[]" value="<%= plato.getId()%>"> <%= plato.getNombre()%>
                    </label><br>
                    <%  }
            } %>

                    <p>Selecciona los Platos Principales:</p>
                    <%
                        List<Plato> platosPrincipal = (List<Plato>) request.getAttribute("platosPrincipal");
                        if (platosPrincipal == null || platosPrincipal.isEmpty()) {
                    %>
                    <p>No hay platos disponibles.</p>
                    <% } else {
                        for (Plato plato : platosPrincipal) {
                    %>
                    <label>
                        <input type="checkbox" name="platoPrincipal[]" value="<%= plato.getId()%>"> <%= plato.getNombre()%>
                    </label><br>
                    <%  }
            }%>

                    <input type="submit" value="Registrar Menú">
                </form>
            </div>
        

        <!-- Formulario Alta Servicio -->
        <div class="formulario-contenedor">
            <h2>Alta de Servicio</h2>
            <form class="formulario" action="procesarAltaServicio.jsp" method="POST">
                <label>Nombre del Servicio:</label>
                <input type="text" name="nombreServicio" required>

                <label>Seleccionar Menú:</label>
                <select name="menu_id">
                    <option value="">Seleccione un Menú</option>
                    <option value="1">Menú Ejecutivo</option>
                    <option value="2">Menú Infantil</option>
                    <option value="3">Menú Gourmet</option>
                </select>

                <input type="submit" value="Registrar Servicio">
            </form>
        </div>

    </div>

</body>
</html>
