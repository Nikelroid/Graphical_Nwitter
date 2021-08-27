package reports;

import jsonContoller.jsonReports;
import jsonContoller.jsonTwittes;
import objects.objReport;
import objects.objTwitte;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.util.Collections;
import java.util.List;

public class Reports {
    jsonReports Rep = new jsonReports();
    List<objReport> report = Rep.get();
    jsonTwittes Get = new jsonTwittes();
    List<objTwitte> twittes = Get.get();

    File f = new File("reports.Reports.json");
    private static final Logger logger = LogManager.getLogger(Reports.class);
    public Reports(String reporter, String reptext, int[] mapper, int response, int counter) {
        logger.info("System: user went to reports.Reports");

                if (f.exists()) {
                    report.add(new objReport(twittes.get(mapper[response - 1]).getSerial()
                            , reporter, twittes.get(mapper[response - 1]).getText()
                            , twittes.get(mapper[response - 1]).getSender(), reptext));

                } else {
                    report = Collections.singletonList(
                            new objReport(twittes.get(mapper[response - 1]).getSerial()
                                    , reporter, twittes.get(mapper[response - 1]).getText()
                                    , twittes.get(mapper[response - 1]).getSender(), reptext));
                }
                new jsonReports(report);
                logger.info("System: Twitte reported");
                JOptionPane.showMessageDialog(null,"Twitte reported !");




    }
}
