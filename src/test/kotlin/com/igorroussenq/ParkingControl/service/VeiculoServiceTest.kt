package com.igorroussenq.ParkingControl.service

import com.igorroussenq.ParkingControl.domain.Mensalista
import com.igorroussenq.ParkingControl.domain.Veiculo
import com.igorroussenq.ParkingControl.dto.VeiculoCreateDto
import com.igorroussenq.ParkingControl.repositories.MensalistaRepository
import com.igorroussenq.ParkingControl.repositories.VeiculoRepository
import com.igorroussenq.ParkingControl.services.VeiculoService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class VeiculoServiceTest {

    private lateinit var veiculoRepository: VeiculoRepository
    private lateinit var mensalistaRepository: MensalistaRepository
    private lateinit var veiculoService: VeiculoService

    @BeforeEach
    fun setUp() {
        veiculoRepository = mock(VeiculoRepository::class.java)
        mensalistaRepository = mock(MensalistaRepository::class.java)
        veiculoService = VeiculoService(veiculoRepository, mensalistaRepository)
    }

    @Test
    fun `deve criar um veiculo com sucesso`() {
        // Arrange
        val mensalistaId = UUID.randomUUID()
        val mensalistaMock = Mensalista(
            id = mensalistaId,
            nome = "John Doe",
            dataDeNascimento = LocalDate.of(1990, 1, 1),
            statusMensalista = com.igorroussenq.ParkingControl.enum.StatusMensalistaEnum.ATIVO,
            cpf = "12345678901"
        )
        val veiculoCreateDto = VeiculoCreateDto(
            placa = "ABC1234",
            nome = "Modelo Exemplo",
            ano = 2021,
            dataEntrada = LocalDateTime.now()
        )
        val veiculoMock = Veiculo(
            id = UUID.randomUUID(),
            placa = veiculoCreateDto.placa,
            nome = veiculoCreateDto.nome,
            ano = veiculoCreateDto.ano,
            dataEntrada = veiculoCreateDto.dataEntrada,
            mensalista = mensalistaMock
        )

        `when`(mensalistaRepository.findById(mensalistaId)).thenReturn(Optional.of(mensalistaMock))
        `when`(veiculoRepository.save(any(Veiculo::class.java))).thenReturn(veiculoMock)

        // Act
        val result = veiculoService.create(veiculoCreateDto)

        // Assert
        assertEquals(veiculoMock.placa, result.placa)
        assertEquals(veiculoMock.nome, result.nome)
        assertEquals(veiculoMock.ano, result.ano)
        assertEquals(veiculoMock.mensalista?.id, mensalistaId)

        verify(veiculoRepository, times(1)).save(any(Veiculo::class.java))
        verify(mensalistaRepository, times(1)).findById(mensalistaId)
    }

    @Test
    fun `deve atualizar um veiculo com sucesso`() {
        // Arrange
        val veiculoId = UUID.randomUUID()
        val mensalistaId = UUID.randomUUID()
        val mensalistaMock = Mensalista(
            id = mensalistaId,
            nome = "John Doe",
            dataDeNascimento = LocalDate.of(1990, 1, 1),
            statusMensalista = com.igorroussenq.ParkingControl.enum.StatusMensalistaEnum.ATIVO,
            cpf = "12345678901"
        )
        val veiculoExistente = Veiculo(
            id = veiculoId,
            placa = "ABC1234",
            nome = "Modelo Anterior",
            ano = 2019,
            dataEntrada = LocalDateTime.now(),
            mensalista = mensalistaMock
        )
        val veiculoUpdateDto = com.igorroussenq.ParkingControl.dto.VeiculoUpdateDto(
            placa = "DEF5678",
            nome = "Modelo Atualizado",
            ano = 2022,
            mensalistaId = mensalistaId
        )
        val veiculoAtualizado = veiculoExistente.copy(
            placa = veiculoUpdateDto.placa!!,
            nome = veiculoUpdateDto.nome!!,
            ano = veiculoUpdateDto.ano!!
        )
        val veiculoExistenteOptional = Optional.of(veiculoExistente)

        `when`(veiculoRepository.findById(veiculoId)).thenReturn(veiculoExistenteOptional)
        `when`(mensalistaRepository.findById(mensalistaId)).thenReturn(Optional.of(mensalistaMock))
        `when`(veiculoRepository.save(any(Veiculo::class.java))).thenReturn(veiculoAtualizado)

        // Act
        val result = veiculoService.update(veiculoId, veiculoUpdateDto)

        // Assert
        assertEquals(veiculoAtualizado.placa, result.placa)
        assertEquals(veiculoAtualizado.nome, result.nome)
        assertEquals(veiculoAtualizado.ano, result.ano)
        assertEquals(veiculoAtualizado.mensalista?.id, mensalistaId)

        verify(veiculoRepository, times(1)).findById(veiculoId)
        verify(mensalistaRepository, times(1)).findById(mensalistaId)
        verify(veiculoRepository, times(1)).save(any(Veiculo::class.java))
    }
}