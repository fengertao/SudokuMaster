package charlie.feng.game.sudokumasterserv.dom;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import javax.persistence.*;
import java.io.IOException;
import java.sql.Timestamp;

@Entity
public class Position {

    //Todo cascadeType?
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JsonSerialize(using = GridSerializer.class)
    Grid grid;
    @Column(length = 720)
    String code;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    @Column(length = 60)
    String createdBy;
    @Column
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    Timestamp createdAt;

    public Position() {
    }

    public Position(Grid grid, String code, String createdBy, Timestamp createdAt, String comment) {
        this.grid = grid;
        this.code = code;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.comment = comment;
    }

    @Column
    String comment;

    public int getId() {

        return id;
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
        return createdAt;
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

    public GridSerializer() {
        this(null);
    }

    public GridSerializer(Class<Grid> grid) {
        super(grid);
    }

    @Override
    public void serialize(
            Grid grid, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeString(grid.id);
    }

}