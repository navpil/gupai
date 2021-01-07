package io.github.navpil.gupai.jielong.player.evaluators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Sorts a list according to the priorities
 */
public class PriorityUtil {

    public static <T> List<T> sort(List<Integer> priorities, List<T> list) {
        if (priorities.size() != list.size()) {
            throw new IllegalArgumentException("Priority list is different in size to other list");
        }
        final ArrayList<ElementWithPriority<T>> elements = new ArrayList<>();
        for (int i = 0; i < priorities.size(); i++) {
            elements.add(new ElementWithPriority<>(list.get(i), priorities.get(i)));
        }
        Collections.sort(elements);
        return elements.stream().map(e -> e.element).collect(Collectors.toList());
    }

    private static class ElementWithPriority<T> implements Comparable<ElementWithPriority<T>>{
        final T element;
        final Integer priority;

        private ElementWithPriority(T element, Integer priority) {
            this.element = element;
            this.priority = priority;
        }

        @Override
        public int compareTo(ElementWithPriority<T> o) {
            return this.priority.compareTo(o.priority);
        }
    }


}
