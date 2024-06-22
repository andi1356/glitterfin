package com.ndr.glitterfin.controller;

import com.ndr.glitterfin.model.Expense;
import com.ndr.glitterfin.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<String> addExpenses(@RequestBody List<Expense> newExpenses) {
        expenseService.addExpenses(newExpenses);
        return ResponseEntity.ok(
                "Successfully added new expenses: " + newExpenses.stream().map(Expense::id).toList());
    }
}
