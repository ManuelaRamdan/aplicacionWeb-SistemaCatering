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
        <link rel="stylesheet" type="text/css" href="css/estiloGeneral.css">
        <link rel="stylesheet" type="text/css" href="css/estiloMostrarReserva.css">

    </head>
    <body>
        <div class="container">
            <!-- Menú Lateral -->
            <input type="checkbox" id="menu-toggle" class="menu-checkbox">
            <label for="menu-toggle" class="menu-icon">☰</label>

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


            <!-- Contenido Central -->
            <div class="contenido-central">
                <h1>Modificar Cliente y Reserva</h1>

                <!-- Pestañas -->
                <div class="tabs">
                    <a href="ControladorCoordinador?accion=mostrarModificarCliente" class="tab">Modificar Cliente</a>
                    <a href="ControladorCoordinador?accion=mostrarModificarReserva" class="tab">Modificar Reserva</a>
                </div>

                <h1>Modificar Reserva</h1>

                <!-- Mostrar mensaje si existe un mensaje de actualización -->
                <c:if test="${not empty mensajeModificarReserva}">
                    <div class="mensaje">${mensajeModificarReserva}</div>
                </c:if>

                <!-- Tabla con los detalles de la reserva -->
                <c:if test="${not empty reservas}">
                    <h2>Detalles de la Reserva</h2>
                    <table border="1">
                        <thead>
                            <tr>
                                <th>Fecha de Inicio</th>
                                <th>Fecha de Fin</th>
                                <th>Código Cliente</th>
                                <th>Restricciones Dietéticas</th>
                                <th>Preferencias del Cliente</th>
                                <th>Tipo de Servicio</th>
                                <th>Cantidad de Personas</th>
                                <th>Modo de Reserva</th>
                                <th>Dirección de Entrega</th>
                                <th>Entregado</th>
                                <th>Servicios</th>
                                <th>Consultar</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="reserva" items="${reservas}">
                                <tr>
                                    <td>${reserva.fechaInicioEvento}</td>
                                    <td>${reserva.fechaFinEvento}</td>
                                    <td>${reserva.codCliente}</td>
                                    <td>${reserva.restriccionesDieteticas}</td>
                                    <td>${reserva.preferenciaCliente}</td>
                                    <td>${reserva.tipoServicio}</td>
                                    <td>${reserva.cantidadPersonas}</td>
                                    <td>${reserva.modoDeReserva}</td>
                                    <td>${reserva.direccionDeEntrega}</td>
                                    <td>${reserva.estaEntregado ? 'Sí' : 'No'}</td>


                                    <td>
                                        <c:if test="${not empty reserva.servicios}">
                                            <table border="1">
                                                <thead>
                                                    <tr>
                                                        <th>Nombre Servicio</th>
                                                        <th>Menú</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="servicio" items="${reserva.servicios}">
                                                        <tr>
                                                            <td>${servicio.nombreServicio}</td>
                                                            <td>
                                                                <c:if test="${not empty servicio.menus}">
                                                                    <table border="1">
                                                                        <thead>
                                                                            <tr>
                                                                                <th>Menú ID</th>
                                                                                <th>Nombre Menú</th>
                                                                                <th>Precio</th>
                                                                                <th>Entradas</th>
                                                                                <th>Platos Principales</th>
                                                                            </tr>
                                                                        </thead>
                                                                        <tbody>
                                                                            <c:forEach var="menu" items="${servicio.menus}">
                                                                                <tr>
                                                                                    <td>${menu.id}</td>
                                                                                    <td>${menu.nombreMenu}</td>
                                                                                    <td>${menu.precio}</td>
                                                                                    <td>
                                                                                        <c:forEach var="plato" items="${menu.platosEntrada}">
                                                                                            ${plato.nombre}<br>
                                                                                        </c:forEach>
                                                                                    </td>
                                                                                    <td>
                                                                                        <c:forEach var="plato" items="${menu.platosPrincipal}">
                                                                                            ${plato.nombre}<br>
                                                                                        </c:forEach>
                                                                                    </td>
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
                                    </td>
                                    <td>
                                        <form action="ControladorCoordinador" method="POST">
                                            <input type="hidden" name="action" value="modificarReserva">
                                            <input type="hidden" name="idReserva" value="${reserva.codReserva}">
                                            <input type="hidden" name="fechaInicio" value="${reserva.fechaInicioEvento}">
                                            <input type="hidden" name="fechaFin" value="${reserva.fechaFinEvento}">
                                            <input type="submit" value="Modificar" class="btn-Modificar">
                                        </form>
                                    </td>
                                </tr>

                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <c:if test="${not empty reserva}">
                    <h2>Editar reserva</h2>
                    <div class="formulario-contenedor">
                        <form action="ControladorCoordinador" method="post">
                            <input type="hidden" name="action" value="actualizarReservaDatos">
                            <input type="hidden" name="idReserva" value="${reserva.codReserva}">

                            <label for="fechaInicioEvento">Fecha de Inicio:</label>
                            <input type="datetime-local" id="fechaInicioEvento" name="fechaInicioEvento"
                                   value="${reserva.fechaInicioEvento != null ? reserva.fechaInicioEvento.toLocalDateTime().toString() : ''}" required>

                            <label for="fechaFinEvento">Fecha de Fin:</label>
                            <input type="datetime-local" id="fechaFinEvento" name="fechaFinEvento"
                                   value="${reserva.fechaFinEvento != null ? reserva.fechaFinEvento.toLocalDateTime().toString() : ''}" required>

                            <label for="codCliente">Código de Cliente:</label>
                            <input type="number" id="codCliente" name="codCliente" value="${reserva.codCliente}" required>

                            <label for="restriccionesDieteticas">Restricciones Dietéticas:</label>
                            <input id="restriccionesDieteticas" name="restriccionesDieteticas" value="${reserva.restriccionesDieteticas}" required>

                            <label for="preferenciaCliente">Preferencias del Cliente:</label>
                            <input id="preferenciaCliente" name="preferenciaCliente" value="${reserva.preferenciaCliente}" required>

                            <label for="tipoServicio">Tipo de Servicio:</label>
                            <input type="text" id="tipoServicio" name="tipoServicio" value="${reserva.tipoServicio}" required>

                            <label for="cantidadPersonas">Cantidad de Personas:</label>
                            <input type="number" id="cantidadPersonas" name="cantidadPersonas" value="${reserva.cantidadPersonas}" required>

                            <label for="precio">Precio:</label>
                            <input type="number" id="precio" name="precio" value="${reserva.precio}" required>

                            <label for="modoDeReserva">Modo de Reserva:</label>
                            <select id="modoDeReserva" name="modoDeReserva" required>
                                <option value="MAIL" ${reserva.modoDeReserva == 'MAIL' ? 'selected' : ''}>Mail</option>
                                <option value="TELEFONO" ${reserva.modoDeReserva == 'TELEFONO' ? 'Telefono' : ''}>Mail</option>

                                <option value="PRESENCIAL" ${reserva.modoDeReserva == 'PRESENCIAL' ? 'selected' : ''}>Presencial</option>
                            </select>

                            <h3>Dirección de Entrega</h3>
                            <label for="calle">Calle:</label>
                            <input type="text" id="calle" name="calle" value="${reserva.direccionDeEntrega.calle}" required>

                            <label for="altura">Altura:</label>
                            <input type="number" id="altura" name="altura" value="${reserva.direccionDeEntrega.altura}" required>

                            <label for="barrio">Barrio:</label>
                            <input type="text" id="barrio" name="barrio" value="${reserva.direccionDeEntrega.barrio}" required>

                            <!-- Nueva opción para indicar si está entregado -->
                            <label for="estaEntregado">Está entregado:</label>
                            <input type="checkbox" id="estaEntregado" name="estaEntregado" ${reserva.estaEntregado ? 'checked' : ''}>

                            <button type="submit">Guardar</button>
                        </form>

                    </div>

                    <!-- Platos de Entrada -->
                    <div class="formulario-contenedor">
                        <h2>Agregar servicio a la reserva </h2>
                        <form action="ControladorCoordinador" method="POST">
                            <input type="hidden" name="action" value="agregarServicioReserva">
                            <input type="hidden" name="idReserva" value="${reserva.codReserva}">

                            <p>Selecciona los servicios</p>
                            <c:if test="${not empty servicios}">
                                <table border="1">
                                    <thead>
                                        <tr>
                                            <th>Seleccionar</th> 
                                            <th>Nombre Servicio</th>
                                            <th>Menú</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="servicio" items="${servicios}">
                                            <tr>
                                                <td>
                                                    <input type="checkbox" name="servicios[]" value="${servicio.id}">
                                                </td>
                                                <td>${servicio.nombreServicio}</td>
                                                <td>
                                                    <c:if test="${not empty servicio.menus}">
                                                        <table border="1">
                                                            <thead>
                                                                <tr>
                                                                    <th>Menú ID</th>
                                                                    <th>Nombre Menú</th>
                                                                    <th>Precio</th>
                                                                    <th>Entradas</th>
                                                                    <th>Platos Principales</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <c:forEach var="menu" items="${servicio.menus}">
                                                                    <tr>
                                                                        <td>${menu.id}</td>
                                                                        <td>${menu.nombreMenu}</td>
                                                                        <td>${menu.precio}</td>
                                                                        <td>
                                                                            <c:forEach var="plato" items="${menu.platosEntrada}">
                                                                                ${plato.nombre}<br>
                                                                            </c:forEach>
                                                                        </td>
                                                                        <td>
                                                                            <c:forEach var="plato" items="${menu.platosPrincipal}">
                                                                                ${plato.nombre}<br>
                                                                            </c:forEach>
                                                                        </td>
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

                            <c:if test="${empty servicios}">
                                <p>No hay servicios disponibles.</p>
                            </c:if>

                            <input type="submit" value="Agregar servicio al menu">
                        </form>


                    </div>

                    <div class="formulario-contenedor">
                        <h2>Eliminar Menús del Servicio</h2>
                        <form action="ControladorCoordinador" method="POST">
                            <input type="hidden" name="action" value="eliminarServicioReserva">
                            <input type="hidden" name="idReserva" value="${reserva.codReserva}">

                            <p>Selecciona los Menús a eliminar del servicio:</p>
                            <c:if test="${not empty reserva.servicios}">
                                <table border="1">
                                    <thead>
                                        <tr>
                                            <th>Seleccionar</th> 
                                            <th>Nombre Servicio</th>
                                            <th>Menú</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="servicio" items="${reserva.servicios}">
                                            <tr>
                                                <td>
                                                    <input type="checkbox" name="serviciosEliminar[]" value="${servicio.id}">
                                                </td>
                                                <td>${servicio.nombreServicio}</td>
                                                <td>
                                                    <c:if test="${not empty servicio.menus}">
                                                        <table border="1">
                                                            <thead>
                                                                <tr>
                                                                    <th>Menú ID</th>
                                                                    <th>Nombre Menú</th>
                                                                    <th>Precio</th>
                                                                    <th>Entradas</th>
                                                                    <th>Platos Principales</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <c:forEach var="menu" items="${servicio.menus}">
                                                                    <tr>
                                                                        <td>${menu.id}</td>
                                                                        <td>${menu.nombreMenu}</td>
                                                                        <td>${menu.precio}</td>
                                                                        <td>
                                                                            <c:forEach var="plato" items="${menu.platosEntrada}">
                                                                                ${plato.nombre}<br>
                                                                            </c:forEach>
                                                                        </td>
                                                                        <td>
                                                                            <c:forEach var="plato" items="${menu.platosPrincipal}">
                                                                                ${plato.nombre}<br>
                                                                            </c:forEach>
                                                                        </td>
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

                            <c:if test="${empty reserva.servicios}">
                                <p>No hay servicios disponibles asociados a la reserva.</p>
                            </c:if>

                            <br>
                            <!-- Confirmación para eliminar los menús seleccionados -->
                            <input type="submit" value="Eliminar los servicios seleccionados" class="btn-eliminar"
                                   onclick="return confirm('¿Estás seguro de que deseas eliminar los servicios seleccionados de la reserva?');">
                        </form>
                    </div>


                </c:if>




            </div>
        </div>
    </body>
</html>
