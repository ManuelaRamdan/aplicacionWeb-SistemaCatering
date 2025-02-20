<%-- 
    Document   : vistaAdmAlta
    Created on : 13 feb 2025, 20:16:20
    Author     : MANUELA
--%>

<%@page import="sistemacatering.sistemacatering.Menu"%>
<%@page import="sistemacatering.sistemacatering.Plato"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Alta de Datos</title>
        <link rel="stylesheet" type="text/css" href="css/estiloAdm.css">
        <link rel="stylesheet" type="text/css" href="css/estiloAlta.css?v=2">
        <link rel="stylesheet" type="text/css" href="css/estilos.css">


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

            <!-- Formulario Alta Coordinador -->
            <div class="formulario-contenedor">
                <form action="ControladorAdm" method="post">
                    <h2>Registrar Coordinador</h2>
                    <input type="hidden" name="action" value="registrarCoordinador">

                    <label for="usuario">Usuario:</label>
                    <input type="text" id="usuario" name="usuario" required>

                    <label for="password">Contraseña:</label>
                    <input type="password" id="password" name="password" required>

                    <input type="submit" value="Registrar Coordinador">


                    <!-- Usando JSTL para mostrar mensajes -->
                    <c:if test="${not empty mensajeCoordinador}">
                        <p>${mensajeCoordinador}</p>
                    </c:if>
                </form>
            </div>

            <hr>

            <!-- Formulario Alta Cliente -->
            <div class="formulario-contenedor">
                <h2>Alta de Cliente</h2>
                <form action="ControladorAdm" method="POST">
                    <input type="hidden" name="action" value="registrarCliente"> <!-- Acción que se envía al controlador -->

                    <label for="usuario">Usuario:</label>
                    <input type="text" id="usuario" name="usuario" required>

                    <label for="password">Contraseña:</label>
                    <input type="password" id="password" name="password" required>

                    <label for="nombre">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" required>

                    <label for="apellido">Apellido:</label>
                    <input type="text" id="apellido" name="apellido" required>

                    <label for="telefono">Teléfono:</label>
                    <input type="number" id="telefono" name="telefono" required>

                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" required>

                    <input type="submit" value="Registrar Cliiente">


                    <!-- Usando JSTL para mostrar mensajes -->
                    <c:if test="${not empty mensajeCliente}">
                        <p>${mensajeCliente}</p>
                    </c:if>
                </form>
            </div>
            <hr>

            <!-- Formulario Alta Administrador -->
            <div class="formulario-contenedor">
                <form action="ControladorAdm" method="POST">
                    <h2>Registrar Administrador</h2>
                    <input type="hidden" name="action" value="registrarAdministrador">

                    <label for="usuario">Usuario:</label>
                    <input type="text" id="usuario" name="usuario" required>

                    <label for="password">Contraseña:</label>
                    <input type="password" id="password" name="password" required>

                    <input type="submit" value="Registrar Administrador">


                    <!-- Usando JSTL para mostrar mensajes -->
                    <c:if test="${not empty mensajeAdm}">
                        <p>${mensajeAdm}</p>
                    </c:if>
                </form>
            </div>

            <hr>

            <!-- Formulario Alta de Platos -->
            <div class="formulario-contenedor">
                <h2>Alta de Plato</h2>
                <form class="formulario" action="ControladorAdm" method="POST">
                    <input type="hidden" name="action" value="registrarPlato"> <!-- Acción que se envía al controlador -->
                    <label>Nombre del Plato:</label>
                    <input type="text" name="nombrePlato" required>

                    <input type="submit" value="Registrar Plato">

                    <!-- Usando JSTL para mostrar mensajes -->
                    <c:if test="${not empty mensajePlato}">
                        <p>${mensajePlato}</p>
                    </c:if>
                </form>
            </div>

            <hr>

            <!-- Formulario Alta de Menú -->
            <div class="formulario-contenedor">
                <h2>Alta de Menú</h2>
                <form action="ControladorAdm" method="POST">
                    <input type="hidden" name="action" value="registrarMenu">

                    <label>Nombre del Menú:</label>
                    <input type="text" name="nombreMenu" required>

                    <p>Selecciona los Platos de la entrada:</p>
                    <c:if test="${not empty platosEntrada}">
                        <c:forEach var="plato" items="${platosEntrada}">
                            <label>
                                <input type="checkbox" name="platoEntrada[]" value="${plato.id}"> ${plato.nombre}
                            </label><br>
                        </c:forEach>
                    </c:if>
                    <c:if test="${empty platosEntrada}">
                        <p>No hay platos disponibles.</p>
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
                        <p>No hay platos disponibles.</p>
                    </c:if>

                    <input type="submit" value="Registrar Menu">

                </form>

                <!-- Usando JSTL para mostrar mensajes -->
                <c:if test="${not empty mensajeMenu}">
                    <p>${mensajeMenu}</p>
                </c:if>
            </div>

            <hr>

            <!-- Formulario Alta Servicio -->
            <div class="formulario-contenedor">
                <h2>Alta de Servicio</h2>
                <form action="ControladorAdm" method="POST">
                    <input type="hidden" name="action" value="registrarServicio">
                    <label>Nombre del Servicio:</label>
                    <input type="text" name="nombreServicio" required>

                    <p>Selecciona los Menus:</p>
                    <c:if test="${not empty menus}">
                        <c:forEach var="menu" items="${menus}">
                            <label>
                                <input type="checkbox" name="menus[]" value="${menu.id}"> ${menu.nombreMenu}
                            </label><br>
                        </c:forEach>
                    </c:if>
                    <c:if test="${empty menus}">
                        <p>No hay Menús disponibles.</p>
                    </c:if>

                    <input type="submit" value="Registrar Servicio">

                </form>

                <!-- Usando JSTL para mostrar mensajes -->
                <c:if test="${not empty mensajeServicio}">
                    <p>${mensajeServicio}</p>
                </c:if>
            </div>

        </div>

    </body>
</html>

