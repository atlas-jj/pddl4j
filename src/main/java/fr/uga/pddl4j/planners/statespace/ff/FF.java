/*
 * Copyright (c) 2010 by Damien Pellier <Damien.Pellier@imag.fr>.
 *
 * This file is part of PDDL4J library.
 *
 * PDDL4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PDDL4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PDDL4J.  If not, see <http://www.gnu.org/licenses/>
 */

package fr.uga.pddl4j.planners.statespace.ff;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.planners.statespace.AbstractStateSpacePlanner;
import fr.uga.pddl4j.planners.statespace.search.strategy.EnforcedHillClimbing;
import fr.uga.pddl4j.planners.statespace.search.strategy.GreedyBestFirstSearch;
import fr.uga.pddl4j.planners.statespace.search.strategy.Node;
import fr.uga.pddl4j.util.SequentialPlan;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * This class implements Fast Forward planner based on Enforced Hill Climbing Algorithm and
 * Greedy Best First Search.
 *
 * @author Samuel Aaron Boyd
 * @author E. Hermellin
 * @version 2.0 - 24.01.2018
 */
public final class FF extends AbstractStateSpacePlanner {

    /**
     * The serial id of the class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new planner.
     */
    public FF() {
        super();
    }

    /**
     * Search a solution plan to a specified domain and problem.
     *
     * @param pb the problem to solve.
     */
    @Override
    public SequentialPlan search(final CodedProblem pb) {
        final Logger logger = this.getLogger();
        Objects.requireNonNull(pb);

        logger.trace("* starting enforced hill climbing\n");
        final EnforcedHillClimbing enforcedHillClimbing = new EnforcedHillClimbing(this, pb);
        Node solutionNode = enforcedHillClimbing.searchSolutionNode();

        if (solutionNode != null) {
            logger.trace("* enforced hill climbing succeeded\n");
            return enforcedHillClimbing.extract(solutionNode, pb);
        } else {
            logger.trace("* enforced hill climbing failed\n");
            logger.trace("* starting greedy best first search\n");
            final GreedyBestFirstSearch greedyBestFirstSearch = new GreedyBestFirstSearch(this, pb);
            solutionNode = greedyBestFirstSearch.searchSolutionNode();

            if (solutionNode == null) {
                logger.trace("* greedy best first search failed\n");
                return null;
            } else {
                logger.trace("* greedy best first search succeeded\n");
                return greedyBestFirstSearch.extract(solutionNode, pb);
            }
        }
    }
}
