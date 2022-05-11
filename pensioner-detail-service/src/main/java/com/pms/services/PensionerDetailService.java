package com.pms.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.beans.PensionerDetail;
import com.pms.result.ErrorMessages;
import com.pms.result.Result;

@Service
public class PensionerDetailService {
	@Autowired
	PensionerDetailRepository pensionerDetailIORepo;

	@Transactional(rollbackOn = Exception.class)
	public Result savePensioner(PensionerDetail pensionerDetail) {
		pensionerDetailIORepo.save(pensionerDetail);
		//return pensionerDetailIORepo.save(pensionerDetails);
		return new Result(200, ErrorMessages.SAVE_SUCCESSFUL);
	}

	public List<PensionerDetail> getPensioners() {
		return pensionerDetailIORepo.findAll();
	}

	public Optional<PensionerDetail> getById(String id) {
		return pensionerDetailIORepo.findById(id);
	}

	public Result deleteById(String id) {
		pensionerDetailIORepo.deleteById(id);
		return new Result(200, ErrorMessages.DELETE_SUCCESSFUL);
	}

	public Result deleteAllPensioners() {
		pensionerDetailIORepo.deleteAll();
		return new Result(200, ErrorMessages.DELETE_SUCCESSFUL);
	}

}
