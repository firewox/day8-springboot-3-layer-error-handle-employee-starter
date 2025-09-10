package com.example.demo;

import com.example.controller.CompanyController;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyController companyController;

    @BeforeEach
    void cleanCompanies() {
        companyController.empty();
    }

    private Company createDefaultCompany() throws Exception {
        Gson gson = new Gson();
        Company company = new Company(null, "Spring",null);
        String companyString = gson.toJson(company);

        String contentAsString = mockMvc.perform(post("/companies")
                .contentType(MediaType.APPLICATION_JSON).content(companyString)).andReturn().getResponse().getContentAsString();
        company.setId(gson.fromJson(contentAsString, Company.class).getId());
        return company;
    }

    @Test
    void should_return_created_company_when_post_companies() throws Exception {
        String requestBody = """
                {
                    "name": "Spring"
                }
                """;
        MockHttpServletRequestBuilder request = post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Spring"));
    }

    @Test
    void should_return_all_companies_when_no_param() throws Exception {
        Company spring = createDefaultCompany();

        mockMvc.perform(get("/companies").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void should_return_company_when_get_id_found() throws Exception {
        Company company = createDefaultCompany();

        MockHttpServletRequestBuilder request = get("/companies/" + company.getId())
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(company.getId()))
                .andExpect(jsonPath("$.name").value(company.getName()));
    }

    @Test
    void should_return_company_when_put_with_id_found() throws Exception {
        Company company = createDefaultCompany();
        company.setName("ByteDance");
        Gson gson = new Gson();
        MockHttpServletRequestBuilder request = put("/companies/" + company.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(company));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(company.getId()))
                .andExpect(jsonPath("$.name").value(company.getName()));
    }

    @Test
    void should_return_no_content_when_delete_id_found() throws Exception {
        Company company = createDefaultCompany();

        MockHttpServletRequestBuilder request = delete("/companies/" + company.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    void should_return_truncated_companies_when_page_size_is_limit() throws Exception {
        Company company = createDefaultCompany();
        companyController.createCompany(company);
        companyController.createCompany(company);
        companyController.createCompany(company);
        companyController.createCompany(company);
        companyController.createCompany(company);
        companyController.createCompany(company);
        companyController.createCompany(company);
        MockHttpServletRequestBuilder request = get("/companies?page=1&size=5")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void should_status_404_when_get_company_by_id_not_found() throws Exception {
        MockHttpServletRequestBuilder request = get("/companies/999")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void should_status_404_when_put_company_by_id_not_found() throws Exception {
        String requestBody = """
                {
                    "name": "Spring2"
                }
                """;
        MockHttpServletRequestBuilder request = put("/companies/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
