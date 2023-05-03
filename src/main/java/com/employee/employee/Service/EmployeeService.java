package com.employee.employee.Service;

import com.employee.employee.Shared.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    EmployeeDto createEmployee(EmployeeDto employeeDetails);
    List<EmployeeDto> getAllEmployees();

    EmployeeDto getEmployeeById(long id);
    EmployeeDto getEmployeeByEmail(String email);
    boolean deleteEmployeeByEmail(String email);

    List<EmployeeDto> getEmployeeByFirstName(String firstName);
    List<EmployeeDto> getEmployeeByLastName(String lastName);
    List<EmployeeDto> getEmployeeByFirstNameAndLastName(String firstName, String lastName);
}
