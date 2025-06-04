package com.igorroussenq.ParkingControl.services

import com.igorroussenq.ParkingControl.dto.*

import com.igorroussenq.ParkingControl.repositories.MensalistaRepository
import com.igorroussenq.ParkingControl.domain.Mensalista
import com.igorroussenq.ParkingControl.enum.StatusMensalistaEnum
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class MensalistaService(
    private val mensalistaRepository: MensalistaRepository
) {

    fun create(dto: MensalistaCreateDto): MensalistaResponseDto {
        if (mensalistaRepository.existsByCpf(dto.cpf)) {
            throw IllegalArgumentException("CPF já cadastrado")
        }

        val mensalista = Mensalista(
            nome = dto.nome,
            dataDeNascimento = dto.dataDeNascimento,
            statusMensalista = dto.statusMensalista,
            cpf = dto.cpf,
            idade = dto.idade,
            documentoValido = dto.documentoValido
        )

        val saved = mensalistaRepository.save(mensalista)
        return toResponseDto(saved)
    }

    @Transactional(readOnly = true)
    fun findById(id: UUID): MensalistaResponseDto {
        val mensalista = mensalistaRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Mensalista não encontrado")
        return toResponseDto(mensalista)
    }

    @Transactional(readOnly = true)
    fun findAll(): List<MensalistaResponseDto> {
        return mensalistaRepository.findAll().map { toResponseDto(it) }
    }

    @Transactional(readOnly = true)
    fun findByCpf(cpf: String): MensalistaResponseDto? {
        return mensalistaRepository.findByCpf(cpf)?.let { toResponseDto(it) }
    }

    @Transactional(readOnly = true)
    fun findByStatus(status: StatusMensalistaEnum): List<MensalistaResponseDto> {
        return mensalistaRepository.findByStatusMensalista(status).map { toResponseDto(it) }
    }

    fun update(id: UUID, dto: MensalistaUpdateDto): MensalistaResponseDto {
        val existing = mensalistaRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Mensalista não encontrado")

        val updated = existing.copy(
            nome = dto.nome ?: existing.nome,
            dataDeNascimento = dto.dataDeNascimento ?: existing.dataDeNascimento,
            statusMensalista = dto.statusMensalista ?: existing.statusMensalista,
            idade = dto.idade ?: existing.idade,
            documentoValido = dto.documentoValido ?: existing.documentoValido
        )

        val saved = mensalistaRepository.save(updated)
        return toResponseDto(saved)
    }

    fun delete(id: UUID) {
        if (!mensalistaRepository.existsById(id)) {
            throw NoSuchElementException("Mensalista não encontrado")
        }
        mensalistaRepository.deleteById(id)
    }

    private fun toResponseDto(mensalista: Mensalista): MensalistaResponseDto {
        return MensalistaResponseDto(
            id = mensalista.id,
            nome = mensalista.nome,
            dataDeNascimento = mensalista.dataDeNascimento,
            statusMensalista = mensalista.statusMensalista,
            cpf = mensalista.cpf,
            idade = mensalista.idade,
            documentoValido = mensalista.documentoValido
        )
    }
}