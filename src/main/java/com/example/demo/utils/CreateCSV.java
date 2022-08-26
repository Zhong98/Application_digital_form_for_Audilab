package com.example.demo.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateCSV {
    public static void createCSV(String societe, String date, int qte, int prixInter, int prixEta, int code) {
        Date date1 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String time = simpleDateFormat.format(date1);
        String fileName = "Facture " + time + ".csv";
        String filePath = "c:/test/";

        File csvFile = null;
        BufferedWriter csvWtriter = null;
        try {
            csvFile = new File(filePath + fileName);
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvFile.createNewFile();
            csvWtriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"), 1024);

            String[] titles = {"SOCIETE", "DATE", "ROLETIERS", "REFEXTERNE", "PRODUIT", "QTE", "LIBELLE", "PRIX UNITAIRE", "AXE ANALYTIQUE", "SECTION", "POSTE BUDGETAIRE", "CENTRE1", "IMMAT", "ECHEANCEMENT", "MANDAT"};
            for (int i = 0; i < titles.length; i++) {
                csvWtriter.write(titles[i]);
                csvWtriter.write(";");
            }
            csvWtriter.newLine();

            for (int i = 0; i < 2; i++) {
                csvWtriter.write("RESSOURCES;");
                csvWtriter.write(date + ";");
                csvWtriter.write("C" + societe.toUpperCase() + ";");
                csvWtriter.write("REFACT;");
                if (i == 0) {
                    csvWtriter.write("CALIB;");
                    csvWtriter.write(qte + ";");
                    csvWtriter.write("CALIBRATION;");
                    csvWtriter.write(prixEta + ";");
                } else {
                    csvWtriter.write("PRESTA;");
                    csvWtriter.write("1;");
                    csvWtriter.write("PRESTATION;");
                    csvWtriter.write(prixInter + ";");
                }
                csvWtriter.write("AUDILAB;");
                csvWtriter.write(code + "-" + societe + ";");
                csvWtriter.write("  ;");
                csvWtriter.write("  ;");
                csvWtriter.write("  ;");
                csvWtriter.write("PRV0JNET27;");
                csvWtriter.write("  ;");
                csvWtriter.newLine();
            }

            csvWtriter.flush();
            csvWtriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}