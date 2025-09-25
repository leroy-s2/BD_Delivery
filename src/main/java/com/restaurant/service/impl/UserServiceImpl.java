package com.restaurant.service.impl;

import com.restaurant.controller.exceptions.BusinessException;
import com.restaurant.controller.exceptions.ResourceNotFoundException;
import com.restaurant.dto.UserDTO;
import com.restaurant.entity.User;
import com.restaurant.entity.UserRole;
import com.restaurant.mappers.UserMapper;
import com.restaurant.repository.UserRepository;
import com.restaurant.service.UserService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO create(UserDTO dto) throws ServiceException {
        try {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new BusinessException("El email ya está registrado");
            }

            User user = userMapper.toEntity(dto);

            // 👇 Respetar el role que llega, si no viene, default a CUSTOMER
            if (dto.getRole() != null && !dto.getRole().isEmpty()) {
                try {
                    user.setRole(UserRole.valueOf(dto.getRole().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    user.setRole(UserRole.CUSTOMER); // fallback
                }
            } else {
                user.setRole(UserRole.CUSTOMER);
            }

            user.setCreatedAt(LocalDateTime.now());

            // Codificar la contraseña antes de guardar
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            } else {
                throw new BusinessException("La contraseña es obligatoria");
            }

            User savedUser = userRepository.save(user);
            return userMapper.toDTO(savedUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("Error al crear usuario", e);
        }
    }


    @Override
    public UserDTO update(Long id, UserDTO dto) throws ServiceException {
        try {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            // Update fields (excluding password and email for security)
            existingUser.setFullName(dto.getFullName());
            existingUser.setPhone(dto.getPhone());
            existingUser.setAddress(dto.getAddress());

            User updatedUser = userRepository.save(existingUser);
            return userMapper.toDTO(updatedUser);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar usuario", e);
        }
    }

    @Override
    public UserDTO findById(Long id) throws ServiceException {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            return userMapper.toDTO(user);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar usuario", e);
        }
    }

    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            if (!userRepository.existsById(id)) {
                throw new ResourceNotFoundException("Usuario no encontrado");
            }
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar usuario", e);
        }
    }

    @Override
    public List<UserDTO> findAll() throws ServiceException {
        try {
            List<User> users = userRepository.findAll();
            return userMapper.toDTOs(users);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener usuarios", e);
        }
    }

    @Override
    public UserDTO findByEmail(String email) throws ServiceException {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            return user.map(userMapper::toDTO).orElse(null);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar usuario por email", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) throws ServiceException {
        try {
            return userRepository.existsByEmail(email);
        } catch (Exception e) {
            throw new ServiceException("Error al verificar email", e);
        }
    }



    @Override
    public UserDTO authenticate(String email, String password) throws ServiceException {
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                throw new BusinessException("Credenciales inválidas");
            }

            User user = userOpt.get();
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BusinessException("Credenciales inválidas");
            }

            return userMapper.toDTO(user);
        } catch (Exception e) {
            throw new ServiceException("Error en autenticación", e);
        }
    }
}