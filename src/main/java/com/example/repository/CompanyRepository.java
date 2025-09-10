package com.example.repository;

import com.example.demo.Company;
import com.example.demo.Employee;
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

    public Company createCompany(Company company) {
        company.setId(companies.size() + 1);
        companies.add(company);
        return company;
    }

    public Company getCompanyById(int id) {
        return companies.stream()
                .filter(employee -> employee.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Company updateCompany(int id, Company updatedCompany) {
        for (Company company : companies) {
            if (company.getId() == id) {
                company.setName(updatedCompany.getName());
                company.setActive(updatedCompany.getActive());
                return company;
            }
        }
        return null;
    }
}