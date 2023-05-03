package com.employee.employee.Data;

import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<EmployeeEntity, Long> {
    EmployeeEntity findByEmail(String email);
    void deleteByEmail(String email);
    Iterable<EmployeeEntity> findByFirstNameAndLastName(String firstName, String lastName);
    Iterable<EmployeeEntity> findByFirstName(String firstName);
    Iterable<EmployeeEntity> findByLastName(String lastName);

}

