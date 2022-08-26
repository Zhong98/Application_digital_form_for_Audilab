package com.example.demo.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreatePDF {
    public static String createPDF(String technicien,String societe,String centre,String date,String timeStart,String timeEnd,String pause,String actions,String refacturer,String prixInter,String poste,String id,String signClient,String signTech,String qte){
        Document document = new Document(PageSize.A4);
        document.open();
        Date date1 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String time = simpleDateFormat.format(date1);
        // file module path
        String inputFileName = "C:\\test\\Fiche_module.pdf";
        // file created path
        String outputFileName = "C:\\pdfTest\\Fiche_Intervention_"+time+".pdf";

        OutputStream os = null;
        PdfStamper ps = null;
        PdfReader reader = null;

        try {
            os = Files.newOutputStream(new File(outputFileName).toPath());
            // lecture pdf
            reader = new PdfReader(inputFileName);
            // créer un nouveau pdf
            ps = new PdfStamper(reader, os);
            // get the formulaire
            AcroFields form = ps.getAcroFields();

            // données insérées
            Map<String, Object> data = new HashMap<>();

            data.put("technicien", technicien);
            data.put("societe", societe);
            data.put("centre", centre);
            data.put("dateInter", date);
            data.put("timeStart", timeStart);
            data.put("timeEnd", timeEnd);
            data.put("pause", pause);
            data.put("actions",actions);
            data.put("refacturer",refacturer);
            data.put("prixInter",prixInter);
            data.put("poste",poste);
            data.put("eta",qte);

            for (String key : data.keySet()) {
                //font-size
                form.setFieldProperty(key,"textsize",13f,null);
                form.setField(key, data.get(key).toString());
            }
            form.setFieldProperty("number","textsize",21f,null);
            form.setField("number",id);

            PdfContentByte over = ps.getOverContent(1);

            //image de signature
            Image image1 = Image.getInstance(signClient);
            Image image2 = Image.getInstance(signTech);

            over.saveState();
            PdfGState pdfGState = new PdfGState();
            over.setGState(pdfGState);

            image1.setAbsolutePosition(100,90);  //position
            image2.setAbsolutePosition(330,90);

            image1.scaleAbsolute(170, 100);  //taille
            image2.scaleAbsolute(170,100);
            over.addImage(image1);  //add image
            over.addImage(image2);
            over.restoreState();

            ps.setFormFlattening(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ps.close();
                reader.close();
                os.close();
                document.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return outputFileName;
    }
}
