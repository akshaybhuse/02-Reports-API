package in.akshay.service;

import java.io.IOException;
import java.util.List;

import in.akshay.request.EligibilityDetailsRequest;
import in.akshay.response.EligibilityDetailsResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface ReportsService {

	public List<String> getAllUniquePlanNames();

	public List<String> getAllUniqueStatuses();

	public List<EligibilityDetailsResponse> search(EligibilityDetailsRequest eligibilityDetailsRequest);

	public void generateExcel(HttpServletResponse httpServletResponse) throws IOException;

	public void generatePdf(HttpServletResponse httpServletResponse) throws Exception;

}
