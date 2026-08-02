package com.barracuda.eshop.customer;

import com.barracuda.eshop.customer.dto.CustomerRegistrationForm;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCustomerTest {

    public final CustomerRegistrationForm.CustomerRegistrationFormBuilder johnRegistrationFormBuilder = CustomerRegistrationForm
            .builder()
            .withFirstName("John")
            .withLastName("Doe")
            .withEmail("test@email.com")
            .withPhone("123456789")
            .withPassword("ValidPass123!")
            .withRepeatedPassword("ValidPass123!");


    public static List<String> passwordSpecialCharacters(){
        return List.of("!","?","#","$","%","^","&","*","(",")","_","+");
    }

    public static List<String> onlyDigitPasswords(){
        return List.of("12345678", "1039012581", "108990835", "3587895790139");
    }

    public static List<String> shortPasswords(){
        return List.of(
                "","1","12","123","1234","passw","123456","1234567"
        );
    }

    public static List<String> blankStrings() {
        var blankStrings = new ArrayList<String>();

        blankStrings.add("");
        blankStrings.add("   ");
        blankStrings.add("   ");
        blankStrings.add(null);

        return blankStrings;
    }

    public static List<String> invalidEmails(){

        return List.of("   Just a string",
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
        );
    }
}
