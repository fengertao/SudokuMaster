package charlie.feng.game.sudokumasterserv.dom;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.io.IOException;
import java.sql.Timestamp;

@Entity
public class Position {

    //Todo cascadeType?
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JsonSerialize(using = GridSerializer.class)
    private Grid grid;
    @Column(length = 720)
    private String code;

    @Id
    /*
        SequenceGenerator works in H2 but not works in MySQL
        initialValue must be manual set, liquibase loaded data do not update sequence.

        @GeneratedValue(generator = "positionsequence")
        @SequenceGenerator(name = "positionsequence", sequenceName = "positionsequence", allocationSize = 1, initialValue = 4)
     */
    /*
        Start from hibernate 5, do not suggest use GenerationType.AUTO, because hibernate default strategy change from Identity to Table.
        As suggestion per https://thorben-janssen.com/5-things-you-need-to-know-when-using-hibernate-with-mysql/, could change as below.
        but this do not work for H2 v2.x, error NULL not allowed for column "ID"
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
        @GenericGenerator(name = "native", strategy = "native")
     */
    /*
        H2 GenerationType.IDENTITY Null value issue declared fixed at early Y2022, but it seems doesn't work
        @GeneratedValue(strategy = GenerationType.IDENTITY)
     */
    /*
        previously work, but after upgrade sometime, hibernate_sequence.csv do not work on initial value for H2 database.
        @GeneratedValue(strategy = GenerationType.AUTO)
     */
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(length = 60)
    private String createdBy;
    @Column
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp createdAt;
    @Column
    private String comment;

    public Position() {
    }

    public Position(Grid grid, String code, String createdBy, Timestamp createdAt, String comment) {
        this.grid = grid;
        this.code = code;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.comment = comment;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedAt() {
        return new Timestamp(createdAt.getTime());
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

class GridSerializer extends StdSerializer<Grid> {

    GridSerializer() {
        this(null);
    }

    GridSerializer(Class<Grid> grid) {
        super(grid);
    }

    @Override
    public void serialize(
            Grid grid, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeString(grid.getId());
    }

}
