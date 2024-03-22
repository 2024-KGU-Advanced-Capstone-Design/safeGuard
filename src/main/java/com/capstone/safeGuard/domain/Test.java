package com.capstone.safeGuard.domain;

import com.capstone.safeGuard.dto.TestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter @RequiredArgsConstructor @Table(name = "test")
public class Test {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column
    private String name;

    @Column
    private int age;

    public static Test toDomain(TestDTO dto){
        Test domain = new Test();
        domain.name = dto.getName();
        domain.age = dto.getAge();

        return domain;
    }

}
