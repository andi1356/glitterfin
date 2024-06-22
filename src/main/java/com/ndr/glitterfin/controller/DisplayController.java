package com.ndr.glitterfin.controller;

import com.ndr.glitterfin.model.Expense;
import com.ndr.glitterfin.service.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DisplayController {

    private ExpenseService expenseService;

    public DisplayController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Expense> expenses = expenseService.parseExpensesCSVFile();
        model.addAttribute("expenses", expenses);
        return "expenses";
    }
}
