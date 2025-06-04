package com.igorroussenq.ParkingControl.services

import com.igorroussenq.ParkingControl.dto.*
import com.igorroussenq.ParkingControl.domain.Veiculo
import com.igorroussenq.ParkingControl.repositories.VeiculoRepository
import com.igorroussenq.ParkingControl.repositories.MensalistaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class VeiculoService(
    private val veiculoRepository: VeiculoRepository,
    private val mensalistaRepository: MensalistaRepository
) {

    fun create(dto: VeiculoCreateDto): VeiculoResponseDto {
        if (veiculoRepository.existsByPlaca(dto.placa)) {
            throw IllegalArgumentException("Placa já cadastrada")
        }

        val mensalista = dto.mensalistaId?.let {
            mensalistaRepository.findByIdOrNull(it)
                ?: throw NoSuchElementException("Mensalista não encontrado")
        }

        val veiculo = Veiculo(
            nome = dto.nome,
            placa = dto.placa,
            ano = dto.ano,
            dataEntrada = dto.dataEntrada,
            mensalista = mensalista
        )

        val saved = veiculoRepository.save(veiculo)
        return toResponseDto(saved)
    }

    @Transactional(readOnly = true)
    fun findById(id: UUID): VeiculoResponseDto {
        val veiculo = veiculoRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Veículo não encontrado")
        return toResponseDto(veiculo)
    }

    @Transactional(readOnly = true)
    fun findAll(): List<VeiculoResponseDto> {
        return veiculoRepository.findAll().map { toResponseDto(it) }
    }

    @Transactional(readOnly = true)
    fun findByPlaca(placa: String): VeiculoResponseDto? {
        return veiculoRepository.findByPlaca(placa)?.let { toResponseDto(it) }
    }

    @Transactional(readOnly = true)
    fun findByMensalista(mensalistaId: UUID): List<VeiculoResponseDto> {
        return veiculoRepository.findByMensalistaId(mensalistaId).map { toResponseDto(it) }
    }

    @Transactional(readOnly = true)
    fun findByAnoRange(anoInicial: Int, anoFinal: Int): List<VeiculoResponseDto> {
        return veiculoRepository.findByAnoRange(anoInicial, anoFinal).map { toResponseDto(it) }
    }

    fun update(id: UUID, dto: VeiculoUpdateDto): VeiculoResponseDto {
        val existing = veiculoRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Veículo não encontrado")

        // Validar placa única se estiver sendo alterada
        if (dto.placa != null && dto.placa != existing.placa && veiculoRepository.existsByPlaca(dto.placa)) {
            throw IllegalArgumentException("Placa já cadastrada")
        }

        val mensalista = dto.mensalistaId?.let {
            mensalistaRepository.findByIdOrNull(it)
                ?: throw NoSuchElementException("Mensalista não encontrado")
        } ?: existing.mensalista

        val updated = existing.copy(
            nome = dto.nome ?: existing.nome,
            placa = dto.placa ?: existing.placa,
            ano = dto.ano ?: existing.ano,
            dataEntrada = dto.dataEntrada ?: existing.dataEntrada,
            mensalista = mensalista
        )

        val saved = veiculoRepository.save(updated)
        return toResponseDto(saved)
    }

    fun delete(id: UUID) {
        if (!veiculoRepository.existsById(id)) {
            throw NoSuchElementException("Veículo não encontrado")
        }
        veiculoRepository.deleteById(id)
    }

    private fun toResponseDto(veiculo: Veiculo): VeiculoResponseDto {
        return VeiculoResponseDto(
            id = veiculo.id,
            nome = veiculo.nome,
            placa = veiculo.placa,
            ano = veiculo.ano,
            dataEntrada = veiculo.dataEntrada,
            mensalista = veiculo.mensalista?.let {
                MensalistaResponseDto(
                    id = it.id,
                    nome = it.nome,
                    dataDeNascimento = it.dataDeNascimento,
                    statusMensalista = it.statusMensalista,
                    cpf = it.cpf,
                    idade = it.idade,
                    documentoValido = it.documentoValido
                )
            }
        )
    }
}
