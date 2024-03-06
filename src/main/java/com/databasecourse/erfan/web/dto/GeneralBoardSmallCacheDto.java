package com.databasecourse.erfan.web.dto;

import java.util.ArrayList;
import java.util.List;

public class GeneralBoardSmallCacheDto {

    public double averageTimePerTask;
    public double averageScore;
    public double bestScore;
    public double averageAttemptsPerTask;

    @Override
    public String toString() {
        return "GeneralBoardSmallCacheDto{" +
                "averageTimePerTask=" + averageTimePerTask +
                ", averageScore=" + averageScore +
                ", bestScore=" + bestScore +
                ", averageAttemptsPerTask=" + averageAttemptsPerTask +
                '}';
    }
}
