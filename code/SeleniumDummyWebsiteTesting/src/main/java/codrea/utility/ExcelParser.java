package codrea.utility;

import codrea.model.Order;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelParser {

    public static List<Order> parseExcelFile(String filePath){
        List<Order> orders = new ArrayList<>();

        try(FileInputStream fis = new FileInputStream(filePath);
            Workbook workbook = WorkbookFactory.create(fis)){

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();

            Row headerRow = rowIterator.next();
            int invoiceNumberIndex = -1;
            int productNameIndex = -1;
            int productPriceIndex = -1;
            int addressIndex = -1;
            int orderedByIndex = -1;

            Iterator<Cell> cellIterator = headerRow.cellIterator();
            while (cellIterator.hasNext()){
                Cell cell = cellIterator.next();
                String header = cell.getStringCellValue().trim();

                switch (header){
                    case "Invoice Number" -> invoiceNumberIndex = cell.getColumnIndex();
                    case "Product Name" -> productNameIndex = cell.getColumnIndex();
                    case "Product Price" -> productPriceIndex = cell.getColumnIndex();
                    case "Address" -> addressIndex = cell.getColumnIndex();
                    case "Ordered By" -> orderedByIndex = cell.getColumnIndex();
                }
            }

            while (rowIterator.hasNext()){
                Row row = rowIterator.next();
                String orderId = getStringCellValue(row.getCell(invoiceNumberIndex));
                String productName = getStringCellValue(row.getCell(productNameIndex));
                String productPrice = getStringCellValue(row.getCell(productPriceIndex));
                String address = getStringCellValue(row.getCell(addressIndex));
                String orderedBy = getStringCellValue(row.getCell(orderedByIndex));

                Order order = new Order(orderId,productName,productPrice,address,orderedBy);

                orders.add(order);
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

        return orders;
    }

    public static String getStringCellValue(Cell cell){
        return  cell == null? "" : cell.getStringCellValue();
    }

//    public static double getNumericCellValue(Cell cell){
//        return  cell == null? 0 : cell.getNumericCellValue();
//    }
}
