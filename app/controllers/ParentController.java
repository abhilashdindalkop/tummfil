package controllers;

import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.fasterxml.jackson.databind.JsonMappingException;

import play.mvc.Controller;
import utils.MyConstants.FailureMessages;
import utils.MyException;
import utils.MyFailureResponse;
import utils.MyResponse;

public class ParentController extends Controller {

	protected MyResponse response = null;

	public MyFailureResponse createFailureResponse(Exception e) {

		if (e instanceof MyException) {
			return new MyFailureResponse(e.getMessage());
		}

		else if (e instanceof JsonMappingException || e instanceof IOException) {
			return new MyFailureResponse(FailureMessages.JSON_MAPPER_ERROR, e);
		}

		else if (e instanceof AmazonClientException || e instanceof AmazonServiceException) {
			return new MyFailureResponse(FailureMessages.JSON_MAPPER_ERROR, e);
		}

		else {
			return new MyFailureResponse(FailureMessages.TECHNICAL_ERROR, e);
		}
	}

}
