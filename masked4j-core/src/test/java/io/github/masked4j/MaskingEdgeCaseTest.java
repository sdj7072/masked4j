package io.github.masked4j;

import io.github.masked4j.annotation.Masked;
import io.github.masked4j.annotation.MaskType;
import io.github.masked4j.core.DefaultStringMasker;
import io.github.masked4j.core.MaskingEngine;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MaskingEdgeCaseTest {

    private final MaskingEngine engine = new MaskingEngine();

    @Test
    void testNestedObjects() {
        AddressDto address = new AddressDto("서울시 성북구 북악산로 101동 1204호");
        UserDto user = new UserDto("Alice", address);

        engine.mask(user);

        assertThat(user.name).isEqualTo("A***e");
        assertThat(user.address.fullAddress).isEqualTo("서울시 성북구 북악산로 ***동 ****호");
    }

    @Test
    void testListMasking() {
        List<UserDto> users = new ArrayList<>();
        users.add(new UserDto("Alice", new AddressDto("서울시 101동")));
        users.add(new UserDto("Bob", new AddressDto("경기도 202동")));

        engine.mask(users);

        assertThat(users.get(0).name).isEqualTo("A***e");
        assertThat(users.get(0).address.fullAddress).isEqualTo("서울시 ***동");
        assertThat(users.get(1).name).isEqualTo("B***b");
        assertThat(users.get(1).address.fullAddress).isEqualTo("경기도 ***동");
    }

    @Test
    void testMapMasking() {
        Map<String, UserDto> userMap = new HashMap<>();
        userMap.put("user1", new UserDto("Alice", new AddressDto("서울시 101동")));
        userMap.put("user2", new UserDto("Bob", new AddressDto("경기도 202동")));

        engine.mask(userMap);

        assertThat(userMap.get("user1").name).isEqualTo("A***e");
        assertThat(userMap.get("user1").address.fullAddress).isEqualTo("서울시 ***동");
        assertThat(userMap.get("user2").name).isEqualTo("B***b");
    }

    @Test
    void testArrayMasking() {
        UserDto[] users = new UserDto[] {
                new UserDto("Alice", new AddressDto("서울시 101동")),
                new UserDto("Bob", new AddressDto("경기도 202동"))
        };

        engine.mask(users);

        assertThat(users[0].name).isEqualTo("A***e");
        assertThat(users[0].address.fullAddress).isEqualTo("서울시 ***동");
        assertThat(users[1].name).isEqualTo("B***b");
    }

    @Test
    void testEmojiMasking() {
        DefaultStringMasker masker = new DefaultStringMasker();
        // "😊Hello🌏" -> "😊***🌏"
        assertThat(masker.mask("😊Hello🌏")).isEqualTo("😊***🌏");
        // "😊🌏" -> "***" (length 2 code points)
        assertThat(masker.mask("😊🌏")).isEqualTo("***");
        // "😊" -> "***"
        assertThat(masker.mask("😊")).isEqualTo("***");
    }

    @Test
    void testNullAndEmpty() {
        UserDto user = new UserDto(null, null);
        engine.mask(user);
        assertThat(user.name).isNull();
        assertThat(user.address).isNull();

        DefaultStringMasker masker = new DefaultStringMasker();
        assertThat(masker.mask(null)).isEqualTo("***");
        assertThat(masker.mask("")).isEqualTo("***");
    }

    static class UserDto {
        @Masked(MaskType.STRING)
        String name;

        AddressDto address;

        public UserDto(String name, AddressDto address) {
            this.name = name;
            this.address = address;
        }
    }

    static class AddressDto {
        @Masked(MaskType.ADDRESS)
        String fullAddress;

        public AddressDto(String fullAddress) {
            this.fullAddress = fullAddress;
        }
    }
}
