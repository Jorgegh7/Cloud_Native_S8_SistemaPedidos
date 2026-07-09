package com.duoc.sistema_pedidos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "guias_procesadas")
@Getter
@Setter
@NoArgsConstructor
public class GuiaProcesada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long guiaId;

    @Column(nullable = false)
    private String numeroGuia;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String detalle;

    @Column(nullable = false)
    private Date fechaProcesado;

    @PrePersist
    public void prePersist() {
        this.fechaProcesado = new Date();
    }
}