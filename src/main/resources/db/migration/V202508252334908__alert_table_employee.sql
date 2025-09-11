create table if not exists employee (
                                        id int auto_increment primary key,
                                        name varchar(255) null,
    age int null,
    gender varchar(255) null,
    salary double null,
    active boolean null,
    company_id int null,
    foreign key (company_id) references company(id)
    );
