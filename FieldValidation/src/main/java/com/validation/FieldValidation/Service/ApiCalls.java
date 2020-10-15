/**
 * 
 */
package com.validation.FieldValidation.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.validation.FieldValidation.Dao.FieldValidationRepository;
import com.validation.FieldValidation.Model.FieldValidationExcelModel;
import com.validation.FieldValidation.Model.FieldValidationModel;

/**
 * @author Goutham
 *
 */
public class ApiCalls implements Runnable {

	private final List<FieldValidationModel> queue;
	List<FieldValidationExcelModel> finalList = new ArrayList<>();

	@Autowired
	FieldValidationRepository repo;

	@Override
	public void run() {
		// TODO Auto-generated method stub
	
		try {
			System.out.println("list size before="+queue.size());
			FieldValidationModel take = queue.remove(0);
			
			while(queue.size()>0) {
				process(take);
				if(queue.size()!=0) {
					take = queue.remove(0);
				}else {
					process(take);
					excelWrite(finalList);
				}
			}
			if(queue.size()==0) {
				System.out.println("No records to process");
			}
			System.out.println("list size after="+queue.size());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Exception in ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ApiCalls(List<FieldValidationModel> queue,FieldValidationRepository repo) {

		this.queue = queue;
		this.repo = repo;
	}

	private void excelWrite(List<FieldValidationExcelModel> finalList) {
		
		{
System.out.println("finalList sizein for loop="+finalList.size());
			
			XSSFWorkbook workbook = new XSSFWorkbook();

			 DataFormat fmt = workbook.createDataFormat();
			    CellStyle textStyle = workbook.createCellStyle();
			    textStyle.setDataFormat(fmt.getFormat("@"));
			    
			// Create a blank sheet
			XSSFSheet sheet = workbook.createSheet("Final Sheet");
			sheet.setDefaultColumnStyle(0, textStyle);

			// This data needs to be written (Object[])
			Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
			//data.put(1, new Object[] { "Field", "Failure Message" });

			
		for(int i=0;i<finalList.size();i++) {
			
			data.put(i,new Object[] {finalList.get(i).getFinalValue(), finalList.get(i).getMessage() });
		}

			Set<Integer> keyset = data.keySet();
			int rownum = 0;
			for (Integer key : keyset) {
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
	}
	
	private void process(FieldValidationModel take) throws InterruptedException, SQLException {

		System.out.println("brloanCode in process="+take.getBrLoanCode());
		
		String errBrLoadCode = checkBrloanCode(take.getBrLoanCode());
		String errappNo = checkApplicationNo(take.getApplicationNo());
		String errMobileNo = checkMobileNo(take.getMobileNo());
		String errOverDueEMI = checkAmount(take.getOverdueEMI());
		String errTotalOverDueEMI = checkAmount(take.getTotaloverdueEMI());
		String errMinimumOverdueAmount = checkAmount(take.getMinimumOverdueAmount());
		String errOverDueBlankField = checkAmount(take.getOverDueBlankField());
		String errCharges = checkAmount(take.getCharges());
		String errTotalChargesAmount = checkAmount(take.getTotalChargesAmount());
		String errMinimumChargeAmount = checkAmount(take.getMinimumChargeAmount());
		String errChargeBlankField = checkAmount(take.getChargeBlankField());

		if (errBrLoadCode == null && errappNo == null && errMobileNo == null && errOverDueEMI == null
				&& errTotalOverDueEMI != null && errMinimumOverdueAmount != null && errOverDueBlankField == null
				&& errCharges == null && errTotalChargesAmount == null && errMinimumChargeAmount == null
				&& errChargeBlankField == null) {

			int insert=repo.insertRecord(take);
			System.out.println("Data inserted="+insert);
		} else {

			FieldValidationExcelModel model = new FieldValidationExcelModel();

			if (errBrLoadCode != null) {
				model.setFinalValue(take.getBrLoanCode());
				model.setMessage(errBrLoadCode);
			} else if (errappNo != null) {
				model.setFinalValue(take.getApplicationNo());
				model.setMessage(errappNo);
			} else if (errMobileNo != null) {
				model.setFinalValue(take.getMobileNo());
				model.setMessage(errMobileNo);
			} else if (errOverDueEMI != null) {
				model.setFinalValue(take.getOverdueEMI());
				model.setMessage(errOverDueEMI);
			} else if (errTotalOverDueEMI != null) {
				model.setFinalValue(take.getTotaloverdueEMI());
				model.setMessage(errTotalOverDueEMI);
			} else if (errMinimumOverdueAmount != null) {
				model.setFinalValue(take.getMinimumOverdueAmount());
				model.setMessage(errMinimumOverdueAmount);
			} else if (errOverDueBlankField != null) {
				model.setFinalValue(take.getOverDueBlankField());
				model.setMessage(errOverDueBlankField);
			} else if (errCharges != null) {
				model.setFinalValue(take.getCharges());
				model.setMessage(errCharges);
			} else if (errTotalChargesAmount != null) {
				model.setFinalValue(take.getTotalChargesAmount());
				model.setMessage(errTotalChargesAmount);
			} else if (errMinimumChargeAmount != null) {
				model.setFinalValue(take.getMinimumChargeAmount());
				model.setMessage(errMinimumChargeAmount);
			} else if (errChargeBlankField != null) {
				model.setFinalValue(take.getChargeBlankField());
				model.setMessage(errChargeBlankField);
			}

			finalList.add(model);
			
			System.out.println("finalList size="+finalList.size());
		}
	}

	private String checkBrloanCode(String brLoanCode) throws SQLException {

		String error = null;

		String regex = "^[a-zA-Z0-9]+$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(brLoanCode);

		//System.out.println("brLoanCode in checkBrloanCode="+brLoanCode);
		
		int result = repo.fetchBrloanCode(brLoanCode);

		if (brLoanCode == null || org.springframework.util.StringUtils.isEmpty(brLoanCode)) {
			error = "No BrLoanCode Specified";
			return error;
		} else if (result >= 1) {
			return error = "Duplication BrLoadCode";
		} else if (!matcher.matches()) {
			return error = "Invalid BrLoadCode";
		}
		return error;
	}

	private String checkApplicationNo(String appNo) throws SQLException {

		String error = null;

		String regex = "^F[a-zA-Z0-9]+$";

		int result = repo.fetchAppNo(appNo);
		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(appNo);
		
		if (appNo == null || org.springframework.util.StringUtils.isEmpty(appNo)) {
			error = "No Application No. Specified";
			return error;
		}else if (result >= 1) {
			return error = "Duplication Application No.";
		}else if (!matcher.matches()) {
			return error = "Invalid Application Number Either not Starting with F or Symbols used in Application Number";
		}

		return error;

	}

	private String checkMobileNo(String mobileNo) {

		String error = null;
		String regex = "^[6-9][0-9]{9}+$";

		if (mobileNo == null || org.springframework.util.StringUtils.isEmpty(mobileNo)) {
			error = null;
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

		if (amount == null || org.springframework.util.StringUtils.isEmpty(amount)) {
			error = null;
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
