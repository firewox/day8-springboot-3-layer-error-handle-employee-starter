package com.example.service;

import com.example.demo.Company;
import com.example.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void empty() {
        companyRepository.empty();
    }

    public List<Company> getCompanies(Integer page, Integer size) {
        if (page != null && size != null) {
            return companyRepository.getCompanies(page, size);
        }
        return companyRepository.getCompanies();
    }

    public Company createCompany(Company company) {
        company.setActive(true);
        return companyRepository.createCompany(company);
    }
}
