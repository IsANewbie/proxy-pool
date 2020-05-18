package com.vtech.newscrawler.util;

import com.vtech.newscrawler.entity.excel.ExcelData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    private static final String TEMAPLATE = "D://usr/local/excel/template.xlsx";
    public static final String NEW = "D://usr/local/excel/today.xlsx";

    public static XSSFWorkbook getExcelTemplate(){
        FileInputStream excelFileInputStream = null;
        XSSFWorkbook workbook = null;
        try {
            excelFileInputStream = new FileInputStream(TEMAPLATE);
            workbook = new XSSFWorkbook(excelFileInputStream);
            excelFileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    public static void createExcel(){
        XSSFWorkbook workbook = getExcelTemplate();
        //保存到本地
        File file = new File(NEW);
        FileOutputStream outputStream = null;
        //将Excel写入输出流中
        try {
            outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void insertData(Map<String, List<ExcelData>> dataList){
        createExcel();
        XSSFWorkbook workbook = getExcelByPath(NEW);
        if(MapUtils.isNotEmpty(dataList)){
            for (String key:dataList.keySet()){
                XSSFSheet sheet = workbook.getSheet(key);
                if(CollectionUtils.isNotEmpty(dataList.get(key))){
                    insertToSheet(sheet,dataList.get(key));

                }
            }
        }
        FileOutputStream excelFileOutPutStream = null;
        try {
            excelFileOutPutStream = new FileOutputStream(NEW);
            workbook.write(excelFileOutPutStream);
            excelFileOutPutStream.flush();
            excelFileOutPutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void insertToSheet(XSSFSheet sheet,List<ExcelData> excelDataList){
        int no = 1;
        for (ExcelData excelData : excelDataList) {
            int currentLastRowIndex = sheet.getLastRowNum();
            int newRowIndex = currentLastRowIndex + 1;
            XSSFRow newRow = sheet.createRow(newRowIndex);
            int cellIndex = 0;
            XSSFCell noCell = newRow.createCell(cellIndex++);
            noCell.setCellValue(no);
            XSSFCell dateCell = newRow.createCell(cellIndex++);
            dateCell.setCellValue(excelData.getDate());
            XSSFCell keywordCell = newRow.createCell(cellIndex++);
            keywordCell.setCellValue(excelData.getKeyword());
            XSSFCell channelCell = newRow.createCell(cellIndex++);
            channelCell.setCellValue(excelData.getChannel());
            XSSFCell mediaCell = newRow.createCell(cellIndex++);
            mediaCell.setCellValue(excelData.getMedia());
            XSSFCell titleCell = newRow.createCell(cellIndex++);
            titleCell.setCellValue(excelData.getTitle());
            XSSFCell urlCell = newRow.createCell(cellIndex++);
            urlCell.setCellValue(excelData.getUrl());
            XSSFCell authorCell = newRow.createCell(cellIndex++);
            authorCell.setCellValue(excelData.getAuthor());
            XSSFCell remarkCell = newRow.createCell(cellIndex++);
            remarkCell.setCellValue(excelData.getRemark());
            XSSFCell typeCell = newRow.createCell(cellIndex++);
            typeCell.setCellValue(excelData.getType());
            XSSFCell readnumCell = newRow.createCell(cellIndex++);
            readnumCell.setCellValue(excelData.getReadnum());
            XSSFCell natureCell = newRow.createCell(cellIndex++);
            natureCell.setCellValue(excelData.getNature());
            no++;
        }
    }
    private static XSSFWorkbook getExcelByPath(String path){
        FileInputStream excelFileInputStream = null;
        XSSFWorkbook workbook = null;
        try {
            excelFileInputStream = new FileInputStream(path);
            workbook = new XSSFWorkbook(excelFileInputStream);
            excelFileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }


    public static void main(String[] args) {
        createExcel();
    }
}
