package charlie.feng.game.sudokumasterserv.master;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Step {

    private static Logger logger = LoggerFactory.getLogger(Step.class);

    private final int index;
    private final String cell;
    private final JsonArray refCells;
    private final String preChangeCandidates;
    private final String position;
    //Todo I18n
    private final String techKey;
    private final MsgKey msgKey;
    private final String[] msgParams;
    //detail level. 0: start, end resolving. 1: generate value. 2: remove candidate value.
    private int level;

    public Step(int index, Cell cell, String preChangeCandidates, List<Cell> refCells, String position, String techKey, MsgKey msgKey, String[] msgParams) {
        this.index = index;
        JsonArray refCellArray = new JsonArray();
        if (refCells != null) {
            refCells.forEach(refCell -> refCellArray.add(refCell.locationString()));
        }
        this.refCells = refCellArray;
        this.cell = (cell == null ? "" : cell.locationString());
        this.preChangeCandidates = preChangeCandidates;
        this.position = position;
        this.msgKey = msgKey;
        this.msgParams = msgParams;
        this.techKey = techKey;
        try {
            MsgKey techMsgKey = MsgKey.valueOf(techKey);
            this.level = techMsgKey.getLevel();
        } catch (IllegalArgumentException e) {
            this.level = msgKey.getLevel();
        }

    }

    public JsonObject getJsonObject(String lang) {
        JsonObject json = new JsonObject();
        json.addProperty("index", index);
        json.addProperty("level", level);
        json.addProperty("cell", cell);
        json.addProperty("preChangeCandidates", preChangeCandidates);
        json.addProperty("position", position);
        json.addProperty("message", msgKey.getMsg(lang, msgParams));
        String techJson;
        try {
            MsgKey techMsgKey = MsgKey.valueOf(techKey);
            techJson = techMsgKey.getMsg(lang);
        } catch (IllegalArgumentException e) {
            techJson = techKey;
        }
        json.addProperty("techniques", techJson);
        json.add("refCells", refCells);
        return json;
    }
}
