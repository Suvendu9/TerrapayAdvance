package com.pms.delegate;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.pms.beans.BankType;
import com.pms.beans.PensionAmount;
import com.pms.beans.PensionType;
import com.pms.beans.PensionerDetail;
import com.pms.beans.ProcessPensionInput;

@Service
public class ProcessPensionDelegate {

	@Autowired
	RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "callPensionerServiceGetData_Fallback")
	public PensionAmount callPensionerServiceAndGetData(ProcessPensionInput input) {
		String response = restTemplate.exchange("http://localhost:8098/pensioner/{id}", HttpMethod.GET, null,
				new ParameterizedTypeReference<String>() {}, input).getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		PensionerDetail pensionerDetail = null;
		try {
			pensionerDetail = objectMapper.readValue(response, PensionerDetail.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		PensionAmount p_amount = new PensionAmount();
		float amount = calculatePensionAmount(pensionerDetail.getPensionType(), pensionerDetail.getSalary(),
				pensionerDetail.getAllowances());
		float bankServiceCharge = calculateBankServiceCharge(pensionerDetail.getBankDetails().get(0).getBanktype());
		p_amount.setPensionAmount(amount);
		p_amount.setBankServiceCharge(bankServiceCharge);
		return p_amount;

	}

	private float calculateBankServiceCharge(BankType banktype) {
		float serviceCharge = 0;
		if (banktype.equals("PUBLIC")) {
			serviceCharge = 500;
		} else if (banktype.equals("PRIVATE")) {
			serviceCharge = 550;
		}
		return serviceCharge;
	}

	private float calculatePensionAmount(PensionType pensionType, float salaryEarned, float allowances) {
		float pensionAmount = 0;
		if (pensionType.equals("SELF")) {
			pensionAmount = (float) (0.8 * salaryEarned + allowances);
		} else if (pensionType.equals("FAMILY")) {
			pensionAmount = (float) (0.5 * salaryEarned + allowances);
		}
		return pensionAmount;
	}

	@SuppressWarnings("unused")
	private String callPensionerServiceGetData_Fallback(String adharNumber) {
		System.out.println("Pensioner Service is down!!! fallback enabled...");
		return "No Response From Pensioner Service at this moment(CIRCUIT BREAKER)."
				+ new Date();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
