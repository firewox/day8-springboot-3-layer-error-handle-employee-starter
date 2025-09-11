package com.example.service;

import com.example.demo.Employee;
import com.example.dto.EmployeeRequest;
import com.example.dto.EmployeeResponse;
import com.example.dto.mapper.EmployeeMapper;
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
    private EmployeeMapper employeeMapper = new EmployeeMapper();

    public EmployeeService(IEmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public List<EmployeeResponse> getEmployees(String gender, Integer page, Integer size) {
        return employeeMapper.toResponse(getEmployeeList(gender, page, size));
    }

    private List<Employee> getEmployeeList(String gender, Integer page, Integer size) {
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

    public EmployeeResponse getEmployeeById(int id) {
        Optional<Employee> employeeById1 = this.employeeRepository.findById(id);
        if (employeeById1.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employeeMapper.toResponse(employeeById1.get());
    }

    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        Employee employee = employeeMapper.toEntity(employeeRequest);
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
        Employee save = employeeRepository.save(employee);
        return employeeMapper.toResponse(save);
    }

    public EmployeeResponse updateEmployee(int id, EmployeeRequest updatedEmployeeRequest) {
        Employee updatedEmployee = employeeMapper.toEntity(updatedEmployeeRequest);
        Optional<Employee> employeeById = employeeRepository.findById(id);
        if (employeeById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        if (!Boolean.TRUE.equals(employeeById.get().getActive())) {
            throw new DeActiveEmployeeException("Employee left company");
        }
        updatedEmployee.setId(id);
        updatedEmployee.setActive(true);
        employeeRepository.save(updatedEmployee);
        return employeeMapper.toResponse(employeeRepository.findById(id).get());
    }

    public void deleteEmployee(int id) {
        Optional<Employee> employeeById = this.employeeRepository.findById(id);
        if (employeeById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        employeeById.get().setActive(false);
        this.employeeRepository.save(employeeById.get());
    }

    public void empty() {
        this.employeeRepository.deleteAll();
    }
}
