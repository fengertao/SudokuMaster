/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master;

import charlie.feng.game.sudokumasterserv.master.method.IMethod;
import charlie.feng.game.sudokumasterserv.master.method.MethodBruteForce;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


/**
 * Reference Documents:
 * http://www.sudokufans.org.cn/forums/topic/42/
 * http://www.sudokufans.org.cn/forums/topic/69/
 */

@Component
public class SudokuMaster {

    private static Logger logger = LoggerFactory.getLogger(SudokuMaster.class);

    private final List<IMethod> methods = new ArrayList<>();

    public SudokuMaster() {
        this.initialMethod();
    }

    public void play(Grid grid) {
        playWithoutBruteForce(grid);
        if (grid.isResolved()) {
            grid.getResolution().logStep(null, null, grid.getPosition(), "", MsgKey.SUCCESS_RESOLVE);
            logger.info("Successfully Resolved Grid " + grid.getId());
        } else {
            int numberOfResolvedCells = grid.getNumberOfResolvedCells();
            new MethodBruteForce().apply(grid);
            grid.getResolution().logStep(null, null, grid.getPosition(), "", MsgKey.BRUTE_FORCE_RESOLVE);
            logger.info("Brute Force Resolved Grid: " + grid.getId());
            logger.info("Before Brute Force Cells Resolved Cells:   " + numberOfResolvedCells);
        }
        grid.validate();
    }

    public void playWithoutBruteForce(Grid grid) {
        initialMethod();
        grid.setChangedInCycle(true);
        grid.getResolution().logStep(null, null, grid.getPosition(), "", MsgKey.START_RESOLVE);

        while ((!grid.isResolved()) && (grid.isChangedInCycle())) {
            grid.setChangedInCycle(false);
            for (IMethod method : methods) {
                method.apply(grid);
            }
        }

    }


    private void initialMethod() {
        Reflections reflections = new Reflections(this.getClass().getPackage().getName() + ".method");
        Set<Class<? extends IMethod>> allMethods = reflections.getSubTypesOf(IMethod.class);
        allMethods.forEach((Class<? extends IMethod> clazz) -> {
            try {
                if (!clazz.getName().equals(MethodBruteForce.class.getName())) {
                    methods.add(clazz.getDeclaredConstructor().newInstance());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        methods.sort(Comparator.comparing(m -> m.getClass().getSimpleName()));
    }

}
