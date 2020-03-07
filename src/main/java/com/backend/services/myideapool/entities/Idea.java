package com.backend.services.myideapool.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @JsonIgnore
    private Integer user_id;

    @Setter
    private String content;

    @Setter
    private Integer impact;

    @Setter
    private Integer ease;

    @Setter
    private Integer confidence;

    private Long created_at;
    
    @Transient
    @Setter
    private Double average_score;
}
