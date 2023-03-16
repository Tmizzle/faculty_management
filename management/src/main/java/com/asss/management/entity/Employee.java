package com.asss.management.entity;

import com.asss.management.entity.Enums.*;
import com.asss.management.service.dto.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(
        name = "pgsql_enum",
        typeClass = PgSQLEnumType.class
)
public class Employee {

    @Id
    @SequenceGenerator(
            name = "new_employee_sequence",
            sequenceName = "new_employee_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "new_employee_sequence"
    )
    @Column(
            updatable = false,
            nullable = false
    )
    private Integer id;
    @Column(
            name = "first_name",
            nullable = false
    )
    private String firstName;
    @Column(
            name = "last_name",
            nullable = false
    )
    private String lastName;
    @Column(
            name = "middle_name",
            nullable = false
    )
    private String middleName;
    @Column(
            name = "email",
            nullable = false,
            unique = true
    )
    private String email;
    @Column(
            name = "jmbg",
            nullable = false,
            unique = true
    )
    private Integer jmbg;
    @Column(
            name = "password",
            nullable = false
    )
    private String password;
    @Column(
            name = "old_password"
    )
    private String oldPassword;
    @Column(
            name = "birth_date",
            nullable = false
    )
    private Date birthDate;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "gender")
    @Type( type = "pgsql_enum" )
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "employee_category")
    @Type( type = "pgsql_enum" )
    private Employee_category employeeCategory;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "status")
    @Type( type = "pgsql_enum" )
    private Employee_status status;

    @Column(
            name = "updated_at"
    )
    private Date updatedAt;
    @Column(
            name = "updated_by"
    )
    private Integer updatedBy;
    @Column(
            name = "created_at"
    )
    private Date createdAt;
}
