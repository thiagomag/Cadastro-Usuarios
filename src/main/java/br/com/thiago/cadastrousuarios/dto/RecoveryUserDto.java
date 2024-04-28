package br.com.thiago.cadastrousuarios.dto;

import br.com.thiago.cadastrousuarios.entity.Role;

import java.util.List;

public record RecoveryUserDto(

        Long id,
        String email,
        List<Role> roles

) {
}
