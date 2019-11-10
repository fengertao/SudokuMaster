/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.rest;

import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.SudokuMaster;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("deprecation")
@RestController
@CrossOrigin
public class GridController {
    private static Logger logger = LoggerFactory.getLogger(GridController.class);

    private final SudokuMaster sudokuMaster;

    public GridController(SudokuMaster sudokuMaster) {
        this.sudokuMaster = sudokuMaster;
    }

    @ResponseBody
    @RequestMapping(value = "/grid/{id}/resolve",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> resolve(@PathVariable String id) throws Exception {
        JSONArray gridErrorMsg = Grid.validateGridId(id);
        if (gridErrorMsg.length() != 0) {
            logger.debug("Wrong grid : " + id);
            JSONObject result = new JSONObject();
            result.put("resolved", false);
            result.put("msg", gridErrorMsg);
            return new ResponseEntity<>(result.toString(2), HttpStatus.BAD_REQUEST);
        }

        logger.debug("Resolving grid : " + id);
        Grid grid = new Grid(id);
        sudokuMaster.play(grid);
        return new ResponseEntity<>(grid.getJsonResult().toString(2), HttpStatus.OK);


    }

    @ResponseBody
    @RequestMapping(value = "/grid/{id}/resolve/{position}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> resolve(@PathVariable String id, @PathVariable String position) throws Exception {
        ResponseEntity<String> validateResponse = validatePosition(id, position,false);
        if (validateResponse != null) return validateResponse;

        //Todo validate position is derived from grid

        Grid grid = new Grid(id, position);
        sudokuMaster.play(grid);
        return new ResponseEntity<>(grid.getJsonResult().put("isValid", true).toString(2), HttpStatus.OK);

    }


    @ResponseBody
    @RequestMapping(value = "/grid/{id}/validate/{position}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> validate(@PathVariable String id, @PathVariable String position) throws Exception {

        ResponseEntity<String> validateResponse = validatePosition(id, position,true);
        if (validateResponse != null) return validateResponse;

        JSONObject result = new JSONObject();
        result.put("msg", new JSONArray());
        result.put("isValid", true);
        return new ResponseEntity<>(result.toString(2), HttpStatus.OK);

    }

    private ResponseEntity<String> validatePosition( String id,  String position, boolean skipValidateNonResolvedGrid ) throws JSONException {
        JSONArray errorList = Grid.validateGridId(id);
        if (errorList.length() > 0 ) {
            JSONObject result = new JSONObject();
            result.put("isValid", false);
            result.put("msg", errorList);
            return new ResponseEntity<String>(result.toString(2), HttpStatus.OK);
        }
        Grid grid = new Grid(id);
        sudokuMaster.play(grid);
        errorList = grid.validatePosition(position, skipValidateNonResolvedGrid);
        if (errorList.length() > 0 ) {
            JSONObject result = new JSONObject();
            result.put("isValid", false);
            result.put("msg", errorList);
            return new ResponseEntity<String>(result.toString(2), HttpStatus.OK);
        }
        return null;
    }

}