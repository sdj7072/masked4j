package io.github.masked4j.benchmark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.masked4j.annotation.MaskType;
import io.github.masked4j.annotation.Masked;
import io.github.masked4j.jackson.MaskedModule;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class SerializationBenchmark {

    private ObjectMapper vanillaMapper;
    private ObjectMapper maskedMapper;
    private UserDto userDto;
    private List<UserDto> userList;

    @Setup
    public void setup() {
        vanillaMapper = new ObjectMapper();
        vanillaMapper.registerModule(new JavaTimeModule());

        maskedMapper = new ObjectMapper();
        maskedMapper.registerModule(new JavaTimeModule());
        maskedMapper.registerModule(new MaskedModule());

        userDto = new UserDto(
                "Hong Gil Dong",
                "test@example.com",
                "010-1234-5678",
                "1234-5678-1234-5678",
                "192.168.0.1",
                "Seoul, Korea");

        userList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            userList.add(userDto);
        }
    }

    @Benchmark
    public String baseline_single() throws JsonProcessingException {
        return vanillaMapper.writeValueAsString(userDto);
    }

    @Benchmark
    public String masked_single() throws JsonProcessingException {
        return maskedMapper.writeValueAsString(userDto);
    }

    @Benchmark
    public String baseline_list_1000() throws JsonProcessingException {
        return vanillaMapper.writeValueAsString(userList);
    }

    @Benchmark
    public String masked_list_1000() throws JsonProcessingException {
        return maskedMapper.writeValueAsString(userList);
    }

    public static class UserDto {
        @Masked(MaskType.NAME)
        public String name;

        @Masked(MaskType.EMAIL)
        public String email;

        @Masked(MaskType.PHONE_NUMBER)
        public String phoneNumber;

        @Masked(MaskType.CREDIT_CARD)
        public String creditCard;

        @Masked(MaskType.IP_ADDRESS)
        public String ipAddress;

        @Masked(MaskType.ADDRESS)
        public String address;

        public UserDto() {
        }

        public UserDto(String name, String email, String phoneNumber, String creditCard, String ipAddress,
                String address) {
            this.name = name;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.creditCard = creditCard;
            this.ipAddress = ipAddress;
            this.address = address;
        }
    }
}
