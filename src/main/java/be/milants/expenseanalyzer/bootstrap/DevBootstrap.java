package be.milants.expenseanalyzer.bootstrap;

import be.milants.expenseanalyzer.controller.FileController;
import be.milants.expenseanalyzer.service.CounterPartService;
import be.milants.expenseanalyzer.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
@Slf4j
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final FileController fileController;
    private final ReportService reportService;
    private final CounterPartService counterPartService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            initData();

            //reportService.logRecurring();
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    private void initData() throws IOException, URISyntaxException {
        Path path1 = Paths.get(ClassLoader.getSystemResource("KBC_export_2016.csv").toURI());
        Path path2 = Paths.get(ClassLoader.getSystemResource("KBC_export_2017.csv").toURI());
        Path path3 = Paths.get(ClassLoader.getSystemResource("KBC_export_2018_01.csv").toURI());
        Path path4 = Paths.get(ClassLoader.getSystemResource("KBC_export_2018_02.csv").toURI());
        Path path5 = Paths.get(ClassLoader.getSystemResource("KBC_export_2019.csv").toURI());

        MultipartFile multipartFile1 = new MockMultipartFile("file1", "name1", "text/plain", Files.readAllBytes(path1));
        MultipartFile multipartFile2 = new MockMultipartFile("file2", "name2", "text/plain", Files.readAllBytes(path2));
        MultipartFile multipartFile3 = new MockMultipartFile("file3", "name3", "text/plain", Files.readAllBytes(path3));
        MultipartFile multipartFile4 = new MockMultipartFile("file4", "name4", "text/plain", Files.readAllBytes(path4));
        MultipartFile multipartFile5 = new MockMultipartFile("file5", "name5", "text/plain", Files.readAllBytes(path5));

        fileController.parseFile(multipartFile1);
        fileController.parseFile(multipartFile2);
        fileController.parseFile(multipartFile3);
        fileController.parseFile(multipartFile4);
        fileController.parseFile(multipartFile5);
    }
}
