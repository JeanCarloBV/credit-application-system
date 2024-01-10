package me.dio.creditapplicationsystem.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import me.dio.creditapplicationsystem.entity.Credit
import me.dio.creditapplicationsystem.entity.Customer
import me.dio.creditapplicationsystem.exception.BusinessException
import me.dio.creditapplicationsystem.repository.CreditRepository
import me.dio.creditapplicationsystem.service.impl.CreditService
import me.dio.creditapplicationsystem.service.impl.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {
    @MockK lateinit var creditRepository: CreditRepository
    @MockK lateinit var customerService: CustomerService
    @InjectMockKs lateinit var creditService: CreditService

    @Test
    fun `should save a credit`() {
        //given
        val fakeCustomer = Customer(id = 1L)
        val fakeCredit: Credit = buildCredit(customer = fakeCustomer)
        every { creditRepository.save(any()) } returns fakeCredit
        every { customerService.findById(any()) } returns fakeCustomer
        //when
        val actual: Credit = creditService.save(fakeCredit)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.save(fakeCredit) }
    }

    @Test
    fun `should find all credits by customer id`() {
        //given
        val fakeCreditList: List<Credit> = mutableListOf(
            buildCredit(id = 1L),
            buildCredit(id = 2L),
            buildCredit(id = 3L)
        )
        every { creditRepository.findAllByCustomerId(any()) } returns fakeCreditList
        //when
        val actual: List<Credit> = creditService.findAllByCustomer(1L)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCreditList)
        verify (exactly = 1) { creditRepository.findAllByCustomerId(any()) }
    }

    @Test
    fun `should find a credit by code and customer id`(){
        //given
        val fakeCustomer = Customer(id = 1L)
        val fakeCredit = buildCredit(customer = fakeCustomer)
        val fakeCode = UUID.randomUUID()
        every { creditRepository.findByCreditCode(fakeCode) } returns fakeCredit
        //when
        val actual: Credit = creditService.findByCreditCode(fakeCustomer.id!!, fakeCode)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        Assertions.assertThat(actual.customer).isSameAs(fakeCustomer)
        verify (exactly = 1) { creditRepository.findByCreditCode(fakeCode) }
    }

    @Test
    fun `should not find a credit by code and customer id`(){
        //given
        val fakeCode = UUID.randomUUID()
        val fakeId = 1L
        every { creditRepository.findByCreditCode(fakeCode) } returns null
        //when
        //then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findByCreditCode(fakeId, fakeCode) }
            .withMessage("Creditcode $fakeCode not found")
    }

    private fun buildCredit(
        creditValue: BigDecimal = BigDecimal.valueOf(1000.0),
        dayFirstInstalment: LocalDate = LocalDate.now(),
        numberOfInstalments: Int = 10,
        customer: Customer = Customer(id = 1L),
        id: Long = 1L
        ) = Credit(
            creditValue = creditValue,
            dayFirstInstalment = dayFirstInstalment,
            numberOfInstalments = numberOfInstalments,
            customer = customer,
            id = id
        )
}