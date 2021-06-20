package ru.netology.patient.medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTest {
    @Test
    void test_MedicalServiceImpl_checkBloodPressure_need_help() {
        String patientId = "1";
        PatientInfo patientInfo = new PatientInfo("1", "Anna", "Ivanova",
                LocalDate.of(1990, 11, 14),
                new HealthInfo(new BigDecimal("36.8"), new BloodPressure(160, 90)));
        BloodPressure idealBloodPressure = new BloodPressure(120, 70);

        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById(patientId)).thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);
        medicalService.checkBloodPressure(patientId, idealBloodPressure);

        Mockito.verify(sendAlertService, Mockito.only()).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: 1, need help", argumentCaptor.getValue());
    }

    @Test
    void test_MedicalServiceImpl_checkBloodPressure_does_not_need_help() {
        String patientId = "2";
        PatientInfo patientInfo = new PatientInfo("2", "Ivan", "Kovalev",
                LocalDate.of(1984, 3, 24),
                new HealthInfo(new BigDecimal("38.6"), new BloodPressure(120, 70)));
        BloodPressure idealBloodPressure = new BloodPressure(120, 70);

        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById(patientId)).thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);
        medicalService.checkBloodPressure(patientId, idealBloodPressure);

        Mockito.verify(sendAlertService, Mockito.never()).send(Mockito.anyString());

    }

    @Test
    void test_MedicalServiceImpl_checkTemperature_need_help() {
        String patientId = "2";
        PatientInfo patientInfo = new PatientInfo("2", "Ivan", "Kovalev",
                LocalDate.of(1984, 3, 24),
                new HealthInfo(new BigDecimal("39.0"), new BloodPressure(120, 70)));
        BigDecimal idealTemperature = new BigDecimal("36.6");

        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById(patientId)).thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);
        medicalService.checkTemperature(patientId, idealTemperature);

        Mockito.verify(sendAlertService, Mockito.only()).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: 2, need help", argumentCaptor.getValue());

    }

    @Test
    void test_MedicalServiceImpl_checkTemperature_does_not_need_help() {
        String patientId = "1";
        PatientInfo patientInfo = new PatientInfo("1", "Anna", "Ivanova",
                LocalDate.of(1990, 11, 14),
                new HealthInfo(new BigDecimal("36.8"), new BloodPressure(160, 90)));
        BigDecimal idealTemperature = new BigDecimal("36.6");

        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById(patientId)).thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);
        medicalService.checkTemperature(patientId, idealTemperature);

        Mockito.verify(sendAlertService, Mockito.never()).send(Mockito.anyString());
    }
}
