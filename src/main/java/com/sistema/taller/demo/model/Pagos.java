package com.sistema.taller.demo.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "PAGOS")
public class Pagos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PAGO")
    private Integer idPago;

    @ManyToOne
    @JoinColumn(name = "ID_SERVICIO", referencedColumnName = "ID_SERVICIO", nullable = false)
    private Servicio servicio;

    @Column(name = "MONTO_PAGADO", nullable = false)
    private double montoPagado;

    @UpdateTimestamp
    @Column(name = "FECHA_PAGO", nullable = true)
    private LocalDateTime fechaPago;

    @Column(name = "METODO_PAGO", nullable = false)
    private String metodoPago;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO", nullable = false)
    private Usuario usuario;
}
