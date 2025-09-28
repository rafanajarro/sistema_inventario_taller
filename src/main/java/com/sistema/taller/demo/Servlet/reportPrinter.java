package com.sistema.taller.demo.Servlet;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Controller
public class reportPrinter {
    @Autowired
    private DataSource dataSource;

    // @Autowired
    // private EstudianteService restudianteService;

    @GetMapping("/reportPrinter/{nombre}/{parametros}")
    @ResponseBody
    public void generarReportePDF(HttpServletResponse response, @PathVariable String nombre,
            @PathVariable String parametros) {
        try {
            // Ruta del archivo .jasper en el directorio de recursos
            InputStream jasperStream = getClass().getResourceAsStream("/static/reports/" + nombre + ".jasper");

            if (jasperStream == null) {
                throw new Exception("No se pudo encontrar el archivo de reporte.");
            }

            // Cargar el reporte Jasper
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

            // Parámetros del reporte (puedes agregar más según tu reporte)
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ID_USUARIO", Integer.getInteger(parametros));

            // Obtener la conexión de la base de datos desde el datasource de Spring
            try (Connection conn = dataSource.getConnection()) {

                // Llenar el reporte con los parámetros y la conexión
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

                // Establecer las cabeceras de la respuesta HTTP
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=\"reporte.pdf\"");

                // Exportar el reporte a PDF y escribir en la respuesta
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream())); // Aquí
                                                                                                              // usamos
                                                                                                              // SimpleOutputStreamExporterOutput
                exporter.exportReport();
            }
        } catch (JRException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/compileReport/{nombre}")
    @ResponseBody
    public void getMethodName(@PathVariable String nombre) {
        try {
            InputStream employeeReportStream = getClass().getResourceAsStream("/static/reports/" + nombre + ".jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
            JRSaver.saveObject(jasperReport, nombre + ".jasper");
            System.out.println("EL REPORTE HA SIDO COMPILADO.");
        } catch (Exception ex) {
            System.out.println("EL REPORTE NO SE PUDO COMPILAR. " + ex.getMessage());
            ex.printStackTrace();
        }

    }

}
