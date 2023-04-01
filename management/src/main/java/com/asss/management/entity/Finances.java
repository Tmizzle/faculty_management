package com.asss.management.entity;

import com.asss.management.entity.Enums.Currency;
import com.asss.management.entity.Enums.Finances_status;
import com.asss.management.entity.Enums.PgSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "finances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(
        name = "pgsql_enum",
        typeClass = PgSQLEnumType.class
)
public class Finances {

    @Id
    @SequenceGenerator(
            name = "new_finances_sequence",
            sequenceName = "new_finances_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "new_finances_sequence"
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
    @Column(
            name = "note",
            nullable = false
    )
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "currency")
    @Type( type = "pgsql_enum" )
    private Currency currency;
    @Column(
            name = "amount",
            nullable = false
    )
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "status")
    @Type( type = "pgsql_enum" )
    private Finances_status status;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Date createdAt;

    @OneToOne(targetEntity = Exams.class)
    @JoinColumn(
            name = "id_exam",
            referencedColumnName = "id"
    )
    private Exams idExam;
}
