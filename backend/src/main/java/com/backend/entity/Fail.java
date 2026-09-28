package com.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "fails")
public class Fail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "description", length = 10000, nullable = false)
    private String description;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser user;

    @OneToMany(mappedBy = "fail", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FailImage> images;

    @OneToOne(mappedBy = "fail", cascade = CascadeType.ALL)
    private Pdf pdf;

    @OneToMany(mappedBy = "fail", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Like> likes;

    @OneToMany(mappedBy = "fail", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Fail(String name, String description, LocalDate date) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.images = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Fail(Long id) {
        this.id = id;
    }
}
