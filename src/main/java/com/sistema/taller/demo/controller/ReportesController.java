package com.sistema.taller.demo.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ReportesController {

    @GetMapping("/reporteria")
    public String getMethodName(Model model) {
        model.addAttribute("reporte", "");
        model.addAttribute("reporte2", "");
        //model.addAttribute("carreras", carreraService.obtenerCarrerasActivas());
        return "reportes/reporteria";
    }

    @GetMapping("/reporteria/{reporte}")
    public String reportesFiltrados(@PathVariable String reporte, Model model) {
        model.addAttribute("reporte", reporte);
        // model.addAttribute("carreras", carreraService.obtenerCarrerasActivas());
        return "reportes/reporteria";
    }

    /*@GetMapping(path = "/reporteria/{form}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String obtenerFormularios(HttpServletResponse response, @PathVariable String  form, Model model ){
       
        StringBuilder htmlResponse = new StringBuilder(); 
        try {
            EstudianteEntity est = estudianteService.obtenerPorCarnet(estudiante); 
            if (est != null ){
                List<Pasantia> listadoPasantias = pasantiaService.obtenerPasantiasPorEstudiante(est); 

                if(listadoPasantias != null && !listadoPasantias.isEmpty()){
                    htmlResponse.append("<div class='table-responsive'> <table class='table table-striped'> ");
                    htmlResponse.append("<thead><tr><td>#</td><td>Código de Carrera</td><td>Correlativo</td><td>Año de estudiante</td><td colspan='3'>Generar Formularios</td></tr></thead>"); 
                    for (Pasantia pasantia : listadoPasantias) {
                        htmlResponse.append("<tr>");
                        htmlResponse.append("<td>"+pasantia.getIdPasantia().toString()+"</td>"); 
                        
                        htmlResponse.append("<td>"+pasantia.getCarrera().getCodCarrera().toString()+"</td>"); 

                        String correlativo = ""; 

                        for(int a = 0; a < 3 - pasantia.getCorrelativo().toString().length() ; a++){
                            correlativo+= "0"; 
                        }
                        correlativo += pasantia.getCorrelativo(); 

                        htmlResponse.append("<td>" +correlativo  + "</td>"); 
                        htmlResponse.append("<td>" + pasantia.getAnioEstudiante()+ "</td>"); 
                        htmlResponse.append("<td><button class='btn btn-secondary'  data-toggle=\"modal\" data-target=\"#exampleModalCenter\" onclick=\" mostrarReporte( '"+est.getCarnet()+"', "+pasantia.getIdPasantia()+",  "+pasantia.getCarrera().getIdCarrera()+", 1 )\">Formulario FI01</button></td>"); 
                        htmlResponse.append("<td><button class='btn btn-secondary'  data-toggle=\"modal\" data-target=\"#exampleModalCenter\"   onclick=\" mostrarReporte( '"+est.getCarnet()+"', "+pasantia.getIdPasantia()+",  "+pasantia.getCarrera().getIdCarrera()+", 2)\">Formulario FI02</button></td>"); htmlResponse.append("<td><button class='btn btn-secondary'   data-toggle=\"modal\" data-target=\"#exampleModalCenter\"onclick=\"mostrarReporte('"+est.getCarnet()+"', "+pasantia.getIdPasantia()+",  "+pasantia.getCarrera().getIdCarrera()+",3)\" >Formulario FI03</button></td>");
                        htmlResponse.append("</tr>");
                    }
                    htmlResponse.append("</table></div>"); 
                }
            }
        }catch
        (Exception ex){
            htmlResponse = new StringBuilder(); 
            htmlResponse.append(ex.getStackTrace()); 
        }
        return htmlResponse.toString(); 
    }*/
}
