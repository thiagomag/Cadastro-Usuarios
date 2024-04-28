package br.com.thiago.cadastrousuarios.dto;

import br.com.thiago.cadastrousuarios.enums.RoleName;

import java.util.List;

public record CreateUserDto(

        String email,
        String password,
        List<RoleName> roles

) {
}
