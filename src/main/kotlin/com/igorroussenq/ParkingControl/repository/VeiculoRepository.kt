package com.igorroussenq.ParkingControl.repositories

import com.igorroussenq.ParkingControl.domain.Veiculo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface VeiculoRepository : JpaRepository<Veiculo, UUID> {
    fun findByPlaca(placa: String): Veiculo?
    fun findByMensalistaId(mensalistaid: UUID): List<Veiculo>

    @Query("SELECT v FROM Veiculo v WHERE v.ano >= :anoInicial AND v.ano <= :anoFinal")
    fun findByAnoRange(anoInicial: Int, anoFinal: Int): List<Veiculo>

    fun existsByPlaca(placa: String): Boolean
}