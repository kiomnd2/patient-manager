package subject.hdjunction.subject.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subject.hdjunction.subject.domain.Hospital;
import subject.hdjunction.subject.domain.Patient;
import subject.hdjunction.subject.domain.Visit;

import java.time.LocalDateTime;


@Transactional
@SpringBootTest
class VisitRepositoryTest {

    @Autowired
    VisitRepository visitRepository;

    @Autowired
    HospitalRepository hospitalRepository;

    @Autowired
    PatientRepository patientRepository;

    Hospital hospital = null;

    Patient patient = null;


    @BeforeEach
    void beforeEach() throws Throwable {
        visitRepository.deleteAll();
        hospitalRepository.deleteAll();
        patientRepository.deleteAll();
        saveHospital();
        savePatient();
    }

    void saveHospital() {

        final String hospitalName = "김병원";
        final String chiefName = "김길동";
        String nursingHomeNo = "1234567";
        Hospital hospital = Hospital.builder()
                .hospitalName(hospitalName)
                .chiefName(chiefName)
                .nursingHomeNo(nursingHomeNo)
                .build();

        this.hospital = hospitalRepository.save(hospital);
    }

    void savePatient() {
        final String patientName = "김개똥";
        final String patientNo = "123123";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";


        Patient patient = Patient.builder()
                .patientName(patientName)
                .patientNo(patientNo)
                .birthDate(birthDate)
                .genderCode(genderCode)
                .phoneNumber(phoneNo)
                .hospital(this.hospital)
                .build();

        this.patient = patientRepository.save(patient);
    }

    @Test
    void insertVisitInfoTest() throws Throwable {
        LocalDateTime receptionDateTime = LocalDateTime.of(2021, 9,16, 12, 59,59);
        final String visitStateCode = "1";
        Visit visit = Visit.builder()
                .receptionDateTime(receptionDateTime)
                .visitStateCode(visitStateCode)
                .hospital(this.hospital)
                .patient(this.patient)
                .build();

        visitRepository.save(visit);

        Visit selectedVisit = visitRepository.findAll().get(0);

        Assertions.assertThat(selectedVisit.getReceptionDateTime()).isEqualTo(receptionDateTime);
        Assertions.assertThat(selectedVisit.getVisitStateCode()).isEqualTo(visitStateCode);
        Assertions.assertThat(selectedVisit.getHospital().getChiefName()).isEqualTo(this.hospital.getChiefName());
        Assertions.assertThat(selectedVisit.getPatient().getPatientName()).isEqualTo(this.patient.getPatientName());
    }

    @Test
    void findLastDatePatientTest() throws Throwable {
        LocalDateTime receptionDateTime = LocalDateTime.of(2021, 9,16, 12, 59,59);
        final String visitStateCode = "1";
        Visit visit = Visit.builder()
                .receptionDateTime(receptionDateTime)
                .visitStateCode(visitStateCode)
                .hospital(this.hospital)
                .patient(this.patient)
                .build();

        visitRepository.save(visit);

        final LocalDateTime receptionDateTime2 = receptionDateTime.plusDays(1);
        final String visitStateCode2= "2";
        Visit visit2 = Visit.builder()
                .receptionDateTime(receptionDateTime2)
                .visitStateCode(visitStateCode2)
                .hospital(this.hospital)
                .patient(this.patient)
                .build();

        visitRepository.save(visit2);

        Visit lastVisit = visitRepository.findTopByPatientOrderByReceptionDateTimeDesc(this.patient).get();

        Assertions.assertThat(receptionDateTime2).isEqualTo(lastVisit.getReceptionDateTime());
        Assertions.assertThat(visitStateCode2).isEqualTo(lastVisit.getVisitStateCode());
    }


}
