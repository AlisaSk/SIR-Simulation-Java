package cvut.fel.cz.logic.model.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Day> days = new ArrayList<>();

    public void addNewDay(Day day) {
        this.days.add(day);
    }

}