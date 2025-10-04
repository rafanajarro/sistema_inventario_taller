package com.sistema.taller.demo.model;

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
@Table(name = "PRODUCTOS")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRODUCTO")
    private Integer idProducto;

    @ManyToOne
    @JoinColumn(name = "ID_CATEGORIA", referencedColumnName = "ID_CATEGORIA", nullable = false)
    private Categorias idCategoria;

    @Column(name = "NOMBRE_PRODUCTO", length = 25, nullable = false)
    private String nombreProducto;

    @Column(name = "DESCRIPCION", length = 25, nullable = false)
    private String descripcion;

    @Column(name = "ESTADO", length = 25, nullable = false)
    private String estado;
}
