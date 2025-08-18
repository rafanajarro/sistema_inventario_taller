package com.sistema.taller.demo.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sistema.taller.demo.model.RolUsuario;
import com.sistema.taller.demo.model.Usuario;
import com.sistema.taller.demo.model.UsuarioActividad;
import com.sistema.taller.demo.service.RolUsuarioService;
import com.sistema.taller.demo.service.UsuarioService;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RolUsuarioService rolUsuarioService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Environment environment;
    private static Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    // Listar usuarios
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.obtenerTodo();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/listado_usuarios";
    }

    // Mostrar formulario para crear un nuevo usuarios
    @GetMapping("/usuarios/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model) {
        List<RolUsuario> rolesActivos = rolUsuarioService.obtenerRolesActivos();
        model.addAttribute("roles", rolesActivos);
        model.addAttribute("usuarios", new Usuario());
        return "usuarios/crear_editar_usuario";
    }

    // Guardar usuarios
    @PostMapping("/usuarios")
    public String guardarUsuario(@ModelAttribute Usuario usuarios, RedirectAttributes redirectAttributes,
            Model model, @RequestParam(value = "fotoUsuario", required = false) MultipartFile fotoUsuario) {
        try {
            // Hashear la contraseña solo al crear el usuarios
            if (usuarios.getIdUsuario() == null) {
                String hashedPassword = passwordEncoder.encode(usuarios.getPassword());
                usuarios.setPassword(hashedPassword);
                usuarios.setEstado("A");

                HashMap<String, String> rutas = guardarLogo(fotoUsuario);
                usuarios.setFoto(rutas != null && rutas.containsKey("rutaFoto") ? rutas.get("rutaFoto") : "");
            } else {
                usuarios.setFechaMod(LocalDateTime.now());
            }
            usuarioService.guardar(usuarios);
            redirectAttributes.addFlashAttribute("mensaje", "El usuarios se guardó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/usuarios";
        } catch (Exception e) {
            model.addAttribute("tipoMensaje", "error");
            model.addAttribute("mensaje", "Ocurrió un error al guardar el usuarios." + e.getMessage());
            return "usuarios/crear_editar_usuario";
        }

    }

    public HashMap<String, String> guardarLogo(MultipartFile logo) {
        HashMap<String, String> rutas = new HashMap<>();
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MMddyyyyHHmmss");
            if (logo != null && !logo.isEmpty()) {
                String contentTypeFU = logo.getContentType();

                @SuppressWarnings("null")
                String[] contentTypeFUS = contentTypeFU.split("/");
                String nameFileFU = String.format("%s_FU.%s", format.format(LocalDateTime.now()), contentTypeFUS[1]);
                String destinyRouteFU = environment.getProperty("route.destiny.files") + File.separator + "usuarios"
                        + File.separator
                        + nameFileFU;

                logo.transferTo(new File(destinyRouteFU));
                rutas.put("rutaFoto", nameFileFU);
            }
        } catch (Exception e) {
            logger.error("Error al procesar archivos: {}", e.getMessage());
        }
        return rutas;
    }

    // Obtener imagenes de la carpeta
    @Value("${route.destiny.files}")
    private String uploadDir;

    @GetMapping("/empleados/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir + "/empleados").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("Archivo no encontrado: " + filename);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Mostrar formulario para editar un usuarios
    @GetMapping("/usuarios/editar/{id}")
    public String mostrarFormularioModificarUsuario(@PathVariable Integer id, Model model) {
        Usuario usuarios = usuarioService.obtenerPorID(id);
        if (usuarios == null) {
            return "redirect:/usuarios";
        }

        List<RolUsuario> rolesActivos = rolUsuarioService.obtenerRolesActivos();
        model.addAttribute("roles", rolesActivos);
        model.addAttribute("usuarios", usuarios);
        return "usuarios/crear_editar_usuario";
    }

    // Perfil
    @GetMapping("/usuarios/perfil/{id}")
    public String mostrarPerfilUsuario(@PathVariable Integer id, Model model) {
        Usuario usuarios = usuarioService.obtenerPorID(id);
        if (usuarios == null) {
            return "redirect:/usuarios";
        }
        List<UsuarioActividad> actividad = usuarioService
                .obtenerUltimaActividadUsuarioActividads(usuarios.getUsername());
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("actividad", actividad);
        return "usuarios/perfil_usuarios";
    }

    // Actualizar usuarios
    @PostMapping("/usuarios/editar/{id}")
    public String actualizarUsuario(@PathVariable Integer id, @ModelAttribute Usuario usuarios,
            RedirectAttributes redirectAttributes, Model model,
            @RequestParam(value = "fotoUsuario", required = false) MultipartFile fotoUsuario) {
        try {
            Usuario usuarioExistente = usuarioService.obtenerPorID(id);
            if (usuarios == null) {
                return "redirect:/usuarios";
            }

            usuarioExistente.setNombres(usuarios.getNombres());
            usuarioExistente.setApellidos(usuarios.getApellidos());
            usuarioExistente.setCorreo(usuarios.getCorreo());
            usuarioExistente.setUsername(usuarios.getUsername());
            usuarioExistente.setIdRol(usuarios.getIdRol());
            usuarioExistente.setDui(usuarios.getDui());
            usuarioExistente.setTelefono(usuarios.getTelefono());

            // LOGO
            HashMap<String, String> rutas = new HashMap<>();
            if (fotoUsuario != null && !fotoUsuario.isEmpty()) {
                // Eliminar archivo antiguo de la foto si existía
                if (usuarioExistente.getFoto() != null
                        && !usuarioExistente.getFoto().isEmpty()) {
                    eliminarArchivoAntiguo(uploadDir + "/empleados/" + usuarioExistente.getFoto());
                }
                // Guardar nueva foto
                rutas.putAll(guardarLogo(fotoUsuario));
            }
            if (rutas.containsKey("rutaFoto")) {
                usuarioExistente.setFoto(rutas.get("rutaFoto"));
            }

            // Hashea la contraseña solo si el campo no está vacío
            if (usuarios.getPassword() != null && !usuarios.getPassword().isEmpty()) {
                usuarioExistente.setPassword(passwordEncoder.encode(usuarios.getPassword()));
            }

            usuarioService.guardar(usuarioExistente);
            redirectAttributes.addFlashAttribute("mensaje", "El usuarios se actualizó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/usuarios";
        } catch (Exception e) {
            model.addAttribute("tipoMensaje", "error");
            model.addAttribute("mensaje", "Ocurrió un error al actualizar el usuarios." + e.getMessage());
            return "usuarios/crear_editar_usuario";
        }
    }

    // Eliminar un usuarios
    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "El usuarios se eliminó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error al eliminar el usuarios.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/usuarios";
    }

    // Eliminar archivos antiguos
    public void eliminarArchivoAntiguo(String rutaArchivo) {
        try {
            Path filePath = Paths.get(rutaArchivo);
            logger.info("RUTA" + filePath);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                logger.info("Archivo eliminado");
            }
        } catch (Exception e) {
            logger.error("Error al eliminar el archivo antiguo: {}", e.getMessage());
        }
    }
}
