package charlie.feng.game.sudokumasterserv.dom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, String>,
        JpaSpecificationExecutor<Position> {

    /**
     * Find the Position by Grid id and Code
     * @param grid the grid id
     * @param code the code of position
     * @return the grid position
     */
    Optional<Position> findByGridAndCode(Grid grid, String code);
}
