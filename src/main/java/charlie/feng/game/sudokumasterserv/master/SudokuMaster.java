/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master;

import charlie.feng.game.sudokumasterserv.master.method.IMethod;
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
        grid.resolution.logStep(null, null, grid.getPosition(), "", MsgKey.START_RESOLVE);

        while ((!grid.isResolved()) && (grid.isChangedInCycle)) {
            cycle++;
            grid.isChangedInCycle = false;
            for (IMethod method : methods) {
                method.apply(grid);
            }
        }

        Date endTime = new Date();

        if (grid.isResolved()) {
            grid.resolution.logStep(null, null, grid.getPosition(), "", MsgKey.SUCCESS_RESOLVE);
            logger.info("Successfully Resolved Grid " + grid.id);
            logger.info("Executed " + cycle + " Cycles in " + (endTime.getTime() - startTime.getTime()) + "ms");
        } else {
            grid.resolution.logStep(null, null, grid.getPosition(), "", MsgKey.ABORT_RESOLVE);
            logger.info("Incompleted Grid: " + grid.id);
            logger.info("Imcompl Solution: " + grid.getIncompleteAnswer());
            logger.info("Resolved Cells:   " + grid.getNumberOfResolvedCells());
            logger.info("Executed " + cycle + " Cycles in " + (endTime.getTime() - startTime.getTime()) + "ms");
        }
    }

    private void initialMethod() {
        Reflections reflections = new Reflections(this.getClass().getPackage().getName() + ".method");
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
