package com.juliana.demo_park_api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spaces")
@EntityListeners(AuditingEntityListener.class)
public class Space implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 4)
    private String code;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusSpace status;

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

    public enum StatusSpace {
        FREE, OCCUPIED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Space space = (Space) o;
        return Objects.equals(id, space.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
