package com.igorroussenq.ParkingControl.service

import com.igorroussenq.ParkingControl.domain.Mensalista
import com.igorroussenq.ParkingControl.domain.Veiculo
import com.igorroussenq.ParkingControl.domain.Pagamento
import com.igorroussenq.ParkingControl.dto.PagamentoCreateDto
import com.igorroussenq.ParkingControl.repositories.MensalistaRepository
import com.igorroussenq.ParkingControl.repositories.VeiculoRepository
import com.igorroussenq.ParkingControl.repositories.PagamentoRepository
import com.igorroussenq.ParkingControl.services.PagamentoService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

class PagamentoServiceTest {

    private lateinit var pagamentoRepository: PagamentoRepository
    private lateinit var mensalistaRepository: MensalistaRepository
    private lateinit var veiculoRepository: VeiculoRepository
    private lateinit var pagamentoService: PagamentoService

    @BeforeEach
    fun setUp() {
        pagamentoRepository = mock(PagamentoRepository::class.java)
        mensalistaRepository = mock(MensalistaRepository::class.java)
        veiculoRepository = mock(VeiculoRepository::class.java)
        pagamentoService = PagamentoService(pagamentoRepository, mensalistaRepository, veiculoRepository)
    }

    @Test
    fun `deve calcular corretamente a tarifa para o pagamento`() {
        // Arrange
        val mensalistaId = UUID.randomUUID()
        val veiculoId = UUID.randomUUID()

        val mensalistaMock = Mensalista(
            id = mensalistaId,
            nome = "John Doe",
            dataDeNascimento = LocalDateTime.now().minusYears(30).toLocalDate(),
            statusMensalista = com.igorroussenq.ParkingControl.enum.StatusMensalistaEnum.ATIVO,
            cpf = "12345678901"
        )

        val veiculoMock = Veiculo(
            id = veiculoId,
            placa = "ABC1234",
            nome = "Carro Exemplo",
            ano = 2021,
            dataEntrada = LocalDateTime.now().minusHours(2),
            mensalista = mensalistaMock
        )

        val tempoSaida = LocalDateTime.now()
        val tarifaPorMeiaHora = BigDecimal("5.00")  // Tarifa definida como 5.00 por meia hora

        val pagamentoCreateDto = PagamentoCreateDto(
            mensalistaId = mensalistaId,
            veiculoId = veiculoId,
            tempoSaida = tempoSaida,
            tarifaMeiaHora = tarifaPorMeiaHora
        )

        val esperadoHoras = 2  // Tempo total de permanência: 2 horas
        val esperadoTarifa = tarifaPorMeiaHora.multiply(BigDecimal(esperadoHoras * 2)) // 4 meias-horas (2 horas)

        `when`(mensalistaRepository.findById(mensalistaId)).thenReturn(Optional.of(mensalistaMock))
        `when`(veiculoRepository.findById(veiculoId)).thenReturn(Optional.of(veiculoMock))
        `when`(pagamentoRepository.save(any(Pagamento::class.java))).thenAnswer { invocation ->
            val pagamento = invocation.getArgument<Pagamento>(0)
            Pagamento(
                id = UUID.randomUUID(),
                mensalista = pagamento.mensalista,
                veiculo = pagamento.veiculo,
                tempoSaida = pagamento.tempoSaida,
                tarifa = pagamento.tarifa
            )
        }

        // Act
        val pagamentoResponse = pagamentoService.create(pagamentoCreateDto)

        // Assert
        assertEquals(esperadoTarifa, pagamentoResponse.tarifa)
        verify(pagamentoRepository, times(1)).save(any(Pagamento::class.java))
        verify(veiculoRepository, times(1)).findById(veiculoId)
        verify(mensalistaRepository, times(1)).findById(mensalistaId)
    }
}