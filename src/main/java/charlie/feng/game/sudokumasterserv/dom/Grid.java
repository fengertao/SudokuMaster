package charlie.feng.game.sudokumasterserv.dom;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Grid {

    public Grid() {
    }

    @JsonIgnore
    @OneToMany(mappedBy = "grid", targetEntity = Position.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Position> positionList;

    @Id
    @Column(nullable = false, length = 81)
    @Size(min = 81, max = 81)
    private String id;

    @Column(nullable = false)
    private boolean resolvedByAi;

    @Column(length = 81)
    @Size(min = 81, max = 81)
    private String answer;

    @Column(length = 60)
    private String createdBy;

    @Column
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp createdAt;

    @Column(length = 2)
    private int difficulty;

    public void setId(String id) {
        this.id = id;
    }

    @Column
    private String comment;

    public Grid(@Size(min = 81, max = 81) String id, List<Position> positionList, boolean resolvedByAi, @Size(min = 81, max = 81) String answer, int difficulty, String createdBy, Timestamp createdAt, String comment) {
        this.id = id;
        this.positionList = positionList;
        this.resolvedByAi = resolvedByAi;
        this.answer = answer;
        this.difficulty = difficulty;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public List<Position> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<Position> positionList) {
        this.positionList = positionList;
    }

    public boolean isResolvedByAi() {
        return resolvedByAi;
    }

    public void setResolvedByAi(boolean resolvedByAi) {
        this.resolvedByAi = resolvedByAi;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
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
