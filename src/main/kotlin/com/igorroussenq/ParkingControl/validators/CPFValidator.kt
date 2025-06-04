package com.igorroussenq.ParkingControl.validators

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [CPFValidator::class])
annotation class ValidCPF(
    val message: String = "CPF inválido",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class CPFValidator : ConstraintValidator<ValidCPF, String> {
    override fun isValid(cpf: String?, context: ConstraintValidatorContext?): Boolean {
        if (cpf.isNullOrBlank()) return false

        val numbers = cpf.filter { it.isDigit() }
        if (numbers.length != 11) return false

        // Verifica se todos os dígitos são iguais
        if (numbers.all { it == numbers[0] }) return false

        // Calcula o primeiro dígito verificador
        val firstDigit = calculateDigit(numbers.substring(0, 9))
        if (firstDigit != numbers[9].digitToInt()) return false

        // Calcula o segundo dígito verificador
        val secondDigit = calculateDigit(numbers.substring(0, 10))
        if (secondDigit != numbers[10].digitToInt()) return false

        return true
    }

    private fun calculateDigit(cpfPartial: String): Int {
        val weights = if (cpfPartial.length == 9) {
            intArrayOf(10, 9, 8, 7, 6, 5, 4, 3, 2)
        } else {
            intArrayOf(11, 10, 9, 8, 7, 6, 5, 4, 3, 2)
        }

        val sum = cpfPartial.mapIndexed { index, char ->
            char.digitToInt() * weights[index]
        }.sum()

        val remainder = sum % 11
        return if (remainder < 2) 0 else 11 - remainder
    }
}