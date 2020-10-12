/**
 * 
 */
package com.validation.FieldValidation.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.validation.FieldValidation.Service.FieldValidationServiceImpl;

/**
 * @author Karthik
 *
 */
@RestController
@RequestMapping("/fieldValidation")
public class FieldValidationController {

	
	@Autowired
	FieldValidationServiceImpl fvimpl;
	
	@RequestMapping(value="/readFile", method=RequestMethod.POST)
	public void readFile(){
	
		System.out.println("in reading file");
		
		try {
			
			fvimpl.readFile();
			
		}catch(Exception e) {
			System.out.println("Exception in reading File "+e.getMessage());
		}
	}
}
