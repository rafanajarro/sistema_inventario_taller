package com.sistema.taller.demo.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "ROL_USUARIO")
public class RolUsuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROL")
    private Integer idRol;

    @Column(name = "DESCRIPCION", length = 25, nullable = false)
    private String descripcion;

    @Column(name = "ESTADO", length = 1, nullable = false)
    private String estado;

    @CreationTimestamp
    @Column(name = "FECHA_CREA", nullable = false)
    private LocalDateTime fechaCrea;

    @UpdateTimestamp
    @Column(name = "FECHA_MOD", nullable = true)
    private LocalDateTime fechaMod;
}
