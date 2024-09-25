package com.mkappworks.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkappworks.dtos.VehicleIdsRequest;
import com.mkappworks.service.VehicleGeoLocationConsumerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleGeoLocationController.class)
@AutoConfigureMockMvc
class VehicleGeoLocationControllerTest {

    @MockBean
    VehicleGeoLocationConsumerService vehicleGeoLocationConsumerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    private List<Map<String, List<Map<String, Object>>>> dataset;
    private VehicleIdsRequest vehicleIdsRequest;

    @BeforeEach
    void setUp() {
        dataset = List.of(
                Map.of(
                        "vehicle_1", List.of(
                                Map.of(
                                        "latitude", 40.712776,
                                        "longitude", -74.005974

                                ),
                                Map.of(
                                        "latitude", 40.712700,
                                        "longitude", -74.006000

                                )
                        )
                ),
                Map.of(
                        "vehicle_2", List.of(
                                Map.of(
                                        "latitude", 34.052235,
                                        "longitude", -118.243683

                                ),
                                Map.of(
                                        "latitude", 34.052240,
                                        "longitude", -118.243690

                                )
                        )
                )
        );

        vehicleIdsRequest = new VehicleIdsRequest(List.of("vehicle_1", "vehicle_2"));

    }

    @Test
    void should_get_vehicle_locations() throws Exception {
        var requestJson = objectMapper.writeValueAsString(vehicleIdsRequest);
        var responseJson = objectMapper.writeValueAsString(dataset);

        when(vehicleGeoLocationConsumerService.getGeoLocationsByVehicle(any())).thenReturn(dataset);

        mockMvc.perform(post("/vehicle/geolocation")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));

        verify(vehicleGeoLocationConsumerService, times(1)).getGeoLocationsByVehicle(any());
    }
}