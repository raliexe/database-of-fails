package com.backend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "images")
public class FailImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    //@Lob
    @Column(name = "content", nullable = false)
    private byte[] content;

    @Column(name = "is_main", nullable = false)
    private Boolean isMain;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fail_id", nullable = false)
    private Fail fail;

    public FailImage(String imageName, String fileName, String contentType, byte[] content) {
        this.imageName = imageName;
        this.fileName = fileName;
        this.contentType = contentType;
        this.content = content;
    }

}
