package quru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Selenide;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FilesParsingTest {

    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void parsingPdfTest() throws Exception {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File pdfDownload = Selenide.$(byText("PDF download")).download();
        PDF pdf = new PDF(pdfDownload);
        assertThat(pdf.author).contains("Marc Philipp");
        assertThat(pdf.numberOfPages).isEqualTo(166);
    }

    @Test
    void parsingXlsTest() throws Exception {
        open("http://romashka2008.ru/price");
        File xlsDownload = Selenide.$(".site-main__inner a[href*='prajs_ot']").download();
        XLS xls = new XLS(xlsDownload);
        assertThat(xls.excel
                .getSheetAt(0)
                .getRow(11)
                .getCell(1)
                .getStringCellValue()).contains("693010, Сахалинская обл, Южно-Сахалинск г, им Анкудинова Федора Степановича б-р, дом № 15, корпус А");
    }

    @Test
    void parsingCsvTest() throws Exception {
        try (InputStream is = classLoader
                .getResourceAsStream("files/business-price-indexes-december-2021-quarter-csv.csv");
            CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            List<String[]> content = reader.readAll();
            assertThat(content.get(0)).contains(
                    "Series_reference",
                    "Period",
                    "Data_value",
                    "STATUS",
                    "UNITS",
                    "Subject",
                    "Group",
                    "Series_title_1",
                    "Series_title_2",
                    "Series_title_3",
                    "Series_title_4",
                    "Series_title_5");
        }
    }

    @Test
    void parsingZipTest() throws Exception {
        try (InputStream is = classLoader
                .getResourceAsStream("files/business-price-indexes-december-2021-quarter-csv.zip");
             ZipInputStream zip = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                assertThat(entry.getName()).isEqualTo("business-price-indexes-december-2021-quarter-csv.csv");

            }


        }



    }




}
