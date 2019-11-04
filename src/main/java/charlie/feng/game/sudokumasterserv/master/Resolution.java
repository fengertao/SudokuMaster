package charlie.feng.game.sudokumasterserv.master;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Resolution {

    private List<Step> steps = new ArrayList<>();


    public void logStep(Cell cell, List<Cell> refCells, String position, String techKey, MsgKey msgKey, String... msgParams) {
        steps.add(new Step(steps.size() + 1, cell, refCells, position, techKey, msgKey, msgParams));
    }

    public List<Step> getSteps() {
        return steps;
    }

    public JSONArray getJson(String lang) {
        JSONArray array = new JSONArray();
        steps.forEach(step -> array.put(step.getJSONObject(lang)));
        return array;
    }
}
