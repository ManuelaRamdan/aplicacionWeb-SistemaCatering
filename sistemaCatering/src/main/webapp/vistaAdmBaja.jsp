<%-- 
    Document   : vistaAdmBaja
    Created on : 13 feb 2025, 20:29:22
    Author     : MANUELA
--%>


<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Baja de Datos</title>
        <link rel="stylesheet" type="text/css" href="css/estiloAdm.css">
        <link rel="stylesheet" type="text/css" href="css/estiloBaja.css">
    </head>
    <body>

        <div class="menu-lateral">
            <ul>
                <li><a href="ControladorAdm?accion=mostrarAlta">Alta</a></li>
                <li><a href="ControladorAdm?accion=mostrarBaja">Baja</a></li>
                <li><a href="vistaAdmModificacion.jsp">Modificación</a></li>
                <li><a href="vistaAdmMostrar.jsp">Mostrar</a></li>
            </ul>
        </div>

        <div class="contenido-central">
            <!-- Formulario Baja Coordinador -->
            <div class="formulario-contenedor">
                <h2>Eliminar Coordinador</h2>

                <p>Seleccione el Coordinador que desea eliminar:</p>

                <!-- Verificación de si la lista de coordinadores no está vacía -->
                <c:if test="${not empty coordinadores}">
                    <table border="1">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Usuario</th>
                                <th>Eliminar</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Iterar sobre los coordinadores -->
                            <c:forEach var="coordinador" items="${coordinadores}">
                                <tr>
                                    <td>${coordinador.codCoordinador}</td>
                                    <td>${coordinador.usuario}</td>
                                    <td>
                                        <!-- Formulario para eliminar coordinador -->
                                        <form action="ControladorAdm" method="POST" style="display:inline;">
                                            <input type="hidden" name="action" value="eliminarCoordinador">
                                            <input type="hidden" name="idCoordinador" value="${coordinador.codCoordinador}">
                                            <input type="submit" value="Eliminar" class="btn-eliminar">
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>


            </div>

            <hr>
            <div class="formulario-contenedor">
                <h2>Eliminar Cliente</h2>

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
                                <th>Eliminar</th>
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
                                        <!-- Formulario para eliminar cliente -->
                                        <form action="ControladorAdm" method="POST" style="display:inline;">
                                            <input type="hidden" name="action" value="eliminarCliente">
                                            <input type="hidden" name="idCliente" value="${cliente.id}">
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






            <hr>
            <!-- Formulario Baja Administrador -->
            <div class="formulario-contenedor">
                <h2>Eliminar Administrador</h2>

                <p>Seleccione el ID del Administrador que desea eliminar:</p>

                <table border="1">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Usuario</th>
                            <th>Eliminar</th> <!-- Columna para el botón -->
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="administrador" items="${administradores}">
                            <tr>
                                <td>${administrador.codAdministrador}</td>
                                <td>${administrador.usuario}</td>
                                <td>
                                    <!-- Formulario para eliminar administrador -->
                                    <form action="ControladorAdm" method="POST" style="display:inline;">
                                        <input type="hidden" name="action" value="eliminarAdministrador">
                                        <input type="hidden" name="idAdministrador" value="${administrador.codAdministrador}">
                                        <input type="submit" value="Eliminar" class="btn-eliminar">
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty administradores}">
                            <tr>
                                <td colspan="3">No hay Administradores disponibles.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <hr>
            <!-- Formulario Baja de Platos -->
            <div class="formulario-contenedor">
                <h2>Eliminar Plato</h2>

                <!-- Verificación de si la lista de platos no está vacía -->
                <c:if test="${not empty platos}">
                    <table border="1">
                        <thead>
                            <tr>
                                <th>Nombre del Plato</th>
                                <th>Eliminar</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Iterar sobre los platos -->
                            <c:forEach var="plato" items="${platos}">
                                <tr>
                                    <td>${plato.nombre}</td>
                                    <td>
                                        <!-- Formulario para eliminar plato -->
                                        <form action="ControladorAdm" method="POST" style="display:inline;">
                                            <input type="hidden" name="action" value="eliminarPlato">
                                            <input type="hidden" name="idPlato" value="${plato.id}">
                                            <input type="submit" value="Eliminar" class="btn-eliminar">
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <c:if test="${empty platos}">
                    <p>No hay platos disponibles para eliminar.</p>
                </c:if>
            </div>

            <hr>
            <!-- Formulario Baja de Menú -->
            <div class="formulario-contenedor">
                <h2>Eliminar Menú</h2>

                <!-- Verificación de si la lista de menús no está vacía -->
                <c:if test="${not empty menus}">
                    <table border="1">
                        <thead>
                            <tr>
                                <th>Nombre del Menú</th>
                                <th>Eliminar</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="menu" items="${menus}">
                                <tr>
                                    <td>${menu.nombreMenu}</td>
                                    <td>
                                        <!-- Formulario para eliminar menú -->
                                        <form action="ControladorAdm" method="POST" style="display:inline;">
                                            <input type="hidden" name="action" value="eliminarMenu">
                                            <input type="hidden" name="idMenu" value="${menu.id}">
                                            <input type="submit" value="Eliminar" class="btn-eliminar">
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <c:if test="${empty menus}">
                    <p>No hay menús disponibles para eliminar.</p>
                </c:if>

            </div>


            <hr>
            <!-- Formulario Baja Servicio -->
            <div class="formulario-contenedor">
                <h2>Eliminar Servicio</h2>

                <!-- Verificación de si la lista de servicios no está vacía -->
                <c:if test="${not empty servicios}">
                    <table border="1">
                        <thead>
                            <tr>
                                <th>Nombre del Servicio</th>
                                <th>Eliminar</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Iterar sobre los servicios -->
                            <c:forEach var="servicio" items="${servicios}">
                                <tr>
                                    <td>${servicio.nombre}</td>
                                    <td>
                                        <!-- Formulario para eliminar servicio -->
                                        <form action="ControladorAdm" method="POST" style="display:inline;">
                                            <input type="hidden" name="action" value="eliminarServicio">
                                            <input type="hidden" name="nombreServicio" value="${servicio.nombre}">
                                            <input type="submit" value="Eliminar" class="btn-eliminar">
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <c:if test="${empty servicios}">
                    <p>No hay servicios disponibles para eliminar.</p>
                </c:if>
            </div>

        </div>
    </body>
</html>



