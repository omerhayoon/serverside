package com.dev.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "youtube_videos")
@Data
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "custom_title", nullable = false)
    private String customTitle;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;
}