package br.ufsm.salas.repository;

import br.ufsm.salas.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
/**
 * Camada de persistência da entidade {@link Sala}.
 *
 * <p>Não há métodos escritos manualmente porque {@link JpaRepository} já
 * fornece o CRUD básico. O segundo parâmetro, {@code Long}, informa que o id
 * da sala é do tipo Long.</p>
 */
public interface SalaRepository extends JpaRepository<Sala, Long> {
}
