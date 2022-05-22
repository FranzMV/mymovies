package com.fran.mymovies.entity;

import com.fran.mymovies.entity.enums.ListTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ListType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private ListTypeName listTypeName;

    public ListType(ListTypeName listTypeName) {
        this.listTypeName = listTypeName;
    }
}
