package br.com.findUp.repositories;

import br.com.findUp.entities.Point;
import br.com.findUp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PointRepository extends JpaRepository<Point, UUID> {

    // Retorna apenas os pontos do usuário logado
    List<Point> findByUser(User user);
}
