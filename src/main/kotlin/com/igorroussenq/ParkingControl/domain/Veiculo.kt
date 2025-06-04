package com.igorroussenq.ParkingControl.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.igorroussenq.ParkingControl.domain.Mensalista
import com.igorroussenq.ParkingControl.domain.Pagamento
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "veiculos")
data class Veiculo(
    @Id
    @Column(name = "id")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "nome", nullable = false, length = 50)
    val nome: String,

    @Column(name = "placa", nullable = false, unique = true, length = 8)
    val placa: String,

    @Column(name = "ano", nullable = false)
    val ano: Int,

    @Column(name = "data_entrada", nullable = false)
    val dataEntrada: LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mensalista_id")
    @JsonBackReference
    val mensalista: Mensalista? = null,

    @OneToMany(mappedBy = "veiculo", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    val pagamentos: List<Pagamento> = mutableListOf()
)