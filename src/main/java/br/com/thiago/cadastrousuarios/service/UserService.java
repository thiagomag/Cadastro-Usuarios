package br.com.thiago.cadastrousuarios.service;

import br.com.thiago.cadastrousuarios.UserController;
import br.com.thiago.cadastrousuarios.configuration.SecurityConfiguration;
import br.com.thiago.cadastrousuarios.dto.*;
import br.com.thiago.cadastrousuarios.entity.Role;
import br.com.thiago.cadastrousuarios.entity.User;
import br.com.thiago.cadastrousuarios.entity.UserDetailsImpl;
import br.com.thiago.cadastrousuarios.enums.RoleName;
import br.com.thiago.cadastrousuarios.repository.RoleRepository;
import br.com.thiago.cadastrousuarios.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final SecurityConfiguration securityConfiguration;
    private final RoleRepository roleRepository;

    // Método responsável por autenticar um usuário e retornar um token JWT
    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Gera um token JWT para o usuário autenticado
        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    // Método responsável por criar um usuário
    public void createUser(CreateUserDto createUserDto) {

        // Cria um novo usuário com os dados fornecidos
        User newUser = User.builder()
                .email(createUserDto.email())
                // Codifica a senha do usuário com o algoritmo bcrypt
                .password(securityConfiguration.passwordEncoder().encode(createUserDto.password()))
                // Atribui ao usuário uma permissão específica
                .roles(fetchUserRole(createUserDto.roles()))
                .build();

        // Salva o novo usuário no banco de dados
        userRepository.save(newUser);
    }

    private List<Role> fetchUserRole(List<RoleName> roleNameList) {
        final var roles = new ArrayList<Role>();
        roleNameList.forEach(roleEnum -> {
                    final var role = roleRepository.findByRoleName(roleEnum.name()).orElse(null);
                    if (role != null) {
                        roles.add(role);
                    }
                });
        return roles;
    }

    public List<UserResponse> getUsers() {
        final var userResponses = new ArrayList<UserResponse>();
        userRepository.findAll()
                .forEach(user -> {
                    final var userResponse = UserResponse.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .roles(user.getRoles())
                            .build();
                    userResponses.add(userResponse);
                });
        return userResponses;
    }

    public String updateUser(UpdateUserDto updateUserDto) {
        final var userId = updateUserDto.userId();
        final var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException();
        }
        if (updateUserDto.passwordChangeAuthorized() && updateUserDto.password() != null) {
            user.setPassword(securityConfiguration.passwordEncoder().encode(updateUserDto.password()));
        }
        if (updateUserDto.emailChangeAuthorized() && updateUserDto.email() != null) {
            user.setEmail(updateUserDto.email());
        }
        return String.format("Usuário com id %s atualizado", userId);
    }

    public String deleteUser(Long userId) {
        userRepository.deleteById(userId);
        return String.format("Usuário com id %s deletado", userId);
    }
}