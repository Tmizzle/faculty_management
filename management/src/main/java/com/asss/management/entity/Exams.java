package com.asss.management.entity;

import com.asss.management.entity.Enums.PgSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "exams")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(
        name = "pgsql_enum",
        typeClass = PgSQLEnumType.class
)
public class Exams {

    @Id
    @SequenceGenerator(
            name = "new_exam_sequence",
            sequenceName = "new_exam_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "new_exam_sequence"
    )
    @Column(
            updatable = false,
            nullable = false
    )
    private Integer id;

    @OneToOne(targetEntity = Employee.class)
    @JoinColumn(
            name = "id_profesor",
            referencedColumnName = "id"
    )
    private Employee profesor;

    @OneToOne(targetEntity = Subjects.class)
    @JoinColumn(
            name = "id_subject",
            referencedColumnName = "id"
    )
    private Subjects subject;

    @OneToOne(targetEntity = Student.class)
    @JoinColumn(
            name = "id_student",
            referencedColumnName = "id"
    )
    private Student student;

    @OneToOne(targetEntity = Events.class)
    @JoinColumn(
            name = "id_events",
            referencedColumnName = "id"
    )
    private Events event;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Date createdAt;
}
