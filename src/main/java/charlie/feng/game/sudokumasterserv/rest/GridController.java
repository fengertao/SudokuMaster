/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.rest;

import charlie.feng.game.sudokumasterserv.dom.GridRepository;
import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.SudokuMaster;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("deprecation")
@RestController
@CrossOrigin
public class GridController {
    private static Logger logger = LoggerFactory.getLogger(GridController.class);

    private final SudokuMaster sudokuMaster;
    private final GridRepository gridRepo;
    private final ObjectMapper mapper;

    public GridController(SudokuMaster sudokuMaster, GridRepository gridRepo, ObjectMapper mapper) {
        this.sudokuMaster = sudokuMaster;
        this.gridRepo = gridRepo;
        this.mapper = mapper;
    }

    @ResponseBody
    @GetMapping(value = "/grids",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> findAllGrid(HttpServletRequest request) throws Exception {
        List<charlie.feng.game.sudokumasterserv.dom.Grid> gridList = gridRepo.findAll();
        Map<String, List<charlie.feng.game.sudokumasterserv.dom.Grid>> resultMap = new HashMap<>();
        resultMap.put("grids", gridList);
        String jsonResult = mapper.writeValueAsString(resultMap);
        return new ResponseEntity<>(jsonResult, HttpStatus.OK);
    }

    @ResponseBody
    @PutMapping(value = "/grid/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> saveGrid(@PathVariable String id, HttpServletRequest request) throws Exception {
        JSONArray gridErrorMsg = Grid.validateGridId(id);
        if (gridErrorMsg.length() != 0) {
            logger.debug("Wrong grid : " + id);
            JSONObject result = new JSONObject();
            result.put("msg", gridErrorMsg);
            return new ResponseEntity<>(result.toString(2), HttpStatus.BAD_REQUEST);
        }

        Grid grid = new Grid(id);
        Optional<charlie.feng.game.sudokumasterserv.dom.Grid> optionalGrid = gridRepo.findById(id);
        System.out.println("optionalGrid isPresent: " + optionalGrid.isPresent());
        sudokuMaster.play(grid);
        charlie.feng.game.sudokumasterserv.dom.Grid domGrid = new charlie.feng.game.sudokumasterserv.dom.Grid(id, null, grid.isResolved(), grid.isResolved() ? grid.getAnswer() : null, -1, request.getUserPrincipal().getName(), new java.sql.Timestamp(System.currentTimeMillis()), "");
        gridRepo.save(domGrid);
        return new ResponseEntity<>("Grid Saved", HttpStatus.OK);
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
    @RequestMapping(value = "/position/{gridId}/{position}/resolve",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> resolvePosition(@PathVariable String gridId, @PathVariable String position) throws Exception {
        ResponseEntity<String> validateResponse = validatePosition(gridId, position, false);
        if (validateResponse != null) return validateResponse;

        //Todo validate position is derived from grid

        Grid grid = new Grid(gridId, position);
        sudokuMaster.play(grid);
        return new ResponseEntity<>(grid.getJsonResult().put("isValid", true).toString(2), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/position/{gridId}/{position}/validate",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> validatePosition(@PathVariable String gridId, @PathVariable String position) throws Exception {

        ResponseEntity<String> validateResponse = validatePosition(gridId, position, true);
        if (validateResponse != null) return validateResponse;

        JSONObject result = new JSONObject();
        result.put("msg", new JSONArray());
        result.put("isValid", true);
        return new ResponseEntity<>(result.toString(2), HttpStatus.OK);

    }

    private ResponseEntity<String> validatePosition(String gridId, String position, boolean skipValidateNonResolvedGrid) throws JSONException {
        JSONArray errorList = Grid.validateGridId(gridId);
        if (errorList.length() > 0) {
            JSONObject result = new JSONObject();
            result.put("isValid", false);
            result.put("msg", errorList);
            return new ResponseEntity<String>(result.toString(2), HttpStatus.OK);
        }
        Grid grid = new Grid(gridId);
        sudokuMaster.play(grid);
        errorList = grid.validatePosition(position, skipValidateNonResolvedGrid);
        if (errorList.length() > 0) {
            JSONObject result = new JSONObject();
            result.put("isValid", false);
            result.put("msg", errorList);
            return new ResponseEntity<String>(result.toString(2), HttpStatus.OK);
        }
        return null;
    }

}