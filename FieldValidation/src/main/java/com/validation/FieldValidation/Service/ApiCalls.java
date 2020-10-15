/**
 * 
 */
package com.validation.FieldValidation.Service;

import java.util.List;

import com.validation.FieldValidation.Model.FieldValidationModel;

/**
 * @author Goutham
 *
 */
public  class ApiCalls implements Runnable{

	private final List<FieldValidationModel> queue;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		FieldValidationModel take=queue.remove(0);
		
		process(take);
	}
	
	public ApiCalls(List<FieldValidationModel> queue) {
		
		this.queue=queue;
	}

}
