package com.backend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pdf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    //@Lob
    @Column(name = "content", nullable = false)
    private byte[] content;

    @OneToOne
    @JoinColumn(name = "fail_id", nullable = false)
    private Fail fail;

}