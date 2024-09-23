package com.amigoscode.demo.customer;
import org.springframework.jdbc.core.JdbcTemplate;
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
        var sql = "\n" +
                "SELECT id, name, email,password, age, gender \n" +
                "FROM customer\n";
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> getCustomerById(int id) {
        var sql = "\n" +
                "SELECT id, name, email,password, age, gender\n" +
                "FROM customer\n" +
                "WHERE id = ?\n" ;

        return jdbcTemplate.query(sql , customerRowMapper , id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = "\n" +
                "INSERT INTO customer( name, email, password, age, gender)\n" +
                "VALUES (?, ?, ?, ?, ?)";

        int result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getAge(),
                customer.getGender().name()
        );
        System.out.println("jdbcTemplate.update = "+result);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        var sql = "\n" +
                "SELECT count(id)\n" +
                "FROM customer \n" +
                "WHERE email = ?";

        Integer count = jdbcTemplate.queryForObject(sql,Integer.class, email);
        return count != null && count > 0 ;
    }

    @Override
    public boolean existPersonWithId(int id) {
        var sql = "\n" +
                "SELECT count(id)\n" +
                "FROM customer \n" +
                "WHERE id = ?";

        Integer count = jdbcTemplate.queryForObject(sql,Integer.class, id);
        return count != null && count > 0 ;
    }

    @Override
    public void deleteCustomerById(int id) {
        var sql = "\n" +
                "DELETE \n" +
                "FROM customer \n" +
                "WHERE id = ?\n" ;

        int result = jdbcTemplate.update(sql , id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        if(customer.getName() != null){
            var sql = "\n" +
                    "UPDATE customer \n" +
                    "SET name = ?\n" +
                    "Where id = ?\n";

            int result = jdbcTemplate.update(
                    sql,
                    customer.getName(),
                    customer.getId()
            );
            System.out.println("update customer name result = "+result);
        }

        if(customer.getEmail() != null){
            var sql = "\n" +
                   "UPDATE customer \n" +
                   "SET email = ?\n" +
                   "Where id = ?\n" ;

            int result = jdbcTemplate.update(
                    sql,
                    customer.getEmail(),
                    customer.getId()
            );
            System.out.println("update customer email result = "+result);
        }
        if(customer.getAge() != null){
            var sql = "\n" +
                    "UPDATE customer\n" +
                    "SET age = ?\n" +
                    "Where id = ?\n" ;

            int result = jdbcTemplate.update(
                    sql,
                    customer.getAge(),
                    customer.getId()
            );
            System.out.println("update customer age result = "+result);
        }
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        var sql = "\n" +
                "SELECT id, name, email,password, age, gender\n" +
                "FROM customer\n" +
                "WHERE email = ?\n" ;

        return jdbcTemplate.query(sql , customerRowMapper , email)
                .stream()
                .findFirst();
    }
}
