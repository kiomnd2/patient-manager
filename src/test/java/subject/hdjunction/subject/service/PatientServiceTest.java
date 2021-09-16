package subject.hdjunction.subject.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subject.hdjunction.subject.domain.Hospital;
import subject.hdjunction.subject.domain.Patient;
import subject.hdjunction.subject.dto.PatientDto;
import subject.hdjunction.subject.repository.HospitalRepository;
import subject.hdjunction.subject.repository.PatientRepository;

import java.util.List;


@Transactional
@SpringBootTest
class PatientServiceTest {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    PatientService patientService;

    @Autowired
    HospitalRepository hospitalRepository;

    Hospital hospital;

    @BeforeEach
    void beforeEach() throws Throwable{
        patientRepository.deleteAll();
        saveHospital();
    }
    void saveHospital() {
        Hospital hospital = Hospital.builder()
                .hospitalName("김병원")
                .chiefName("김길동")
                .nursingHomeNo("1234567")
                .build();

        this.hospital = hospitalRepository.save(hospital);
    }


    @Test
    void patientRegisterTest() throws Throwable {
        final String patientName = "김개똥";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";

        PatientDto patientDto = PatientDto.builder()
                .patientName(patientName)
                .birthDate(birthDate)
                .genderCode(genderCode)
                .hospitalId(this.hospital.getId())
                .phoneNumber(phoneNo).build();

        PatientDto registerDto = patientService.register(patientDto);

        Patient patient = patientRepository.findById(registerDto.getId()).get();

        Assertions.assertThat(registerDto.getPatientName()).isEqualTo(patient.getPatientName());
        Assertions.assertThat(registerDto.getBirthDate()).isEqualTo(patient.getBirthDate());
        Assertions.assertThat(registerDto.getGenderCode()).isEqualTo(patient.getGenderCode());
        Assertions.assertThat(registerDto.getHospitalId()).isEqualTo(patient.getHospital().getId());
    }

    @Test
    void patientRemoveTest() throws Throwable {
        final String patientName = "김개똥";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";

        PatientDto patientDto = PatientDto.builder()
                .patientName(patientName)
                .birthDate(birthDate)
                .genderCode(genderCode)
                .hospitalId(this.hospital.getId())
                .phoneNumber(phoneNo).build();

        PatientDto registerDto = patientService.register(patientDto);

        patientService.removePatient(registerDto.getId());
    }

    @Test
    void patientInfoTest() throws Throwable {
        final String patientName = "김개똥";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";

        PatientDto patientDto = PatientDto.builder()
                .patientName(patientName)
                .birthDate(birthDate)
                .genderCode(genderCode)
                .hospitalId(this.hospital.getId())
                .phoneNumber(phoneNo).build();

        PatientDto registerDto = patientService.register(patientDto);

        PatientDto patient = patientService.getPatient(registerDto.getId());

        Assertions.assertThat(patient.getPatientName()).isEqualTo(registerDto.getPatientName());
        Assertions.assertThat(patient.getHospitalId()).isEqualTo(registerDto.getHospitalId());
        Assertions.assertThat(patient.getGenderCode()).isEqualTo(registerDto.getGenderCode());
        Assertions.assertThat(patient.getLastReceptionDateTime()).isEqualTo(registerDto.getLastReceptionDateTime());
    }

    @Test
    void patientInfosTest() throws Throwable {
        final String patientName = "김개똥";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";

        PatientDto patientDto = PatientDto.builder()
                .patientName(patientName)
                .birthDate(birthDate)
                .genderCode(genderCode)
                .hospitalId(this.hospital.getId())
                .phoneNumber(phoneNo).build();

        patientService.register(patientDto);

        final String patientName2 = "김개똥2";
        final String birthDate2 = "19910221";
        final String genderCode2 = "M";
        final String phoneNo2 = "01111123123";

        PatientDto patientDto2 = PatientDto.builder()
                .patientName(patientName2)
                .birthDate(birthDate2)
                .genderCode(genderCode2)
                .hospitalId(this.hospital.getId())
                .phoneNumber(phoneNo2).build();

        patientService.register(patientDto2);

        List<PatientDto> patients = patientService.getPatients();

        Assertions.assertThat(patients.size()).isEqualTo(2);
        Assertions.assertThat(patients.get(0).getPatientName()).isEqualTo(patientName);
        Assertions.assertThat(patients.get(1).getPatientName()).isEqualTo(patientName2);
    }

    @Test
    void updatePatientInfoTest() throws Throwable {
        final String patientName = "김개똥";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";

        PatientDto patientDto = PatientDto.builder()
                .patientName(patientName)
                .birthDate(birthDate)
                .genderCode(genderCode)
                .hospitalId(this.hospital.getId())
                .phoneNumber(phoneNo).build();

        PatientDto register = patientService.register(patientDto);

        final String toPatientName = "김길동";
        final String toBirthDate = "19910502";
        final String toPhoneNo = "01112343123";

        PatientDto toUpdatePatientDto = PatientDto.builder()
                .patientName(toPatientName)
                .birthDate(toBirthDate)
                .phoneNumber(toPhoneNo).build();

        patientService.updatePatient(register.getId(), toUpdatePatientDto);

        Patient patient = patientRepository.findAll().get(0);

        Assertions.assertThat(patient.getPatientName()).isEqualTo(toPatientName);
        Assertions.assertThat(patient.getBirthDate()).isEqualTo(toBirthDate);
        Assertions.assertThat(patient.getPhoneNumber()).isEqualTo(toPhoneNo);

    }

}
