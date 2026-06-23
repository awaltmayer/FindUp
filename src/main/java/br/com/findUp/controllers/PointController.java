package br.com.findUp.controllers;

import br.com.findUp.dtos.PointDTO;
import br.com.findUp.entities.Point;
import br.com.findUp.entities.User;
import br.com.findUp.services.PointService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ws/point")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    // Retorna apenas os pontos do usuário autenticado via JWT
    @GetMapping
    public ResponseEntity<List<Point>> getPoints() {
        User user = getLoggedUser();
        List<Point> points = pointService.findByUser(user);
        return ResponseEntity.ok(points);
    }

    // Cria um novo ponto no mapa (achado ou perdido)
    @PostMapping
    public ResponseEntity<Point> postPoint(@RequestBody PointDTO dto) throws Exception {
        User user = getLoggedUser();
        Point point = new Point();
        BeanUtils.copyProperties(dto, point);
        Point saved = pointService.save(point, user);
        return ResponseEntity.status(201).body(saved);
    }

    // Atualiza um ponto existente usando o mesmo DTO do POST
    @PutMapping("/{id}")
    public ResponseEntity<Point> putPoint(@PathVariable UUID id,
                                          @RequestBody PointDTO dto) throws Exception {
        User user = getLoggedUser();
        Point point = new Point();
        BeanUtils.copyProperties(dto, point);
        Point updated = pointService.update(id, point, user);
        return ResponseEntity.ok(updated);
    }

    // Recupera o usuário logado a partir do contexto de segurança do Spring
    private User getLoggedUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
