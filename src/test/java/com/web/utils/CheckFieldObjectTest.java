package com.web.utils;

import com.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CheckFieldObjectTest {
    private CheckFieldObject checkFieldObject;

    @BeforeEach
    void setUp() {
        checkFieldObject = new CheckFieldObject();
    }

    @Test
    void check_objectNull_throwsException() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> checkFieldObject.check(DummyDTO.class, null, "name"));
    }

    @Test
    void check_fieldNull_throwsException() {
        DummyDTO dto = new DummyDTO(null, 20);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> checkFieldObject.check(DummyDTO.class, dto, "name"));
    }

    @Test
    void check_fieldValid_noException() {
        DummyDTO dto = new DummyDTO("Test", 20);
        Assertions.assertDoesNotThrow(() -> checkFieldObject.check(DummyDTO.class, dto, "name"));
    }

    @Test
    void check_multipleFields_oneNull_throwsException() {
        DummyDTO dto = new DummyDTO("Test", null);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> checkFieldObject.check(DummyDTO.class, dto, "name", "age"));
    }

    @Test
    void check_multipleFields_allValid_noException() {
        DummyDTO dto = new DummyDTO("Test", 30);
        Assertions.assertDoesNotThrow(() -> checkFieldObject.check(DummyDTO.class, dto, "name", "age"));
    }

    static class DummyDTO {
        private String name;
        private Integer age;

        public DummyDTO(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}