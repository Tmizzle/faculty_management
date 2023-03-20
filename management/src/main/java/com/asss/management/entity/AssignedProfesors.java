package com.asss.management.entity;

import com.asss.management.entity.Enums.PgSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name = "assigned_profesors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(
        name = "pgsql_enum",
        typeClass = PgSQLEnumType.class
)
public class AssignedProfesors {

    @Id
    @SequenceGenerator(
            name = "new_assigned_profesor_sequence",
            sequenceName = "new_assigned_profesor_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "new_assigned_profesor_sequence"
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
}
