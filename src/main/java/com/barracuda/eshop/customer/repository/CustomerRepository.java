package com.barracuda.eshop.customer.repository;

import com.barracuda.eshop.customer.entity.Customer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Repository
public class CustomerRepository {

    private static final String INSERT_CUSTOMER = "INSERT INTO CUSTOMERS(first_name,last_name,email,password) VALUES(:firstName,:lastName,:email,:password)";
    private static final String SELECT_CUSTOMER_BY_EMAIL = "SELECT * FROM CUSTOMERS WHERE email = :email";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CustomerRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Customer> findByEmail(String email) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_CUSTOMER_BY_EMAIL, Map.of("email", email), CustomerRepository::mapRow));
        }catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void insertCustomer(Customer customer) {
        jdbcTemplate.update(INSERT_CUSTOMER, params(customer));
    }

    private Map<String,String> params(Customer customer){
        return Map.of("firstName",customer.firstName(),"lastName",customer.lastName(),"email",customer.email(),"password",customer.password());
    }

    private static Customer mapRow(ResultSet resultSet, int row) throws SQLException {
        var id = resultSet.getLong("id");
        var firstName = resultSet.getString("first_name");
        var lastName = resultSet.getString("last_name");
        var customerEmail = resultSet.getString("email");
        var customerPassword = resultSet.getString("password");

        return new Customer(id, firstName, lastName, customerEmail, customerPassword);
    }
}
