package com.example.repository;

import com.example.demo.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class EmployeeRepository {
    private final List<Employee> employees = new ArrayList<>();

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        Stream<Employee> stream = employees.stream();
        if (gender != null) {
            stream = stream.filter(employee -> employee.getGender().compareToIgnoreCase(gender) == 0);
        }
        if (page != null && size != null) {
            stream = stream.skip((long) (page - 1) * size).limit(size);
        }
        return stream.toList();
    }

    public Employee getEmployeeById(int id) {
        return employees.stream()
                .filter(employee -> employee.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Employee createEmployee(Employee employee) {
        employee.setId(employees.size() + 1);
        employees.add(employee);
        return employee;
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        for (Employee employee : this.getEmployees(null, null, null)) {
            if (employee.getId() == id) {
                employee.setName(updatedEmployee.getName());
                employee.setAge(updatedEmployee.getAge());
                employee.setGender(updatedEmployee.getGender());
                employee.setSalary(updatedEmployee.getSalary());
                employee.setActive(updatedEmployee.getActive());
                return employee;
            }
        }
        return null;
    }

    public void empty() {
        employees.clear();
    }
}
