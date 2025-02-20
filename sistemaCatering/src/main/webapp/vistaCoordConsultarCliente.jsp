<%-- 
    Document   : vistaCoordConsultarCliente
    Created on : 17 feb 2025, 14:24:01
    Author     : Usuario
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Consultar Cliente</title>
        <link rel="stylesheet" type="text/css" href="css/estiloUnificado.css">

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
                <h2>Consultar Cliente</h2>

                <!-- Mostrar mensaje de confirmación o error -->
                <c:if test="${not empty mensajeBajaCliente}">
                    <div class="mensaje">${mensajeBajaCliente}</div>
                </c:if>

                <!-- Verificación de si la lista de clientes no está vacía -->
                <c:if test="${not empty clientes}">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nombre</th>
                                <th>Apellido</th>
                                <th>Teléfono</th>
                                <th>Email</th>
                                <th>Consultar</th> <!-- Cambiado de "Eliminar" a "Consultar" -->
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
                                        <form action="ControladorCoordinador" method="POST" style="display:inline;">
                                            <input type="hidden" name="action" value="consultarCliente">
                                            <input type="hidden" name="persona_id" value="${cliente.persona_id}">
                                            <input type="submit" value="Consultar" class="btn-consultar">
                                        </form>

                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <!-- Mensaje cuando no hay clientes -->
                <c:if test="${empty clientes}">
                    <p>No se encontraron clientes disponibles.</p>
                </c:if>


            </div>
        </div>

    </body>
</html>



