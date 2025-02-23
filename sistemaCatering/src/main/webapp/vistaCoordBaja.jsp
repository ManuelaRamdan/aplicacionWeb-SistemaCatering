<%-- 
    Document   : vistaCoordBaja
    Created on : 18 feb 2025, 11:23:53
    Author     : Usuario
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Baja de Datos</title>
        <link rel="stylesheet" type="text/css" href="css/estiloGeneral.css">
        <link rel="stylesheet" type="text/css" href="css/estiloBaja.css">
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
                <li><a href="ControladorCoordinador?accion=salir">Salir</a></li>

            </ul>
        </div>

        <div class="contenido-central">
            <div class="formulario-contenedor">
                <h2>Eliminar Cliente</h2>

                <!-- Mensaje de confirmación o error -->
                <c:if test="${not empty mensajeBajaCliente}">
                    <div class="mensaje">${mensajeBajaCliente}</div>
                </c:if>

                <!-- Tabla de clientes -->
                <c:if test="${not empty clientes}">
                    <table border="1">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Usuario</th>
                                <th>Password</th>
                                <th>Nombre</th>
                                <th>Apellido</th>
                                <th>Teléfono</th>
                                <th>Email</th>
                                <th>Eliminar</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cliente" items="${clientes}">
                                <tr>
                                    <td>${cliente.id}</td>
                                    <td>${cliente.usuario}</td>
                                    <td>${cliente.password}</td>
                                    <td>${cliente.nombre}</td>
                                    <td>${cliente.apellido}</td>
                                    <td>${cliente.telReferencia}</td>
                                    <td>${cliente.email}</td>
                                    <td>
                                        <form action="ControladorCoordinador" method="POST" style="display:inline;"
                                              onsubmit="return confirm('¿Estás seguro de que deseas eliminar a ${cliente.nombre} ${cliente.apellido}?');">
                                            <input type="hidden" name="action" value="eliminarCliente">
                                            <input type="hidden" name="idCliente" value="${cliente.id}">
                                            <input type="hidden" name="persona_id" value="${cliente.persona_id}">
                                            <input type="submit" value="Eliminar" class="btn-eliminar">
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <c:if test="${empty clientes}">
                    <p>No se encontraron clientes disponibles.</p>
                </c:if>
            </div>

            <!-- Tabla de Reservas -->
            <div class="formulario-contenedor">
                <h2>Reservas</h2>

                <c:if test="${empty reservas}">
                    <p>No hay reservas disponibles.</p>
                </c:if>

                <c:if test="${not empty reservas}">
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
                                <th>Eliminar</th>
                            </tr>
                        </thead>
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
                                    <td>
                                        <!-- Formulario para eliminar reserva con confirmación -->
                                        <form action="ControladorCoordinador" method="POST" style="display:inline;"
                                              onsubmit="return confirm('¿Estás seguro de que deseas eliminar la reserva ${reserva.codReserva}?');">
                                            <input type="hidden" name="action" value="eliminarReserva">
                                            <input type="hidden" name="codReserva" value="${reserva.codReserva}">
                                            <input type="submit" value="Eliminar" class="btn-eliminar">
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>
            </div>

        </div>
    </body>
</html>



