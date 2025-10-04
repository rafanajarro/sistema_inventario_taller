package com.sistema.taller.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "SERVICIOS")
@Data
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SERVICIO")
    private Integer idServicio;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE", nullable = false)
    private Clientes cliente;

    @Column(name = "NOMBRE_SERVICIO", nullable = false, length = 100)
    private String nombreServicio;

    @Column(name = "DESCRIPCION", length = 255)
    private String descripcion;

    @Column(name = "PRECIO_BASE", nullable = false)
    private Double precioBase;

    @Column(name = "FECHA_SERVICIO", nullable = false)
    private LocalDate fechaServicio;

    @Column(name = "ESTADO_PAGO", nullable = false, length = 20)
    private String estadoPago;
}
