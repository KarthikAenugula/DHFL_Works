/**
 * 
 */
package com.validation.FieldValidation.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import org.springframework.stereotype.Component;


import com.validation.FieldValidation.Model.FieldValidationExcelModel;
import com.validation.FieldValidation.Model.FieldValidationModel;

/**
 * @author Karthik
 *
 */
@Component
public class FieldValidationServiceImpl {

	public void readFile() {

		try {

			File excel = new File("E://Click_To_Pay_Fields_15_06_2020-edited.xlsx");
			
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
				
				

				System.out.println("excelList size=" + excelList.size());
			}
			book.close();
			fis.close();

			List<Thread> threadList=new ArrayList<>();
			
			List<FieldValidationModel> queue=Collections.synchronizedList(new LinkedList<FieldValidationModel>());
			queue.addAll(excelList);
			
			int threadCount=queue.size()/minimumSize;
			System.out.println("threadCount=" + threadCount);
			
			ApiCalls apicalls=new ApiCalls(queue);
			
			for(int i=1;i<threadCount;i++) {
				
				Thread thread=new Thread(apicalls);
				thread.setName("Thread"+i);
				threadList.add(thread);
			}
			
			for(Thread t : threadList) {
				System.out.println("Thread Started="+t.getName());
				t.start();
			}
			
			
			
			
			
			//List<FieldValidationModel> finalList = validationMethod(excelList);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<FieldValidationModel> validationMethod(List<FieldValidationModel> excelList) {

		try {

			List<FieldValidationExcelModel> finalList = new ArrayList<>();

			for (FieldValidationModel value : excelList) {

				String errBrLoadCode = checkBrloanCode(value.getBrLoanCode());
				String errappNo = checkApplicationNo(value.getApplicationNo());
				String errMobileNo = checkMobileNo(value.getMobileNo());
				String errOverDueEMI = checkAmount(value.getOverdueEMI());
				String errTotalOverDueEMI = checkAmount(value.getTotaloverdueEMI());
				String errMinimumOverdueAmount = checkAmount(value.getMinimumOverdueAmount());
				String errOverDueBlankField = checkAmount(value.getOverDueBlankField());
				String errCharges = checkAmount(value.getCharges());
				String errTotalChargesAmount = checkAmount(value.getTotalChargesAmount());
				String errMinimumChargeAmount = checkAmount(value.getMinimumChargeAmount());
				String errChargeBlankField = checkAmount(value.getChargeBlankField());

				if (errBrLoadCode == null || errappNo == null || errMobileNo == null || errOverDueEMI == null
						|| errTotalOverDueEMI != null || errMinimumOverdueAmount != null || errOverDueBlankField == null
						|| errCharges == null || errTotalChargesAmount == null || errMinimumChargeAmount == null
						|| errChargeBlankField == null) {

					System.out.println("Insert record into Database");
				} else {

					FieldValidationExcelModel model = new FieldValidationExcelModel();

					if (errBrLoadCode != null) {
						model.setFinalValue(value.getBrLoanCode());
						model.setMessage(errBrLoadCode);
					} else if (errappNo != null) {
						model.setFinalValue(value.getApplicationNo());
						model.setMessage(errappNo);
					} else if (errMobileNo != null) {
						model.setFinalValue(value.getMobileNo());
						model.setMessage(errMobileNo);
					} else if (errOverDueEMI != null) {
						model.setFinalValue(value.getOverdueEMI());
						model.setMessage(errOverDueEMI);
					} else if (errTotalOverDueEMI != null) {
						model.setFinalValue(value.getTotaloverdueEMI());
						model.setMessage(errTotalOverDueEMI);
					} else if (errMinimumOverdueAmount != null) {
						model.setFinalValue(value.getMinimumOverdueAmount());
						model.setMessage(errMinimumOverdueAmount);
					} else if (errOverDueBlankField != null) {
						model.setFinalValue(value.getOverDueBlankField());
						model.setMessage(errOverDueBlankField);
					} else if (errCharges != null) {
						model.setFinalValue(value.getCharges());
						model.setMessage(errCharges);
					} else if (errTotalChargesAmount != null) {
						model.setFinalValue(value.getTotalChargesAmount());
						model.setMessage(errTotalChargesAmount);
					} else if (errMinimumChargeAmount != null) {
						model.setFinalValue(value.getMinimumChargeAmount());
						model.setMessage(errMinimumChargeAmount);
					} else if (errChargeBlankField != null) {
						model.setFinalValue(value.getChargeBlankField());
						model.setMessage(errChargeBlankField);
					}

					finalList.add(model);
				}

			}

			if (finalList.size() != 0) {

				XSSFWorkbook workbook = new XSSFWorkbook();

				// Create a blank sheet
				XSSFSheet sheet = workbook.createSheet("student Details");

				// This data needs to be written (Object[])
				Map<String, Object[]> data = new TreeMap<String, Object[]>();
				data.put("1", new Object[] { "ID", "NAME", "LASTNAME" });

				for (FieldValidationExcelModel a : finalList) {

					int i = 2;
					data.put(String.valueOf(i), new Object[] { 1, a.getFinalValue(), a.getMessage() });
					i++;
				}

				Set<String> keyset = data.keySet();
				int rownum = 0;
				for (String key : keyset) {
					// this creates a new row in the sheet
					Row row = sheet.createRow(rownum++);
					Object[] objArr = data.get(key);
					int cellnum = 0;
					for (Object obj : objArr) {
						// this line creates a cell in the next column of that row
						Cell cell = row.createCell(cellnum++);
						if (obj instanceof String)
							cell.setCellValue((String) obj);
						else if (obj instanceof Integer)
							cell.setCellValue((Integer) obj);
					}
				}

				try {
					// this Writes the workbook gfgcontribute
					FileOutputStream out = new FileOutputStream(
							new File("E://Click_To_Pay_Fields_15_06_2020-edited_final.xlsx"));
					workbook.write(out);
					out.close();
					System.out.println("Click_To_Pay_Fields_15_06_2020-edited_final.xlsx written successfully");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} catch (Exception e) {

		}

		return excelList;
	}

	private String checkBrloanCode(String brLoanCode) {

		String error = null;

		String regex = "^[a-zA-Z0-9]+$";
		
		
		if(brLoanCode==null  || org.springframework.util.StringUtils.isEmpty(brLoanCode)) {
			error=null;
			return error;
		}
		
		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(brLoanCode);
		if (!matcher.matches()) {
			return error = "Invalid BrLoadCode";
		}

		return error;
	}

	private String checkApplicationNo(String appNo) {

		String error = null;

		String regex = "^F[a-zA-Z0-9]+$";

		if(appNo==null  || org.springframework.util.StringUtils.isEmpty(appNo)) {
			error=null;
			return error;
		}
		
		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(appNo);
		if (!matcher.matches()) {
			return error = "Invalid Application Number Either not Starting with F or Symbols used in Application Number";
		}

		return error;

	}

	private String checkMobileNo(String mobileNo) {

		String error = null;
		String regex = "^[6-9][0-9]{9}+$";

		if(mobileNo==null  || org.springframework.util.StringUtils.isEmpty(mobileNo)) {
			error=null;
			return error;
		}
		
		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(mobileNo);

		if (!matcher.matches() || "9999999999" == mobileNo || "8888888888" == mobileNo || "7777777777" == mobileNo
				|| "6666666666" == mobileNo) {
			return error = "Invalid Mobile Number";
		}

		return error;
	}

	private String checkAmount(String amount) {

		String error = null;
		
		if(amount==null  || org.springframework.util.StringUtils.isEmpty(amount)) {
			error=null;
			return error;
		}
		
		String regex = "^[0-9]+$";

		String number = amount.substring(amount.indexOf(".")).substring(1);

		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(amount);

		if (!matcher.matches()) {
			return error = "Invalid Amount";
		} else if ("0" != number) {
			return error = "Decimals not allowed in Amount";
		}

		return error;
	}
}
