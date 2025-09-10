package com.example.service;

import com.example.demo.Employee;
import com.example.exception.DeActiveEmployeeException;
import com.example.exception.InvalidAgeEmployeeException;
import com.example.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;


    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        return this.employeeRepository.getEmployees(gender, page, size);
    }

    public Employee getEmployeeById(int id) {
        Employee employeeById = this.employeeRepository.getEmployeeById(id);
        if (employeeById == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employeeById;
    }

    public Employee createEmployee(Employee employee) {
        if (employee.getAge() == null) {
            throw new InvalidAgeEmployeeException("Age is required");
        } else if (employee.getAge() < 18 || employee.getAge() > 65) {
            throw new InvalidAgeEmployeeException("Age must be between 18 and 65");
        } else if (employee.getAge() > 30 && employee.getSalary() < 20000) {
            throw new InvalidAgeEmployeeException("Salary must be greater than 20000.0");
        }
        employee.setActive(true);
        return employeeRepository.createEmployee(employee);
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        Employee found = this.getEmployeeById(id);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        if (!Boolean.TRUE.equals(found.getActive())) {
            throw new DeActiveEmployeeException("Employee left company");
        }
        return employeeRepository.updateEmployee(id, updatedEmployee);
    }

    public void deleteEmployee(int id) {
        Employee found =  this.getEmployeeById(id);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        found.setActive(false);
        this.employeeRepository.updateEmployee(found.getId(), found);
    }

    public void empty() {
        this.employeeRepository.empty();
    }
}
