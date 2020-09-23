package charlie.feng.game.sudokumasterserv.master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Step {

    private static Logger logger = LoggerFactory.getLogger(Step.class);

    private final int index;
    private final String cell;
    private final JSONArray refCells;
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
        JSONArray refCellArray = new JSONArray();
        if (refCells != null) {
            refCells.forEach(refCell -> refCellArray.put(refCell.locationString()));
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

    public JSONObject getJSONObject(String lang) {
        try {
            JSONObject json = new JSONObject();
            json.put("index", index);
            json.put("level", level);
            json.put("cell", cell);
            json.put("preChangeCandidates", preChangeCandidates);
            json.put("position", position);
            json.put("message", msgKey.getMsg(lang, msgParams));
            Object techJson;
            try {
                MsgKey techMsgKey = MsgKey.valueOf(techKey);
                techJson = techMsgKey.getMsg(lang);
            } catch (IllegalArgumentException e) {
                techJson = techKey;
            }
            json.put("techniques", techJson);
            json.put("refCells", refCells);
            return json;
        } catch (JSONException e) {
            logger.error("Error during generate step Json", e);
        }
        return null;
    }
}
