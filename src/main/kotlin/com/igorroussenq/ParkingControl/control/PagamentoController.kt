package com.igorroussenq.ParkingControl.controllers

import com.igorroussenq.ParkingControl.dto.*
import com.igorroussenq.ParkingControl.services.PagamentoService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.time.LocalDateTime
import java.util.UUID

@RestController
@RequestMapping("/api/pagamentos")
@CrossOrigin(origins = ["*"])
class PagamentoController(
    private val pagamentoService: PagamentoService
) {

    @PostMapping
    fun create(@Valid @RequestBody dto: PagamentoCreateDto): ResponseEntity<PagamentoResponseDto> {
        val created = pagamentoService.create(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<PagamentoResponseDto>> {
        val pagamentos = pagamentoService.findAll()
        return ResponseEntity.ok(pagamentos)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): ResponseEntity<PagamentoResponseDto> {
        val pagamento = pagamentoService.findById(id)
        return ResponseEntity.ok(pagamento)
    }

    @GetMapping("/mensalista/{mensalistaId}")
    fun findByMensalista(@PathVariable mensalistaId: UUID): ResponseEntity<List<PagamentoResponseDto>> {
        val pagamentos = pagamentoService.findByMensalista(mensalistaId)
        return ResponseEntity.ok(pagamentos)
    }

    @GetMapping("/veiculo/{veiculoId}")
    fun findByVeiculo(@PathVariable veiculoId: UUID): ResponseEntity<List<PagamentoResponseDto>> {
        val pagamentos = pagamentoService.findByVeiculo(veiculoId)
        return ResponseEntity.ok(pagamentos)
    }

    @GetMapping("/periodo")
    fun findByPeriodo(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) inicio: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) fim: LocalDateTime
    ): ResponseEntity<List<PagamentoResponseDto>> {
        val pagamentos = pagamentoService.findByPeriodo(inicio, fim)
        return ResponseEntity.ok(pagamentos)
    }

    @GetMapping("/total/{mensalistaId}")
    fun getTotalTarifasByMensalista(@PathVariable mensalistaId: UUID): ResponseEntity<Map<String, Double>> {
        val total = pagamentoService.getTotalTarifasByMensalista(mensalistaId)
        return ResponseEntity.ok(mapOf("total" to total))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: PagamentoUpdateDto
    ): ResponseEntity<PagamentoResponseDto> {
        val updated = pagamentoService.update(id, dto)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        pagamentoService.delete(id)
        return ResponseEntity.noContent().build()
    }
}