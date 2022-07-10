/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.rest;

import charlie.feng.game.sudokumasterserv.dom.GridRepository;
import charlie.feng.game.sudokumasterserv.dom.Position;
import charlie.feng.game.sudokumasterserv.dom.PositionRepository;
import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.SudokuMaster;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
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
    private final PositionRepository positionRepo;
    private final ObjectMapper mapper;

    public GridController(SudokuMaster sudokuMaster, GridRepository gridRepo, PositionRepository positionRepo, ObjectMapper mapper) {
        this.sudokuMaster = sudokuMaster;
        this.gridRepo = gridRepo;
        this.positionRepo = positionRepo;
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
    public ResponseEntity<String> saveGrid(@PathVariable String id, HttpServletRequest request) {
        //Todo validate whether this grid is resolvable.
        JsonArray gridErrorMsg = Grid.validateGridId(id);
        if (gridErrorMsg.size() != 0) {
            logger.debug("Wrong grid : " + id);
            JsonObject result = new JsonObject();
            result.add("msg", gridErrorMsg);
            return new ResponseEntity<>(result.toString(), HttpStatus.BAD_REQUEST);
        }

        Optional<charlie.feng.game.sudokumasterserv.dom.Grid> optionalGrid = gridRepo.findById(id);
        if (optionalGrid.isPresent()) {
            return new ResponseEntity<>("Grid Existing.", HttpStatus.OK);
        }

        Grid grid = new Grid(id);
        sudokuMaster.play(grid);
        charlie.feng.game.sudokumasterserv.dom.Grid domGrid = new charlie.feng.game.sudokumasterserv.dom.Grid(id, null, grid.isResolved(), grid.isResolved() ? grid.getAnswer() : null, -1, request.getUserPrincipal().getName(), new java.sql.Timestamp(System.currentTimeMillis()), "");
        gridRepo.save(domGrid);
        return new ResponseEntity<>("Grid Saved.", HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/grid/{id}/resolve",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> resolve(@PathVariable String id) {
        JsonArray gridErrorMsg = Grid.validateGridId(id);
        if (gridErrorMsg.size() != 0) {
            logger.debug("Wrong grid : " + id);
            JsonObject result = new JsonObject();
            result.addProperty("resolved", false);
            result.add("msg", gridErrorMsg);
            return new ResponseEntity<>(result.toString(), HttpStatus.BAD_REQUEST);
        }

        logger.debug("Resolving grid : " + id);
        Grid grid = new Grid(id);
        sudokuMaster.play(grid);
        if (grid.isWrongGrid()) {
            return new ResponseEntity<>(grid.getJsonResult().toString(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(grid.getJsonResult().toString(), HttpStatus.OK);
        }
    }

    @ResponseBody
    @GetMapping(value = "/positions",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> findAllPositions(HttpServletRequest request) throws Exception {
        List<Position> positionList = positionRepo.findAll();
        Map<String, List<Position>> resultMap = new HashMap<>();
        resultMap.put("positions", positionList);
        String jsonResult = mapper.writeValueAsString(resultMap);
        return new ResponseEntity<>(jsonResult, HttpStatus.OK);
    }

    @ResponseBody
    @PutMapping(value = "/position/{gridId}/{positionCode}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> savePosition(@PathVariable String gridId, @PathVariable String positionCode, HttpServletRequest request) {
        ResponseEntity<String> validateResponse = validatePosition(gridId, positionCode, true);
        if (validateResponse != null) {
            return validateResponse;
        }

        Optional<charlie.feng.game.sudokumasterserv.dom.Grid> optionalGrid = gridRepo.findById(gridId);
        charlie.feng.game.sudokumasterserv.dom.Grid domGrid;
        Position position;
        if (optionalGrid.isPresent()) {
            domGrid = optionalGrid.get();

            Optional<Position> optionalPosition = positionRepo.findByGridAndCode(domGrid, positionCode);
            if (optionalPosition.isPresent()) {
                return new ResponseEntity<>("Position existing.", HttpStatus.OK);
            }
        } else {
            Grid grid = new Grid(gridId);
            sudokuMaster.play(grid);
            domGrid = new charlie.feng.game.sudokumasterserv.dom.Grid(gridId, null, grid.isResolved(), grid.isResolved() ? grid.getAnswer() : null, -1, request.getUserPrincipal().getName(), new java.sql.Timestamp(System.currentTimeMillis()), "");
        }

        position = new Position(domGrid, positionCode, request.getUserPrincipal().getName(), new java.sql.Timestamp(System.currentTimeMillis()), "");
        positionRepo.save(position);
        return new ResponseEntity<>("Position Saved", HttpStatus.OK);

    }

    @ResponseBody
    @RequestMapping(value = "/position/{gridId}/{positionCode}/resolve",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> resolvePosition(@PathVariable String gridId, @PathVariable String positionCode) {
        ResponseEntity<String> validateResponse = validatePosition(gridId, positionCode, false);
        if (validateResponse != null) {
            return validateResponse;
        }

        //Todo validate position is derived from grid

        Grid grid = new Grid(gridId, positionCode);
        sudokuMaster.play(grid);
        JsonObject result = grid.getJsonResult();
        result.addProperty("isValid", true);
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/position/{gridId}/{positionCode}/validate",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> validatePosition(@PathVariable String gridId, @PathVariable String positionCode) {

        ResponseEntity<String> validateResponse = validatePosition(gridId, positionCode, true);
        if (validateResponse != null) {
            return validateResponse;
        }

        JsonObject result = new JsonObject();
        result.add("msg", new JsonArray());
        result.addProperty("isValid", true);
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);

    }

    private ResponseEntity<String> validatePosition(String gridId, String positionCode, boolean skipValidateNonResolvedGrid) {
        JsonArray errorList = Grid.validateGridId(gridId);
        if (errorList.size() > 0) {
            JsonObject result = new JsonObject();
            result.addProperty("isValid", false);
            result.add("msg", errorList);
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        }
        Grid grid = new Grid(gridId);
        sudokuMaster.play(grid);
        errorList = grid.validatePosition(positionCode, skipValidateNonResolvedGrid);
        if (errorList.size() > 0) {
            JsonObject result = new JsonObject();
            result.addProperty("isValid", false);
            result.add("msg", errorList);
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        }
        return null;
    }

}
