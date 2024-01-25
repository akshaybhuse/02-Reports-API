package in.akshay.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.akshay.request.EligibilityDetailsRequest;
import in.akshay.response.EligibilityDetailsResponse;
import in.akshay.service.ReportsService;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ReportRestController {

	@Autowired
	private ReportsService service;

	@GetMapping("/plans")
	public ResponseEntity<List<String>> getAllUniquePlans() {

		List<String> allUniquePlanNames = service.getAllUniquePlanNames();
		return new ResponseEntity<>(allUniquePlanNames, HttpStatus.OK);
	}

	@GetMapping("/statuses")
	public ResponseEntity<List<String>> getAllUniqueStatuses() {

		List<String> allUniqueStatuses = service.getAllUniqueStatuses();
		return new ResponseEntity<>(allUniqueStatuses, HttpStatus.OK);
	}

	@PostMapping("/search")
	public ResponseEntity<List<EligibilityDetailsResponse>> search(@RequestBody EligibilityDetailsRequest request) {

		List<EligibilityDetailsResponse> search = service.search(request);
		return new ResponseEntity<>(search, HttpStatus.OK);
	}

	@GetMapping("/excel")
	public void downloadExcel(HttpServletResponse response) throws Exception {

		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=data.xls";

		response.setHeader(headerKey, headerValue);

		service.generateExcel(response);
	}

	@GetMapping("/pdf")
	public void downloadPdf(HttpServletResponse response) throws Exception {

		response.setContentType("application/pdf");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=data.pdf";

		response.setHeader(headerKey, headerValue);

		service.generatePdf(response);
	}

}
