package com.asss.management.entity;

import com.asss.management.entity.Enums.Exam_status;
import com.asss.management.entity.Enums.PgSQLEnumType;
import com.asss.management.entity.Enums.Student_status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name = "exam_status_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(
        name = "pgsql_enum",
        typeClass = PgSQLEnumType.class
)
public class ExamStatusInfo {

    @Id
    @SequenceGenerator(
            name = "new_exam_status_sequence",
            sequenceName = "new_exam_status_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "new_exam_status_sequence"
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
            name = "grade"
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

    @Column(
            name = "colloquium_one"
    )
    private Integer colloquiumOne;
    @Column(
            name = "colloquium_two"
    )
    private Integer colloquiumTwo;

    @Column(
            name = "colloquium_three"
    )
    private Integer colloquiumThree;

    @Column(
            name = "exam_points"
    )
    private Integer examPoints;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "status")
    @Type( type = "pgsql_enum" )
    private Exam_status status;

}
