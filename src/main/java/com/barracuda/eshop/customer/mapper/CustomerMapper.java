package com.barracuda.eshop.customer.mapper;

import com.barracuda.eshop.customer.dto.CustomerRegistrationForm;
import com.barracuda.eshop.customer.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer customerRegistrationFormToCustomer(CustomerRegistrationForm registrationForm);
}
