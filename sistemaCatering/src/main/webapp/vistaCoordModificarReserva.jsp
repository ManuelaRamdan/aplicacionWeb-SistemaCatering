<%-- 
    Document   : vistaCoordMofidicar
    Created on : 19 feb 2025, 12:00:12
    Author     : Usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Modificar Cliente y Reserva</title>
        <link rel="stylesheet" type="text/css" href="css/estiloAdm.css">
        <link rel="stylesheet" type="text/css" href="css/estiloBaja.css">
    </head>
    <body>
        <!-- Menú Lateral -->
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

        <!-- Contenido Central -->
        <div class="contenido-central">
            <h1>Modificar Cliente y Reserva</h1>

            <!-- Pestañas -->
            <div class="tabs">
                <a href="ControladorCoordinador?accion=mostrarModificar&tab=cliente" class="tab">Modificar Cliente</a>
                <a href="ControladorCoordinador?accion=mostrarModificar&tab=reserva" class="tab">Modificar Reserva</a>
            </div>




            <!-- Contenido de las Pestañas -->
            <div class="tab-content" id="cliente">

                <!-- Contenido de las Pestañas -->
                <c:choose>
                    <c:when test="${param.tab == 'cliente' or empty param.tab}">
                        <!-- Contenido de la pestaña Modificar Cliente -->
                        <div class="tab-content">
                            <h2>Modificar Cliente</h2>

                            <c:if test="${not empty mensajeActualizarCliente}">
                                <div class="mensaje">${mensajeActualizarCliente}</div>
                            </c:if>

                            <c:if test="${not empty clientes}">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Nombre</th>
                                            <th>Apellido</th>
                                            <th>Teléfono</th>
                                            <th>Email</th>
                                            <th>Consultar</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="cliente" items="${clientes}">
                                            <tr>
                                                <td>${cliente.id}</td>
                                                <td>${cliente.nombre}</td>
                                                <td>${cliente.apellido}</td>
                                                <td>${cliente.telReferencia}</td>
                                                <td>${cliente.email}</td>
                                                <td>
                                                    <form action="ControladorCoordinador" method="POST">
                                                        <input type="hidden" name="action" value="modificarCliente">
                                                        <input type="hidden" name="persona_id" value="${cliente.persona_id}">
                                                        <input type="submit" value="Modificar" class="btn-Modificar">
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:if>

                            <c:if test="${not empty cliente}">
                                <form action="ControladorCoordinador" method="post">
                                    <input type="hidden" name="action" value="actualizarCliente">
                                    <input type="hidden" name="idCliente" value="${cliente.id}">
                                    <label>Nombre:</label>
                                    <input type="text" name="nombre" value="${cliente.nombre}">
                                    <label>Apellido:</label>
                                    <input type="text" name="apellido" value="${cliente.apellido}">
                                    <label>Teléfono:</label>
                                    <input type="text" name="telefono" value="${cliente.telReferencia}">
                                    <label>Email:</label>
                                    <input type="email" name="email" value="${cliente.email}">
                                    <button type="submit">Guardar</button>
                                </form>
                            </c:if>
                        </div>
                    </c:when>

                    <c:when test="${param.tab == 'reserva'}">
                        <!-- Contenido de la pestaña Modificar Reserva -->
                        <div class="tab-content">
                            <h2>Modificar Reserva</h2>
                            <form action="ControladorCoordinador" method="post">
                                <input type="hidden" name="accion" value="buscarReserva">
                                <label>ID Reserva:</label>
                                <input type="text" name="idReserva" required>
                                <button type="submit">Buscar</button>
                            </form>

                            <c:if test="${not empty reserva}">
                                <form action="ControladorCoordinador" method="post">
                                    <input type="hidden" name="accion" value="actualizarReserva">
                                    <input type="hidden" name="idReserva" value="${reserva.codReserva}">
                                    <label>Fecha Inicio:</label>
                                    <input type="datetime-local" name="fechaInicio" value="${reserva.fechaInicioEvento}">
                                    <label>Fecha Fin:</label>
                                    <input type="datetime-local" name="fechaFin" value="${reserva.fechaFinEvento}">
                                    <label>Tipo Servicio:</label>
                                    <input type="text" name="tipoServicio" value="${reserva.tipoServicio}">
                                    <label>Preferencias:</label>
                                    <input type="text" name="preferencias" value="${reserva.preferenciaCliente}">
                                    <button type="submit">Guardar</button>
                                </form>
                            </c:if>
                        </div>
                    </c:when>
                </c:choose>

            </div>
    </body>
</html>
