package org.pbms.pbmsserver.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class JSONUtilTest {
    private static final Logger log = LoggerFactory.getLogger(JSONUtil.class);

    static class TestBean {
        private Integer id;
        private String name;
        private String code;

        public TestBean() {
        }

        public TestBean(Integer id, String name, String code) {
            this.id = id;
            this.name = name;
            this.code = code;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return "TestBean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", code='" + code + '\'' +
                    '}';
        }
    }

    @Test
    void str2Object_null() {
        assertThrows(NullPointerException.class, () -> JSONUtil.str2Object(null, TestBean.class));
    }

    @Test
    void str2Object_empty() {
        assertThrows(NullPointerException.class, () -> JSONUtil.str2Object("", TestBean.class));
    }

    @Test
    void str2Object_id_null() {
        String jsonStr = "{\"name\":\"abc\", \"code\":\"12345678\"}";
        TestBean actual = JSONUtil.str2Object(jsonStr, TestBean.class);
        assertNull(actual.getId());
        assertEquals("abc", actual.getName());
        assertEquals("12345678", actual.getCode());
    }

    @Test
    void str2Object() {
        String jsonStr = "{\"id\":1, \"name\":\"abc\", \"code\":\"12345678\"}";
        TestBean actual = JSONUtil.str2Object(jsonStr, TestBean.class);
        assertEquals(1, actual.getId());
        assertEquals("abc", actual.getName());
        assertEquals("12345678", actual.getCode());
    }

    @Test
    void str2Object_extra_field() {
        String jsonStr = "{\"id\":1, \"name\":\"abc\", \"code\":\"12345678\", \"extra\":123456}";
        TestBean actual = JSONUtil.str2Object(jsonStr, TestBean.class);
        assertEquals(1, actual.getId());
        assertEquals("abc", actual.getName());
        assertEquals("12345678", actual.getCode());
    }

    @Test
    void object2Str_null() {
        String expected = "{}";
        String actual = JSONUtil.object2Str(null);
        assertEquals(expected, actual);
    }

    @Test
    void object2Str_id_null() {
        String expected = "{\"name\":\"abc\",\"code\":\"12345678\"}";
        TestBean testBean = new TestBean(null, "abc", "12345678");
        String actual = JSONUtil.object2Str(testBean);
        assertEquals(expected, actual);
    }

    @Test
    void object2Str() {
        String expected = "{\"id\":1,\"name\":\"abc\",\"code\":\"12345678\"}";
        TestBean testBean = new TestBean(1, "abc", "12345678");
        String actual = JSONUtil.object2Str(testBean);
        assertEquals(expected, actual);
    }
}