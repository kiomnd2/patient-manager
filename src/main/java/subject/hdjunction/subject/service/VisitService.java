package subject.hdjunction.subject.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subject.hdjunction.subject.domain.Hospital;
import subject.hdjunction.subject.domain.Patient;
import subject.hdjunction.subject.domain.Visit;
import subject.hdjunction.subject.dto.PatientDto;
import subject.hdjunction.subject.dto.VisitDto;
import subject.hdjunction.subject.exception.NotFoundHospitalException;
import subject.hdjunction.subject.exception.NotFoundPatientException;
import subject.hdjunction.subject.repository.HospitalRepository;
import subject.hdjunction.subject.repository.PatientRepository;
import subject.hdjunction.subject.repository.VisitRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class VisitService {

    final private VisitRepository visitRepository;

    final private PatientRepository patientRepository;

    final private HospitalRepository hospitalRepository;

    public VisitDto registerVisit(VisitDto visitDto) {

        Patient patient = patientRepository.findById(visitDto.getPatientId())
                .orElseThrow(NotFoundPatientException::new);
        Hospital hospital = hospitalRepository.findById(visitDto.getHospitalId())
                .orElseThrow(NotFoundHospitalException::new);

        Visit visits = Visit.builder()
                .receptionDateTime(visitDto.getReceptionDateTime())
                .hospital(hospital)
                .patient(patient)
                .visitStateCode(visitDto.getVisitStateCode()).build();

        Visit savedVisits = visitRepository.save(visits);

        return VisitDto.builder()
                .visitStateCode(savedVisits.getVisitStateCode())
                .hospitalId(savedVisits.getHospital().getId())
                .patientId(savedVisits.getPatient().getId())
                .receptionDateTime(savedVisits.getReceptionDateTime())
                .build();
    }

    public VisitDto getVisit(Long patientId) {
        return null;
    }

    public VisitDto updateVisit(Long patientId, VisitDto visitDto) {
        return null;
    }

    public void removeVisit(Long visitId) {

    }
}