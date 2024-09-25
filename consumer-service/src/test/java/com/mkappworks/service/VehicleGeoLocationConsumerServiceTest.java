package com.mkappworks.service;

import com.mkappworks.repository.VehicleGeoLocationConsumerRepoInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VehicleGeoLocationConsumerServiceTest {
    @InjectMocks
    private VehicleGeoLocationConsumerService vehicleGeoLocationConsumerService;

    @Mock
    private VehicleGeoLocationConsumerRepoInterface vehicleGeoLocationConsumerRepo;

    private List<Map<String, List<Map<String, Object>>>> dataset;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
                )
        );
    }

    @Test
    public void should_successfully_get_vehicle_geo_locations() {
        when(vehicleGeoLocationConsumerRepo.getGeoLocationsByVehicle(any())).thenReturn(dataset);

        var result = vehicleGeoLocationConsumerService.getGeoLocationsByVehicle(any());

        assertEquals(dataset, result);
        verify(vehicleGeoLocationConsumerRepo, times(1)).getGeoLocationsByVehicle(any());
    }
}