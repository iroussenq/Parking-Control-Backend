package com.igorroussenq.ParkingControl.dto

import com.igorroussenq.ParkingControl.validators.ValidPlaca
import jakarta.validation.constraints.*
import java.time.LocalDateTime
import java.util.UUID

data class VeiculoCreateDto(
    @field:NotBlank(message = "Nome é obrigatório")
    @field:Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    val nome: String,

    @field:NotBlank(message = "Placa é obrigatória")
    @field:ValidPlaca
    val placa: String,

    @field:NotNull(message = "Ano é obrigatório")
    @field:Min(value = 1900, message = "Ano deve ser maior que 1900")
    @field:Max(value = 2030, message = "Ano deve ser menor que 2030")
    val ano: Int,

    @field:NotNull(message = "Data de entrada é obrigatória")
    val dataEntrada: LocalDateTime,

    val mensalistaCpf: String? = null
)

data class VeiculoUpdateDto(
    @field:Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    val nome: String? = null,

    @field:ValidPlaca
    val placa: String? = null,

    @field:Min(value = 1900, message = "Ano deve ser maior que 1900")
    @field:Max(value = 2030, message = "Ano deve ser menor que 2030")
    val ano: Int? = null,

    val dataEntrada: LocalDateTime? = null,
    val mensalistaId: UUID? = null
)

data class VeiculoResponseDto(
    val id: UUID,
    val nome: String,
    val placa: String,
    val ano: Int,
    val dataEntrada: LocalDateTime,
    val mensalista: MensalistaResponseDto?
)
