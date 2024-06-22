package com.ndr.glitterfin.service;
import com.ndr.glitterfin.model.Expense;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {

    private final String EXPENSES_FILE_PATH;

    public ExpenseService(@Value("${filepath.expenses}") String expensesFilePath) {
        this.EXPENSES_FILE_PATH = expensesFilePath;
    }

    public List<Expense> parseExpensesCSVFile() {
        List<Expense> expenses = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(EXPENSES_FILE_PATH))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.Builder.create()
                    .setHeader("ID", "Timestamp", "Amount", "Currency", "Type", "Category", "Merchant", "Card", "Location", "Comment")
//                     .setHeader(Expense.class) TODO Expense needs to extend Enum
                    .setSkipHeaderRecord(true)
                    .setTrim(true)
                    .build());

            for (CSVRecord csvRecord : csvParser) {
                Expense expense = new Expense(
                        csvRecord.get("ID"),
                        csvRecord.get("Timestamp"),
                        Double.parseDouble(csvRecord.get("Amount")),
                        csvRecord.get("Currency"),
                        csvRecord.get("Type"),
                        csvRecord.get("Category"),
                        csvRecord.get("Merchant"),
                        csvRecord.get("Card"),
                        csvRecord.get("Location"),
                        csvRecord.get("Comment")
                );

                expenses.add(expense);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return expenses;
    }

    public void addExpenses(List<Expense> newExpenses) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(EXPENSES_FILE_PATH), StandardOpenOption.APPEND);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.Builder.create()
                     .build())) {
            for (Expense expense : newExpenses) {
                csvPrinter.printRecord(expense.id(), expense.timestamp(), expense.amount(), expense.currency(),
                        expense.type(), expense.category(), expense.merchant(), expense.card(), expense.location(), expense.comment());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


