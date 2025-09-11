package com.example.dto.mapper;

import com.example.demo.Employee;
import com.example.dto.EmployeeRequest;
import com.example.dto.EmployeeResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {
    public static EmployeeResponse toResponse(Employee employee) {
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setId(employee.getId());
        employeeResponse.setAge(employee.getAge());
        employeeResponse.setName(employee.getName());
        employeeResponse.setGender(employee.getGender());
        employeeResponse.setActive(employee.getActive());
        return employeeResponse;
    }

    public static List<EmployeeResponse> toResponse(List<Employee> employee) {
       return employee.stream().map(EmployeeMapper::toResponse).collect(Collectors.toList());
    }

    public static Employee toEntity(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        if (employeeRequest.getId() != null) {
            employee.setId(employeeRequest.getId());
        }
        employee.setAge(employeeRequest.getAge());
        employee.setName(employeeRequest.getName());
        employee.setGender(employeeRequest.getGender());
        employee.setSalary(employeeRequest.getSalary());
        if (employeeRequest.getActive() != null) {
            employee.setActive(employeeRequest.getActive());
        }
        if (employeeRequest.getCompanyId() != null) {
            employee.setCompanyId(employeeRequest.getCompanyId());
        }
        return employee;
    }
}
