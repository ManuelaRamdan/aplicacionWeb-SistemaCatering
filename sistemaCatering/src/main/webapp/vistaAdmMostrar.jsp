<%-- 
    Document   : vistaAdmMostrar
    Created on : 16 feb 2025, 23:53:19
    Author     : Usuario
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="fnt" uri="http://java.sun.com/jsp/jstl/functions" %>




<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Mostrar Datos</title>
        <link rel="stylesheet" type="text/css" href="css/estiloAdm.css">
        <link rel="stylesheet" type="text/css" href="css/estiloMostrar.css">
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
            <h2>Administradores</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Persona ID</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="admin" items="${administradores}">
                        <tr>
                            <td>${admin.codAdministrador}</td>
                            <td>${admin.usuario}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <h2>Clientes</h2>
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
                    <c:forEach var="cliente" items="${clientes}">
                        <tr>
                            <td>${cliente.id}</td>
                            <td>${cliente.nombre}</td>
                            <td>${cliente.apellido}</td>
                            <td>${cliente.email}</td>
                            <td>${cliente.telReferencia}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <h2>Coordinadores</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Persona ID</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="coordinador" items="${coordinadores}">
                        <tr>
                            <td>${coordinador.codCoordinador}</td>
                            <td>${coordinador.usuario}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <h2>Platos</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="plato" items="${platos}">
                        <tr>
                            <td>${plato.id}</td>
                            <td>${plato.nombre}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <h2>Menús</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Precio</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="menu" items="${menus}">
                        <tr>
                            <td>${menu.id}</td>
                            <td>${menu.nombreMenu}</td>
                            <td>${menu.precio}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <h2>Reservas</h2>
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

            <h2>Servicios</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="servicio" items="${servicios}">
                        <tr>
                            <td>${servicio.id}</td>
                            <td>${servicio.nombreServicio}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

    </body>
</html>

