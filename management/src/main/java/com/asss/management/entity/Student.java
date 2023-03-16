package com.asss.management.entity;

import com.asss.management.entity.Enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
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
            name = "renewed",
            nullable = false
    )
    private Boolean renewed;
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

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "gender")
    @Type( type = "pgsql_enum" )
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "course_of_studies")
    @Type( type = "pgsql_enum" )
    private Course_of_studies courseOfStudies;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "type_of_studies")
    @Type( type = "pgsql_enum" )
    private Type_of_studies typeOfStudies;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "year_of_studies")
    @Type( type = "pgsql_enum" )
    private Year_of_studies yearOfStudies;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "status")
    @Type( type = "pgsql_enum" )
    private Student_status status;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private Date updatedAt;
    @Column(
            name = "updated_by",
            nullable = false
    )
    private Integer updatedBy;
    @Column(
            name = "created_at",
            nullable = false
    )
    private Date createdAt;
    @Column(
            name = "created_by",
            nullable = false
    )
    private Integer createdBy;
}
