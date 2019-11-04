package charlie.feng.game.sudokumasterserv.master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Step {

    private static Logger logger = LoggerFactory.getLogger(Step.class);

    private int index;
    private int level; //detail level. 0: start, end resolving. 1: generate value. 2: remove candidate value.
    private String cell;
    private JSONArray refCells;
    private String position;
    private String techKey; //Todo i18n
    private MsgKey msgKey;
    private String[] msgParams;

    public Step(int index, Cell cell, List<Cell> refCells, String position, String techKey, MsgKey msgKey, String[] msgParams) {
        this.index = index;
        JSONArray refCellArray = new JSONArray();
        if (refCells != null) {
            refCells.forEach(refCell -> refCellArray.put(refCell.locationString()));
        }
        this.refCells = refCellArray;
        this.level = msgKey.getLevel();
        this.cell = (cell == null ? "" : cell.locationString());
        this.position = position;
        this.techKey = techKey;
        this.msgKey = msgKey;
        this.msgParams = msgParams;

    }

    public String getMsg(String lang) {
        if (lang.startsWith("ZH")) {
            return String.format(msgKey.getCnKey(), msgParams);
        } else {
            return String.format(msgKey.getEnKey(), msgParams);
        }
    }

    public JSONObject getJSONObject(String lang) {
        try {
            JSONObject json = new JSONObject();
            json.put("index", index);
            json.put("level", level);
            json.put("cell", cell);
            json.put("position", position);
            json.put("message", getMsg(lang));
            json.put("techniques", techKey);
            json.put("refCells", refCells);
            return json;
        } catch (JSONException e) {
            logger.error("Error during generate step Json", e);
        }
        return null;
    }
}
