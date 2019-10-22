/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.solution;

import charlie.feng.game.sudokumasterserv.solution.method.IMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Reference Documents:
 * http://www.sudokufans.org.cn/forums/topic/42/
 * http://www.sudokufans.org.cn/forums/topic/69/
 */

@Component
public class SudokuMaster {

    private static Logger logger = LoggerFactory.getLogger(SudokuMaster.class);

    private List<IMethod> methods = new ArrayList<>();

    public SudokuMaster() {
        this.initialMethod();
    }

    public void play(Grid grid) {
        Date startTime = new Date();
        initialMethod();
        int cycle = 0;
        grid.isChangedInCycle = true;
        while ((!grid.isResolved()) && (grid.isChangedInCycle)) {
            cycle++;
            grid.isChangedInCycle = false;
            for (IMethod method : methods) {
                method.apply(grid);
            }
        }

        if (grid.isResolved()) {
            grid.validate();
            Date endTime = new Date();

            logger.info("Successfully Resolved Grid " + grid.id);
            logger.info("Cycles Executed: " + cycle);
            logger.info("Time spend: " + (endTime.getTime() - startTime.getTime()) + "ms");
        } else {
            Date endTime = new Date();

            logger.info("Incompleted Grid: " + grid.id);
            logger.info("Imcompl Solution: " + grid.getIncompleteSolutionString());
            logger.info("Resolved Cells:   " + grid.getNumberOfResolvedCells());
            logger.info("Cycles Executed:  " + cycle);
            logger.info("Time Spend:       " + (endTime.getTime() - startTime.getTime()) + "ms");
        }
    }

    private void initialMethod() {
        Reflections reflections = new Reflections("charlie.feng.game.sudokumasterserv.solution.method");
        Set<Class<? extends IMethod>> allMethods = reflections.getSubTypesOf(IMethod.class);
        allMethods.forEach((Class<? extends IMethod> clazz) -> {
            try {
                methods.add(clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        methods.sort(Comparator.comparing(m -> m.getClass().getSimpleName()));
    }

}
