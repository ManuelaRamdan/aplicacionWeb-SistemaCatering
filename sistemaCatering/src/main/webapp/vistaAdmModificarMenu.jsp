<%-- 
    Document   : vistaAdmModificarMenu
    Created on : 20 feb 2025, 23:03:43
    Author     : Usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Modificar Menu</title>
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


            <h1>Modificar Menu</h1>

            <c:if test="${not empty mensajeActualizarMenu}">
                <div class="mensaje">${mensajeActualizarMenu}</div>
            </c:if>

            <c:if test="${not empty menus}">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Precio</th>
                            <th>Entradas</th>
                            <th>Platos Principales</th>
                            <th>Consultar</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="menu" items="${menus}">
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

                                <td>
                                    <form action="ControladorAdm" method="POST">
                                        <input type="hidden" name="action" value="modificarMenu">
                                        <input type="hidden" name="idMenu" value="${menu.id}">
                                        <input type="submit" value="Modificar" class="btn-Modificar">
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

            <c:if test="${not empty menu}">
                <h2>Editar Menú</h2>
                <div class="formulario-contenedor">
                    <form action="ControladorAdm" method="post">
                        <input type="hidden" name="action" value="actualizarMenuNombrePrecio">
                        <input type="hidden" name="idMenu" value="${menu.id}">

                        <!-- Nombre del Menú -->
                        <label>Nombre:</label>
                        <input type="text" name="nombre" value="${menu.nombreMenu}">

                        <!-- Precio del Menú -->
                        <label>Precio:</label>
                        <input type="text" name="precio" value="${menu.precio}">
                        <button type="submit">Guardar</button>

                    </form>

                </div>
                <!-- Platos de Entrada -->
                <div class="formulario-contenedor">
                    <h2>Agregar platos al menu </h2>
                    <form action="ControladorAdm" method="POST">
                        <input type="hidden" name="action" value="agregarPlatosMenu">
                        <input type="hidden" name="idMenu" value="${menu.id}">

                        <p>Selecciona los Platos de la entrada:</p>
                        <c:if test="${not empty platosEntrada}">
                            <c:forEach var="plato" items="${platosEntrada}">
                                <label>
                                    <input type="checkbox" name="platoEntrada[]" value="${plato.id}"> ${plato.nombre}
                                </label><br>
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty platosEntrada}">
                            <p>No hay platos de entrada disponibles.</p>
                        </c:if>

                        <p>Selecciona los Platos Principales:</p>
                        <c:if test="${not empty platosPrincipal}">
                            <c:forEach var="plato" items="${platosPrincipal}">
                                <label>
                                    <input type="checkbox" name="platoPrincipal[]" value="${plato.id}"> ${plato.nombre}
                                </label><br>
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty platosPrincipal}">
                            <p>No hay platos principales disponibles.</p>
                        </c:if>

                        <input type="submit" value="Agregar platos al menu ">
                    </form>

                </div>
                <!-- Eliminar Platos (solo si ya están asociados al menú) -->
                <div class="formulario-contenedor">
                    <h2>Eliminar platos del menú</h2>
                    <form action="ControladorAdm" method="POST">
                        <input type="hidden" name="action" value="eliminarPlatoMenu">
                        <input type="hidden" name="idMenu" value="${menu.id}">

                        <p>Selecciona los Platos de Entrada a eliminar:</p>
                        <c:if test="${not empty menu.platosEntrada}">
                            <c:forEach var="plato" items="${menu.platosEntrada}">
                                <label>
                                    <input type="checkbox" name="platoEntradaEliminar[]" value="${plato.id}">
                                    ${plato.nombre}
                                </label><br>
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty menu.platosEntrada}">
                            <p>No hay platos de entrada asociados al menú.</p>
                        </c:if>

                        <p>Selecciona los Platos Principales a eliminar:</p>
                        <c:if test="${not empty menu.platosPrincipal}">
                            <c:forEach var="plato" items="${menu.platosPrincipal}">
                                <label>
                                    <input type="checkbox" name="platoPrincipalEliminar[]" value="${plato.id}">
                                    ${plato.nombre}
                                </label><br>
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty menu.platosPrincipal}">
                            <p>No hay platos principales asociados al menú.</p>
                        </c:if>

                        <br>
                        <!-- Confirmación para eliminar los platos seleccionados -->
                        <input type="submit" value="Eliminar los platos seleccionados" class="btn-eliminar" 
                               onclick="return confirm('¿Estás seguro de que deseas eliminar los platos seleccionados del menú ${menu.nombreMenu}?');">
                    </form>
                </div>

            </c:if>

        </div>
    </body>
</html>
