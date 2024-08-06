package com.mooland.bj;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BjGameDto {
    private int games;
    private int win;
    private int lose;
    private double rate;
    private String playerName;
    private String vsName;

    //승률 Getter
    public double getWinRate() {
        if (games == 0) {
            return 0;
        }
        double winRate = (double) win / games * 100;
        return Double.parseDouble(String.format("%.1f", winRate));
    }

}
