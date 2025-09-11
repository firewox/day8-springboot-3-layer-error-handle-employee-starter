package com.example.service;

import com.example.demo.Employee;
import com.example.exception.DeActiveEmployeeException;
import com.example.exception.InvalidAgeEmployeeException;
import com.example.repository.EmployeeRepository;
import com.example.repository.IEmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final IEmployeeRepository employeeRepository;


    public EmployeeService(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        if(gender == null) {
            if (page == null || size == null) {
                return employeeRepository.findAll();
            } else {
                PageRequest pageable = PageRequest.of(page - 1, size, Sort.by("id"));
                return employeeRepository.findAll(pageable).stream().toList();
            }
        } else {
            if (page == null || size == null) {
                return employeeRepository.findEmployeeByGender(gender);
            } else {
                PageRequest pageable = PageRequest.of(page - 1, size, Sort.by("id"));
                Page<Employee> pageEmployee = employeeRepository.findEmployeeByGender(gender, pageable);
                return pageEmployee.stream().toList();
            }
        }
    }

    public Employee getEmployeeById(int id) {
        Optional<Employee> employeeById1 = this.employeeRepository.findById(id);
        if (employeeById1.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employeeById1.get();
    }

    public Employee createEmployee(Employee employee) {
        if (employee.getAge() == null) {
            throw new InvalidAgeEmployeeException("Age is required");
        } else if (employee.getAge() < 18 || employee.getAge() > 65) {
            throw new InvalidAgeEmployeeException("Age must be between 18 and 65");
        } else if( employee.getSalary() == null) { // check if salary is null
            throw new InvalidAgeEmployeeException("Salary is required");
        } else if (employee.getAge() > 30 && employee.getSalary() < 20000) {
            throw new InvalidAgeEmployeeException("Salary must be greater than 20000.0");
        }
        employee.setActive(true);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        Optional<Employee> employeeById = employeeRepository.findById(id);
        if (employeeById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        if (!Boolean.TRUE.equals(employeeById.get().getActive())) {
            throw new DeActiveEmployeeException("Employee left company");
        }
        updatedEmployee.setId(id);
        updatedEmployee.setActive(true);
        return employeeRepository.save(updatedEmployee);
    }

    public void deleteEmployee(int id) {
        Employee employeeById = getEmployeeById(id);
        employeeById.setActive(false);
        this.employeeRepository.save(employeeById);
    }

    public void empty() {
        this.employeeRepository.deleteAll();
    }
}
