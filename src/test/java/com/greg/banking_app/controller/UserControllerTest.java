package com.greg.banking_app.controller;

import com.google.gson.Gson;
import com.greg.banking_app.domain.User;
import com.greg.banking_app.dto.user.UserBaseDto;
import com.greg.banking_app.dto.user.UserCreateDto;
import com.greg.banking_app.mapper.UserMapper;
import com.greg.banking_app.service.UserDbService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDbService userDbService;

    @MockBean
    private UserMapper userMapper;

    private UserBaseDto getActiveUserBaseDto() {
        return new UserBaseDto(1L, "123456789", "John", "Doe", "TestEmail",
                "12345", true);
    }

    private UserBaseDto getDeActiveUserBaseDto() {
        return new UserBaseDto(2L, "987654321", "Andrew", "Smith", "TestEmail2",
                "54321", false);
    }

    @Test
    void shouldReturnEmptyUsersList() throws Exception {
        //Given
        when(userMapper.mapToUserBaseDtoList(userDbService.getAllUsers())).thenReturn(List.of());
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnUsersList() throws Exception {
        //Given
        List<UserBaseDto> dtos = List.of(getActiveUserBaseDto(), getDeActiveUserBaseDto());
        when(userMapper.mapToUserBaseDtoList(userDbService.getAllUsers())).thenReturn(dtos);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].peselNumber", Matchers.is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName", Matchers.is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email", Matchers.is("TestEmail2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].telephone", Matchers.is("54321")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].active", Matchers.is(false)));
    }

    @Test
    void shouldReturnUser() throws Exception {
        //Given
        UserBaseDto dto = getActiveUserBaseDto();
        when(userMapper.mapToUserBaseDto(userDbService.getUser(anyLong()))).thenReturn(dto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/users/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.peselNumber", Matchers.is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("TestEmail")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.telephone", Matchers.is("12345")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.active", Matchers.is(true)));
    }

    @Test
    void shouldCreateUser() throws Exception {
        //Given
        UserBaseDto dto = getActiveUserBaseDto();
        UserCreateDto createDto =
                new UserCreateDto("123456789", "John", "Doe", "TestEmail", "12345");
        User user = new User();

        when(userMapper.mapToUserCreate(any(UserCreateDto.class))).thenReturn(user);
        when(userMapper.mapToUserBaseDto(userDbService.createUser(any(User.class)))).thenReturn(dto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(createDto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.peselNumber", Matchers.is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("TestEmail")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.telephone", Matchers.is("12345")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.active", Matchers.is(true)));
    }

    @Test
    void shouldDeActiveUser() throws Exception {
        //Given
        Mockito.doNothing().when(userDbService).deActiveUser(anyLong());
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/users/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is("User is deActivated")));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        //Given
        UserBaseDto dto = getDeActiveUserBaseDto();
        User user = new User();

        when(userMapper.mapToUser(any(UserBaseDto.class))).thenReturn(user);
        when(userMapper.mapToUserBaseDto(userDbService.updateUser(any(User.class)))).thenReturn(dto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(dto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.peselNumber", Matchers.is("987654321")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("Smith")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("TestEmail2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.telephone", Matchers.is("54321")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.active", Matchers.is(false)));
    }
}