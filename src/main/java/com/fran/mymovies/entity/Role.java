package com.fran.mymovies.entity;

import com.fran.mymovies.entity.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>Representa el rol asignado a cada tipo de {@link User} y sus atributos.</p>
 * @see User
 * @see RoleName
 * @see java.io.Serializable
 *
 * @author Francisco David Manzanedo.
 * @version 1.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role implements Serializable {

    /**
     * El Id correspondiente al rol.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * El nombre {@link RoleName} del rol.
     * @see RoleName
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleName roleName;

}
