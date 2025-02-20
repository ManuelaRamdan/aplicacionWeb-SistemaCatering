<%-- 
    Document   : vistaCoordMostrarReserva
    Created on : 17 feb 2025, 14:05:13
    Author     : Usuario
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Reservas</title>
        <link rel="stylesheet" type="text/css" href="css/estiloMostrar.css">
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
            <div>
                <h2>Mis Reservas</h2> <!-- Título con estilo -->
                <table border="1">
                    <thead>
                        <tr>
                            <th>Código Reserva</th>
                            <th>Código Cliente</th>
                            <th>Fecha Inicio Evento</th>
                            <th>Fecha Fin Evento</th>
                            <th>Restricciones Dietéticas</th>
                            <th>Preferencia Cliente</th>
                            <th>Tipo Servicio</th>
                            <th>Cantidad Personas</th>
                            <th>Precio</th>
                            <th>Modo de Reserva</th>
                            <th>Dirección de Entrega</th>
                            <th>Entregado</th>
                        </tr>
                    </thead>
                    <c:if test="${empty reservas}">
                        <p>No hay reservas disponibles.</p>
                    </c:if>

                    <c:if test="${not empty reservas}">
                        <tbody>
                            <c:forEach var="reserva" items="${reservas}">
                                <tr>
                                    <td>${reserva.codReserva}</td>
                                    <td>${reserva.codCliente}</td>
                                    <td>
                                        <c:if test="${not empty reserva.fechaInicioEvento}">
                                            <fmt:formatDate value="${reserva.fechaInicioEvento}" pattern="yyyy-MM-dd HH:mm"/>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${not empty reserva.fechaFinEvento}">
                                            <fmt:formatDate value="${reserva.fechaFinEvento}" pattern="yyyy-MM-dd HH:mm"/>
                                        </c:if>
                                    </td>
                                    <td>${reserva.restriccionesDieteticas}</td>
                                    <td>${reserva.preferenciaCliente}</td>
                                    <td>${reserva.tipoServicio}</td>
                                    <td>${reserva.cantidadPersonas}</td>
                                    <td>${reserva.precio}</td>
                                    <td>${reserva.modoDeReserva}</td>
                                    <td>${reserva.direccionDeEntrega}</td>
                                    <td>${reserva.estaEntregado ? 'Sí' : 'No'}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </c:if>

                </table>
            </div>
        </div>

    </body>
</html>
