package com.sistema.taller.demo.model;

import java.time.LocalDateTime;
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
@Table(name = "CUENTAS_PENDIENTES")
public class CuentasPendientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CUENTA")
    private Integer idCuenta;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID_CLIENTE", nullable = false)
    private Clientes idCliente;

    @Column(name = "MONTO_TOTAL", nullable = false)
    private Double montoTotal;

    @Column(name = "MONTO_PAGADO", nullable = false)
    private Double montoPagado;

    @UpdateTimestamp
    @Column(name = "ULTIMA_FECHA_PAGO", nullable = false)
    private LocalDateTime ultimaFechaPago;

    @Column(name = "ESTADO", nullable = false)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "ID_SERVICIO", referencedColumnName = "ID_SERVICIO", nullable = false)
    private Servicios idServicio;

    @Column(name = "SALDO_PENDIENTE", nullable = false)
    private Double saldoPendiente;
}
