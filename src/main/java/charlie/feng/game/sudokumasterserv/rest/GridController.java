/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.rest;

import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.SudokuMaster;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class GridController {
    private static Logger logger = LoggerFactory.getLogger(GridController.class);

    private final SudokuMaster sudokuMaster;

    public GridController(SudokuMaster sudokuMaster) {
        this.sudokuMaster = sudokuMaster;
    }

    @ResponseBody
    @RequestMapping(value = "/grid/{id}",
            produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> resolve(@PathVariable String id) throws Exception {
        if (validateGrid(id)) {
            logger.info("Resolving grid : " + id);
            Grid grid = new Grid(id);
            sudokuMaster.play(grid);
            return new ResponseEntity<>(grid.getJsonResult().toString(2), HttpStatus.OK);
        } else {
            logger.info("Wrong grid : " + id);
            JSONObject result = new JSONObject();
            result.put("resolved", false);
            result.put("msg", "Wrong grid");
            return new ResponseEntity<>(result.toString(2), HttpStatus.BAD_REQUEST);
        }

    }

    private boolean validateGrid(String id) {
        //Todo validate there is no duplicated digital in row, column, block
        return id.length() == 81 && id.matches("[0-9]{81}");
    }
}