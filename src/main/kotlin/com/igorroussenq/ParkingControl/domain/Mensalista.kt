package com.igorroussenq.ParkingControl.domain

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.igorroussenq.ParkingControl.enum.StatusMensalistaEnum
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "mensalistas")
data class Mensalista(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "nome", nullable = false, length = 100)
    val nome: String,

    @Column(name = "data_nascimento", nullable = false)
    val dataDeNascimento: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(name = "status_mensalista", nullable = false, length = 20)
    val statusMensalista: StatusMensalistaEnum,

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    val cpf: String,

    @Column(name = "idade", length = 10)
    val idade: String? = null,

    @Column(name = "documento_valido")
    val documentoValido: Boolean? = null,

    @OneToMany(mappedBy = "mensalista", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    val veiculos: List<Veiculo> = mutableListOf(),

    @OneToMany(mappedBy = "mensalista", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    val pagamentos: List<Pagamento> = mutableListOf()
)