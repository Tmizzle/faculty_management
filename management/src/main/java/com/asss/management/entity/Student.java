package com.asss.management.entity;

import com.asss.management.entity.Enums.PgSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(
        name = "pgsql_enum",
        typeClass = PgSQLEnumType.class
)
public class Student {

    @Id
    @SequenceGenerator(
            name = "new_student_sequence",
            sequenceName = "new_student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "new_student_sequence"
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
            name = "jmbg",
            nullable = false,
            unique = true
    )
    private Integer jmbg;
    @Column(
            name = "index",
            nullable = false,
            unique = true
    )
    private String index;
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
    @Column(
            name = "gender",
            nullable = false
    )
    private String gender;
    @Column(
            name = "course_of_studies",
            nullable = false
    )
    private String courseOfStudies;
    @Column(
            name = "type_of_studies",
            nullable = false
    )
    private String typeOfStudies;
    @Column(
            name = "year_of_studies",
            nullable = false
    )
    private Integer yearOfStudies;
    @Column(
            name = "status",
            nullable = false
    )
    private String status;
    @Column(
            name = "updated_at",
            nullable = false
    )
    private Date updatedAt;
    @Column(
            name = "updated_by",
            nullable = false
    )
    private Date updatedBy;
    @Column(
            name = "created_at",
            nullable = false
    )
    private Date createdAt;
    @Column(
            name = "created_by",
            nullable = false
    )
    private Date createdBy;
}
