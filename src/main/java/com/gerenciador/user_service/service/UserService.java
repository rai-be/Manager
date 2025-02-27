package com.gerenciador.user_service.service;

import com.gerenciador.user_service.model.User;
import com.gerenciador.user_service.respository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;  //só adicionei os metodos que ficaram de fora
import java.util.Optional;

@Service
public class UserService {

    // Injeção da dependência do repositório, que vai acessar os dados do usuário no banco.
    private final UserRepository userRepository;

    // Construtor que recebe o repositório para ser utilizado no serviço.
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Pega todos os usuarios do banco de dados.
    public List<User> getAllUsers() {
        // Retorna todos os usuários encontrados no banco de dados.
        return userRepository.findAll();
    }

    // Pega um usuario específico pelo nome de usuário.
    public Optional<User> getUserByUsername(String username) {
        // Retorna um usuário que tenha o mesmo nome de usuário fornecido.
        return userRepository.findByUsername(username);
    }

    // Salva um novo usuaio no banco de dados.
    public User saveUser(User user) {
        // Verifica se o nome de usuario já existe no banco.
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Usuário já existe!");
        }

        // Se o nome de usuário não existir, o usuário é salvo no banco de dados.
        return userRepository.save(user);
    }

    // Deleta um usuario usando seu id.
    public void deleteUser(String id) {
        // Verifica se o usuario com o id informado existe no banco.
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado!");
        }

        // Se o usuario existir, ele é removido do banco de dados.
        userRepository.deleteById(id);
    }

    public User getUserById(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }

    public boolean deleteUserById(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
