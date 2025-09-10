package com.example.controller;

import com.example.demo.Company;
import com.example.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @DeleteMapping("/all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void empty() {
        companyService.empty();
    }

    @GetMapping
    public List<Company> getCompanies(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return this.companyService.getCompanies(page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company createCompany(@RequestBody Company company) {
        return this.companyService.createCompany(company);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Company updateCompany(@PathVariable int id, @RequestBody Company updatedCompany) {
        return this.companyService.updateCompany(id, updatedCompany);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Company getCompanyById(@PathVariable int id) {
        return this.companyService.getCompanyById(id);
    }

//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteCompany(@PathVariable int id) {
//        this.companyService.deleteCompany(id);
//        Company found = null;
//        for (Company c : companies) {
//            if (c.getId().equals(id)) {
//                found = c;
//                break;
//            }
//        }
//        if (found != null) {
//            companies.remove(found);
//            return;
//        }
//        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
//    }
}
