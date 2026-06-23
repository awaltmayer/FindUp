package br.com.findUp.services;

import br.com.findUp.entities.Point;
import br.com.findUp.entities.User;
import br.com.findUp.repositories.PointRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PointService {

    private final PointRepository repository;

    public PointService(PointRepository repository) {
        this.repository = repository;
    }

    // Retorna somente os pontos do usuário logado
    public List<Point> findByUser(User user) {
        return repository.findByUser(user);
    }

    // Cria um novo ponto vinculado ao usuário logado
    public Point save(Point point, User user) throws Exception {
        validate(point);
        point.setUser(user);
        return repository.save(point);
    }

    // Atualiza um ponto existente — verifica existência e dono
    public Point update(UUID id, Point updatedPoint, User user) throws Exception {
        // Verifica se o ponto existe
        Point existing = repository.findById(id)
                .orElseThrow(() -> new Exception("Ponto não encontrado!"));

        // Verifica se o ponto pertence ao usuário logado
        if (!existing.getUser().getId().equals(user.getId()))
            throw new Exception("Você não tem permissão para editar este ponto!");

        validate(updatedPoint);

        existing.setDescricao(updatedPoint.getDescricao());
        existing.setLatitude(updatedPoint.getLatitude());
        existing.setLongitude(updatedPoint.getLongitude());
        existing.setTipo(updatedPoint.getTipo());
        existing.setDetalhes(updatedPoint.getDetalhes());

        return repository.save(existing);
    }

    // Validações de negócio compartilhadas entre save e update
    private void validate(Point point) throws Exception {
        if (point.getDescricao() == null || point.getDescricao().isBlank())
            throw new Exception("Descrição do objeto é obrigatória!");

        if (point.getLatitude() == null)
            throw new Exception("Latitude é obrigatória!");

        if (point.getLongitude() == null)
            throw new Exception("Longitude é obrigatória!");

        if (point.getTipo() == null || (!point.getTipo().equals("lost") && !point.getTipo().equals("found")))
            throw new Exception("Tipo inválido! Use 'lost' para perdido ou 'found' para achado.");
    }
}
