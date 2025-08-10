package com.sistema.taller.demo.model;

import java.time.LocalDateTime;

public interface UsuarioActividad {
    String getNombre();

    Integer getCantidad();

    LocalDateTime getFechaMovimiento();

    String getTipoMovimiento();
}
