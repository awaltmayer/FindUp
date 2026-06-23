package br.com.findUp.services;

import br.com.findUp.entities.User;
import br.com.findUp.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public User save(User newUser) throws Exception {
        if (newUser == null)
            throw new Exception("Objeto Nulo!");

        if (newUser.getName() == null || newUser.getName().isBlank())
            throw new Exception("Nome informado inválido!");
        newUser.setName(newUser.getName().trim());

        if (newUser.getEmail() == null || newUser.getEmail().isBlank())
            throw new Exception("E-mail informado inválido!");
        newUser.setEmail(newUser.getEmail().trim().toLowerCase());

        // Validação de e-mail: deve conter @ e pelo menos dois domínios (ex: gmail.com, bol.com.br)
        if (!newUser.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+(\\.[^@\\s]+)*$"))
            throw new Exception("E-mail inválido! Use um formato válido como usuario@gmail.com");

        if (repository.existsByEmail(newUser.getEmail()))
            throw new Exception("Já existe usuário cadastrado com este e-mail!");

        if (newUser.getPassword() == null || newUser.getPassword().length() < 8)
            throw new Exception("Senha deve ter no mínimo 8 caracteres!");

        // Validação de senha: ao menos uma letra maiúscula, uma minúscula e um número
        if (!newUser.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$"))
            throw new Exception("Senha fraca! A senha deve conter ao menos uma letra maiúscula, uma minúscula e um número.");

        newUser.setPassword(encoder.encode(newUser.getPassword()));

        if (newUser.getType() == null)
            throw new Exception("Tipo de usuário informado inválido!");

        return repository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com este e-mail!"));
    }
}
