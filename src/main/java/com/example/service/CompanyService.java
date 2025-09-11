package com.example.service;

import com.example.demo.Company;
import com.example.exception.DeActiveCompanyException;
import com.example.repository.ICompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final ICompanyRepository companyRepository;
    public CompanyService(ICompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void empty() {
        companyRepository.deleteAll();
    }

    public List<Company> getCompanies(Integer page, Integer size) {
        if (page != null && size != null) {
            PageRequest pageable = PageRequest.of(page - 1, size, Sort.by("id"));
            Page<Company> all = companyRepository.findAll(pageable);
            return all.stream().toList();
        }
        return companyRepository.findAll();
    }

    public Company createCompany(Company company) {
        company.setActive(true);
        return companyRepository.save(company);
    }

    public Company getCompanyById(int id) {
        Optional<Company> companyById = companyRepository.findById((long) id);
        if (companyById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        return companyById.get();
    }

    public Company updateCompany(int id, Company updatedCompany) {
        Company found = getCompanyById(id);
        if (!Boolean.TRUE.equals(found.getActive())) {
            throw new DeActiveCompanyException("Company was deleted");
        }
        updatedCompany.setId(id);
        if (updatedCompany.getActive()==null){
            updatedCompany.setActive(found.getActive());
        }
        if (updatedCompany.getEmployees().isEmpty()){
            updatedCompany.setEmployees(found.getEmployees());
        }
        return companyRepository.save(updatedCompany);
    }

    public void deleteCompany(int id) {
        Company found =  this.getCompanyById(id);
        if (!Boolean.TRUE.equals(found.getActive())) {
            throw new DeActiveCompanyException("Company was deleted");
        }
        found.setActive(false);
        this.companyRepository.save(found);
    }
}
