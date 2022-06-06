package com.fran.mymovies.entity;

import com.fran.mymovies.entity.enums.ListTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>Representa el tipo de lista que tiene por defecto un {@link User y sus atributos.}</p>
 * @see User
 * @see ListTypeName
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
public class ListType implements Serializable {

    /**
     * El Id de la lista.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * {@link Enum} correspondiente al nombre {@link ListTypeName} de la lista.
     * @see ListTypeName
     * @see Enum
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private ListTypeName listTypeName;

}
