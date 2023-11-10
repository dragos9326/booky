package com.booky.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author dragos
 */

@Getter
@RequiredArgsConstructor
public enum RoleEnum {
    /**
     *  NORMAL - normal user who wants to book something
     *  OWNER - owner of a property, can also book for other properties, can block his own.
     *  ADMIN - can do everything
     */

    ROLE_NORMAL("NORMAL"),
    ROLE_OWNER("OWNER"),
    ROLE_ADMIN("ADMIN");

    private final String value;

}
