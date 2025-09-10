package com.example.service;

import com.example.demo.Company;
import com.example.demo.Employee;
import com.example.exception.DeActiveEmployeeException;
import com.example.repository.CompanyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Company getCompanyById(int id) {
        Company companyById = companyRepository.getCompanyById(id);
        if (companyById == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        return companyById;
    }

    public Company updateCompany(int id, Company updatedCompany) {
        Company found = getCompanyById(id);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        if (!Boolean.TRUE.equals(found.getActive())) {
            throw new DeActiveEmployeeException("Company was deleted");
        }
        return companyRepository.updateCompany(id, updatedCompany);
    }
}
