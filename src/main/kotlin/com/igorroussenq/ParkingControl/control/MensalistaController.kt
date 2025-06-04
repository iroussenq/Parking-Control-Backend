package com.igorroussenq.ParkingControl.controllers

import com.igorroussenq.ParkingControl.dto.*
import com.igorroussenq.ParkingControl.services.MensalistaService
import com.igorroussenq.ParkingControl.enum.StatusMensalistaEnum
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/mensalistas")
@CrossOrigin(origins = ["*"])
class MensalistaController(
    private val mensalistaService: MensalistaService
) {

    @PostMapping
    fun create(@Valid @RequestBody dto: MensalistaCreateDto): ResponseEntity<MensalistaResponseDto> {
        val created = mensalistaService.create(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<MensalistaResponseDto>> {
        val mensalistas = mensalistaService.findAll()
        return ResponseEntity.ok(mensalistas)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): ResponseEntity<MensalistaResponseDto> {
        val mensalista = mensalistaService.findById(id)
        return ResponseEntity.ok(mensalista)
    }

    @GetMapping("/cpf/{cpf}")
    fun findByCpf(@PathVariable cpf: String): ResponseEntity<MensalistaResponseDto?> {
        val mensalista = mensalistaService.findByCpf(cpf)
        return ResponseEntity.ok(mensalista)
    }

    @GetMapping("/status/{status}")
    fun findByStatus(@PathVariable status: StatusMensalistaEnum): ResponseEntity<List<MensalistaResponseDto>> {
        val mensalistas = mensalistaService.findByStatus(status)
        return ResponseEntity.ok(mensalistas)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: MensalistaUpdateDto
    ): ResponseEntity<MensalistaResponseDto> {
        val updated = mensalistaService.update(id, dto)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        mensalistaService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
