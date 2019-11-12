package charlie.feng.game.sudokumasterserv.dom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, String>,
        JpaSpecificationExecutor<Position> {

    Optional<Position> findByGridAndCode(Grid grid, String code);
}
