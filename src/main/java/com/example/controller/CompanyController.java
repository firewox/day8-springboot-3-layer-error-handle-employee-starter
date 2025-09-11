package com.example.controller;

import com.example.demo.Company;
import com.example.dto.CompanyRequest;
import com.example.dto.mapper.CompanyMapper;
import com.example.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public Company createCompany(@RequestBody CompanyRequest companyRequest) {
        return this.companyService.createCompany(CompanyMapper.toEntity(companyRequest));
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable int id) {
        this.companyService.deleteCompany(id);
    }
}
