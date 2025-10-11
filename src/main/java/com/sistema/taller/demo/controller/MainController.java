package com.sistema.taller.demo.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sistema.taller.demo.model.Usuario;
import com.sistema.taller.demo.service.UsuarioService;

@Controller
public class MainController {

    @Autowired
    private UsuarioService usuarioService;

    @RequestMapping(method = RequestMethod.GET, path = { "/", "", "/inicio", "/index" })
    public String obtenerPaginaPrincipal(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "Anónimo";
        Usuario usuario = usuarioService.obtenerPorUsername(username);
        model.addAttribute("usuario", usuario);
        return "index.html";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String requestMethodName(Model model) {
        return "login.html";
    }

    // Obtener imagenes de la carpeta
    @Value("${route.destiny.files}")
    private String uploadDir;

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                // Imagen por defecto
                Path defaultImagePath = Paths.get(uploadDir).resolve("11244147.jpg").normalize();
                Resource defaultResource = new UrlResource(defaultImagePath.toUri());

                if (defaultResource.exists() && defaultResource.isReadable()) {
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_JPEG)
                            .body(defaultResource);
                } else {
                    return ResponseEntity.notFound().build();
                }
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
