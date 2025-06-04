package com.igorroussenq.ParkingControl.validators

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PlacaValidator::class])
annotation class ValidPlaca(
    val message: String = "Placa inválida",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class PlacaValidator : ConstraintValidator<ValidPlaca, String> {
    override fun isValid(placa: String?, context: ConstraintValidatorContext?): Boolean {
        if (placa.isNullOrBlank()) return false

        val cleanPlaca = placa.replace("-", "").uppercase()

        // Formato antigo: ABC1234
        val oldFormat = "^[A-Z]{3}[0-9]{4}$".toRegex()
        // Formato Mercosul: ABC1D23
        val mercosulFormat = "^[A-Z]{3}[0-9]{1}[A-Z]{1}[0-9]{2}$".toRegex()

        return oldFormat.matches(cleanPlaca) || mercosulFormat.matches(cleanPlaca)
    }
}