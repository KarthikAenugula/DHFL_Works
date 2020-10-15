/**
 * 
 */
package com.validation.FieldValidation.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.validation.FieldValidation.Dao.FieldValidationRepository;
import com.validation.FieldValidation.Model.FieldValidationExcelModel;
import com.validation.FieldValidation.Model.FieldValidationModel;

/**
 * @author Karthik
 *
 */
@Component
public class FieldValidationServiceImpl {

	@Autowired
	FieldValidationRepository repo;

	public void readFile() {

		try {

			File excel = new File("E://test.xlsx");
			
			int minimumSize=2000;

			FileInputStream fis = new FileInputStream(excel);

			XSSFWorkbook book = new XSSFWorkbook(fis);
			XSSFSheet sheet = book.getSheetAt(0);
			// System.out.println("last row num=" + sheet.getLastRowNum());

			Iterator<Row> iterator = sheet.iterator(); // Iterating over Excel file in Java

			List<FieldValidationModel> excelList = new ArrayList<>();

			while (iterator.hasNext()) {

				Row nextRow = iterator.next();

				// System.out.println("nextRow=" + nextRow.getRowNum());

				if (nextRow.getRowNum() == 0) {
					continue;
				}

				FieldValidationModel model = new FieldValidationModel();
				if (null == nextRow.getCell(0)) {
					model.setBrLoanCode(null);
				} else {
					model.setBrLoanCode(nextRow.getCell(0).toString());
				}

				if (null == nextRow.getCell(1)) {
					model.setApplicationNo(null);
				} else {
					model.setApplicationNo(nextRow.getCell(01).toString());
				}

				if (null == nextRow.getCell(2)) {
					model.setCustomerName(null);
				} else {
					model.setCustomerName(nextRow.getCell(2).toString());
				}

				if (null == nextRow.getCell(3)) {
					model.setMobileNo(null);
				} else {
					model.setMobileNo(nextRow.getCell(3).toString());
				}

				if (null == nextRow.getCell(4)) {
					model.setOverdueEMI(null);
				} else {
					model.setOverdueEMI(nextRow.getCell(4).toString());
				}

				if (null == nextRow.getCell(5)) {
					model.setTotaloverdueEMI(null);
				} else {
					model.setTotaloverdueEMI(nextRow.getCell(5).toString());
				}

				if (null == nextRow.getCell(6)) {
					model.setMinimumOverdueAmount(null);
				} else {
					model.setMinimumOverdueAmount(nextRow.getCell(6).toString());
				}

				if (null == nextRow.getCell(7)) {
					model.setOverDueBlankField(null);
				} else {
					model.setOverDueBlankField(nextRow.getCell(7).toString());
				}

				if (null == nextRow.getCell(8)) {
					model.setCharges(null);
				} else {
					model.setCharges(nextRow.getCell(8).toString());
				}

				if (null == nextRow.getCell(9)) {
					model.setTotalChargesAmount(null);
				} else {
					model.setTotalChargesAmount(nextRow.getCell(9).toString());
				}

				if (null == nextRow.getCell(10)) {
					model.setMinimumChargeAmount(null);
				} else {
					model.setMinimumChargeAmount(nextRow.getCell(10).toString());
				}

				if (null == nextRow.getCell(11)) {
					model.setChargeBlankField(null);
				} else {
					model.setChargeBlankField(nextRow.getCell(11).toString());
				}

				excelList.add(model);
				
				

				
			}
			book.close();
			fis.close();

			List<Thread> threadList=new ArrayList<>();
			
			System.out.println("excelList size=" + excelList.size());
			
			List<FieldValidationModel> queue=Collections.synchronizedList(new LinkedList<FieldValidationModel>());
			queue.addAll(excelList);
			
			int threadCount=queue.size()/minimumSize;
			System.out.println("threadCount=" + threadCount);
			
			ApiCalls apicalls=new ApiCalls(queue,repo);
			
			for(int i=1;i<=threadCount;i++) {
				
				Thread thread=new Thread(apicalls);
				thread.setName("Thread"+i);
				threadList.add(thread);
			}
			
			for(Thread t : threadList) {
				System.out.println("Thread Started="+t.getName());
				t.start();
			}
			
			for(Thread t : threadList) {
				System.out.println("Thread Joined="+t.getName());
				t.join();
			}
			
			for(Thread t : threadList) {
				System.out.println("Thread Stopped="+t.getName());
				t.stop();
			}
			
		
			//List<FieldValidationModel> finalList = validationMethod(excelList);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
