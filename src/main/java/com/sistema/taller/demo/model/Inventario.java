package com.sistema.taller.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INVENTARIO")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INVENTARIO")
    private Integer idInventario;

    @ManyToOne
    @JoinColumn(name = "ID_PRODUCTO", referencedColumnName = "ID_PRODUCTO", nullable = false)
    private Producto producto;

    @Column(name = "STOCK_ACTUAL", nullable = false)
    private Integer stockActual;

    @Column(name = "STOCK_MINIMO", nullable = false)
    private Integer stockMinimo;

    @Column(name = "COSTO_UNITARIO", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoUnitario;

    @Column(name = "PRECIO_VENTA", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "ESTADO", nullable = false, length = 1)
    private String estado;

    @CreationTimestamp
    @Column(name = "FECHA_REGISTRO", updatable = false)
    private LocalDateTime fechaRegistro;
}
