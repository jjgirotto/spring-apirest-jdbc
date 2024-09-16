package com.juliana.demo_park_api.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "clients_have_spaces")
@EntityListeners(AuditingEntityListener.class)
public class ClientSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "recipt_number", nullable = false, unique = true, length = 15)
    private String recipt;
    @Column(name = "plate", nullable = false, length = 8)
    private String plate;
    @Column(name = "brand", nullable = false, length = 45)
    private String brand;
    @Column(name = "model", nullable = false, length = 45)
    private String model;
    @Column(name = "color", nullable = false, length = 45)
    private String color;
    @Column(name = "date_entry", nullable = false)
    private LocalDateTime dateEntry;
    @Column(name = "date_exit")
    private LocalDateTime dateExit;
    @Column(name = "price", columnDefinition = "decimal(7,2)")
    private BigDecimal value;
    @Column(name = "discount", columnDefinition = "decimal(7,2)")
    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_space", nullable = false)
    private Space space;

    @CreatedDate
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "alter_date")
    @LastModifiedDate
    private LocalDateTime alterDate;
    @Column(name = "creation_by")
    @CreatedBy
    private String createdBy;
    @Column(name = "alter_by")
    @LastModifiedBy
    private String alterBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientSpace that = (ClientSpace) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
