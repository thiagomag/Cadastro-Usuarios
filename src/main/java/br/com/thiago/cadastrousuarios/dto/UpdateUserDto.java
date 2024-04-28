package br.com.thiago.cadastrousuarios.dto;

import br.com.thiago.cadastrousuarios.enums.RoleName;

import java.util.List;

public record UpdateUserDto(

        Long userId,
        String email,
        String password,
        Boolean passwordChangeAuthorized,
        Boolean emailChangeAuthorized

) {
}
