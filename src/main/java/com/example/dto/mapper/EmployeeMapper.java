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
        BeanUtils.copyProperties(employee, employeeResponse);
        return employeeResponse;
    }

    public static List<EmployeeResponse> toResponse(List<Employee> employee) {
       return employee.stream().map(EmployeeMapper::toResponse).collect(Collectors.toList());
    }

    public static Employee toEntity(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        employee.setId(employeeRequest.getId());
        employee.setAge(employeeRequest.getAge());
        employee.setName(employeeRequest.getName());
        employee.setGender(employeeRequest.getGender());
        employee.setSalary(employeeRequest.getSalary());
        employee.setActive(employeeRequest.getActive());
        return employee;
    }
}
