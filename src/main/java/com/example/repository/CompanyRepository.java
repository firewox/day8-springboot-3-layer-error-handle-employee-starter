package com.example.repository;

import com.example.demo.Company;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyRepository {
    private final List<Company> companies = new ArrayList<>();


    public void empty() {
        companies.clear();
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public List<Company> getCompanies(Integer page, Integer size) {
        return companies.stream().skip((long) (page - 1) * size).limit(size).toList();
    }
}
