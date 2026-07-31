package com.barracuda.eshop.customer.repository;

import com.barracuda.eshop.customer.entity.Customer;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class CustomerRepository {

    private static final String INSERT_CUSTOMER = "INSERT INTO CUSTOMERS(first_name,last_name,email,password) VALUES(:firstName,:lastName,:email,:password)";
    private static final String SELECT_CUSTOMER_BY_EMAIL = "SELECT * FROM CUSTOMERS WHERE email = :email";

    private final JdbcClient jdbcClient;

    public CustomerRepository( JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<Customer> findByEmail(String email) {
        return jdbcClient.sql(SELECT_CUSTOMER_BY_EMAIL)
                .param("email", email)
                .query(CustomerRepository::mapRow)
                .optional();
    }

    public void insertCustomer(Customer customer) {
        jdbcClient
                .sql(INSERT_CUSTOMER)
                .paramSource(customer)
                .update();
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
