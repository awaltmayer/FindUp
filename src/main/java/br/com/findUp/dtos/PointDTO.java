package br.com.findUp.dtos;

// DTO usado tanto no POST (criar) quanto no PUT (atualizar) — conforme requisito
public record PointDTO(
        String descricao,
        Double latitude,
        Double longitude,
        String tipo,
        String detalhes
) {}
