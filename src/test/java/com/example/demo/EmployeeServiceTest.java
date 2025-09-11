package com.example.demo;

import com.example.exception.DeActiveEmployeeException;
import com.example.exception.InvalidAgeEmployeeException;
import com.example.repository.EmployeeRepository;
import com.example.repository.IEmployeeRepository;
import com.example.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private IEmployeeRepository employeeRepository;

    @Test
    void should_return_an_employee_when_create_an_employee() {
        Employee employee = new Employee(null, "Tom", 20, "gender", 10000.0);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        Employee employeeResult = employeeService.createEmployee(employee);
        assertEquals(employee.getName(), employeeResult.getName());
    }

    @Test
    void should_throw_exception_when_create_an_employee_of_greater_than_65_or_less_than_18() {
        Employee employee = new Employee(null, "Tom", 66, "gender", 10000.0);
        when(employeeRepository.save(any(Employee.class))).thenThrow(InvalidAgeEmployeeException.class);
        assertThrows(InvalidAgeEmployeeException.class, () -> employeeService.createEmployee(employee));
    }

    @Test
    void should_throw_exception_when_create_an_employee_of_greater_than_30_and_salary_less_than_20000() {
        Employee employee = new Employee(null, "Tom", 30, "gender", 19999.0);
        when(employeeRepository.save(any(Employee.class))).thenThrow(InvalidAgeEmployeeException.class);
        assertThrows(InvalidAgeEmployeeException.class, () -> employeeService.createEmployee(employee));
    }

    @Test
    void should_return_employee_status_is_active_when_create_an_employee() {
        Employee employee = new Employee(null, "Tom", 30, "gender", 29999.0, true);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        assertEquals(employee.getActive(), employeeService.createEmployee(employee).getActive());
    }

    @Test
    void should_return_employee_status_is_deactive_when_delete_an_employee() {
        Employee currrentEmployee = new Employee(1, "Tom", 30, "gender", 29999.0, true);
        assertTrue(currrentEmployee.getActive());
        when(employeeRepository.findById(1)).thenReturn(Optional.of(currrentEmployee));
        employeeService.deleteEmployee(1);
        verify(employeeRepository)
                .save(argThat(employee -> !employee.getActive()));
    }

    @Test
    void should_throw_exception_without_employee_when_delete_an_employee_not_exist() {
        Employee currrentEmployee = new Employee(1, "Tom", 30, "gender", 29999.0, true);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(currrentEmployee));
        assertThrows(ResponseStatusException.class, () -> employeeService.deleteEmployee(2));
    }

    @Test
    void should_return_updated_employee_when_employee_active_status_is_active() {
        Employee targetEmployee = new Employee(1, "Tom", 30, "gender", 29999.0, true);
        Employee updatedEmployee = new Employee(1, "Tom", 32, "gender", 39999.0, true);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(targetEmployee));
        when(employeeRepository.findAll()).thenReturn(List.of(targetEmployee));
        employeeService.updateEmployee(1, updatedEmployee);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void should_throw_exception_when_employee_active_status_is_deactive() {
//        Employee targetEmployee = new Employee(1, "Tom", 30, "gender", 29999.0, false);
        Employee updatedEmployee = new Employee(1, "Tom", 32, "gender", 39999.0, false);
        assertThrows(ResponseStatusException.class, () -> employeeService.updateEmployee(1, updatedEmployee));
    }
}
