<%-- 
    Document   : vistaAdmModificarCoordinador
    Created on : 20 feb 2025, 11:34:07
    Author     : Usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Modificar Coordinador</title>
        <link rel="stylesheet" type="text/css" href="css/estiloGeneral.css">
        <link rel="stylesheet" type="text/css" href="css/estiloBaja.css">
    </head>
    <body>
        <div class="container">
            <input type="checkbox" id="menu-toggle" class="menu-checkbox">
            <label for="menu-toggle" class="menu-icon">☰</label>

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

                <div class="pestañas">
                    <a href="ControladorAdm?accion=mostrarModificarAdministrador" class="tab">Modificar Administrador</a>
                    <a href="ControladorAdm?accion=mostrarModificarCoordinador" class="tab">Modificar Coordinador</a>
                    <a href="ControladorAdm?accion=mostrarModificarCliente" class="tab">Modificar Cliente</a>
                    <a href="ControladorAdm?accion=mostrarModificarPlato" class="tab">Modificar Plato</a>
                    <a href="ControladorAdm?accion=mostrarModificarMenu" class="tab">Modificar Menu</a>
                    <a href="ControladorAdm?accion=mostrarModificarServicio" class="tab">Modificar Servicio</a>

                </div>
                <h1>Modificar Coordinador</h1>

                <c:if test="${not empty mensajeActualizarCoordinador}">
                    <div class="mensaje">${mensajeActualizarCoordinador}</div>
                </c:if>

                <c:if test="${not empty coordinadores}">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Usuario</th>
                                <th>Password</th>

                                <th>Consultar</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="coordinador" items="${coordinadores}">
                                <tr>
                                    <td>${coordinador.codCoordinador}</td>
                                    <td>${coordinador.usuario}</td>
                                    <td>${coordinador.password}</td>
                                    <td>
                                        <form action="ControladorAdm" method="POST">
                                            <input type="hidden" name="action" value="modificarCoordinador">
                                            <input type="hidden" name="idCoordinador" value="${coordinador.codCoordinador}">
                                            <input type="submit" value="Modificar" class="btn-Modificar">
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <c:if test="${not empty coordinador}">
                    <h2>Editar Coordinador</h2>
                    <div class="formulario-contenedor">

                        <form action="ControladorAdm" method="post">
                            <input type="hidden" name="action" value="actualizarCoordinador">
                            <input type="hidden" name="idCoordinador" value="${coordinador.codCoordinador}">
                            <label>Usuario:</label>
                            <input type="text" name="usuario" value="${coordinador.usuario}" required>
                            <label for="password">Contraseña:</label>
                            <input type="password" name="password" value="${coordinador.password}" required>
                            <button type="submit">Guardar</button>
                        </form>
                    </div>

                </c:if>
            </div>
        </div>

    </body>
</html>

