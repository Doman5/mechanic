package com.domanski.mechanic.features;

import com.domanski.mechanic.BaseIntegrationTest;
import com.domanski.mechanic.IntegrationConfiguration;
import com.domanski.mechanic.MechanicApplication;
import com.domanski.mechanic.domain.part.dto.PartResponse;
import com.domanski.mechanic.domain.repair.dto.RepairReportResponse;
import com.domanski.mechanic.domain.repair.dto.RepairResponse;
import com.domanski.mechanic.domain.repair.model.RepairStatus;
import com.domanski.mechanic.infrastucture.repair.scheduler.RepairDateSetterScheduler;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {MechanicApplication.class, IntegrationConfiguration.class})
public class TypicalScenarioUserWantToReportRepairAndMechanicRepairCarIntegrationTest extends BaseIntegrationTest {

    @Autowired
    RepairDateSetterScheduler repairDateSetterScheduler;

    @Test
    public void user_want_to_report_repair_and_mechanic_should_repair_his_car() throws Exception {
        // step 1: mechanic made POST /token with username=mechanic, password=password and system returned OK(200) with token "QQQQ.WWWW.EEEE".
        // step 2: mechanic made GET /mechanic/repairs with header"Authorization: Bearer "QQQQ.WWWW.EEEE" and return OK(200) with 0 repairs.
        //given && when
        String performMechanicGetRepairsJson = mockMvc.perform(get("/mechanic/repairs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<RepairResponse> mechanicRepairsListWithNoRepairs = objectMapper.readValue(performMechanicGetRepairsJson, new TypeReference<>() {
        });
        //then
        assertThat(mechanicRepairsListWithNoRepairs).isEmpty();


        // step 3: user tried to GET jwt token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401).
        // step 4: user tried to POST /repair with repair report and return FORBIDDEN(403).
        // step 5: user made POST to /register username=someUser, password=somePassword and system register with status CREATED(201).
        // step 6: user tried get jwt token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwt token=AAAA.BBBB.CCCC.


        // step 7: user made GET to /repairs with header"Authorization: Bearer "AAAA.BBBB.CCCC" and system returned OK(200) with 0 repair.
        //given && when
        String performUserGetRepairsWithNoRepairsJson = mockMvc.perform(get("/repairs/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RepairResponse> userRepairsListWithNoRepairs = objectMapper.readValue(performUserGetRepairsWithNoRepairsJson, new TypeReference<>() {
        });
        //then
        assertThat(userRepairsListWithNoRepairs).isEmpty();


        // step 8: user made POST to /repairs with header"Authorization: Bearer "AAAA.BBBB.CCCC" and system returned CREATED(201) with reported repair with id 1000.
        // given && when
        String performUserPostRepairReportFirstTimeJson = mockMvc.perform(post("/repairs").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "description": "Repair description 1",
                                "userId": 1
                                }
                                """.trim()))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        RepairReportResponse repairReportResponse = objectMapper.readValue(performUserPostRepairReportFirstTimeJson, RepairReportResponse.class);
        // then
        assertAll(
                () -> assertThat(repairReportResponse.message()).isEqualTo("Naprawa została zgłoszona"),
                () -> assertThat(repairReportResponse.repair().id()).isEqualTo(1L),
                () -> assertThat(repairReportResponse.repair().repairStatus()).isEqualTo(RepairStatus.DATE_NOT_SPECIFIED),
                () -> assertThat(repairReportResponse.repair().description()).isEqualTo("Repair description 1")
        );


        // step 9: user made GET to /repairs with header"Authorization: Bearer "AAAA.BBBB.CCCC" and system returned OK(200) with 1 repair.
        //given && when
        String performUserGetRepairsWithOneRepairsJson = mockMvc.perform(get("/repairs/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<RepairResponse> userRepairsListWithOneRepairs = objectMapper.readValue(performUserGetRepairsWithOneRepairsJson, new TypeReference<>() {
        });
        //then
        assertAll(
                () -> assertThat(userRepairsListWithOneRepairs).hasSize(1),
                () -> assertThat(userRepairsListWithOneRepairs.get(0).id()).isEqualTo(1L),
                () -> assertThat(userRepairsListWithOneRepairs.get(0).date()).isNull(),
                () -> assertThat(userRepairsListWithOneRepairs.get(0).repairStatus()).isEqualTo(RepairStatus.DATE_NOT_SPECIFIED),
                () -> assertThat(userRepairsListWithOneRepairs.get(0).repairCost()).isEqualTo(BigDecimal.valueOf(0.00).setScale(2)),
                () -> assertThat(userRepairsListWithOneRepairs.get(0).workTime()).isEqualTo(0.00),
                () -> assertThat(userRepairsListWithOneRepairs.get(0).parts()).isEmpty(),
                () -> assertThat(userRepairsListWithOneRepairs.get(0).description()).isEqualTo("Repair description 1")
        );


        // step 10: user made GET to /repairs/1000 at 05.05.2022 11:55 with header"Authorization: Bearer "AAAA.BBBB.CCCC" and system returned OK(200) with repair without specified date;
        //given
        Long firstRepairId = userRepairsListWithOneRepairs.get(0).id();
        //when
        String userFirstPerformGetRepairWithId1 = mockMvc.perform(get("/repairs/" + firstRepairId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        RepairResponse firstTimeSearchedRepairWithId = objectMapper.readValue(userFirstPerformGetRepairWithId1, RepairResponse.class);
        //then
        assertAll(
                () -> assertThat(firstTimeSearchedRepairWithId.date()).isNull(),
                () -> assertThat(firstTimeSearchedRepairWithId.repairStatus()).isEqualTo(RepairStatus.DATE_NOT_SPECIFIED)
        );


        // step 11: scheduler run repairDateSetter and set date for repair with id 1000.
        repairDateSetterScheduler.generateAndSetRepairsDates();


        // step 10: user made GET to /repairs/1000 at 05.05.2022 12:05 with header"Authorization: Bearer "AAAA.BBBB.CCCC" and system returned OK(200) with repair with 06.05.2022 date and status awaiting;
        //given && when
        String userSecondPerformGetRepairWithId = mockMvc.perform(get("/repairs/" + firstRepairId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RepairResponse secondTimeSearchedRepairWithId = objectMapper.readValue(userSecondPerformGetRepairWithId, RepairResponse.class);
        //then
        assertAll(
                () -> assertThat(secondTimeSearchedRepairWithId.date()).isEqualTo(LocalDate.of(2022, 5, 6)),
                () -> assertThat(secondTimeSearchedRepairWithId.repairStatus()).isEqualTo(RepairStatus.AWAITING)
        );


        // step 11: mechanic made GET /mechanic/repairs with header"Authorization: Bearer "QQQQ.WWWW.EEEE" and return OK(200) with 1 repairs.
        //given && when
        String performSecondTimeMechanicGetRepairsJson = mockMvc.perform(get("/mechanic/repairs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<RepairResponse> mechanicRepairsListWithOneRepairs = objectMapper.readValue(performSecondTimeMechanicGetRepairsJson, new TypeReference<>() {
        });
        //then
        assertThat(mechanicRepairsListWithOneRepairs).hasSize(1);


        // step 12: mechanic made GET /parts with header"Authorization: Bearer "QQQQ.WWWW.EEEE" and return OK(200) with empty body.
        //given && when && then
        mockMvc.perform(get("/parts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));


        // step 13: mechanic made 2 time POST /parts with header"Authorization: Bearer "QQQQ.WWWW.EEEE" and new parts and system returned CREATED(201) and part.
        //given && when
        String performFirstPostParts = mockMvc.perform(post("/parts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "part1",
                                    "price": "10.00"
                                }
                                """.trim()))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String performSecondPostParts = mockMvc.perform(post("/parts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "part2",
                                    "price": "100.00"
                                }
                                """.trim()))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String performThirdPostParts = mockMvc.perform(post("/parts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "part3",
                                    "price": "1000.00"
                                }
                                """.trim()))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        PartResponse firstPart = objectMapper.readValue(performFirstPostParts, PartResponse.class);
        PartResponse secondPart = objectMapper.readValue(performSecondPostParts, PartResponse.class);
        PartResponse thirdPart = objectMapper.readValue(performThirdPostParts, PartResponse.class);
        //then
        assertAll(
                () -> assertThat(firstPart.id()).isEqualTo(1),
                () -> assertThat(firstPart.name()).isEqualTo("part1"),
                () -> assertThat(firstPart.price()).isEqualTo(BigDecimal.valueOf(10).setScale(2)),
                () -> assertThat(secondPart.id()).isEqualTo(2),
                () -> assertThat(secondPart.name()).isEqualTo("part2"),
                () -> assertThat(secondPart.price()).isEqualTo(BigDecimal.valueOf(100).setScale(2)),
                () -> assertThat(thirdPart.id()).isEqualTo(3),
                () -> assertThat(thirdPart.name()).isEqualTo("part3"),
                () -> assertThat(thirdPart.price()).isEqualTo(BigDecimal.valueOf(1000).setScale(2))
        );


        // step 14: mechanic made GET /parts with header"Authorization: Bearer "QQQQ.WWWW.EEEE" and return OK(200) with 2 parts.
        //given && when
        String performMechanicPartsWith2PartsJson = mockMvc.perform(get("/parts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<PartResponse> listWith3Parts = objectMapper.readValue(performMechanicPartsWith2PartsJson, new TypeReference<>() {
        });
        //then
        assertAll(
                () -> assertThat(listWith3Parts).hasSize(3),
                () -> assertThat(listWith3Parts.get(0).id()).isEqualTo(1),
                () -> assertThat(listWith3Parts.get(0).name()).isEqualTo("part1"),
                () -> assertThat(listWith3Parts.get(1).id()).isEqualTo(2),
                () -> assertThat(listWith3Parts.get(1).name()).isEqualTo("part2"),
                () -> assertThat(listWith3Parts.get(2).id()).isEqualTo(3),
                () -> assertThat(listWith3Parts.get(2).name()).isEqualTo("part3")
        );


        // step 15: mechanic made PUT /mechanic/repairs/1000 with header"Authorization: Bearer "QQQQ.WWWW.EEEE", 2 parts, 1 work hour and system returned OK().
        String performFirstTimeMechanicPutRepair = mockMvc.perform(put("/mechanic/repairs/" + firstRepairId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "parts": [
                                    {"partId": "1", "quantity": "1"},
                                    {"partId": "2", "quantity": "2"}
                                ],
                                "workiTime": "1.0"
                                }
                                """.trim()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        RepairResponse firstStepOfRepair = objectMapper.readValue(performFirstTimeMechanicPutRepair, RepairResponse.class);
        //then
        assertAll(
                () -> assertThat(firstStepOfRepair.parts()).hasSize(2),
                () -> assertThat(firstStepOfRepair.workTime()).isEqualTo(1.0),
                () -> assertThat(firstStepOfRepair.repairStatus()).isEqualTo(RepairStatus.WORK_IN_PROGRESS)
        );


        // step 16: user made GET to /repairs/1000 at with header"Authorization: Bearer "AAAA.BBBB.CCCC" and system returned OK(200) with repair status work in progres, new parts and work time;
        //given && when
        String userThirdPerformGetRepairWithIdJson = mockMvc.perform(get("/repairs/" + firstRepairId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        RepairResponse thirdTimeSearchedRepairWithId = objectMapper.readValue(userThirdPerformGetRepairWithIdJson, RepairResponse.class);
        //then
        assertAll(
                () -> assertThat(thirdTimeSearchedRepairWithId.repairStatus()).isEqualTo(RepairStatus.WORK_IN_PROGRESS),
                () -> assertThat(thirdTimeSearchedRepairWithId.parts()).hasSize(2),
                () -> assertThat(thirdTimeSearchedRepairWithId.parts().get(0).name()).isEqualTo("part1"),
                () -> assertThat(thirdTimeSearchedRepairWithId.parts().get(0).quantity()).isEqualTo(1),
                () -> assertThat(thirdTimeSearchedRepairWithId.parts().get(1).name()).isEqualTo("part2"),
                () -> assertThat(thirdTimeSearchedRepairWithId.parts().get(1).quantity()).isEqualTo(2),
                () -> assertThat(thirdTimeSearchedRepairWithId.workTime()).isEqualTo(1.00),
                () -> assertThat(thirdTimeSearchedRepairWithId.repairCost()).isEqualTo(BigDecimal.valueOf(310).setScale(2))
        );


        // step 17: mechanic made POST /mechanic/repairs/1000 with header"Authorization: Bearer "QQQQ.WWWW.EEEE", 1 earlier used part, 1 new part, 4 work hour and system returned OK().
        //given && when
        String performSecondTimeMechanicPutRepair = mockMvc.perform(put("/mechanic/repairs/" + firstRepairId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "parts": [
                                    {"partId": "1", "quantity": "1"},
                                    {"partId": "3", "quantity": "1"}
                                ],
                                "workiTime": "1.0"
                                }
                                """.trim()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        RepairResponse secondStepOfRepair = objectMapper.readValue(performSecondTimeMechanicPutRepair, RepairResponse.class);
        //then
        assertAll(
                () -> assertThat(secondStepOfRepair.parts()).hasSize(3),
                () -> assertThat(secondStepOfRepair.workTime()).isEqualTo(2.0),
                () -> assertThat(secondStepOfRepair.repairStatus()).isEqualTo(RepairStatus.WORK_IN_PROGRESS)
        );


        // step 18: user made GET to /repairs/1000 at with header"Authorization: Bearer "AAAA.BBBB.CCCC" and system returned OK(200) with new parts and work time;
        String userFourthPerformGetRepairWithIdJson = mockMvc.perform(get("/repairs/" + firstRepairId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        RepairResponse fourthTimeSearchedRepairWithId = objectMapper.readValue(userFourthPerformGetRepairWithIdJson, RepairResponse.class);
        //then
        assertAll(
                () -> assertThat(fourthTimeSearchedRepairWithId.repairStatus()).isEqualTo(RepairStatus.WORK_IN_PROGRESS),
                () -> assertThat(fourthTimeSearchedRepairWithId.parts()).hasSize(3),
                () -> assertThat(fourthTimeSearchedRepairWithId.parts().get(0).name()).isEqualTo("part1"),
                () -> assertThat(fourthTimeSearchedRepairWithId.parts().get(0).quantity()).isEqualTo(2),
                () -> assertThat(fourthTimeSearchedRepairWithId.parts().get(1).name()).isEqualTo("part2"),
                () -> assertThat(fourthTimeSearchedRepairWithId.parts().get(1).quantity()).isEqualTo(2),
                () -> assertThat(fourthTimeSearchedRepairWithId.parts().get(2).name()).isEqualTo("part3"),
                () -> assertThat(fourthTimeSearchedRepairWithId.parts().get(2).quantity()).isEqualTo(1),
                () -> assertThat(fourthTimeSearchedRepairWithId.workTime()).isEqualTo(2.00),
                () -> assertThat(fourthTimeSearchedRepairWithId.repairCost()).isEqualTo(BigDecimal.valueOf(1420).setScale(2))
        );


        // step 19: mechanic made PUT /mechanic/repairs/1000/end with header"Authorization: Bearer "QQQQ.WWWW.EEEE" and system returned OK().



        // step 20: user made GET to /repairs/1000 at with header"Authorization: Bearer "AAAA.BBBB.CCCC" and system returned OK(200) with status finished;
        // step 21: mechanic made GET /mechanic/repairs with header"Authorization: Bearer "QQQQ.WWWW.EEEE" and return OK(200) with 0 repairs.
        // step 22: user twice made POST to /repairs with header"Authorization: Bearer "AAAA.BBBB.CCCC" and system returned CREATED(201) with reported repair with id 2000 and 3000.
        // step 23: scheduler run repairDateSetter and set date for repair with id 2000 and 3000.
        // step 24: user made GET to /repairs with header"Authorization: Bearer "AAAA.BBBB.CCCC" and system returned OK(200) with 3 repairs.
    }
}
