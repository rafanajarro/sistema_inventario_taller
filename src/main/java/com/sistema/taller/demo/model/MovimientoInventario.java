package com.sistema.taller.demo.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MOVIMIENTOS_INVENTARIO")
public class MovimientoInventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MOVIMIENTO")
    private Integer idMovimiento;

    @Column(name = "TIPO_MOVIMIENTO", length = 25, nullable = false)
    private String tipoMovimiento;

    @Column(name = "CANTIDAD", length = 1, nullable = false)
    private Integer cantidad;

    @CreationTimestamp
    @Column(name = "FECHA_MOVIMIENTO", nullable = false)
    private LocalDateTime fechaMovimiento;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO", nullable = false)
    private Usuario idUsuario;

    @ManyToOne
    @JoinColumn(name = "ID_PRODUCTO", referencedColumnName = "ID_PRODUCTO", nullable = false)
    private Producto idProducto;
}
