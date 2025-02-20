<%-- 
    Document   : vistaCoordAlta
    Created on : 18 feb 2025, 14:51:34
    Author     : Usuario
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Alta de Reserva</title>
        <link rel="stylesheet" type="text/css" href="css/estiloAdm.css">
        <link rel="stylesheet" type="text/css" href="css/estiloAlta.css">
        <style>
            .formulario-contenedor label {
                display: block;
                margin-top: 10px;
            }
            .fecha-container {
                display: flex;
                gap: 20px;
            }
            .fecha-container label {
                display: inline;
            }
        </style>
    </head>
    <body>
        <div class="menu-lateral">
            <ul>
                <li><a href="ControladorCoordinador?accion=mostrarAlta">Alta</a></li>
                <li><a href="ControladorCoordinador?accion=mostrarBaja">Baja</a></li>
                <li><a href="ControladorCoordinador?accion=mostrarModificarCliente">Modificación</a></li>
                <li><a href="ControladorCoordinador?accion=mostrarReservas">Mostrar Reservas</a></li>
                <li><a href="ControladorCoordinador?accion=mostrarCliente">Mostrar Cliente</a></li>
                <li><a href="ControladorCoordinador?accion=consultarCliente">Consultar Cliente</a></li>
            </ul>
        </div>

        <div class="contenido-central">
            <div class="formulario-contenedor">
                <!-- Formulario para consultar servicios disponibles -->
                <!-- Formulario para consultar servicios disponibles -->
                <h2>Registrar Reserva</h2>
                <form action="ControladorCoordinador" method="POST">
                    <input type="hidden" name="action" value="buscarServiciosDisponibles">

                    <div class="fecha-container">
                        <div>
                            <label for="fechaInicioEvento">Fecha de Inicio:</label>
                            <input type="datetime-local" id="fechaInicioEvento" name="fechaInicioEvento"
                                   value="${not empty fechaInicioEvento ? fechaInicioEvento : ''}" required>
                        </div>
                        <div>
                            <label for="fechaFinEvento">Fecha de Fin:</label>
                            <input type="datetime-local" id="fechaFinEvento" name="fechaFinEvento"
                                   value="${not empty fechaFinEvento ? fechaFinEvento : ''}" required>
                        </div>
                    </div>

                    <button type="submit">Buscar Servicios Disponibles</button>
                </form>

                <!-- Mostrar servicios disponibles 
                <div id="serviciosDisponibles">
                <c:if test="${not empty serviciosDisponibles}">
                    <h3>Servicios Disponibles:</h3>
                    <div class="checkbox-container">
                    <c:forEach var="servicio" items="${serviciosDisponibles}">
                        <div>
                            <input type="checkbox" id="servicio_${servicio.id}" name="serviciosSeleccionados" value="${servicio.id}"
                        <c:if test="${not empty serviciosSeleccionados && serviciosSeleccionados.contains(servicio.id)}">checked</c:if>>
                 <label for="servicio_${servicio.id}">${servicio.nombreServicio}</label>
             </div>
                    </c:forEach>
                </div>
                </c:if>

                <c:if test="${empty serviciosDisponibles}">
                    <p>No hay servicios disponibles en ese rango de fechas.</p>
                </c:if>
            </div>-->

                <!-- Formulario para registrar la reserva -->

                <form action="ControladorCoordinador" method="POST">
                    <input type="hidden" name="action" value="registrarReserva">

                    <!-- Campos ocultos para mantener las fechas seleccionadas -->
                    <input type="hidden" name="fechaInicioEvento" value="${fechaInicioEvento}">
                    <input type="hidden" name="fechaFinEvento" value="${fechaFinEvento}">

                    <!-- Campos para los servicios seleccionados -->
                    <div class="checkbox-container">
                        <c:forEach var="servicio" items="${serviciosDisponibles}">
                            <div>
                                <input type="checkbox" id="servicio_${servicio.id}" name="serviciosSeleccionados" value="${servicio.id}"
                                       <c:if test="${not empty serviciosSeleccionados && serviciosSeleccionados.contains(servicio.id)}">checked</c:if>>
                                <label for="servicio_${servicio.id}">${servicio.nombreServicio}</label>
                            </div>
                        </c:forEach>
                    </div>

                    <label for="codCliente">Código de Cliente:</label>
                    <input type="number" id="codCliente" name="codCliente" required>

                    <label for="restriccionesDieteticas">Restricciones Dietéticas:</label>
                    <input id="restriccionesDieteticas" name="restriccionesDieteticas" required>

                    <label for="preferenciaCliente">Preferencias del Cliente:</label>
                    <input id="preferenciaCliente" name="preferenciaCliente" required>

                    <label for="tipoServicio">Tipo de Servicio:</label>
                    <input type="text" id="tipoServicio" name="tipoServicio" required>

                    <label for="cantidadPersonas">Cantidad de Personas:</label>
                    <input type="number" id="cantidadPersonas" name="cantidadPersonas" required>

                    <label for="precio">Precio:</label>
                    <input type="number" id="precio" name="precio" required>

                    <label for="modoDeReserva">Modo de Reserva:</label>
                    <select id="modoDeReserva" name="modoDeReserva" required>
                        <option value="online">Online</option>
                        <option value="presencial">Presencial</option>
                    </select>

                    <h3>Dirección de Entrega</h3>
                    <label for="calle">Calle:</label>
                    <input type="text" id="calle" name="calle" required>

                    <label for="altura">Altura:</label>
                    <input type="number" id="altura" name="altura" required>

                    <label for="barrio">Barrio:</label>
                    <input type="text" id="barrio" name="barrio" required>

                    <input type="submit" value="Registrar Reserva">
                </form>

                <c:if test="${not empty mensajeReserva}">
                    <p>${mensajeReserva}</p>
                </c:if>

            </div>

            <hr>

            <!-- Formulario Alta Cliente -->
            <div class="formulario-contenedor">
                <h2>Alta de Cliente</h2>
                <form action="ControladorCoordinador" method="POST">
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

                    <!-- Usando JSTL para mostrar mensajes -->
                    <c:if test="${not empty mensajeCliente}">
                        <p>${mensajeCliente}</p>
                    </c:if>
                </form>
            </div>
        </div>
    </body>
</html>
