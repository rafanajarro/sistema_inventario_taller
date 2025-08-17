package com.sistema.taller.demo.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USUARIOS")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "NOMBRES", nullable = false)
    private String nombres;

    @Column(name = "APELLIDOS", nullable = false)
    private String apellidos;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "CORREO", nullable = false)
    private String correo;

    @ManyToOne
    @JoinColumn(name = "ID_ROL", referencedColumnName = "ID_ROL", nullable = false)
    private RolUsuario idRol;

    @Column(name = "ESTADO", nullable = false)
    private String estado;

    @Column(name = "FOTO", nullable = true)
    private String foto;

    @Column(name = "DUI", nullable = false, length = 10)
    private String dui;

    @Column(name = "TELEFONO", nullable = false, length = 9)
    private String telefono;

    @CreationTimestamp
    @Column(name = "FECHA_CREA", nullable = false)
    private LocalDateTime fechaCrea;

    @UpdateTimestamp
    @Column(name = "FECHA_MOD")
    private LocalDateTime fechaMod;

    // Constructor con parámetros
    public Usuario(String nombres, String apellidos, String username, String password, String correo, RolUsuario idRol,
            String estado, LocalDateTime fechaCrea) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.username = username;
        this.password = password;
        this.correo = correo;
        this.idRol = idRol;
        this.estado = estado;
        this.fechaCrea = fechaCrea;
    }
}
