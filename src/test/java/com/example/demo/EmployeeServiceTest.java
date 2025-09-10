package com.example.demo;

import com.example.exception.DeActiveEmployeeException;
import com.example.exception.InvalidAgeEmployeeException;
import com.example.repository.EmployeeRepository;
import com.example.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
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

    @Test
    void should_return_employee_status_is_active_when_create_an_employee() {
        Employee employee = new Employee(null, "Tom", 30, "gender", 29999.0, true);
        when(employeeRepository.createEmployee(any(Employee.class))).thenReturn(employee);
        assertEquals(employee.getActive(), employeeService.createEmployee(employee).getActive());
    }

    @Test
    void should_return_employee_status_is_deactive_when_delete_an_employee() {
        Employee currrentEmployee = new Employee(1, "Tom", 30, "gender", 29999.0, true);
        when(employeeRepository.getEmployees(null, null, null)).thenReturn(List.of(currrentEmployee));
        doAnswer(invocation -> {
            Employee employee = invocation.getArgument(0);
            employee.setActive(false);
            return null;
        }).when(employeeRepository).deleteEmployee(any(Employee.class));
        employeeService.deleteEmployee(1);
        when(employeeRepository.getEmployeeById(1)).thenReturn(currrentEmployee);
        assertEquals(false, employeeService.getEmployeeById(1).getActive());
    }

    @Test
    void should_throw_exception_without_employee_when_delete_an_employee_not_exist() {
        Employee currrentEmployee = new Employee(1, "Tom", 30, "gender", 29999.0, true);
        when(employeeRepository.getEmployeeById(1)).thenReturn(currrentEmployee);
        assertThrows(ResponseStatusException.class, () -> employeeService.deleteEmployee(1));
    }

    @Test
    void should_return_updated_employee_when_employee_active_status_is_active() {
        Employee targetEmployee = new Employee(1, "Tom", 30, "gender", 29999.0, true);
        Employee updatedEmployee = new Employee(1, "Tom", 32, "gender", 39999.0, true);
        when(employeeRepository.getEmployees(null, null, null)).thenReturn(List.of(targetEmployee));
        when(employeeRepository.updateEmployee(any(Integer.class), any(Employee.class))).thenReturn(updatedEmployee);
        assertEquals(updatedEmployee, employeeService.updateEmployee(1, updatedEmployee));
    }

    @Test
    void should_throw_exception_when_employee_active_status_is_deactive() {
//        Employee targetEmployee = new Employee(1, "Tom", 30, "gender", 29999.0, false);
        Employee updatedEmployee = new Employee(1, "Tom", 32, "gender", 39999.0, false);
        assertThrows(DeActiveEmployeeException.class, () -> employeeService.updateEmployee(1, updatedEmployee));
    }
}
