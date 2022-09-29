package com.greg.banking_app.controller;

import com.google.gson.Gson;
import com.greg.banking_app.domain.UserAddress;
import com.greg.banking_app.dto.useraddress.UserAddressDto;
import com.greg.banking_app.mapper.UserAddressMapper;
import com.greg.banking_app.service.UserAddressDbService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
@WebMvcTest(UserAddressController.class)
class UserAddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAddressDbService userAddressDbService;

    @MockBean
    private UserAddressMapper userAddressMapper;

    private UserAddressDto getUserAddressDto() {
        return new UserAddressDto(1L, "TestAddressLine", "11-111",
                "TestCity", true, 10L);
    }

    private UserAddressDto getUserAddressCorrespondenceFalseDto() {
        return new UserAddressDto(2L, "TestAddressLine2", "22-222",
                "TestCity2", false, 10L);
    }

    @Test
    void shouldReturnEmptyAddressesList() throws Exception {
        //Given
        when(userAddressMapper.mapToUserAddressDtoList(userAddressDbService.getUserAddresses(anyLong()))).thenReturn(List.of());
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/addresses/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnAddressesList() throws Exception {
        //Given
        List<UserAddressDto> dtos = List.of(getUserAddressDto(), getUserAddressCorrespondenceFalseDto());
        when(userAddressMapper.mapToUserAddressDtoList(userAddressDbService.getUserAddresses(anyLong()))).thenReturn(dtos);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/addresses/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].addressLine", Matchers.is("TestAddressLine")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].zipCode", Matchers.is("11-111")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].city", Matchers.is("TestCity2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].correspondence", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].userId", Matchers.is(10)));
    }

    @Test
    void shouldReturnAddress() throws Exception {
        //Given
        UserAddressDto dto = getUserAddressDto();
        when(userAddressMapper.mapToUserAddressDto(userAddressDbService.getUserAddress(anyLong(), anyLong()))).thenReturn(dto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/addresses/{userId}/{addressId}", 10, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressLine", Matchers.is("TestAddressLine")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode", Matchers.is("11-111")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city", Matchers.is("TestCity")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.correspondence", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Matchers.is(10)));
    }

    @Test
    void shouldCreateAddress() throws Exception {
        //Given
        UserAddressDto dto = getUserAddressDto();
        UserAddress userAddress = new UserAddress();

        when(userAddressMapper.mapToUserAddress(any(UserAddressDto.class))).thenReturn(userAddress);
        when(userAddressMapper.mapToUserAddressDto(userAddressDbService.createUserAddress(any(UserAddress.class)))).thenReturn(dto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(dto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressLine", Matchers.is("TestAddressLine")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode", Matchers.is("11-111")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city", Matchers.is("TestCity")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.correspondence", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Matchers.is(10)));
    }

    @Test
    void shouldUpdateAddress() throws Exception {
        //Given
        UserAddressDto dto = getUserAddressDto();
        UserAddress userAddress = new UserAddress();

        when(userAddressMapper.mapToUserAddress(any(UserAddressDto.class))).thenReturn(userAddress);
        when(userAddressMapper.mapToUserAddressDto(userAddressDbService.updateUserAddress(any(UserAddress.class)))).thenReturn(dto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(dto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressLine", Matchers.is("TestAddressLine")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode", Matchers.is("11-111")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city", Matchers.is("TestCity")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.correspondence", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Matchers.is(10)));
    }

    @Test
    void shouldRemoveAddress() throws Exception {
        //Given
        Mockito.doNothing().when(userAddressDbService).deleteUserAddress(anyLong());
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/addresses/{addressId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is("Address with id: 1 deleted")));
    }
}