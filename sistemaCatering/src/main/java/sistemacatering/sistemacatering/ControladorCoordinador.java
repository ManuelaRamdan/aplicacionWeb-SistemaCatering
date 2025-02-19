/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemacatering.sistemacatering;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Usuario
 */
@WebServlet(name = "ControladorCoordinador", urlPatterns = {"/ControladorCoordinador"})
public class ControladorCoordinador extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) // obtener datos para la vista
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String idCoordinador = (String) session.getAttribute("userId");

        if (idCoordinador != null) {
            Modelo modelo = new Modelo("localhost", "catering"); // Conexión con la base de datos

            // Obtener la acción que el administrador desea realizar (en caso de que haya una acción específica)
            String accion = request.getParameter("accion");

            // Si no hay acción específica, por defecto se carga la vista de menú
            if (accion == null) {
                accion = "menu"; // Acción por defecto
            }

            switch (accion) {
                case "menu":
                    // Redirigir a la vista de menú
                    RequestDispatcher dispatcherMenu = request.getRequestDispatcher("vistaCoordinadorMenu.jsp");
                    dispatcherMenu.forward(request, response);
                    break;

                case "mostrarAlta":
                    request.getRequestDispatcher("vistaCoordAlta.jsp").forward(request, response);
                    break;

                case "mostrarBaja":
                    List<Cliente> clientes = modelo.obtenerClientesBd();
                    request.setAttribute("clientes", clientes);

                    List<Reserva> reservas = modelo.obtenerReservaBd();
                    request.setAttribute("reservas", reservas);

                    request.getRequestDispatcher("vistaCoordBaja.jsp").forward(request, response);
                    break;

                case "mostrarModificar":

                    break;

                case "mostrarReservas":
                    reservas = modelo.obtenerReservaBd();
                    request.setAttribute("reservas", reservas);
                    request.getRequestDispatcher("vistaCoordMostrarReserva.jsp").forward(request, response);

                    break;
                case "mostrarCliente":
                    clientes = modelo.obtenerClientesConReservas(); // Obtener clientes con reservas
                    request.setAttribute("clientes", clientes);  // Pasar la lista de clientes al JSP
                    request.getRequestDispatcher("vistaCoordMostrarCliente.jsp").forward(request, response);

                    break;
                case "consultarCliente":

                    clientes = modelo.obtenerClientesBd();
                    request.setAttribute("clientes", clientes);
                    request.getRequestDispatcher("vistaCoordConsultarCliente.jsp").forward(request, response);

                    break;
                default:
                    // Si la acción no es reconocida, redirige a una página de error
                    request.setAttribute("mensajeError", "Acción no válida");
                    request.getRequestDispatcher("vistaError.jsp").forward(request, response);
                    break;
            }

        } else {
            // Si el usuario no está logueado, redirigir al login
            RequestDispatcher dispatcherLogin = request.getRequestDispatcher("login.jsp");
            dispatcherLogin.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        // Crear la instancia del modelo
        Modelo modelo = new Modelo("localhost", "catering");

        if ("consultarCliente".equals(action)) {
            String personaId = request.getParameter("persona_id");  // Obtener persona_id
            List<Reserva> reservas = modelo.obtenerReservasPorCliente(personaId);  // Obtener reservas usando persona_id
            request.setAttribute("reservas", reservas);  // Establecer las reservas en el request
            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaCoordConsultarClienteReserva.jsp");
            dispatcher.forward(request, response);
        } else if ("eliminarCliente".equals(action)) {
            int idCliente = Integer.parseInt(request.getParameter("idCliente"));

            boolean eliminado = modelo.eliminarCliente(idCliente);

            if (eliminado) {
                request.setAttribute("mensajeBajaCliente", "Cliente dado de baja exitosamente.");
            } else {
                request.setAttribute("mensajeBajaCliente", "Error, no se puso eliminar");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaCoordBaja.jsp");
            dispatcher.forward(request, response);
        } else if ("eliminarReserva".equals(action)) {
            int idReserva = Integer.parseInt(request.getParameter("codReserva"));

            boolean eliminado = modelo.eliminarReserva(idReserva);

            if (eliminado) {
                request.setAttribute("mensajeBajaReserva", "Reserva dado de baja exitosamente.");
            } else {
                request.setAttribute("mensajeBajaReserva", "Error, no se puso eliminar");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaCoordBaja.jsp");
            dispatcher.forward(request, response);
        } else if ("registrarReserva".equals(action)) {
            try {

// Obtener parámetros del formulario
                int codCliente = Integer.parseInt(request.getParameter("codCliente"));
                String fechaInicioUsuario = request.getParameter("fechaInicioEvento");
                String fechaFinUsuario = request.getParameter("fechaFinEvento");
                String restriccionesDieteticas = request.getParameter("restriccionesDieteticas");
                String preferenciaCliente = request.getParameter("preferenciaCliente");
                String tipoServicio = request.getParameter("tipoServicio");
                int cantidadPersonas = Integer.parseInt(request.getParameter("cantidadPersonas"));
                int precio = Integer.parseInt(request.getParameter("precio"));
                String modoDeReserva = request.getParameter("modoDeReserva");
                String calle = request.getParameter("calle");
                int altura = Integer.parseInt(request.getParameter("altura"));
                String barrio = request.getParameter("barrio");
                String[] serviciosSeleccionados = request.getParameterValues("serviciosSeleccionados");

                // Verificar que el código del cliente es válido
                boolean clienteExiste = modelo.verificarCliente(codCliente);
                if (!clienteExiste) {
                    request.setAttribute("error", "El código del cliente no existe.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("vistaCoordAlta.jsp");
                    dispatcher.forward(request, response);
                    return;
                }

                // Formato para parsear la fecha
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                Date fechaInicioEvento = inputFormat.parse(fechaInicioUsuario);
                Date fechaFinEvento = inputFormat.parse(fechaFinUsuario);
                Date fechaReserva = new Date(); // Fecha de reserva actual

                // Convertir fechas a LocalDateTime para validar que sean futuras
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime hoy = LocalDateTime.now();
                LocalDateTime fechaInicioLocal = fechaInicioEvento.toInstant().atZone(zoneId).toLocalDateTime();
                LocalDateTime fechaFinLocal = fechaFinEvento.toInstant().atZone(zoneId).toLocalDateTime();

                if (fechaInicioLocal.isBefore(hoy) || fechaFinLocal.isBefore(hoy)) {
                    request.setAttribute("error", "Las fechas de inicio y fin deben ser mayores a la fecha y hora actual.");
                    request.setAttribute("fechaInicioEvento", fechaInicioUsuario);
                    request.setAttribute("fechaFinEvento", fechaFinUsuario);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("vistaCoordAlta.jsp");
                    dispatcher.forward(request, response);
                    return;
                }

                // Crear objeto Domicilio
                Domicilio domicilio = new Domicilio(0, calle, altura, barrio);

                // Crear objeto Reserva
                Reserva reserva = new Reserva(0, codCliente, fechaInicioEvento, fechaFinEvento, restriccionesDieteticas,
                        preferenciaCliente, tipoServicio, cantidadPersonas, precio, modoDeReserva, domicilio, false);

                // Agregar servicios a la reserva si fueron seleccionados
                if (serviciosSeleccionados != null) {
                    for (String idServicio : serviciosSeleccionados) {
                        Servicio servicio = modelo.obtenerServicioPorId(Integer.parseInt(idServicio));
                        if (servicio != null) {
                            reserva.getServicios().add(servicio);
                        }
                    }
                } else {
                    System.out.println("No se seleccionaron servicios.");
                }

                // Registrar la reserva en el modelo
                boolean registroExitoso = modelo.registrarReserva(reserva, domicilio);

                if (registroExitoso) {
                    request.setAttribute("mensajeReserva", "Reserva registrada exitosamente.");
                } else {
                    request.setAttribute("error", "Hubo un problema al registrar la reserva.");
                }

            } catch (NumberFormatException e) {
                request.setAttribute("error", "Formato incorrecto en los campos numéricos.");
            } catch (ParseException e) {
                request.setAttribute("error", "Error en el formato de fecha.");
            }

            // Redirigir a la vista
            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaCoordAlta.jsp");
            dispatcher.forward(request, response);

        } else if ("buscarServiciosDisponibles".equals(action)) {
            // Obtener las fechas del request
            String fechaInicioUsuario = request.getParameter("fechaInicioEvento");
            String fechaFinUsuario = request.getParameter("fechaFinEvento");

            // Verificar si las fechas están presentes
            if (fechaInicioUsuario == null || fechaFinUsuario == null || fechaInicioUsuario.isEmpty() || fechaFinUsuario.isEmpty()) {
                request.setAttribute("error", "Las fechas de inicio y fin son obligatorias.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("vistaCoordAlta.jsp");
                dispatcher.forward(request, response);
                return;
            }

            // Formato para parsear las fechas recibidas (con 'T' en el medio)
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

            // Formato de salida para las fechas (sin 'T' y con segundos)
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {

                // Parsear las fechas
                Date fechaInicio = inputFormat.parse(fechaInicioUsuario);
                Date fechaFin = inputFormat.parse(fechaFinUsuario);

                // Formatear las fechas al nuevo formato
                String fechaInicioFormatted = outputFormat.format(fechaInicio);
                String fechaFinFormatted = outputFormat.format(fechaFin);
                // Convertir Date a Instant y luego a LocalDateTime
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime fechaInicioLocal = fechaInicio.toInstant().atZone(zoneId).toLocalDateTime();
                LocalDateTime fechaFinLocal = fechaFin.toInstant().atZone(zoneId).toLocalDateTime();

                // Obtener la fecha y hora actual
                LocalDateTime hoy = LocalDateTime.now();

                if (fechaInicioLocal.isBefore(hoy) || fechaFinLocal.isBefore(hoy)) {
                    request.setAttribute("error", "Las fechas de inicio y fin deben ser mayores a la fecha y hora actual.");
                    request.setAttribute("fechaInicioEvento", fechaInicioUsuario);
                    request.setAttribute("fechaFinEvento", fechaFinUsuario);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("vistaError.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
                // Llamar al modelo para obtener los servicios disponibles con las fechas formateadas
                List<Servicio> serviciosDisponibles = modelo.obtenerServiciosDisponibles(fechaInicioFormatted, fechaFinFormatted);

                request.setAttribute("fechaInicioEvento", fechaInicioUsuario);
                request.setAttribute("fechaFinEvento", fechaFinUsuario);
                request.setAttribute("serviciosDisponibles", serviciosDisponibles);

            } catch (ParseException e) {
                // Manejo de error si las fechas no se pueden parsear
                e.printStackTrace();
                request.setAttribute("error", "Las fechas no tienen el formato correcto.");
            }

            // Redirigir a la vista
            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaCoordAlta.jsp");
            dispatcher.forward(request, response);
        } else if ("registrarCliente".equals(action)) {
            String usuario = request.getParameter("usuario");
            String password = request.getParameter("password");
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String telefono = request.getParameter("telefono");
            String email = request.getParameter("email");

            boolean registrado = modelo.registrarCliente(usuario, password, nombre, apellido, telefono, email);
            if (registrado) {
                request.setAttribute("mensajeCliente", "Cliente registrado correctamente.");
            } else {
                request.setAttribute("mensajeCliente", "Error al registrar el cliente.");
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("vistaCoordAlta.jsp");
            dispatcher.forward(request, response);
        }

    }

}
