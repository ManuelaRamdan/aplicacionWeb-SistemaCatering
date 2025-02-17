<%-- 
    Document   : vistaAdmModificar
    Created on : 16 feb 2025, 21:15:57
    Author     : Usuario
--%>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Modificación de Datos</title>
        <link rel="stylesheet" type="text/css" href="css/estiloAdm.css">
        <link rel="stylesheet" type="text/css" href="css/estiloModificar.css">
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

        <!-- Formulario de búsqueda universal -->
        <div class="contenido-central">
            <div class="search-container">
                <h2>Buscar Datos para Modificar</h2>
                <form action="ControladorAdm" method="GET">
                    <label for="busqueda">Ingrese un término de búsqueda (ID, nombre, usuario...):</label>
                    <input type="text" id="busqueda" name="busqueda" placeholder="Buscar..." required>
                    <button type="submit">Buscar</button>
                </form>
            </div>

            <hr>

            <!-- Resultados de búsqueda -->
            <div class="formulario-contenedor">
                <h2>Resultados de Búsqueda</h2>

                <!-- Coordinadores -->
                <c:if test="${not empty coordinadores}">
                    <h3>Coordinadores</h3>
                    <table border="1">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Usuario</th>
                                <th>Modificar</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="coordinador" items="${coordinadores}">
                                <tr>
                                    <td>${coordinador.id}</td>
                                    <td>${coordinador.usuario}</td>
                                    <td>
                                        <form action="ControladorAdm" method="POST" style="display:inline;">
                                            <input type="hidden" name="action" value="modificarCoordinador">
                                            <input type="hidden" name="idCoordinador" value="${coordinador.id}">
                                            <input type="submit" value="Modificar" class="btn-modificar">
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <!-- Clientes -->
                <c:if test="${not empty clientes}">
                    <h3>Clientes</h3>
                    <table border="1">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nombre</th>
                                <th>Apellido</th>
                                <th>Teléfono</th>
                                <th>Email</th>
                                <th>Modificar</th>
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
                                        <form action="ControladorAdm" method="POST" style="display:inline;">
                                            <input type="hidden" name="action" value="modificarCliente">
                                            <input type="hidden" name="idCliente" value="${cliente.id}">
                                            <input type="submit" value="Modificar" class="btn-modificar">
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <!-- Administradores -->
                <c:if test="${not empty administradores}">
                    <h3>Administradores</h3>
                    <table border="1">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Usuario</th>
                                <th>Modificar</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="administrador" items="${administradores}">
                                <tr>
                                    <td>${administrador.id}</td>
                                    <td>${administrador.usuario}</td>
                                    <td>
                                        <form action="ControladorAdm" method="POST" style="display:inline;">
                                            <input type="hidden" name="action" value="modificarAdministrador">
                                            <input type="hidden" name="idAdministrador" value="${administrador.id}">
                                            <input type="submit" value="Modificar" class="btn-modificar">
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <!-- Platos -->
                <c:if test="${not empty platos}">
                    <h3>Platos</h3>
                    <table border="1">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nombre del Plato</th>
                                <th>Modificar</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="plato" items="${platos}">
                                <tr>
                                    <td>${plato.id}</td>
                                    <td>${plato.nombre}</td>
                                    <td>
                                        <form action="ControladorAdm" method="POST" style="display:inline;">
                                            <input type="hidden" name="action" value="modificarPlato">
                                            <input type="hidden" name="idPlato" value="${plato.id}">
                                            <input type="submit" value="Modificar" class="btn-modificar">
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <!-- Si no hay resultados -->
                <c:if test="${empty coordinadores && empty clientes && empty administradores && empty platos}">
                    <p>No se encontraron resultados para su búsqueda.</p>
                </c:if>
            </div>
        </div>
    </body>
</html>

