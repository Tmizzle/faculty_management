package com.asss.management.entity;

import com.asss.management.entity.Enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name = "subjects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(
        name = "pgsql_enum",
        typeClass = PgSQLEnumType.class
)
public class Subjects {

    @Id
    @SequenceGenerator(
            name = "new_subject_sequence",
            sequenceName = "new_subject_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "new_subject_sequence"
    )
    @Column(
            updatable = false,
            nullable = false
    )
    private Integer id;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "year")
    @Type( type = "pgsql_enum" )
    private Year_of_studies year;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "semester")
    @Type( type = "pgsql_enum" )
    private Semester semester;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "course")
    @Type( type = "pgsql_enum" )
    private Course_of_studies course;

    @Column(
            name = "espb",
            nullable = false
    )
    private Integer espb;

}
