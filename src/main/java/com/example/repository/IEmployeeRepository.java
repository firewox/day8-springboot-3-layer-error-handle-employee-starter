package com.example.repository;

import com.example.demo.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IEmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findEmployeeByGender(String gender);
    Page<Employee> findEmployeeByGender(String gender, PageRequest pageable);

    Optional<Employee> findEmployeeById(Integer id);
}
