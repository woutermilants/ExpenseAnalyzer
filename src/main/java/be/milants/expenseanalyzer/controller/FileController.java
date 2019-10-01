package be.milants.expenseanalyzer.controller;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Direction;
import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.service.CounterPartService;
import be.milants.expenseanalyzer.service.ExpenseService;
import be.milants.expenseanalyzer.service.ReportService;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class FileController {


    private final ExpenseService expenseService;
    private final CounterPartService counterPartService;
    private final ReportService reportService;

    @PostMapping("/uploadFile")
    public void uploadFile(@RequestParam("file") MultipartFile[] files) {
        //System.out.println(file);

        for (MultipartFile file : files) {
            parseFile(file);
        }
    }

    public void parseFile(MultipartFile file) {
        try {
            log.info("parsing file");
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

                CounterPart.CounterPartBuilder counterPartBuilder = CounterPart.builder();

                if (StringUtils.isBlank(counterPartAccount)) {
                    if (description.contains("BANCONTACT")) {
                        log.info("BANCONTACT");
                        counterPartAccount = "BANCONTACT";
                        counterPartBuilder.accountNumber(counterPartAccount);
                    } else if (description.contains("GELDOPNEMING")) {
                        log.info("GELDOPNEMING");
                        counterPartAccount = "GELDOPNEMING";
                        counterPartBuilder.accountNumber(counterPartAccount);
                    } else if (description.contains("MOBIELE BETALING")) {
                        log.info("MOBIELE BETALING");
                        counterPartAccount = "MOBIELE_BETALING";
                        counterPartBuilder.accountNumber(counterPartAccount);
                    } else if (description.contains("PRICOS")) {
                        log.info("PRICOS");
                        counterPartAccount = "PRICOS";
                        counterPartBuilder.accountNumber(counterPartAccount);
                        counterPartBuilder.recurringCounterPart(true);
                    } else if (description.contains("PREPAID LADING")) {
                        log.info("PREPAID LADING");
                        counterPartAccount = "PREPAID_LADING";
                        counterPartBuilder.accountNumber(counterPartAccount);
                    } else if (description.contains("AUTOMATISCH SPAREN")) {
                        log.info("AUTOMATISCH SPAREN");
                        counterPartAccount = "AUTOMATISCH_SPAREN";
                        counterPartBuilder.accountNumber(counterPartAccount);
                    } else if (description.contains("KBC-PLUSREKENING")) {
                        log.info("KBC-PLUSREKENING");
                        counterPartAccount = "KBC_PLUSREKENING";
                        counterPartBuilder.accountNumber(counterPartAccount);
                        counterPartBuilder.recurringCounterPart(true);
                    } else if (description.contains("BETALING AANKOPEN VIA MAESTRO")) {
                        log.info("BETALING AANKOPEN VIA MAESTRO");
                        counterPartAccount = "BETALING_AANKOPEN_VIA_MAESTRO";
                        counterPartBuilder.accountNumber(counterPartAccount);
                    } else if (description.contains("BETALING AANKOPEN")) {
                        log.info("BETALING AANKOPEN");
                        counterPartAccount = "BETALING_AANKOPEN";
                        counterPartBuilder.accountNumber(counterPartAccount);
                    } else if (description.contains("STORTING KBC-AUTOMAAT")) {
                        log.info("STORTING KBC-AUTOMAAT");
                        counterPartAccount = "STORTING_KBC_AUTOMAAT";
                        counterPartBuilder.accountNumber(counterPartAccount);
                    } else if (description.contains("BETALING TANKBEURT")) {
                        log.info("BETALING TANKBEURT");
                        counterPartAccount = "BETALING_TANKBEURT";
                        counterPartBuilder.accountNumber(counterPartAccount);
                    } else if (description.contains("TERUGBETALING") && description.contains("WONINGKREDIET")) {
                        log.info("TERUGBETALING WONINGKREDIET");
                        counterPartAccount = "TERUGBETALING_WONINGKREDIET";
                        counterPartBuilder.accountNumber(counterPartAccount);
                    } else if (description.contains("DOSSIERSKOSTEN")) {
                        log.info("DOSSIERSKOSTEN");
                        counterPartAccount = "DOSSIERKOSTEN";
                        counterPartBuilder.accountNumber(counterPartAccount);
                    } else if (description.contains("AUTOMATISCH BELEGGEN")) {
                        log.info("AUTOMATISCH BELEGGEN");
                        counterPartAccount = "AUTOMATISCH_BELEGGEN";
                        counterPartBuilder.accountNumber(counterPartAccount);
                        counterPartBuilder.recurringCounterPart(true);
                    } else {
                        log.warn("Empty counter part account. {}", description);
                        continue;
                    }
                } else {
                    counterPartBuilder.accountNumber(counterPartAccount.replaceAll(" ", ""));
                }
                counterPartBuilder.name(counterPartName);
                CounterPart counterPart = counterPartBuilder.build();
                counterPartService.create(counterPart);

                Direction direction = determineCostOrIncome(incomeAmount, costAmount);
                expenseService.createExpense(accountNumber.replaceAll(" ", ""), accountName, currency, date, description, currentBalance, absAmount, direction, counterPart, statement);
                //log.info("expense created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/recurring")
    public void getRecurring() {
        Map<String, List<Expense>> stringListMap = reportService.recurringPayments();
        for (String key : stringListMap.keySet()) {
            log.info(key);
            log.info(stringListMap.get(key).toString());
            log.info("----");
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
        output.append(expenseService.getAllTotalsPerCounterPart(Direction.INCOME));
        output.append("\n\n");
        output.append("Total cost per counter part : \n");
        output.append(expenseService.getAllTotalsPerCounterPart(Direction.COST));
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
