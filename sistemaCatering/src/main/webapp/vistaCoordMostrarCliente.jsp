<%-- 
    Document   : vistaCoordMoctrarCliente
    Created on : 17 feb 2025, 13:07:51
    Author     : Usuario
--%>


<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Clientes y Reservas</title>
        <link rel="stylesheet" type="text/css" href="css/estiloUnificado.css">

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
            <h1>Clientes y Reservas</h1>

            <c:if test="${empty clientes}">
                <p>No hay clientes disponibles.</p>
            </c:if>

            <c:if test="${not empty clientes}">
                <table border="1">
                    <thead>
                        <tr>
                            <th>Cliente ID</th>
                            <th>Nombre</th>
                            <th>Apellido</th>
                            <th>Reservas</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="cliente" items="${clientes}">
                            <tr>
                                <td>${cliente.id}</td>
                                <td>${cliente.nombre}</td>
                                <td>${cliente.apellido}</td>
                                <td>
                                    <c:if test="${empty cliente.reservas}">
                                        <p>No tiene reservas</p>
                                    </c:if>
                                    <c:if test="${not empty cliente.reservas}">
                                        <table border="1">
                                            <thead>
                                                <tr>
                                                    <th>Código Reserva</th>
                                                    <th>Fecha Inicio</th>
                                                    <th>Fecha Fin</th>
                                                    <th>Tipo Servicio</th>
                                                    <th>Preferencias</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="reserva" items="${cliente.reservas}">
                                                    <tr>
                                                        <td>${reserva.codReserva}</td>
                                                        <td><fmt:formatDate value="${reserva.fechaInicioEvento}" pattern="yyyy-MM-dd HH:mm" /></td>
                                                        <td><fmt:formatDate value="${reserva.fechaFinEvento}" pattern="yyyy-MM-dd HH:mm" /></td>
                                                        <td>${reserva.tipoServicio}</td>
                                                        <td>${reserva.preferenciaCliente}</td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>

    </body>
</html>
