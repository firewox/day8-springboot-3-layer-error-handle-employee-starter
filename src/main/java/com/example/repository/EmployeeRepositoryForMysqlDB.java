package com.example.repository;

import com.example.demo.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class EmployeeRepositoryForMysqlDB implements IEmployeeRepositoryFinal {

    private IEmployeeRepository employeeRepository;


    @Override
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
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        return employeeRepository.save(updatedEmployee);
    }

    public void empty() {
        employeeRepository.deleteAll();
    }
}
