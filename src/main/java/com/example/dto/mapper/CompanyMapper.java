package com.example.dto.mapper;

import com.example.demo.Company;
import com.example.dto.CompanyRequest;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public static Company toEntity(CompanyRequest companyRequest) {
        Company company = new Company();
        company.setName(companyRequest.getName());
        if (companyRequest.getId()!=null) {
            company.setId(companyRequest.getId());
        }
        return company;
    }
}
