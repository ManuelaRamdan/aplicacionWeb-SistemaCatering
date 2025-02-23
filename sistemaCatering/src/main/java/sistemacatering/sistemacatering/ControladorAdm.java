/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemacatering.sistemacatering;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ControladorAdm", urlPatterns = {"/ControladorAdm"})
public class ControladorAdm extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) // obtener datos para la vista
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String idAdministrador = (String) session.getAttribute("userId");

        if (idAdministrador != null) {
            Modelo modelo = new Modelo("localhost", "catering"); // Conexión con la base de datos

            // Obtener la acción que el administrador desea realizar (en caso de que haya una acción específica)
            String accion = request.getParameter("accion");

            // Si no hay acción específica, por defecto se carga la vista de menú
            if (accion == null) {
                accion = "menu"; // Acción por defecto
            }

            switch (accion) {
                case "salir":
                    // Redirigir a la vista de menú
                    RequestDispatcher dispatcherMenu = request.getRequestDispatcher("index.jsp");
                    dispatcherMenu.forward(request, response);
                    break;
                case "menu":
                    // Redirigir a la vista de menú
                    dispatcherMenu = request.getRequestDispatcher("vistaAdministradorMenu.jsp");
                    dispatcherMenu.forward(request, response);
                    break;

                case "mostrarAlta":

                    List<Plato> platos = modelo.obtenerPlatosBd();
                    List<Menu> menus = modelo.obtenerMenusConPlatos();

                    request.setAttribute("platosEntrada", platos);
                    request.setAttribute("platosPrincipal", platos);
                    request.setAttribute("menus", menus);
                    request.getRequestDispatcher("vistaAdmAlta.jsp").forward(request, response);
                    break;

                case "mostrarBaja":
                    List<Coordinador> coordinadores = modelo.obtenerCoordinadoresBd();
                    List<Administrador> administradores = modelo.obtenerAdministradoresBd();

                    request.setAttribute("coordinadores", coordinadores);
                    request.setAttribute("administradores", administradores);

                    List<Cliente> clientes = modelo.obtenerClientesBd();
                    request.setAttribute("clientes", clientes);

                    platos = modelo.obtenerPlatosBd();
                    menus = modelo.obtenerMenusConPlatos();

                    request.setAttribute("platos", platos);

                    request.setAttribute("menus", menus);

                    List<Servicio> servicios = modelo.obtenerServiciosConMenusYPlatos();
                    request.setAttribute("servicios", servicios);

                    request.getRequestDispatcher("vistaAdmBaja.jsp").forward(request, response);
                    break;

                case "mostrarModificarAdministrador":

                    administradores = modelo.obtenerAdministradoresBd();

                    request.setAttribute("administradores", administradores);

                    request.getRequestDispatcher("vistaAdmModificarAdm.jsp").forward(request, response);
                    break;

                case "mostrarModificarCoordinador":
                    coordinadores = modelo.obtenerCoordinadoresBd();

                    request.setAttribute("coordinadores", coordinadores);
                    request.getRequestDispatcher("vistaAdmModificarCoordinador.jsp").forward(request, response);
                    break;
                case "mostrarModificarCliente":

                    clientes = modelo.obtenerClientesBd();
                    request.setAttribute("clientes", clientes);

                    request.getRequestDispatcher("vistaAdmModificarCliente.jsp").forward(request, response);
                    break;

                case "mostrarModificarPlato":

                    platos = modelo.obtenerPlatosBd();

                    request.setAttribute("platos", platos);

                    request.getRequestDispatcher("vistaAdmModificarPlato.jsp").forward(request, response);
                    break;

                case "mostrarModificarMenu":

                    menus = modelo.obtenerMenusConPlatos();

                    request.setAttribute("menus", menus);
                    request.getRequestDispatcher("vistaAdmModificarMenu.jsp").forward(request, response);
                    break;

                case "mostrarModificarServicio":

                    servicios = modelo.obtenerServiciosConMenusYPlatos();

                    request.setAttribute("servicios", servicios);

                    request.getRequestDispatcher("vistaAdmModificarServicio.jsp").forward(request, response);
                    break;
                case "mostrar":
                    // Fetch all data
                    administradores = modelo.obtenerAdministradoresBd();
                    clientes = modelo.obtenerClientesBd();
                    coordinadores = modelo.obtenerCoordinadoresBd();
                    platos = modelo.obtenerPlatosBd();
                    menus = modelo.obtenerMenusConPlatos();  // Método que devuelve los menús con sus platos.

                    servicios = modelo.obtenerServiciosConMenusYPlatos();

                    // Set the data as request attributes
                    request.setAttribute("administradores", administradores);
                    request.setAttribute("clientes", clientes);
                    request.setAttribute("coordinadores", coordinadores);
                    request.setAttribute("platos", platos);
                    request.setAttribute("menus", menus);
                    request.setAttribute("servicios", servicios);

                    List<Reserva> reservas = modelo.obtenerReservaBd();
                    request.setAttribute("reservas", reservas);

                    // Forward to the JSP page
                    request.getRequestDispatcher("vistaAdmMostrar.jsp").forward(request, response);
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) // enviar datos necesarios
            throws ServletException, IOException {

        String action = request.getParameter("action");

        /* toma lo que le envio el formulario */
        Modelo modelo = new Modelo("localhost", "catering");

        switch (action) {
            case "registrarCoordinador":
                String usuario = request.getParameter("usuario");
                String password = request.getParameter("password");
                boolean registrado = false;
                boolean personaRepetida = modelo.verificarPersona(usuario, password);
                if (personaRepetida) {
                    registrado = modelo.registrarCoordinador(usuario, password);

                    /* dependiendo si se pudo registrar o no , muestra un mensaje */
                    if (registrado) {
                        request.setAttribute("mensajeCoordinador", "Coordinador registrado correctamente.");
                    } else {
                        request.setAttribute("mensajeCoordinador", "Error al registrar el coordinador.");
                    }
                } else {
                    request.setAttribute("mensajeCoordinador", "Error al registrar el coordinador, el usuario y contraseña ya existe.");

                }

                response.sendRedirect("ControladorAdm?accion=mostrarAlta");
                break;

            case "registrarAdministrador":

                usuario = request.getParameter("usuario");
                password = request.getParameter("password");
                personaRepetida = modelo.verificarPersona(usuario, password);
                if (personaRepetida) {
                    registrado = modelo.registrarAdministrador(usuario, password);

                    if (registrado) {
                        request.setAttribute("mensajeAdm", "Administrador registrado correctamente.");
                    } else {
                        request.setAttribute("mensajeAdm", "Error al registrar el Administrador.");
                    }

                } else {
                    request.setAttribute("mensajeAdm", "Error al registrar el administrador, el usuario y contraseña ya existe.");

                }

                response.sendRedirect("ControladorAdm?accion=mostrarAlta");

                break;

            case "registrarCliente":
                usuario = request.getParameter("usuario");
                password = request.getParameter("password");

                personaRepetida = modelo.verificarPersona(usuario, password);
                if (personaRepetida) {

                    String nombre = request.getParameter("nombre");
                    String apellido = request.getParameter("apellido");
                    String telefono = request.getParameter("telefono");
                    String email = request.getParameter("email");

                    boolean telefonoRepetido = modelo.verificarTelefono(telefono);
                    boolean emailRepetido = modelo.verificarEmail(email);

                    if (emailRepetido || telefonoRepetido) {
                        registrado = modelo.registrarCliente(usuario, password, nombre, apellido, telefono, email);
                        if (registrado) {
                            request.setAttribute("mensajeCliente", "Cliente registrado correctamente.");
                        } else {
                            request.setAttribute("mensajeCliente", "Error al registrar el cliente.");
                        }
                    } else {
                        request.setAttribute("mensajeCliente", "Error al registrar el cliente ya que existe un cliente con ese telefono o email.");

                    }

                } else {
                    request.setAttribute("mensajeCliente", "Error al registrar el cliente, el usuario y contraseña ya existe.");

                }

                response.sendRedirect("ControladorAdm?accion=mostrarAlta");

                break;

            case "registrarPlato":

                String nombrePlato = request.getParameter("nombrePlato");
                boolean platoRepetido = modelo.verificarPlato(nombrePlato);

                if (platoRepetido) {
                    registrado = modelo.registrarPlato(nombrePlato);

                    if (registrado) {
                        request.setAttribute("mensajePlato", "Plato registrado correctamente.");
                    } else {
                        request.setAttribute("mensajePlato", "Error al registrar el plato.");
                    }

                } else {
                    request.setAttribute("mensajePlato", "Error al registrar el plato ya que existe un plato con ese nombre.");

                }

                response.sendRedirect("ControladorAdm?accion=mostrarAlta");

                break;

            case "registrarMenu":
                String nombreMenu = request.getParameter("nombreMenu");
                int precio = Integer.parseInt(request.getParameter("precio"));
                // Obtener IDs de platos de entrada seleccionados
                String[] platosEntradaIds = request.getParameterValues("platoEntrada[]");
                List<Integer> platosEntradaSeleccionados = modelo.obtenerIdsSeleccionados(platosEntradaIds);

                // Obtener IDs de platos principales seleccionados
                String[] platosPrincipalIds = request.getParameterValues("platoPrincipal[]");
                List<Integer> platosPrincipalSeleccionados = modelo.obtenerIdsSeleccionados(platosPrincipalIds);
                boolean menuRepetido = modelo.verificarMenu(nombreMenu);

                if (menuRepetido) {
                    registrado = modelo.registrarMenu(nombreMenu, platosEntradaSeleccionados, platosPrincipalSeleccionados, precio);

                    if (registrado) {
                        request.setAttribute("mensajeMenu", "Menu registrado correctamente.");
                    } else {
                        request.setAttribute("mensajeMenu", "Error al registrar el Menu.");
                    }
                } else {
                    request.setAttribute("mensajeMenu", "Error al registrar el Menu ya que existe un menu con ese nombre.");

                }

                response.sendRedirect("ControladorAdm?accion=mostrarAlta");

                break;
            case "registrarServicio":
                String nombreServicio = request.getParameter("nombreServicio");

                // Obtener IDs de menús seleccionados
                String[] menuIds = request.getParameterValues("menus[]");
                List<Integer> menusSeleccionados = modelo.obtenerIdsSeleccionados(menuIds);

                boolean servicioRepetido = modelo.verificarServicio(nombreServicio);

                if (servicioRepetido) {
                    registrado = modelo.registrarServicio(nombreServicio, menusSeleccionados);

                    if (registrado) {
                        request.setAttribute("mensajeServicio", "Servicio registrado correctamente.");
                    } else {
                        request.setAttribute("mensajeServicio", "Error al registrar el Servicio.");
                    }
                } else {
                    request.setAttribute("mensajeServicio", "Error al registrar el Servicio ya que existe un servicio con ese nombre.");

                }

                response.sendRedirect("ControladorAdm?accion=mostrarAlta");

                break;
            case "eliminarCoordinador":
                int idCoordinador = Integer.parseInt(request.getParameter("idCoordinador"));

                boolean eliminado = modelo.eliminarCoordinador(idCoordinador);

                if (eliminado) {
                    request.setAttribute("mensajeBajaCoordinador", "Coordinador dado de baja exitosamente.");
                } else {
                    request.setAttribute("mensajeBajaCoordinador", "Error, no se puso eliminar");
                }

                response.sendRedirect("ControladorAdm?accion=mostrarBaja");

                break;
            case "eliminarAdministrador":
                int idAdministrador = Integer.parseInt(request.getParameter("idAdministrador"));

                eliminado = modelo.eliminarAdministrador(idAdministrador);

                if (eliminado) {
                    request.setAttribute("mensajeBajaAdministrador", "Administrador dado de baja exitosamente.");
                } else {
                    request.setAttribute("mensajeBajaAdministrador", "Error, no se puso eliminar");
                }

                response.sendRedirect("ControladorAdm?accion=mostrarBaja");

                break;

            case "eliminarCliente":
                int idCliente = Integer.parseInt(request.getParameter("idCliente"));
                String persona_id = request.getParameter("persona_id");
                eliminado = modelo.eliminarCliente(idCliente);

                Cliente cliente = modelo.obtenerClientePorId(persona_id);

                List<Reserva> reservas = modelo.obtenerReservasPorCliente(persona_id);
                boolean eliminarReserva = false;
                for (Reserva r : reservas) {
                    eliminarReserva = modelo.eliminarReserva(r.getCodReserva());
                    if (!eliminarReserva) {
                        request.setAttribute("mensajeBajaCliente", "Error. no se pudo eliminar la reserva correctamente");
                    } 
                }

                if (eliminado) {
                    request.setAttribute("mensajeBajaCliente", "Cliente dado de baja exitosamente.");
                } else {
                    request.setAttribute("mensajeBajaCliente", "Error, no se puso eliminar");
                }

                response.sendRedirect("ControladorAdm?accion=mostrarBaja");

                break;

            case "eliminarPlato":
                int idPlato = Integer.parseInt(request.getParameter("idPlato"));
                eliminado = modelo.eliminarPlato(idPlato);

                if (eliminado) {
                    request.setAttribute("mensajeBajaPlato", "Plato dado de baja exitosamente.");
                } else {
                    request.setAttribute("mensajeBajaPlato", "Error, no se pudo eliminar");
                }

                response.sendRedirect("ControladorAdm?accion=mostrarBaja");

                break;

            case "eliminarMenu":
                int idMenu = Integer.parseInt(request.getParameter("idMenu"));
                eliminado = modelo.eliminarMenu(idMenu);

                if (eliminado) {
                    request.setAttribute("mensajeBajaMenu", "Menú dado de baja exitosamente.");
                } else {
                    request.setAttribute("mensajeBajaMenu", "Error, no se pudo eliminar");
                }

                response.sendRedirect("ControladorAdm?accion=mostrarBaja");

                break;

            case "eliminarServicio":
                int idServicio = Integer.parseInt(request.getParameter("idServicio"));
                eliminado = modelo.eliminarServicio(idServicio);

                if (eliminado) {
                    request.setAttribute("mensajeBajaServicio", "Servicio dado de baja exitosamente.");
                } else {
                    request.setAttribute("mensajeBajaServicio", "Error, no se pudo eliminar");
                }

                response.sendRedirect("ControladorAdm?accion=mostrarBaja");

                break;

            case "modificarCliente":
                String personaId = request.getParameter("persona_id");  // Obtener persona_id
                cliente = modelo.obtenerClientePorId(personaId);
                request.setAttribute("cliente", cliente);  // Establecer las reservas en el request
                RequestDispatcher dispatcher = request.getRequestDispatcher("vistaAdmModificarCliente.jsp");
                dispatcher.forward(request, response);

                break;

            case "actualizarCliente":

                try {
                    idCliente = Integer.parseInt(request.getParameter("idCliente"));
                    usuario = request.getParameter("usuario");
                    password = request.getParameter("password");

                    personaRepetida = modelo.verificarPersona(usuario, password);
                    if (personaRepetida) {
                        String nombre = request.getParameter("nombre");
                        String apellido = request.getParameter("apellido");
                        String telefono = request.getParameter("telefono");
                        String email = request.getParameter("email");

                        boolean telefonoRepetido = modelo.verificarTelefono(telefono);
                        boolean emailRepetido = modelo.verificarEmail(email);

                        if (emailRepetido || telefonoRepetido) {
                            boolean actualizado = modelo.actualizarCliente(idCliente, nombre, apellido, telefono, email, usuario, password);

                            if (actualizado) {
                                request.setAttribute("mensajeActualizarCliente", "Cliente actualizado exitosamente.");
                            } else {
                                request.setAttribute("mensajeActualizarCliente", "Error, no se pudo actualizar.");
                            }
                        } else {
                            request.setAttribute("mensajeActualizarCliente", "Error, no se pudo actualizar el cliente ya se existe un cliente con ese telefono o email.");

                        }
                    } else {
                        request.setAttribute("mensajeActualizarCliente", "Error, no se pudo actualizar el cliente ya se existe un cliente con ese usuario y contraseña.");

                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("mensajeActualizarCliente", "Error: ID de cliente no válido.");
                }

                response.sendRedirect("ControladorAdm?accion=mostrarModificarCliente");

                break;

            case "modificarAdministrador":
                idAdministrador = Integer.parseInt(request.getParameter("idAdministrador"));
                Administrador administrador = modelo.obtenerAdministradorPorId(idAdministrador);  // Obtener reservas usando persona_id
                request.setAttribute("administrador", administrador);  // Establecer las reservas en el request
                dispatcher = request.getRequestDispatcher("vistaAdmModificarAdm.jsp");
                dispatcher.forward(request, response);

                break;

            case "modificarCoordinador":
                idCoordinador = Integer.parseInt(request.getParameter("idCoordinador"));

                Coordinador coordinador = modelo.obtenerCoordinadorPorId(idCoordinador);  // Obtener coordinador usando persona_id

                request.setAttribute("coordinador", coordinador);  // Establecer los datos del coordinador en el request
                dispatcher = request.getRequestDispatcher("vistaAdmModificarCoordinador.jsp");
                dispatcher.forward(request, response);

                break;

            case "modificarPlato":
                idPlato = Integer.parseInt(request.getParameter("idPlato"));
                Plato plato = modelo.obtenerPlatoPorId(idPlato);  // Obtener reservas usando persona_id
                request.setAttribute("plato", plato);  // Establecer las reservas en el request
                dispatcher = request.getRequestDispatcher("vistaAdmModificarPlato.jsp");
                dispatcher.forward(request, response);

                break;

            case "actualizarAdm":

                try {
                    idAdministrador = Integer.parseInt(request.getParameter("idAdministrador"));
                    usuario = request.getParameter("usuario");
                    password = request.getParameter("password");

                    personaRepetida = modelo.verificarPersona(usuario, password);
                    if (personaRepetida) {
                        boolean actualizado = modelo.actualizarAdministrador(idAdministrador, usuario, password);

                        if (actualizado) {
                            request.setAttribute("mensajeActualizarAdministrador", "Administrador actualizado exitosamente.");
                        } else {
                            request.setAttribute("mensajeActualizarAdministrador", "Error, no se pudo actualizar.");
                        }
                    } else {
                        request.setAttribute("mensajeActualizarAdministrador", "Error, no se pudo actualizar ya que el nombre de usuario ya existe.");

                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("mensajeActualizarAdministrador", "Error: ID de cliente no válido.");
                }

                response.sendRedirect("ControladorAdm?accion=mostrarModificarAdministrador");

                break;

            case "actualizarCoordinador":
                try {
                    idCoordinador = Integer.parseInt(request.getParameter("idCoordinador"));
                    usuario = request.getParameter("usuario");
                    password = request.getParameter("password");

                    personaRepetida = modelo.verificarPersona(usuario, password);

                    if (personaRepetida) {
                        boolean actualizado = modelo.actualizarCoordinador(idCoordinador, usuario, password);

                        if (actualizado) {
                            request.setAttribute("mensajeActualizarCoordinador", "Coordinador actualizado exitosamente.");
                        } else {
                            request.setAttribute("mensajeActualizarCoordinador", "Error, no se pudo actualizar.");
                        }
                    } else {
                        request.setAttribute("mensajeActualizarCoordinador", "Error, no se pudo actualizar ya que ya existe un usuario con ese nombre.");

                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("mensajeActualizarCoordinador", "Error: ID de Coordinador no válido.");
                }

                response.sendRedirect("ControladorAdm?accion=mostrarModificarCoordinador");

                break;

            case "actualizarPlato":

                try {
                    idPlato = Integer.parseInt(request.getParameter("idPlato"));
                    String nombre = request.getParameter("nombre");

                    platoRepetido = modelo.verificarPlato(nombre);
                    if (platoRepetido) {
                        boolean actualizado = modelo.actualizarPlato(idPlato, nombre);

                        if (actualizado) {
                            request.setAttribute("mensajeActualizarPlato", "Plato actualizado exitosamente.");
                        } else {
                            request.setAttribute("mensajeActualizarPlato", "Error, no se pudo actualizar.");
                        }
                    } else {
                        request.setAttribute("mensajeActualizarPlato", "Error, no se pudo actualizar ya que existe un plato con ese nombre.");

                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("mensajeActualizarPlato", "Error: ID de Plato no válido.");
                }

                response.sendRedirect("ControladorAdm?accion=mostrarModificarPlato");

                break;

            case "modificarMenu":
                idMenu = Integer.parseInt(request.getParameter("idMenu"));

                List<Plato> platosEntrada = modelo.obtenerPlatosNoSeleccionados(idMenu, "Entrada");
                List<Plato> platosPrincipal = modelo.obtenerPlatosNoSeleccionados(idMenu, "Principal");

                request.setAttribute("platosEntrada", platosEntrada);
                request.setAttribute("platosPrincipal", platosPrincipal);
                Menu menu = modelo.obtenerMenuConId(idMenu);  // Obtener reservas usando persona_id
                request.setAttribute("menu", menu);  // Establecer las reservas en el request
                dispatcher = request.getRequestDispatcher("vistaAdmModificarMenu.jsp");
                dispatcher.forward(request, response);

                break;

            case "actualizarMenuNombrePrecio":
                try {
                    // Obtén los parámetros del formulario
                    idMenu = Integer.parseInt(request.getParameter("idMenu"));
                    String nombre = request.getParameter("nombre");
                    precio = Integer.parseInt(request.getParameter("precio"));

                    menuRepetido = modelo.verificarMenu(nombre);
                    if (menuRepetido) {
                        boolean actualizado = modelo.actualizarMenuNombrePrecio(idMenu, nombre, precio);

                        if (actualizado) {
                            request.setAttribute("mensajeActualizarMenu", "Menú actualizado exitosamente.");
                        } else {
                            request.setAttribute("mensajeActualizarMenu", "Error, no se pudo actualizar el menú.");
                        }
                    } else {
                        request.setAttribute("mensajeActualizarMenu", "Error, no se pudo actualizar el menú ya que existe un menú con ese nombre.");

                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("mensajeActualizarMenu", "Error: ID de menú o precio no válido.");
                }

                // Redirige para mostrar el resultado
                response.sendRedirect("ControladorAdm?accion=mostrarModificarMenu");
                break;
            case "agregarPlatosMenu":
                try {
                    // Obtén los parámetros del formulario
                    idMenu = Integer.parseInt(request.getParameter("idMenu"));
                    platosEntradaIds = request.getParameterValues("platoEntrada[]");
                    platosEntradaSeleccionados = modelo.obtenerIdsSeleccionados(platosEntradaIds);
                    for (Integer platoId : platosEntradaSeleccionados) {
                        System.out.println("Plato seleccionado ID: " + platoId);
                    }
                    // Obtener IDs de platos principales seleccionados
                    platosPrincipalIds = request.getParameterValues("platoPrincipal[]");
                    platosPrincipalSeleccionados = modelo.obtenerIdsSeleccionados(platosPrincipalIds);
                    for (Integer platoId : platosPrincipalSeleccionados) {
                        System.out.println("Plato seleccionado ID: " + platoId);
                    }
                    boolean actualizado = modelo.agregarPlatosMenu(idMenu, platosEntradaSeleccionados, platosPrincipalSeleccionados);

                    if (actualizado) {
                        request.setAttribute("mensajeActualizarMenu", "Menú actualizado exitosamente.");
                    } else {
                        request.setAttribute("mensajeActualizarMenu", "Error, no se pudo actualizar el menú.");
                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("mensajeActualizarMenu", "Error: ID de menú o precio no válido.");
                }

                // Redirige para mostrar el resultado
                response.sendRedirect("ControladorAdm?accion=mostrarModificarMenu");
                break;
            case "eliminarPlatoMenu":
                try {
                    // Obtén los parámetros del formulario
                    idMenu = Integer.parseInt(request.getParameter("idMenu"));

                    // Obtén los platos de entrada seleccionados para eliminar
                    String[] platosEntradaEliminarIds = request.getParameterValues("platoEntradaEliminar[]");
                    List<Integer> platosEntradaEliminar = modelo.obtenerIdsSeleccionados(platosEntradaEliminarIds);

                    // Obtén los platos principales seleccionados para eliminar
                    String[] platosPrincipalEliminarIds = request.getParameterValues("platoPrincipalEliminar[]");
                    List<Integer> platosPrincipalEliminar = modelo.obtenerIdsSeleccionados(platosPrincipalEliminarIds);
                    menu = modelo.obtenerMenuConId(idMenu);
                    if (platosPrincipalEliminar != null && platosPrincipalEliminar.size() >= menu.getPlatosPrincipal().size()) {
                        // Si hay más platos seleccionados para eliminar que los disponibles en el menú
                        request.setAttribute("mensajeActualizarMenu", "No se puede eliminar todos los platos principales. Debe haber al menos uno.");
                        // Redirige para mostrar el resultado
                        dispatcher = request.getRequestDispatcher("vistaAdmModificarMenu.jsp");
                        dispatcher.forward(request, response);
                        return;
                    }
                    // Llama al método del modelo para eliminar los platos seleccionados
                    eliminado = modelo.eliminarPlatosMenu(idMenu, platosEntradaEliminar, platosPrincipalEliminar);

                    if (eliminado) {
                        request.setAttribute("mensajeActualizarMenu", "Platos eliminados exitosamente del menú.");
                    } else {
                        request.setAttribute("mensajeActualizarMenu", "Error, no se pudo eliminar los platos del menú.");
                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("mensajeActualizarMenu", "Error: ID de menú no válido.");
                }

                // Redirige para mostrar el resultado
                response.sendRedirect("ControladorAdm?accion=mostrarModificarMenu");
                break;

            case "modificarServicio":
                idServicio = Integer.parseInt(request.getParameter("idServicio"));
                List<Menu> menus = modelo.obtenerMenuNoSeleccionado(idServicio);
                Servicio servicio = modelo.obtenerServicioConId(idServicio);  // Obtener reservas usando persona_id
                request.setAttribute("servicio", servicio);
                request.setAttribute("menus", menus);
                dispatcher = request.getRequestDispatcher("vistaAdmModificarServicio.jsp");
                dispatcher.forward(request, response);

                break;
            case "actualizarServicioMenu":
                try {
                    // Obtén los parámetros del formulario
                    idServicio = Integer.parseInt(request.getParameter("idServicio"));
                    String nombre = request.getParameter("nombre");

                    servicioRepetido = modelo.verificarServicio(nombre);
                    if (servicioRepetido) {
                        boolean actualizado = modelo.actualizarServicioMenu(idServicio, nombre);

                        if (actualizado) {
                            request.setAttribute("mensajeActualizarServicio", "Servicio actualizado exitosamente.");
                        } else {
                            request.setAttribute("mensajeActualizarServicio", "Error, no se pudo actualizar el servicio.");
                        }
                    } else {
                        request.setAttribute("mensajeActualizarServicio", "Error, no se pudo actualizar el servicio ya que existe un servicio con ese nombre.");

                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("mensajeActualizarServicio", "Error: ID de Servicio no válido.");
                }

                // Redirige para mostrar el resultado
                response.sendRedirect("ControladorAdm?accion=mostrarModificarServicio");
                break;

            case "agregarMenuServicio":
                try {
                    // Obtén los parámetros del formulario
                    idServicio = Integer.parseInt(request.getParameter("idServicio"));
                    menuIds = request.getParameterValues("menus[]");
                    menusSeleccionados = modelo.obtenerIdsSeleccionados(menuIds);

                    boolean actualizado = modelo.agregarMenuServicio(idServicio, menusSeleccionados);

                    if (actualizado) {
                        request.setAttribute("mensajeActualizarServicio", "Servicio actualizado exitosamente.");
                    } else {
                        request.setAttribute("mensajeActualizarServicio", "Error, no se pudo actualizar el servicio.");
                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("mensajeActualizarServicio", "Error: ID de servicio no válido.");
                }

                // Redirige para mostrar el resultado
                response.sendRedirect("ControladorAdm?accion=mostrarModificarServicio");
                break;
            case "eliminarMenuServicio":
                try {
                    // Obtén los parámetros del formulario
                    idServicio = Integer.parseInt(request.getParameter("idServicio"));

                    // Obtén los platos de entrada seleccionados para eliminar
                    menuIds = request.getParameterValues("menusEliminar[]");
                    menusSeleccionados = modelo.obtenerIdsSeleccionados(menuIds);

                    servicio = modelo.obtenerServicioConId(idServicio);
                    if (menusSeleccionados != null && menusSeleccionados.size() >= servicio.getMenus().size()) {
                        // Si hay más platos seleccionados para eliminar que los disponibles en el menú
                        request.setAttribute("mensajeActualizarServicio", "No se puede eliminar todos los menus. Debe haber al menos uno.");
                        // Redirige para mostrar el resultado
                        dispatcher = request.getRequestDispatcher("vistaAdmModificarServicio.jsp");
                        dispatcher.forward(request, response);
                        return;
                    }
                    // Llama al método del modelo para eliminar los platos seleccionados
                    eliminado = modelo.eliminarMenuServicio(idServicio, menusSeleccionados);

                    if (eliminado) {
                        request.setAttribute("mensajeActualizarServicio", "Menus seleccionados exitosamente");
                    } else {
                        request.setAttribute("mensajeActualizarServicio", "Error, no se pudo eliminar los menus del serviccio.");
                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("mensajeActualizarServicio", "Error: ID de servicio no válido.");
                }

                // Redirige para mostrar el resultado
                response.sendRedirect("ControladorAdm?accion=mostrarModificarServicio");
                break;
            default:
                // Si la acción no es reconocida, redirige a una página de error
                request.setAttribute("mensajeError", "Acción no válida");
                request.getRequestDispatcher("vistaError.jsp").forward(request, response);
                break;
        }

    }
}
