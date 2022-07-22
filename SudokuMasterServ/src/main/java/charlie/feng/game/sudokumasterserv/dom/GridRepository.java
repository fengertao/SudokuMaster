package charlie.feng.game.sudokumasterserv.dom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GridRepository extends JpaRepository<Grid, String>,
        JpaSpecificationExecutor<Grid> {

}
