/**
 * 
 */
package com.validation.FieldValidation.Dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.validation.FieldValidation.Model.FieldValidationModel;
import com.validation.FieldValidation.Model.FieldValidationProperties;

/**
 * @author Karthik
 *
 */
@Component
public class FieldValidationRepository{

	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	FieldValidationProperties prop;
	
	public int fetchBrloanCode(String brloanCode) throws SQLException{
		
		int result=0;
		
		Connection connection =null;
		
		//System.out.println("brloanCode in FieldValidationRepository="+brloanCode);
		try {
			
			connection=dataSource.getConnection();
			Statement statement=connection.createStatement();
			
			//for mysql
			String sql= "select * from at_hospital.tbl_dhfl_customers where brloancode="+brloanCode;
			
			//for db2
			//String sql= "select * from"+prop.getSchema()+".tbl_dhfl_customers where brloancode="+brloanCode;
			
			System.out.println("Datasource{} "+ dataSource);
			System.out.println("sql{} "+ sql);
			
			ResultSet resultList=statement.executeQuery(sql);
			
			result=resultList.getFetchSize();
			
		}catch(Exception e) {
			
		}
		
		return result;
	}
	
	public int fetchAppNo(String applno) throws SQLException{
		
		int result=0;
		
		Connection connection =null;
		
		//System.out.println("brloanCode in FieldValidationRepository="+brloanCode);
		try {
			
			connection=dataSource.getConnection();
			Statement statement=connection.createStatement();
			
			//for mysql
			String sql= "select * from at_hospital.tbl_dhfl_customers where applno="+applno;
			
			//for db2
			//String sql= "select * from"+prop.getSchema()+".tbl_dhfl_customers where brloancode="+brloanCode;
			
			System.out.println("Datasource{} "+ dataSource);
			System.out.println("sql{} "+ sql);
			
			ResultSet resultList=statement.executeQuery(sql);
			
			result=resultList.getFetchSize();
			
		}catch(Exception e) {
			
		}
		
		return result;
	}
	
public int insertRecord(FieldValidationModel take) throws SQLException{
		
		int result=0;
		
		Connection connection =null;
		
		//System.out.println("brloanCode in FieldValidationRepository="+brloanCode);
		try {
			
			connection=dataSource.getConnection();
			Statement statement=connection.createStatement();
			
			//for mysql
			String sql= "insert into at_hospital.tbl_dhfl_customers(brloancode,applno,customername,mobileno,TotalOverdueEMI,MinimumOverdueAmount,OverdueBlankField,TotalChargesAmount,"
					+ "MinimumChargeAmount,ChargeBlankField) values("+take.getBrLoanCode()+","+take.getApplicationNo()+","+take.getCustomerName()+","+take.getMobileNo()+
					","+take.getTotaloverdueEMI()+","+take.getMinimumOverdueAmount()+","+take.getOverDueBlankField()+","+take.getTotalChargesAmount()+","+take.getMinimumChargeAmount()+
					","+take.getChargeBlankField()+")";
			
			
			
			//for db2
			/*String sql= "insert into "+prop.getSchema()+".tbl_dhfl_customers(brloancode,applno,customername,mobileno,TotalOverdueEMI,MinimumOverdueAmount,OverdueBlankField,TotalChargesAmount,"
					+ "MinimumChargeAmount,ChargeBlankField) values("+take.getBrLoanCode()+","+take.getApplicationNo()+","+take.getCustomerName()+","+take.getMobileNo()+
					","+take.getTotaloverdueEMI()+","+take.getMinimumOverdueAmount()+","+take.getOverDueBlankField()+","+take.getTotalChargesAmount()+","+take.getMinimumChargeAmount()+
					","+take.getChargeBlankField()+")";*/
			
			statement.executeUpdate(sql);
			
			System.out.println("Datasource{} "+ dataSource);
			System.out.println("sql{} "+ sql);
			
			ResultSet resultList=statement.executeQuery(sql);
			
			result=resultList.getFetchSize();
			
		}catch(Exception e) {
			
		}
		
		return result;
	}
}
