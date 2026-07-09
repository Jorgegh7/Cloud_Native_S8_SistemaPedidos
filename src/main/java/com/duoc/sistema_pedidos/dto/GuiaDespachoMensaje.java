package com.duoc.sistema_pedidos.dto;

public record GuiaDespachoMensaje(
        Long guiaId,
        String numeroGuia,
        String estado,
        String detalle
) {
}