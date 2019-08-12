package com.myleads.android.custom;

import android.os.Environment;
import android.util.Log;

import com.myleads.android.model.LeadModel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelCreator {

    public boolean save(String fileName, ArrayList<LeadModel> list) {
        String path;
        File dir;
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e("Failed", "Storage not available or read only");
            return false;
        }
        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("LeadsReport");

        // Generate column headings
        Row row = null;

        row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("Title");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Name");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Middle Name");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("Last Name");
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("Email");
        c.setCellStyle(cs);

        c = row.createCell(5);
        c.setCellValue("Phone");
        c.setCellStyle(cs);

        c = row.createCell(6);
        c.setCellValue("Company");
        c.setCellStyle(cs);

        c = row.createCell(7);
        c.setCellValue("Source");
        c.setCellStyle(cs);

        c = row.createCell(8);
        c.setCellValue("Position");
        c.setCellStyle(cs);

        c = row.createCell(9);
        c.setCellValue("Address");
        c.setCellStyle(cs);

        c = row.createCell(10);
        c.setCellValue("Suburb");
        c.setCellStyle(cs);

        c = row.createCell(11);
        c.setCellValue("City");
        c.setCellStyle(cs);

        c = row.createCell(12);
        c.setCellValue("Province");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(3, (15 * 500));
        sheet1.setColumnWidth(4, (15 * 500));
        sheet1.setColumnWidth(5, (15 * 500));
        sheet1.setColumnWidth(6, (15 * 500));
        sheet1.setColumnWidth(7, (15 * 500));
        sheet1.setColumnWidth(8, (15 * 500));
        sheet1.setColumnWidth(9, (15 * 500));
        sheet1.setColumnWidth(10, (15 * 500));
        sheet1.setColumnWidth(11, (15 * 500));
        sheet1.setColumnWidth(12, (15 * 500));


        int k = 1;
        for(LeadModel lead : list){
            row = sheet1.createRow(k);
            c = row.createCell(0);
            c.setCellValue(lead.getTitle());
            c.setCellStyle(cs);

            c = row.createCell(1);
            c.setCellValue(lead.getName());
            c.setCellStyle(cs);

            c = row.createCell(2);
            c.setCellValue(lead.getMiddleName());
            c.setCellStyle(cs);

            c = row.createCell(3);
            c.setCellValue(lead.getLastName());
            c.setCellStyle(cs);

            c = row.createCell(4);
            c.setCellValue(lead.getEmail());
            c.setCellStyle(cs);

            c = row.createCell(5);
            c.setCellValue(lead.getEmail());
            c.setCellStyle(cs);

            c = row.createCell(6);
            c.setCellValue(lead.getCompany());
            c.setCellStyle(cs);

            c = row.createCell(7);
            c.setCellValue(lead.getSource());
            c.setCellStyle(cs);

            c = row.createCell(8);
            c.setCellValue(lead.getPosition());
            c.setCellStyle(cs);

            c = row.createCell(9);
            c.setCellValue(lead.getAddress());
            c.setCellStyle(cs);

            c = row.createCell(10);
            c.setCellValue(lead.getSuburb());
            c.setCellStyle(cs);

            c = row.createCell(11);
            c.setCellValue(lead.getCity());
            c.setCellStyle(cs);

            c = row.createCell(12);
            c.setCellValue(lead.getProvince());
            c.setCellStyle(cs);


            sheet1.setColumnWidth(0, (15 * 500));
            sheet1.setColumnWidth(1, (15 * 500));
            sheet1.setColumnWidth(3, (15 * 500));
            sheet1.setColumnWidth(4, (15 * 500));
            sheet1.setColumnWidth(5, (15 * 500));
            sheet1.setColumnWidth(6, (15 * 500));
            sheet1.setColumnWidth(7, (15 * 500));
            sheet1.setColumnWidth(8, (15 * 500));
            sheet1.setColumnWidth(9, (15 * 500));
            sheet1.setColumnWidth(10, (15 * 500));
            sheet1.setColumnWidth(11, (15 * 500));
            sheet1.setColumnWidth(12, (15 * 500));

            k++;
        }

        path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/EXCEL/";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        return success;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
