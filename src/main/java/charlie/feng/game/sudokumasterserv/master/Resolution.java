package charlie.feng.game.sudokumasterserv.master;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Resolution {

    private final List<Step> steps = new ArrayList<>();

    /*
     * Log step when the Cell has not changed yet.
     */
    public void logStep(Cell cell, List<Cell> refCells, String position, String techKey, MsgKey msgKey, String... msgParams) {
        logStep(cell, (cell == null ? "" : cell.getCandidateString()), refCells, position, techKey, msgKey, msgParams);
    }

    /*
     * Log step when the Cell has changed and the caller know the candidate list before change.
     */
    public void logStep(Cell cell, String preChangeCandidates, List<Cell> refCells, String position, String techKey, MsgKey msgKey, String... msgParams) {
        steps.add(new Step(steps.size() + 1, cell, preChangeCandidates, refCells, position, techKey, msgKey, msgParams));
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
