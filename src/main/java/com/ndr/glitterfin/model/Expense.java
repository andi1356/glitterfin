package com.ndr.glitterfin.model;

public record Expense(
        String id,
        String timestamp,
        Double amount,
        String currency,
        String type,
        String category,
        String merchant,
        String card,
        String location,
        String comment
) { }
