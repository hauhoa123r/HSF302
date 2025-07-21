package com.web.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MergeObjectUtilsTest {
    @Test
    void merge_NonNullFields_shouldCopyNonNullFields() {
        Dummy source = new Dummy();
        source.setName("Alice");
        source.setAge(null);
        source.setAddress("Hanoi");

        Dummy target = new Dummy();
        target.setName("Bob");
        target.setAge(30);
        target.setAddress(null);

        MergeObjectUtils merger = new MergeObjectUtils();
        merger.mergeNonNullFields(source, target);

        Assertions.assertEquals("Alice", target.getName()); // from source
        Assertions.assertEquals(30, target.getAge()); // from target (source null)
        Assertions.assertEquals("Hanoi", target.getAddress()); // from source
    }

    @Test
    void merge_NonNullFields_shouldNotOverwriteWithNull() {
        Dummy source = new Dummy();
        source.setName(null);
        source.setAge(null);
        source.setAddress(null);

        Dummy target = new Dummy();
        target.setName("Bob");
        target.setAge(25);
        target.setAddress("Saigon");

        MergeObjectUtils merger = new MergeObjectUtils();
        merger.mergeNonNullFields(source, target);

        Assertions.assertEquals("Bob", target.getName());
        Assertions.assertEquals(25, target.getAge());
        Assertions.assertEquals("Saigon", target.getAddress());
    }

    static class Dummy {
        private String name;
        private Integer age;
        private String address;

        // getters and setters
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
