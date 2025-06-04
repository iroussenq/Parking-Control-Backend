package com.igorroussenq.ParkingControl.services

import com.igorroussenq.ParkingControl.dto.*
import com.igorroussenq.ParkingControl.repositories.PagamentoRepository
import com.igorroussenq.ParkingControl.repositories.MensalistaRepository
import com.igorroussenq.ParkingControl.repositories.VeiculoRepository
import com.igorroussenq.ParkingControl.domain.Pagamento
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class PagamentoService(
    private val pagamentoRepository: PagamentoRepository,
    private val mensalistaRepository: MensalistaRepository,
    private val veiculoRepository: VeiculoRepository
) {

    fun create(dto: PagamentoCreateDto): PagamentoResponseDto {
        val mensalista = mensalistaRepository.findByIdOrNull(dto.mensalistaId)
            ?: throw NoSuchElementException("Mensalista não encontrado")

        val veiculo = veiculoRepository.findByIdOrNull(dto.veiculoId)
            ?: throw NoSuchElementException("Veículo não encontrado")

        val pagamento = Pagamento(
            mensalista = mensalista,
            veiculo = veiculo,
            tempoSaida = dto.tempoSaida,
            tarifa = dto.tarifa
        )

        val saved = pagamentoRepository.save(pagamento)
        return toResponseDto(saved)
    }

    @Transactional(readOnly = true)
    fun findById(id: UUID): PagamentoResponseDto {
        val pagamento = pagamentoRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Pagamento não encontrado")
        return toResponseDto(pagamento)
    }

    @Transactional(readOnly = true)
    fun findAll(): List<PagamentoResponseDto> {
        return pagamentoRepository.findAll().map { toResponseDto(it) }
    }

    @Transactional(readOnly = true)
    fun findByMensalista(mensalistaId: UUID): List<PagamentoResponseDto> {
        return pagamentoRepository.findByMensalistaId(mensalistaId).map { toResponseDto(it) }
    }

    @Transactional(readOnly = true)
    fun findByVeiculo(veiculoId: UUID): List<PagamentoResponseDto> {
        return pagamentoRepository.findByVeiculoId(veiculoId).map { toResponseDto(it) }
    }

    @Transactional(readOnly = true)
    fun findByPeriodo(inicio: LocalDateTime, fim: LocalDateTime): List<PagamentoResponseDto> {
        return pagamentoRepository.findByPeriodo(inicio, fim).map { toResponseDto(it) }
    }

    @Transactional(readOnly = true)
    fun getTotalTarifasByMensalista(mensalistaId: UUID): Double {
        return pagamentoRepository.getTotalTarifasByMensalista(mensalistaId) ?: 0.0
    }

    fun update(id: UUID, dto: PagamentoUpdateDto): PagamentoResponseDto {
        val existing = pagamentoRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Pagamento não encontrado")

        val updated = existing.copy(
            tempoSaida = dto.tempoSaida ?: existing.tempoSaida,
            tarifa = dto.tarifa ?: existing.tarifa
        )

        val saved = pagamentoRepository.save(updated)
        return toResponseDto(saved)
    }

    fun delete(id: UUID) {
        if (!pagamentoRepository.existsById(id)) {
            throw NoSuchElementException("Pagamento não encontrado")
        }
        pagamentoRepository.deleteById(id)
    }

    private fun toResponseDto(pagamento: Pagamento): PagamentoResponseDto {
        return PagamentoResponseDto(
            id = pagamento.id,
            mensalista = MensalistaResponseDto(
                id = pagamento.mensalista.id,
                nome = pagamento.mensalista.nome,
                dataDeNascimento = pagamento.mensalista.dataDeNascimento,
                statusMensalista = pagamento.mensalista.statusMensalista,
                cpf = pagamento.mensalista.cpf,
                idade = pagamento.mensalista.idade,
                documentoValido = pagamento.mensalista.documentoValido
            ),
            veiculo = VeiculoResponseDto(
                id = pagamento.veiculo.id,
                nome = pagamento.veiculo.nome,
                placa = pagamento.veiculo.placa,
                ano = pagamento.veiculo.ano,
                dataEntrada = pagamento.veiculo.dataEntrada,
                mensalista = pagamento.veiculo.mensalista?.let {
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
            ),
            tempoSaida = pagamento.tempoSaida,
            tarifa = pagamento.tarifa
        )
    }
}
