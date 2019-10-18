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
        Path path1 = Paths.get(ClassLoader.getSystemResource("KBC_export_2010.csv").toURI());
        Path path2 = Paths.get(ClassLoader.getSystemResource("KBC_export_2011.csv").toURI());
        Path path3 = Paths.get(ClassLoader.getSystemResource("KBC_export_2012.csv").toURI());
        Path path4 = Paths.get(ClassLoader.getSystemResource("KBC_export_2013.csv").toURI());
        Path path5 = Paths.get(ClassLoader.getSystemResource("KBC_export_2014.csv").toURI());
        Path path6 = Paths.get(ClassLoader.getSystemResource("KBC_export_2015.csv").toURI());
        Path path7 = Paths.get(ClassLoader.getSystemResource("KBC_export_2016.csv").toURI());
        Path path8 = Paths.get(ClassLoader.getSystemResource("KBC_export_2017.csv").toURI());
        Path path9 = Paths.get(ClassLoader.getSystemResource("KBC_export_2018_01.csv").toURI());
        Path path10 = Paths.get(ClassLoader.getSystemResource("KBC_export_2018_02.csv").toURI());
        Path path11 = Paths.get(ClassLoader.getSystemResource("KBC_export_2019_01.csv").toURI());
        Path path12 = Paths.get(ClassLoader.getSystemResource("KBC_export_2019_02.csv").toURI());

        MultipartFile multipartFile1 = new MockMultipartFile("file1", "name1", "text/plain", Files.readAllBytes(path1));
        MultipartFile multipartFile2 = new MockMultipartFile("file2", "name2", "text/plain", Files.readAllBytes(path2));
        MultipartFile multipartFile3 = new MockMultipartFile("file3", "name3", "text/plain", Files.readAllBytes(path3));
        MultipartFile multipartFile4 = new MockMultipartFile("file4", "name4", "text/plain", Files.readAllBytes(path4));
        MultipartFile multipartFile5 = new MockMultipartFile("file5", "name5", "text/plain", Files.readAllBytes(path5));
        MultipartFile multipartFile6 = new MockMultipartFile("file6", "name6", "text/plain", Files.readAllBytes(path6));
        MultipartFile multipartFile7 = new MockMultipartFile("file7", "name7", "text/plain", Files.readAllBytes(path7));
        MultipartFile multipartFile8 = new MockMultipartFile("file8", "name8", "text/plain", Files.readAllBytes(path8));
        MultipartFile multipartFile9 = new MockMultipartFile("file9", "name9", "text/plain", Files.readAllBytes(path9));
        MultipartFile multipartFile10 = new MockMultipartFile("file10", "name10", "text/plain", Files.readAllBytes(path10));
        MultipartFile multipartFile11 = new MockMultipartFile("file11", "name11", "text/plain", Files.readAllBytes(path11));
        MultipartFile multipartFile12 = new MockMultipartFile("file12", "name12", "text/plain", Files.readAllBytes(path12));

/*        fileController.parseFile(multipartFile1);
        fileController.parseFile(multipartFile2);
        fileController.parseFile(multipartFile3);
        fileController.parseFile(multipartFile4);
        fileController.parseFile(multipartFile5);
        fileController.parseFile(multipartFile6);
        fileController.parseFile(multipartFile7);
        fileController.parseFile(multipartFile8);
        fileController.parseFile(multipartFile9);
        fileController.parseFile(multipartFile10);
        fileController.parseFile(multipartFile11);*/
        fileController.parseFile(multipartFile12);
    }
}
