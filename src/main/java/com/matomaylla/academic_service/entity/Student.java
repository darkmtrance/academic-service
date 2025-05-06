package com.matomaylla.academic_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "enrollments")
public class Student {
    @Id
    private Long id;
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private List<Enrollment> enrollments = new ArrayList<>();
}