package com.igorroussenq.ParkingControl.dto

import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class PagamentoCreateDto(
    @field:NotBlank(message = "ID do mensalista é obrigatório")
    val mensalistaId: UUID,

    @field:NotBlank(message = "ID do veículo é obrigatório")
    val veiculoId: UUID,

    @field:NotNull(message = "Tempo de saída é obrigatório")
    val tempoSaida: LocalDateTime,

    @field:DecimalMin(value = "0.0", inclusive = false, message = "Tarifa deve ser maior que zero")
    @field:Digits(integer = 8, fraction = 2, message = "Tarifa deve ter no máximo 8 dígitos inteiros e 2 decimais")
    val tarifaMeiaHora: BigDecimal
)

data class PagamentoUpdateDto(
    val tempoSaida: LocalDateTime? = null,

    @field:DecimalMin(value = "0.0", inclusive = false, message = "Tarifa deve ser maior que zero")
    @field:Digits(integer = 8, fraction = 2, message = "Tarifa deve ter no máximo 8 dígitos inteiros e 2 decimais")
    val tarifa: BigDecimal? = null
)

data class PagamentoResponseDto(
    val id: UUID?,
    val mensalista: MensalistaResponseDto,
    val veiculo: VeiculoResponseDto,
    val tempoSaida: LocalDateTime,
    val tarifa: BigDecimal?
)
