package com.example.demo;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

//    private static Employee employee(String name, int age, String gender, double salary) {
//        Employee e = new Employee();
//        e.setName(name);
//        e.setAge(age);
//        e.setGender(gender);
//        e.setSalary(salary);
//        return e;
//    }

    private Employee createJohnSmith() throws Exception {
        Gson gson = new Gson();
        Employee john = new Employee(null, "John Smith", 28, "MALE", 60000.0);
        String johnString = gson.toJson(john).toString();

        String contentAsString = mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON).content(johnString)).andReturn().getResponse().getContentAsString();
        john.setId(gson.fromJson(contentAsString, Employee.class).getId());
        return john;
    }

    private Employee createJaneDoe() throws Exception {
        Gson gson = new Gson();
        Employee jane = new Employee(null, "Jane Doe", 22, "FEMALE", 60000.0);
        String janeString = gson.toJson(jane).toString();
        String contentAsStringJane = mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON).content(janeString)).andReturn().getResponse().getContentAsString();
        jane.setId(gson.fromJson(contentAsStringJane, Employee.class).getId());
        return jane;
    }

    @BeforeEach
    void cleanEmployees() throws Exception {
        jdbcTemplate.execute("truncate table employee_db_test.employee");
        jdbcTemplate.execute("delete from employee_db_test.employee_sequence");
        jdbcTemplate.execute("alert table employee_db_test.employee AUTO_INcrement=1");
    }

    @Test
    void should_return_404_when_employee_not_found() throws Exception {
        mockMvc.perform(get("/employees/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_all_employee() throws Exception {
        createJohnSmith();
        createJaneDoe();
        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_return_employee_when_employee_found() throws Exception {
        Employee expect = createJohnSmith();

        mockMvc.perform(get("/employees/" + expect.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expect.getId()))
                .andExpect(jsonPath("$.name").value(expect.getName()))
                .andExpect(jsonPath("$.age").value(expect.getAge()))
                .andExpect(jsonPath("$.gender").value(expect.getGender()))
                .andExpect(jsonPath("$.salary").value(expect.getSalary()));
    }

    @Test
    void should_return_male_employee_when_employee_found() throws Exception {
        Employee expect = createJohnSmith();
        Employee janeDoe = createJaneDoe();

        mockMvc.perform(get("/employees?gender=male")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expect.getId()))
                .andExpect(jsonPath("$[0].name").value(expect.getName()))
                .andExpect(jsonPath("$[0].age").value(expect.getAge()))
                .andExpect(jsonPath("$[0].gender").value(expect.getGender()))
                .andExpect(jsonPath("$[0].salary").value(expect.getSalary()));
    }

    @Test
    void should_create_employee() throws Exception {
        String requestBody = """
                        {
                            "name": "John Smith",
                            "age": 28,
                            "gender": "MALE",
                            "salary": 60000
                        }
                """;

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(28))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.salary").value(60000));
    }

    @Test
    void should_return_200_with_empty_body_when_no_employee() throws Exception {
        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void should_return_200_with_employee_list() throws Exception {
        Employee expect = createJohnSmith();

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expect.getId()))
                .andExpect(jsonPath("$[0].name").value(expect.getName()))
                .andExpect(jsonPath("$[0].age").value(expect.getAge()))
                .andExpect(jsonPath("$[0].gender").value(expect.getGender()))
                .andExpect(jsonPath("$[0].salary").value(expect.getSalary()));
    }

    @Test
    void should_status_204_when_delete_employee() throws Exception {
        Employee johnSmith = createJohnSmith();

        mockMvc.perform(delete("/employees/" + johnSmith.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_status_200_when_update_employee() throws Exception {
        Employee expect = createJohnSmith();
        String requestBody = """
                            {
                                "name": "John Smith",
                                "age": 29,
                                "gender": "MALE",
                                "salary": 65000.0
                            }
                    """;

        mockMvc.perform(put("/employees/" + expect.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expect.getId()))
                .andExpect(jsonPath("$.age").value(29))
                .andExpect(jsonPath("$.salary").value(65000.0));
    }

    @Test
    void should_status_200_and_return_paged_employee_list() throws Exception {
        createJohnSmith();
        for (int i = 0; i < 5; i++) {
            createJaneDoe();
        }

        mockMvc.perform(get("/employees?page=1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void should_throw_exception_when_create_an_employee_of_greater_than_65_or_less_than_18() throws Exception {
        String requestBody = """
                            {
                                "name": "John Smith",
                                "age": 31,
                                "gender": "MALE",
                                "salary": 5000.0
                            }
                    """;

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isBadRequest());
    }

    @Test
    void should_return_employee_with_status_active_when_create_an_employee() throws Exception {
        String requestBody = """
                            {
                                "name": "John Smith",
                                "age": 29,
                                "gender": "MALE",
                                "salary": 65000.0
                            }
                    """;

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isCreated());
    }

    @Test
    void should_return_employee_not_exist_message_when_update_an_employee() throws Exception {
        String requestBody = """
                            {
                                "name": "John Smith",
                                "age": 29,
                                "gender": "MALE",
                                "salary": 65000.0
                            }
                    """;
        Employee johnSmith = createJohnSmith();
        mockMvc.perform(delete("/employees/" + johnSmith.getId()));
        mockMvc.perform(put("/employees/"+johnSmith.getId())
                .contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isBadGateway());
    }

    @Test
    void should_return_not_found_msg_when_delete_not_exist_employee() throws Exception {
        String requestBody = """
                            {
                                "name": "John Smith",
                                "age": 29,
                                "gender": "MALE",
                                "salary": 65000.0
                            }
                    """;

        mockMvc.perform(delete("/employees/"+999)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isNotFound());
    }


}
