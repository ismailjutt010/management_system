package com.amigoscode.demo.customer;

import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> getAllCustomers() {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> getCustomerById(int id) {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql , customerRowMapper , id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name,email,age)
                VALUES (?, ?, ?)
                """;
        int result = jdbcTemplate.update(
                sql, customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
        System.out.println("jdbcTemplate.update = "+result);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM customer 
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class, email);
        return count != null && count > 0 ;
    }

    @Override
    public boolean existPersonWithId(int id) {
        var sql = """
                SELECT count(id)
                FROM customer 
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class, id);
        return count != null && count > 0 ;
    }

    @Override
    public void deleteCustomerById(int id) {
        var sql = """
                DELETE 
                FROM customer 
                WHERE id = ?
                """;
        int result = jdbcTemplate.update(sql , id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        if(customer.getName() != null){
            var sql = """
                    UPDATE customer 
                    SET name = ?
                    Where id = ?
                    """;
            int result = jdbcTemplate.update(
                    sql,
                    customer.getName(),
                    customer.getId()
            );
            System.out.println("update customer name result = "+result);
        }

        if(customer.getEmail() != null){
            var sql = """
                    UPDATE customer 
                    SET email = ?
                    Where id = ?
                    """;
            int result = jdbcTemplate.update(
                    sql,
                    customer.getEmail(),
                    customer.getId()
            );
            System.out.println("update customer email result = "+result);
        }
        if(customer.getAge() != null){
            var sql = """
                    UPDATE customer 
                    SET age = ?
                    Where id = ?
                    """;
            int result = jdbcTemplate.update(
                    sql,
                    customer.getAge(),
                    customer.getId()
            );
            System.out.println("update customer age result = "+result);
        }
    }
}
