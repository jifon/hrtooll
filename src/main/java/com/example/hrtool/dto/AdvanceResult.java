package com.example.hrtool.dto;

import java.util.List;

public class AdvanceResult {
    private List<String> remainingBlocks;

    public AdvanceResult(List<String> remainingBlocks) {
        this.remainingBlocks = remainingBlocks;
    }

    public List<String> getRemainingBlocks() {
        return remainingBlocks;
    }
}

