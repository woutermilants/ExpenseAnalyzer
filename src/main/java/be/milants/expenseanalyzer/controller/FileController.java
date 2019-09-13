package be.milants.expenseanalyzer.controller;

import be.milants.expenseanalyzer.data.Direction;
import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.service.ExpenseService;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class FileController {


    @Autowired
    ExpenseService expenseService;

    @PostMapping("/uploadFiles")
    public void uploadFile(@RequestParam("file") MultipartFile[] files) {
        for (MultipartFile file : files) {
            uploadFile(file);
        }
    }

    @PostMapping("/uploadFile")
    public void uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println(file);

        try {

            File csvFile = convert(file);
            // Create an object of filereader
            // class with CSV file as a parameter.
            //FileReader filereader = new FileReader(csvFile);
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(csvFile), StandardCharsets.ISO_8859_1);
            // create csvReader object passing
            // file reader as a parameter
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
            CSVReader csvReader = new CSVReaderBuilder(inputStreamReader).withCSVParser(parser).build();

            String[] nextRecord;

            // we are going to read data line by line
            //ignore first 2 lines
            csvReader.readNext();
            csvReader.readNext();
            while ((nextRecord = csvReader.readNext()) != null) {
                String accountNumber = nextRecord[0];
                String accountName = nextRecord[1];
                String currency = nextRecord[3];
                String date = nextRecord[5];
                String description = nextRecord[6];
                String absAmount = nextRecord[8];
                String currentBalance = nextRecord[9];
                String incomeAmount = nextRecord[10];
                String costAmount = nextRecord[11];
                String counterPartAccount = nextRecord[12];
                String counterPartName = nextRecord[14];
                String statement = nextRecord[17];

                Direction direction = determineCostOrIncome(incomeAmount, costAmount);
                expenseService.createExpense(accountNumber, accountName, currency, date, description, currentBalance, absAmount, direction, counterPartAccount, counterPartName, statement);
                for (String cell : nextRecord) {
                    //     System.out.print(cell + "\t");
                }
                //  System.out.println();
            }
            expenseService.getExpensesByMonth(Direction.COST);
            expenseService.getExpensesByMonth(Direction.INCOME);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/report")
    public String getAllReports() {
        StringBuilder output = new StringBuilder();
        output.append("Costs per month \n");
        output.append(expenseService.getExpensesByMonth(Direction.COST));
        output.append("\n\n");
        output.append("Income per month \n");
        output.append(expenseService.getExpensesByMonth(Direction.INCOME));
        output.append("\n\n");
        output.append("Total income per counter part : \n");
        output.append(expenseService.getTotalPerCounterPart(Direction.INCOME));
        output.append("\n\n");
        output.append("Total cost per counter part : \n");
        output.append(expenseService.getTotalPerCounterPart(Direction.COST));
        output.append("Recurring payments : \n");
        output.append(expenseService.getGroupedByCounterPart(Direction.COST));

        return output.toString();
    }

    private Direction determineCostOrIncome(String incomeAmount, String costAmount) {
        if (StringUtils.isNotBlank(incomeAmount) && StringUtils.isBlank(costAmount)) {
            return Direction.INCOME;
        } else if (StringUtils.isBlank(incomeAmount) && StringUtils.isNotBlank(costAmount)) {
            return Direction.COST;
        }
        log.error("Either income or cost should be filled in. Income Amount {}, Cost Amount {}", incomeAmount, costAmount);
        return null;
    }

    public static File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
