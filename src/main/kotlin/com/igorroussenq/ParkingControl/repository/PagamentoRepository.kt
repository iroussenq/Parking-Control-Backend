package com.igorroussenq.ParkingControl.repositories

import com.igorroussenq.ParkingControl.domain.Pagamento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface PagamentoRepository : JpaRepository<Pagamento, UUID> {
    fun findByMensalistaId(mensalistaid: UUID): List<Pagamento>
    fun findByVeiculoId(veiculoid: UUID): List<Pagamento>

    @Query("SELECT p FROM Pagamento p WHERE p.tempoSaida BETWEEN :inicio AND :fim")
    fun findByPeriodo(inicio: LocalDateTime, fim: LocalDateTime): List<Pagamento>

    @Query("SELECT SUM(p.tarifa) FROM Pagamento p WHERE p.mensalista.id = :mensalistaId")
    fun getTotalTarifasByMensalista(mensalistaid: UUID): Double?
}