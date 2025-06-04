package com.igorroussenq.ParkingControl.repositories

import com.igorroussenq.ParkingControl.domain.Mensalista
import com.igorroussenq.ParkingControl.enum.StatusMensalistaEnum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MensalistaRepository : JpaRepository<Mensalista, UUID> {
    fun findByCpf(cpf: String): Mensalista?
    fun findByStatusMensalista(status: StatusMensalistaEnum): List<Mensalista>

    @Query("SELECT m FROM Mensalista m WHERE m.nome LIKE %:nome%")
    fun findByNomeContaining(nome: String): List<Mensalista>

    fun existsByCpf(cpf: String): Boolean
}