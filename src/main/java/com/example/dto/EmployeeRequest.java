package com.example.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.validation.annotation.Validated;

public class EmployeeRequest {
    private Integer id;
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age should be greater than 18")
    @Max(value = 65, message = "Age should be less than 65")
    private Integer age;
    private String gender;
    private Boolean active;
    private Double salary;
    private String address;
    private Integer companyId;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public EmployeeRequest() {
    }

    public EmployeeRequest(String name, Integer age, String gender, Boolean active, Double salary, String address) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.active = active;
        this.salary = salary;
        this.address = address;
    }

    public EmployeeRequest(Integer id, String name, Integer age, String gender, Double salary, Boolean active) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.active = active;
        this.salary = salary;
    }
}
