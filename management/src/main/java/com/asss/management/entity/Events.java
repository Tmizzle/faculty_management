package com.asss.management.entity;

import com.asss.management.entity.Enums.PgSQLEnumType;
import com.asss.management.entity.Enums.Type_of_event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(
        name = "pgsql_enum",
        typeClass = PgSQLEnumType.class
)
public class Events {

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

    @Column(
            name = "name",
            unique = true,
            nullable = false
    )
    private String name;

    @Column(
            name = "start_date",
            nullable = false
    )
    private Date startDate;

    @Column(
            name = "end_date",
            nullable = false
    )
    private Date endDate;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "type")
    @Type( type = "pgsql_enum" )
    private Type_of_event type;

    @OneToOne
    @JoinColumn(
            name = "id_exam_period"
    )
    private Events idExamPeriod;
}
