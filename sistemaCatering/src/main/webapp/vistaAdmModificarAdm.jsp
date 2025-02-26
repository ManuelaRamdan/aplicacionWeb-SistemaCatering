<%-- 
    Document   : vistaAdmModificar
    Created on : 16 feb 2025, 21:15:57
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
        <link rel="stylesheet" type="text/css" href="css/estiloBaja.css">
    </head>
    <body>
        <!-- Menú Lateral -->
        <div class="menu-lateral">
            <ul>
                <li><a href="ControladorAdm?accion=mostrarAlta">Alta</a></li>
                <li><a href="ControladorAdm?accion=mostrarBaja">Baja</a></li>
                <li><a href="ControladorAdm?accion=mostrarModificarAdministrador">Modificación</a></li>
                <li><a href="ControladorAdm?accion=mostrar">Mostrar</a></li>
                <li><a href="ControladorAdm?accion=salir">Salir</a></li>

            </ul>
        </div>

        <!-- Contenido Central -->
        <div class="contenido-central">
            <h1>Modificar</h1>

            <!-- Pestañas -->
            <div class="pestañas">
                <a href="ControladorAdm?accion=mostrarModificarAdministrador" class="tab">Modificar Administrador</a>
                <a href="ControladorAdm?accion=mostrarModificarCoordinador" class="tab">Modificar Coordinador</a>
                <a href="ControladorAdm?accion=mostrarModificarCliente" class="tab">Modificar Cliente</a>
                <a href="ControladorAdm?accion=mostrarModificarPlato" class="tab">Modificar Plato</a>
                <a href="ControladorAdm?accion=mostrarModificarMenu" class="tab">Modificar Menu</a>
                <a href="ControladorAdm?accion=mostrarModificarServicio" class="tab">Modificar Servicio</a>

            </div>

            <!-- Contenido de las Pestañas -->
            <div>
                <h1>Modificar Administrador</h1>

                <c:if test="${not empty mensajeActualizarAdministrador}">
                    <div class="mensaje">${mensajeActualizarAdministrador}</div>
                </c:if>

                <c:if test="${not empty administradores}">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Usuario</th>
                                <th>Contraseña</th>
                                <th>Consultar</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="administrador" items="${administradores}">
                                <tr>
                                    <td>${administrador.codAdministrador}</td>
                                    <td>${administrador.usuario}</td>
                                    <td>${administrador.password}</td>
                                    <td>
                                        <form action="ControladorAdm" method="POST">
                                            <input type="hidden" name="action" value="modificarAdministrador">
                                            <input type="hidden" name="idAdministrador" value="${administrador.codAdministrador}">
                                            <input type="submit" value="Modificar" class="btn-Modificar">
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <c:if test="${not empty administrador}">
                    <h2>Editar Coordinador</h2>
                    <form action="ControladorAdm" method="post">
                        <input type="hidden" name="action" value="actualizarAdm">
                        <input type="hidden" name="idAdministrador" value="${administrador.codAdministrador}">
                        <label>Usuario:</label>
                        <input type="text" name="usuario" value="${administrador.usuario}" required>
                        <label for="password">Contraseña:</label>
                        <input type="password" name="password" value="${administrador.password}" required>
                        <button type="submit">Guardar</button>
                    </form>
                </c:if>
            </div>
        </div>
    </body>
</html>
