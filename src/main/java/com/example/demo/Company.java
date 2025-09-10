package com.example.demo;

public class Company {
    private Integer id;
    private String name;
    private Boolean active;
    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setId(int id) {
        this.id = id;
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

    public Company() {
    }

    public Company(Integer id, String name, Boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }
}
