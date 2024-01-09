package me.dio.creditapplicationsystem.DTO

import me.dio.creditapplicationsystem.entity.Credit
import me.dio.creditapplicationsystem.entity.Customer
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

class CreditDto(
    private val creditValue: BigDecimal,
    private val dayFirstOfInstallment: LocalDate,
    private val numberOfInstallments: Int,
    private val customerId: Long
) {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstalment = this.dayFirstOfInstallment,
        numberOfInstalments = this.numberOfInstallments,
        customer = Customer(id = this.customerId)
    )
}
