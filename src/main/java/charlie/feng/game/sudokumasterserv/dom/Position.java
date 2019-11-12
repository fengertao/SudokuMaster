package charlie.feng.game.sudokumasterserv.dom;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Position {
    public Position() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    //Todo cascadeType?
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    Grid grid;

    @Column(length = 60)
    String createBy;

    @Column
    Date createAt;

    @Column
    String comment;

}
