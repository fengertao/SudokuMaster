package charlie.feng.game.sudokumasterserv.master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Step {

    private int index;
    private int level; //detail level. 0: start, end resolving. 1: generate value. 2: remove candidate value.
    private Cell cell;
    private List<Cell> refCells;
    private String position;
    private String techKey; //Todo i18n
    private MsgKey msgKey;
    private String[] msgParams;

    public Step(int index, Cell cell, List<Cell> refCells, String position, String techKey, MsgKey msgKey, String[] msgParams) {
        this.index = index;
        this.refCells = refCells;
        this.level = msgKey.getLevel();
        this.cell = cell;
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
            JSONArray refCellArray = new JSONArray();
            if (refCells != null) {
                refCells.forEach(cell -> refCellArray.put(cell.locationString()));
            }

            JSONObject json = new JSONObject();
            json.put("index", index);
            json.put("level", level);
            json.put("cell", cell == null ? "" : cell.locationString());
            json.put("position", position);
            json.put("message", getMsg(lang));
            json.put("techniques", techKey);
            json.put("refCells", refCellArray);

            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
