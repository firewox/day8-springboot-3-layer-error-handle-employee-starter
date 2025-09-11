package com.example.dto;


import java.util.List;

public class CompanyResponse {
    private Integer id;
    private String name;
    private Boolean active;
    private List<EmployeeResponse> employees;

    public CompanyResponse(Integer id, String name, Boolean active, List<EmployeeResponse> employees) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.employees = employees;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<EmployeeResponse> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeResponse> employees) {
        this.employees = employees;
    }
}
