package com.ndr.glitterfin.service;

import com.ndr.glitterfin.model.Expense;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpenseServiceTest {

    private static final String EXPENSES_TEST_FILE_PATH = "src/test/resources/db/expenses.csv";
    private static final String BAD_EXPENSES_TEST_FILE_PATH = "src/test/resources/db/bad_expenses.csv";

    @BeforeAll
    static void setUp() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(EXPENSES_TEST_FILE_PATH),
                StandardOpenOption.CREATE)) {
             writer.write("ID,Timestamp,Amount,Currency,Type,Category,Merchant,Card,Location,Comment\n" +
                     "1,2024-06-19T09:30:00,100.00,USD,Need,Groceries,Hannaford,Revolut,Bar Harbor,\"weekend groceries\"\n" +
                     "2,2024-06-20T09:35:22,100.00,USD,Need,Bill,apple/icloud,Wise,WEB,\n");
        }
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(BAD_EXPENSES_TEST_FILE_PATH),
                StandardOpenOption.CREATE)) {
            writer.write("ID,Timestamp,Amount,Currency,Type,Category,Merchant,Card,Location,Comment\n" +
                    "1,2024-06-19T09:30:00,100.00,USD,Need,Groceries,Hannaford,Revolut,Bar Harbor,\"weekend groceries\"\n" +
                    "2,2024-06-20T09:35:22,100.00,USD,Bill,apple/icloud,Wise,WEB,\n");
        }
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(EXPENSES_TEST_FILE_PATH));
        Files.deleteIfExists(Paths.get(BAD_EXPENSES_TEST_FILE_PATH));
    }

    @Test
    void parseExpensesCSVFileTest() {
        ExpenseService expenseService = new ExpenseService(EXPENSES_TEST_FILE_PATH);
        List<Expense> expenses = expenseService.parseExpensesCSVFile();
        Expense expense1 = new Expense("1", "2024-06-19T09:30:00", 100.00, "USD", "Need", "Groceries", "Hannaford", "Revolut","Bar Harbor", "weekend groceries");
        Expense expense2 = new Expense("2", "2024-06-20T09:35:22", 100.00, "USD", "Need", "Bill", "apple/icloud", "Wise","WEB", "");

        assertThat(expenses).contains(expense1, expense2);
    }

    @Test
    void expenseFileNotFoundTest() {
        ExpenseService expenseService = new ExpenseService("src/test/resources/db/nonexistent.csv");
        assertThrows(RuntimeException.class, expenseService::parseExpensesCSVFile);
        assertThrows(RuntimeException.class, () -> expenseService.addExpenses(emptyList()));
    }

    @Test
    void csvParsingFailsTest() {
        ExpenseService expenseService = new ExpenseService(BAD_EXPENSES_TEST_FILE_PATH);
        assertThrows(RuntimeException.class, expenseService::parseExpensesCSVFile);
    }

    @Test
    void addExpensesTest() {
        ExpenseService expenseService = new ExpenseService(EXPENSES_TEST_FILE_PATH);
        List<Expense> expensesBeforeAddition = expenseService.parseExpensesCSVFile();
        Expense addedExpense1 = new Expense("11", "2024-06-19T09:30:00", 100.00, "USD", "Need", "Groceries", "Hannaford", "Revolut","Bar Harbor", "weekend groceries");
        Expense addedExpense2 = new Expense("22", "2024-06-20T09:35:22", 100.00, "USD", "Need", "Bill", "apple/icloud", "Wise","WEB", "");

        expenseService.addExpenses(List.of(addedExpense1, addedExpense2));
        List<Expense> updatedExpenses = expenseService.parseExpensesCSVFile();

        assertThat(updatedExpenses)
                .hasSize(expensesBeforeAddition.size()+2)
                .contains(addedExpense1, addedExpense2);
    }
}