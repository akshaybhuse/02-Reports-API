package in.akshay.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import in.akshay.entity.EligibilityDetails;
import in.akshay.repo.EligibilityDetailsRepo;

@Component
public class AppRunner implements ApplicationRunner {

	@Autowired
	private EligibilityDetailsRepo repo;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		EligibilityDetails entity1 = new EligibilityDetails();
		entity1.setEligId(1);
		entity1.setName("Akshay");
		entity1.setMobile(112233l);
		entity1.setGender('M');
		entity1.setSsn(88996758l);
        entity1.setPlanName("SNAP");
        entity1.setPlanStatus("Approved");
        entity1.setEmail("akshay.ab@gmail.com");
        repo.save(entity1);
        
        EligibilityDetails entity2 = new EligibilityDetails();
        entity2.setEligId(2);
        entity2.setName("John");
        entity2.setMobile(3345233l);
        entity2.setGender('M');
        entity2.setSsn(55432898l);
        entity2.setPlanName("CCAP");
        entity2.setPlanStatus("Denied");
        entity2.setEmail("john.h@gmail.com");
        repo.save(entity2);
        
        EligibilityDetails entity3 = new EligibilityDetails();
        entity3.setEligId(3);
        entity3.setName("Smit");
        entity3.setMobile(44552233l);
        entity3.setGender('M');
        entity3.setSsn(1118758l);
        entity3.setPlanName("Medicaid");
        entity3.setPlanStatus("Closed");
        entity3.setEmail("smit.s@gmail.com");
        repo.save(entity3);
	}
}
