<%-- 
    Document   : vistaAdmModificarCliente
    Created on : 20 feb 2025, 15:13:24
    Author     : Usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Modificar Cliente</title>
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
                <h1>Modificar Cliente</h1>

                <c:if test="${not empty mensajeActualizarCliente}">
                    <div class="mensaje">${mensajeActualizarCliente}</div>
                </c:if>

                <c:if test="${not empty clientes}">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Usuario</th>
                                <th>Password</th>
                                <th>Nombre</th>
                                <th>Apellido</th>
                                <th>Teléfono</th>
                                <th>Email</th>
                                <th>Consultar</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cliente" items="${clientes}">
                                <tr>
                                    <td>${cliente.id}</td>
                                    <td>${cliente.usuario}</td>
                                    <td>${cliente.password}</td>
                                    <td>${cliente.nombre}</td>
                                    <td>${cliente.apellido}</td>
                                    <td>${cliente.telReferencia}</td>
                                    <td>${cliente.email}</td>
                                    <td>
                                        <form action="ControladorAdm" method="POST">
                                            <input type="hidden" name="action" value="modificarCliente">
                                            <input type="hidden" name="persona_id" value="${cliente.persona_id}">
                                            <input type="submit" value="Modificar" class="btn-Modificar">
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <c:if test="${not empty cliente}">
                    <h2>Editar Cliente</h2>
                    <div class="formulario-contenedor">
                        <form action="ControladorAdm" method="post">
                            <input type="hidden" name="action" value="actualizarCliente">
                            <input type="hidden" name="idCliente" value="${cliente.id}">
                            <input type="hidden" name="persona_id" value="${cliente.persona_id}">

                            <label>Usuario:</label>
                            <input type="text" name="usuario" value="${cliente.usuario}" required>
                            <label for="password">Contraseña:</label>
                            <input type="password" name="password" value="${cliente.password}" required>
                            <label>Nombre:</label>
                            <input type="text" name="nombre" value="${cliente.nombre}">
                            <label>Apellido:</label>
                            <input type="text" name="apellido" value="${cliente.apellido}">
                            <label>Teléfono:</label>
                            <input type="text" name="telefono" value="${cliente.telReferencia}">
                            <label>Email:</label>
                            <input type="email" name="email" value="${cliente.email}">
                            <button type="submit">Guardar</button>
                        </form>
                    </div>
                </c:if>
            </div>
        </div>

    </body>
</html>
