<%-- 
    Document   : vistaAdmModificarServicio
    Created on : 21 feb 2025, 22:01:14
    Author     : Usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Modificar Servicio</title>
        <link rel="stylesheet" type="text/css" href="css/estiloGeneral.css">
        <link rel="stylesheet" type="text/css" href="css/estiloBaja.css">
        <link rel="stylesheet" type="text/css" href="css/estiloMostrar.css">

    </head>
    <body>
        <div class="menu-lateral">
            <ul>
                <li><a href="ControladorAdm?accion=mostrarAlta">Alta</a></li>
                <li><a href="ControladorAdm?accion=mostrarBaja">Baja</a></li>
                <li><a href="ControladorAdm?accion=mostrarModificarAdministrador">Modificación</a></li>
                <li><a href="ControladorAdm?accion=mostrar">Mostrar</a></li>
                <li><a href="ControladorAdm?accion=salir">Salir</a></li>

            </ul>
        </div>

        <div class="contenido-central">
            <h1>Modificar</h1>

            <div class="tabs">
                <a href="ControladorAdm?accion=mostrarModificarAdministrador" class="tab">Modificar Administrador</a>
                <a href="ControladorAdm?accion=mostrarModificarCoordinador" class="tab">Modificar Coordinador</a>
                <a href="ControladorAdm?accion=mostrarModificarCliente" class="tab">Modificar Cliente</a>
                <a href="ControladorAdm?accion=mostrarModificarPlato" class="tab">Modificar Plato</a>
                <a href="ControladorAdm?accion=mostrarModificarMenu" class="tab">Modificar Menu</a>
                <a href="ControladorAdm?accion=mostrarModificarServicio" class="tab">Modificar Servicio</a>



            </div>


            <h1>Modificar Servicio</h1>

            <c:if test="${not empty mensajeActualizarServicio}">
                <div class="mensaje">${mensajeActualizarServicio}</div>
            </c:if>

            <c:if test="${not empty servicios}">
                <table border="1">
                    <thead>
                        <tr>
                            <th>Servicio ID</th>
                            <th>Nombre Servicio</th>
                            <th>Menú</th> <!-- Nueva columna para los menús -->
                            <th>Consultar</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="servicio" items="${servicios}">
                            <tr>
                                <td>${servicio.id}</td>
                                <td>${servicio.nombreServicio}</td>
                                <td>
                                    <!-- Tabla anidada para los menús de este servicio -->
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

                                                        <!-- Entradas -->
                                                        <td>
                                                            <c:forEach var="plato" items="${menu.platosEntrada}">
                                                                ${plato.nombre}<br>
                                                            </c:forEach>
                                                        </td>

                                                        <!-- Platos Principales -->
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
                                <td>
                                    <form action="ControladorAdm" method="POST">
                                        <input type="hidden" name="action" value="modificarServicio">
                                        <input type="hidden" name="idServicio" value="${servicio.id}">
                                        <input type="submit" value="Modificar" class="btn-Modificar">
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

            <c:if test="${not empty servicio}">
                <h2>Editar servicio</h2>
                <div class="formulario-contenedor">
                    <form action="ControladorAdm" method="post">
                        <input type="hidden" name="action" value="actualizarServicioMenu">
                        <input type="hidden" name="idServicio" value="${servicio.id}">

                        <!-- Nombre del Menú -->
                        <label>Nombre:</label>
                        <input type="text" name="nombre" value="${servicio.nombreServicio}">

                        <button type="submit">Guardar</button>

                    </form>

                </div>
                <!-- Platos de Entrada -->
                <div class="formulario-contenedor">
                    <h2>Agregar menus al servicio </h2>
                    <form action="ControladorAdm" method="POST">
                        <input type="hidden" name="action" value="agregarMenuServicio">
                        <input type="hidden" name="idServicio" value="${servicio.id}">

                        <p>Selecciona los Menus:</p>
                        <c:if test="${not empty menus}">
                            <table border="1">
                                <thead>
                                    <tr>
                                        <th>Seleccionar</th>
                                        <th>Nombre Menú</th>
                                        <th>Precio</th>
                                        <th>Entradas</th>
                                        <th>Platos Principales</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="menu" items="${menus}">
                                        <tr>
                                            <!-- Checkbox para seleccionar el menú -->
                                            <td>
                                                <input type="checkbox" name="menus[]" value="${menu.id}">
                                            </td>
                                            <td>${menu.nombreMenu}</td>
                                            <td>${menu.precio}</td>

                                            <!-- Entradas -->
                                            <td>
                                                <c:if test="${not empty menu.platosEntrada}">
                                                    <ul>
                                                        <c:forEach var="plato" items="${menu.platosEntrada}">
                                                            <li>${plato.nombre}</li>
                                                            </c:forEach>
                                                    </ul>
                                                </c:if>
                                                <c:if test="${empty menu.platosEntrada}">
                                                    <p>No tiene entradas.</p>
                                                </c:if>
                                            </td>

                                            <!-- Platos Principales -->
                                            <td>
                                                <ul>
                                                    <c:forEach var="plato" items="${menu.platosPrincipal}">
                                                        <li>${plato.nombre}</li>
                                                        </c:forEach>
                                                </ul>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:if>

                        <c:if test="${empty menus}">
                            <p>No hay Menús disponibles.</p>
                        </c:if>

                        <input type="submit" value="Agregar menus al servicio ">
                    </form>

                </div>

                <div class="formulario-contenedor">
                    <h2>Eliminar Menús del Servicio</h2>
                    <form action="ControladorAdm" method="POST">
                        <input type="hidden" name="action" value="eliminarMenuServicio">
                        <input type="hidden" name="idServicio" value="${servicio.id}">

                        <p>Selecciona los Menús a eliminar del servicio:</p>
                        <c:if test="${not empty servicio.menus}">
                            <table border="1">
                                <thead>
                                    <tr>
                                        <th>Seleccionar</th>
                                        <th>Nombre Menú</th>
                                        <th>Precio</th>
                                        <th>Entradas</th>
                                        <th>Platos Principales</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="menu" items="${servicio.menus}">
                                        <tr>
                                            <!-- Checkbox para seleccionar el menú a eliminar -->
                                            <td>
                                                <input type="checkbox" name="menusEliminar[]" value="${menu.id}">
                                            </td>
                                            <td>${menu.nombreMenu}</td>
                                            <td>${menu.precio}</td>

                                            <!-- Entradas -->
                                            <td>
                                                <c:if test="${not empty menu.platosEntrada}">
                                                    <ul>
                                                        <c:forEach var="plato" items="${menu.platosEntrada}">
                                                            <li>${plato.nombre}</li>
                                                            </c:forEach>
                                                    </ul>
                                                </c:if>
                                                <c:if test="${empty menu.platosEntrada}">
                                                    <p>No tiene entradas.</p>
                                                </c:if>
                                            </td>

                                            <!-- Platos Principales -->
                                            <td>
                                                <c:if test="${not empty menu.platosPrincipal}">
                                                    <ul>
                                                        <c:forEach var="plato" items="${menu.platosPrincipal}">
                                                            <li>${plato.nombre}</li>
                                                            </c:forEach>
                                                    </ul>
                                                </c:if>
                                                <c:if test="${empty menu.platosPrincipal}">
                                                    <p>No tiene platos principales.</p>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:if>

                        <c:if test="${empty servicio.menus}">
                            <p>No hay Menús disponibles asociados al servicio.</p>
                        </c:if>

                        <br>
                        <!-- Confirmación para eliminar los menús seleccionados -->
                        <input type="submit" value="Eliminar los menús seleccionados" class="btn-eliminar"
                               onclick="return confirm('¿Estás seguro de que deseas eliminar los menús seleccionados del servicio ${servicio.nombreServicio}?');">
                    </form>
                </div>


            </c:if>

        </div>
    </body>
</html>

