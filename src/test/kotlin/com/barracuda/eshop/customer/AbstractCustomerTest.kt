package com.barracuda.eshop.customer

import com.barracuda.eshop.customer.dto.CustomerRegistrationForm

abstract class AbstractCustomerTest {

    @JvmField
    val johnRegistrationFormBuilder: CustomerRegistrationForm.CustomerRegistrationFormBuilder =
        CustomerRegistrationForm
            .builder()
            .withFirstName("John")
            .withLastName("Doe")
            .withEmail("test@email.com")
            .withPhone("123456789")
            .withPassword("ValidPass123!")
            .withRepeatedPassword("ValidPass123!")


    companion object {
        @JvmStatic
        fun passwordSpecialCharacters() = mutableListOf("!", "?", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+")

        @JvmStatic
        fun onlyDigitPasswords() = mutableListOf("12345678", "1039012581", "108990835", "3587895790139")

        @JvmStatic
        fun shortPasswords() = mutableListOf("", "1", "12", "123", "1234", "passw", "123456", "1234567")

        @JvmStatic
        fun blankStrings(): MutableList<String?> {
            val blankStrings = ArrayList<String?>()

            blankStrings.add("")
            blankStrings.add("   ")
            blankStrings.add("   ")
            blankStrings.add(null)

            return blankStrings
        }

        @JvmStatic
        fun invalidEmails() = mutableListOf(
                "   Just a string",
                "string",
                "(comment)",
                "()@example.com",
                "fred(&)barny@example.com",
                "fred\\ barny@example.com",
                "Abigail <abi gail @ example.com>",
                "Abigail <abigail(fo(o)@example.com>",
                "Abigail <abigail(fo) o)@example.com>",
                "\"Abi\"gail\" <abigail@example.com>",
                "abigail@[exa]ple.com]",
                "abigail@[exa[ple.com]",
                "abigail@[exaple].com]",
                "abigail@",
                "@example.com",
                "phrase: abigail@example.com abigail@example.com ;",
                "invalid�char@example.com"
            )
    }
}
