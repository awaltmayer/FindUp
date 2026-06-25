package br.com.findUp.dtos;

import br.com.findUp.entities.UserType;
import java.util.UUID;

public record SignupResponseDTO(UUID id, String name, String email, UserType type) {}