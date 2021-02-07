package org.tenpo.test.mstenpotest.multiply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "multiply")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiplyEntity {

    @Id @GeneratedValue
    private int id;

    @Version
    private int version;

    @NotNull
    private BigDecimal numberA;
    @NotNull
    private BigDecimal numberB;
    @NotNull
    private BigDecimal result;

    @CreationTimestamp
    private LocalDateTime createdDate;


    public MultiplyEntity(BigDecimal numberA, BigDecimal numberB, BigDecimal result) {
        this.numberA = numberA;
        this.numberB = numberB;
        this.result = result;
    }
}
