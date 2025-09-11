package com.example.repository;

import com.example.demo.Employee;

import java.util.List;

public interface IEmployeeRepositoryFinal {
    public List<Employee> getEmployees(String gender, Integer page, Integer size);

    public Employee getEmployeeById(int id);

    public Employee createEmployee(Employee employee);

    public Employee updateEmployee(int id, Employee updatedEmployee);

    public void empty();
}
