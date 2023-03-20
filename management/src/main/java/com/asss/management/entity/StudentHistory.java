package com.asss.management.entity;

import com.asss.management.entity.Enums.PgSQLEnumType;
import com.asss.management.entity.Enums.Year_of_studies;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "student_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(
        name = "pgsql_enum",
        typeClass = PgSQLEnumType.class
)
public class StudentHistory {

    @Id
    @SequenceGenerator(
            name = "new_student_history_sequence",
            sequenceName = "new_student_history_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "new_student_history_sequence"
    )
    @Column(
            updatable = false,
            nullable = false
    )
    private Integer id;

    @OneToOne(targetEntity = Student.class)
    @JoinColumn(
            name = "id_student",
            referencedColumnName = "id"
    )
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "year_of_studies")
    @Type( type = "pgsql_enum" )
    private Year_of_studies yearOfStudies;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Date createdAt;

    @OneToOne(targetEntity = Employee.class)
    @JoinColumn(
            name = "created_by",
            referencedColumnName = "id"
    )
    private Employee createdBy;
}
