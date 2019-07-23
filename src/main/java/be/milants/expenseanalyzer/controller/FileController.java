package be.milants.expenseanalyzer.controller;

import be.milants.expenseanalyzer.service.ExpenseService;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileController {


    @Autowired
    ExpenseService expenseService;

    @PostMapping("/uploadFile")
    public void uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println(file);

        try {

            File csvFile = convert(file);
            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader(csvFile);

            // create csvReader object passing
            // file reader as a parameter
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
            CSVReader csvReader = new CSVReaderBuilder(filereader).withCSVParser(parser).build();

            String[] nextRecord;

            // we are going to read data line by line
            //ignore first 2 lines
            csvReader.readNext();
            csvReader.readNext();
            while ((nextRecord = csvReader.readNext()) != null) {
                System.out.println("Account Number : " + nextRecord[0]);
                System.out.println("Account Name : " + nextRecord[1]);
                System.out.println("Ignore : " + nextRecord[2]);
                System.out.println("Currency : " + nextRecord[3]);
                System.out.println("Ignore : " + nextRecord[4]);
                String date = nextRecord[5];
                System.out.println("Date : " + date);
                System.out.println("Description : " + nextRecord[6]);
                System.out.println("Ignore : " + nextRecord[7]);
                System.out.println("Amount : " + nextRecord[8]);
                String currentBalance = nextRecord[9];
                System.out.println("Current balance  : " + currentBalance);
                String receivingAmount = nextRecord[10];
                System.out.println("Credit (positive) : " + receivingAmount);
                String costAmount = nextRecord[11];
                System.out.println("Debit (negative) : " + costAmount);
                String counterPartAccount = nextRecord[12];
                System.out.println("Counterpart : " + counterPartAccount);
                System.out.println("ignore : " + nextRecord[13]);
                String counterPartName = nextRecord[14];
                System.out.println("Counterpart name : " + counterPartName);
                System.out.println("ignore : " + nextRecord[15]);
                System.out.println("ignore : " + nextRecord[16]);
                String statement = nextRecord[17];
                System.out.println("Statement : " + statement);

                expenseService.createCounterPart(counterPartAccount, counterPartName);
                expenseService.createCost(counterPartAccount, costAmount, statement, date, currentBalance);
                expenseService.createReceiving(counterPartAccount, receivingAmount, statement, date, currentBalance);

                for (String cell : nextRecord) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
       /* String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());*/
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
