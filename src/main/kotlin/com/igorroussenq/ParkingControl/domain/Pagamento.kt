package com.igorroussenq.ParkingControl.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "pagamentos")
data class Pagamento(
    @Id
    @Column(name = "id")
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mensalista_id", nullable = false)
    @JsonBackReference
    val mensalista: Mensalista,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    @JsonBackReference
    val veiculo: Veiculo,

    @Column(name = "tempo_saida", nullable = false)
    val tempoSaida: LocalDateTime,

    @Column(name = "tarifa", precision = 10, scale = 2)
    val tarifa: BigDecimal? = null
)