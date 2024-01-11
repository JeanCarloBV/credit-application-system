package me.dio.creditapplicationsystem.DTO

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.creditapplicationsystem.entity.Credit
import me.dio.creditapplicationsystem.entity.Customer
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

class CreditDto(
    @field:NotNull(message = "Invalid input") val creditValue: BigDecimal,
    @field:Future(message = "Invalid input") val dayFirstOfInstallment: LocalDate,
    @field:Max(value = 48, message = "Max installments number exceeded") val numberOfInstallments: Int,
    @field:NotNull(message = "Invalid input") val customerId: Long
) {

    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstalment = this.dayFirstOfInstallment,
        numberOfInstalments = this.numberOfInstallments,
        customer = Customer(id = this.customerId)
    )
}
