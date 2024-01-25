package in.akshay.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EligibilityDetailsResponse {

	private String name;
	private Long mobile;
	private String email;
	private Character gender;
	private Long ssn;
	

}
