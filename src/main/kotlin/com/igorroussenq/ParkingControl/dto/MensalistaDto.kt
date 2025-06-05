package com.igorroussenq.ParkingControl.dto

import com.igorroussenq.ParkingControl.validators.ValidCPF
import com.igorroussenq.ParkingControl.enum.StatusMensalistaEnum
import jakarta.validation.constraints.*
import java.time.LocalDate
import java.util.UUID

data class MensalistaCreateDto(
    @field:NotBlank(message = "Nome é obrigatório")
    @field:Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    val nome: String,

    @field:NotNull(message = "Data de nascimento é obrigatória")
    @field:Past(message = "Data de nascimento deve ser no passado")
    val dataDeNascimento: LocalDate,

    @field:NotNull(message = "Status é obrigatório")
    val statusMensalista: StatusMensalistaEnum,

    @field:NotBlank(message = "CPF é obrigatório")
    @field:ValidCPF
    val cpf: String,

    @field:Size(max = 10, message = "Idade deve ter no máximo 10 caracteres")
    val idade: String? = null,

    val documentoValido: Boolean? = null
)

data class MensalistaUpdateDto(
    @field:Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    val nome: String? = null,

    @field:Past(message = "Data de nascimento deve ser no passado")
    val dataDeNascimento: LocalDate? = null,

    val statusMensalista: StatusMensalistaEnum? = null,

    @field:Size(max = 10, message = "Idade deve ter no máximo 10 caracteres")
    val idade: String? = null,

    val documentoValido: Boolean? = null
)

data class MensalistaResponseDto(
    val id: UUID?,
    val nome: String,
    val dataDeNascimento: LocalDate,
    val statusMensalista: StatusMensalistaEnum,
    val cpf: String,
    val idade: String?,
    val documentoValido: Boolean?
)