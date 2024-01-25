package in.akshay.service;

import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import in.akshay.entity.EligibilityDetails;
import in.akshay.repo.EligibilityDetailsRepo;
import in.akshay.request.EligibilityDetailsRequest;
import in.akshay.response.EligibilityDetailsResponse;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ReportsServiceImpl implements ReportsService {

	@Autowired
	private EligibilityDetailsRepo eligibilityDetailsRepo;

	@Override
	public List<String> getAllUniquePlanNames() {
		/*
		  List<String> planNamesList = eligibilityDetailsRepo.findPlanNames(); 
		  return planNamesList;*/

		return eligibilityDetailsRepo.findPlanNames()
				.stream()
				.distinct()
				.collect(Collectors.toList());
	}

	@Override
	public List<String> getAllUniqueStatuses() {
		/*return eligibilityDetailsRepo.findPlanStatues();*/

		return eligibilityDetailsRepo.findPlanStatues()
				.stream()
				.distinct()
				.collect(Collectors.toList());
	}

	@Override
	public List<EligibilityDetailsResponse> search(EligibilityDetailsRequest eligibilityDetailsRequest) {

		List<EligibilityDetailsResponse> response = new ArrayList<>();

		EligibilityDetails queryBuilder = new EligibilityDetails();

		String planName = eligibilityDetailsRequest.getPlanName();

		if (planName != null && !planName.equals("")) {
			queryBuilder.setPlanName(planName);
		}

		String planStatus = eligibilityDetailsRequest.getPlanStatus();
		if (planStatus != null && !planStatus.equals("")) {
			queryBuilder.setPlanStatus(planStatus);
		}

		LocalDate planStartDate = eligibilityDetailsRequest.getPlanStartDate();
		if (planStartDate != null) {
			queryBuilder.setPlanStartDate(planStartDate);
		}

		LocalDate planEndDate = eligibilityDetailsRequest.getPlanEndDate();
		if (planEndDate != null) {
			queryBuilder.setPlanEndDate(planEndDate);
		}

		Example<EligibilityDetails> example = Example.of(queryBuilder);

		List<EligibilityDetails> entitiesList = eligibilityDetailsRepo.findAll(example);

		for (EligibilityDetails entity : entitiesList) {

			EligibilityDetailsResponse eResponse = new EligibilityDetailsResponse();

			BeanUtils.copyProperties(entity, eResponse);

			response.add(eResponse);
		}
		return response;

		/*		EligibilityDetails queryBuilder = new EligibilityDetails();
		
		Optional.ofNullable(eligibilityDetailsRequest.getPlanName())
		        .filter(planName -> !planName.isEmpty())
		        .ifPresent(queryBuilder::setPlanName);
		
		Optional.ofNullable(eligibilityDetailsRequest.getPlanStatus())
		        .filter(planStatus -> !planStatus.isEmpty())
		        .ifPresent(queryBuilder::setPlanStatus);
		
		Optional.ofNullable(eligibilityDetailsRequest.getPlanStartDate())
		        .ifPresent(queryBuilder::setPlanStartDate);
		
		Optional.ofNullable(eligibilityDetailsRequest.getPlanEndDate())
		        .ifPresent(queryBuilder::setPlanEndDate);
		
		Example<EligibilityDetails> example = Example.of(queryBuilder);
		
		return eligibilityDetailsRepo.findAll(example).stream()
		        .map(entity -> {
		            EligibilityDetailsResponse eResponse = new EligibilityDetailsResponse();
		            BeanUtils.copyProperties(entity, eResponse);
		            return eResponse;
		        })
		        .collect(Collectors.toList());
		
		return response;
		*/
	}

	@Override
	public void generateExcel(HttpServletResponse httpServletResponse) throws IOException {

		List<EligibilityDetails> entities = eligibilityDetailsRepo.findAll();

		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheetCreated = workBook.createSheet();
		HSSFRow headerRowCreated = sheetCreated.createRow(0);

		headerRowCreated.createCell(0).setCellValue("Name");
		headerRowCreated.createCell(1).setCellValue("Email");
		headerRowCreated.createCell(2).setCellValue("Mobile");
		headerRowCreated.createCell(3).setCellValue("Gender");
		headerRowCreated.createCell(4).setCellValue("SSN");

		int i = 1;
		for (EligibilityDetails entity : entities) {
			HSSFRow dataRow = sheetCreated.createRow(i);
			dataRow.createCell(0).setCellValue(entity.getName());
			dataRow.createCell(1).setCellValue(entity.getEmail());
			dataRow.createCell(2).setCellValue(entity.getMobile());
			dataRow.createCell(3).setCellValue(String.valueOf(entity.getGender()));
			dataRow.createCell(4).setCellValue(entity.getSsn());
			i++;
		}

		// created workbook attaching to ServletOutputStream
		ServletOutputStream outputStream = httpServletResponse.getOutputStream();
		workBook.write(outputStream);
		workBook.close();
		outputStream.close();
	}

	@Override
	public void generatePdf(HttpServletResponse httpServletResponse) throws Exception {

		List<EligibilityDetails> entities = eligibilityDetailsRepo.findAll();
		Document document = new Document(PageSize.A4);

		PdfWriter.getInstance(document, httpServletResponse.getOutputStream());

		document.open();

		// creating paragraph
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);

		Paragraph p = new Paragraph("Search Report ", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		// paragraph adding to document
		document.add(p);

		// Table is created with 5 columns
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.5f, 3.5f, 3.0f, 1.5f, 3.0f });
		table.setSpacingBefore(10);

		// every column we need cell, creating cell and adding to table
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("Name", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("E-mail", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Phone No.", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Gender", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("SSN", font));
		table.addCell(cell);

		// adding data to cell, after executing loop new row will be create
		for (EligibilityDetails entity : entities) {
			table.addCell(entity.getName());
			table.addCell(entity.getEmail());
			table.addCell(String.valueOf(entity.getMobile()));
			table.addCell(String.valueOf(entity.getGender()));
			table.addCell(String.valueOf(entity.getSsn()));
		}
		document.add(table);
		document.close();
	}

}
