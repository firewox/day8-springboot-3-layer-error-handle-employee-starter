package com.example.dto.mapper;

import com.example.demo.Company;
import com.example.dto.CompanyRequest;
import com.example.dto.CompanyResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyMapper {
    public static CompanyResponse toResponse(Company company) {
        return new CompanyResponse(company.getId(), company.getName(), company.getActive(), EmployeeMapper.toResponse(company.getEmployees()));
    }
    public static List<CompanyResponse> toResponse(List<Company> company) {
        return company.stream().map(CompanyMapper::toResponse).collect(Collectors.toList());
    }
    public static Company toEntity(CompanyRequest companyRequest) {
        Company company = new Company();
        company.setName(companyRequest.getName());
        if (companyRequest.getId()!=null) {
            company.setId(companyRequest.getId());
        }
        return company;
    }
}
