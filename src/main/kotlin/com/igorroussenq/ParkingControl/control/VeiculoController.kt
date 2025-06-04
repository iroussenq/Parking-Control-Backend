package com.igorroussenq.ParkingControl.controllers

import com.igorroussenq.ParkingControl.dto.*
import com.igorroussenq.ParkingControl.services.VeiculoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/veiculos")
@CrossOrigin(origins = ["*"])
class VeiculoController(
    private val veiculoService: VeiculoService
) {

    @PostMapping
    fun create(@Valid @RequestBody dto: VeiculoCreateDto): ResponseEntity<VeiculoResponseDto> {
        val created = veiculoService.create(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<VeiculoResponseDto>> {
        val veiculos = veiculoService.findAll()
        return ResponseEntity.ok(veiculos)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): ResponseEntity<VeiculoResponseDto> {
        val veiculo = veiculoService.findById(id)
        return ResponseEntity.ok(veiculo)
    }

    @GetMapping("/placa/{placa}")
    fun findByPlaca(@PathVariable placa: String): ResponseEntity<VeiculoResponseDto?> {
        val veiculo = veiculoService.findByPlaca(placa)
        return ResponseEntity.ok(veiculo)
    }

    @GetMapping("/mensalista/{mensalistaId}")
    fun findByMensalista(@PathVariable mensalistaId: UUID): ResponseEntity<List<VeiculoResponseDto>> {
        val veiculos = veiculoService.findByMensalista(mensalistaId)
        return ResponseEntity.ok(veiculos)
    }

    @GetMapping("/ano")
    fun findByAnoRange(
        @RequestParam anoInicial: Int,
        @RequestParam anoFinal: Int
    ): ResponseEntity<List<VeiculoResponseDto>> {
        val veiculos = veiculoService.findByAnoRange(anoInicial, anoFinal)
        return ResponseEntity.ok(veiculos)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: VeiculoUpdateDto
    ): ResponseEntity<VeiculoResponseDto> {
        val updated = veiculoService.update(id, dto)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        veiculoService.delete(id)
        return ResponseEntity.noContent().build()
    }
}