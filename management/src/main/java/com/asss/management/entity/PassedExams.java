package com.asss.management.entity;

import com.asss.management.entity.Enums.PgSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name = "passed_exams")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(
        name = "pgsql_enum",
        typeClass = PgSQLEnumType.class
)
public class PassedExams {

    @Id
    @SequenceGenerator(
            name = "new_event_sequence",
            sequenceName = "new_event_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "new_event_sequence"
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

    @OneToOne(targetEntity = Subjects.class)
    @JoinColumn(
            name = "id_subject",
            referencedColumnName = "id"
    )
    private Subjects subject;

    @Column(
            name = "grade",
            nullable = false
    )
    private Integer grade;

    @OneToOne(targetEntity = Employee.class)
    @JoinColumn(
            name = "id_profesor",
            referencedColumnName = "id"
    )
    private Employee profesor;

    @OneToOne(targetEntity = Events.class)
    @JoinColumn(
            name = "id_event",
            referencedColumnName = "id"
    )
    private Events event;


}
