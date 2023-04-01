package com.asss.management.entity;

import com.asss.management.entity.Enums.PgSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

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
}
