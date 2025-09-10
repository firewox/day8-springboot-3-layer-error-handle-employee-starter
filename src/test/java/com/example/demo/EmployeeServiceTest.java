package com.example.demo;

import com.example.exception.InvalidAgeEmployeeException;
import com.example.repository.EmployeeRepository;
import com.example.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void should_return_an_employee_when_create_an_employee() {
        Employee employee = new Employee(null, "Tom", 20, "gender", 10000.0);
        when(employeeRepository.createEmployee(any(Employee.class))).thenReturn(employee);
        Employee employeeResult = employeeService.createEmployee(employee);
        assertEquals(employee.getName(), employeeResult.getName());
    }

    @Test
    void should_throw_exception_when_create_an_employee_of_greater_than_65_or_less_than_18() {
        Employee employee = new Employee(null, "Tom", 66, "gender", 10000.0);
        when(employeeRepository.createEmployee(any(Employee.class))).thenThrow(InvalidAgeEmployeeException.class);
        assertThrows(InvalidAgeEmployeeException.class, () -> employeeService.createEmployee(employee));
    }

    @Test
    void should_throw_exception_when_create_an_employee_of_greater_than_30_and_salary_less_than_20000() {
        Employee employee = new Employee(null, "Tom", 30, "gender", 19999.0);
        when(employeeRepository.createEmployee(any(Employee.class))).thenThrow(InvalidAgeEmployeeException.class);
        assertThrows(InvalidAgeEmployeeException.class, () -> employeeService.createEmployee(employee));
    }
}
