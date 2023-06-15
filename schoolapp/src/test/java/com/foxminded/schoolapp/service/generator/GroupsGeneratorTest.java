package com.foxminded.schoolapp.service.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.foxminded.schoolapp.dao.entity.GroupEntity;
import com.foxminded.schoolapp.exception.DomainException;

class GroupsGeneratorTest {

    private static final int GROUPS_QUANTITY = 11;
    private static final char HYPHEN = '-';
    private static final String PATH = "./src/test/resources/generatorsConfigurationTest.properties";

    private GroupsGenerator group;
    private List<GroupEntity> groupList;

    @BeforeEach
    public void initEach() throws Exception {
        group = new GroupsGenerator(PATH);
        groupList = group.generate();

    }

    @Test
    void testGroupsGenerator_throwsDomainException() {
        DomainException ex = assertThrows(DomainException.class, () -> new GroupsGenerator("test"));
        String expectedMessage = "Property file missing";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    void testGroupsGenerator_thatGroupNameSizeEqualsToDefined() {
        int actual = groupList.size();
        int expected = GROUPS_QUANTITY;
        assertEquals(expected, actual);
    }

    @Test
    void testGroupsGenerator_thatGroupNameHaveCorrectFormat() {
        groupList.stream().limit(groupList.size() - 1).map(s -> s.getName()).forEach(s -> {
            char[] actual = s.toCharArray();
            boolean indexZeroisChar = Character.isAlphabetic(actual[0]);
            boolean indexOneisChar = Character.isAlphabetic(actual[1]);
            char indexTwo = actual[2];
            boolean indexThreeisDigit = Character.isDigit(actual[3]);
            boolean indexFoгrisDigit = Character.isDigit(actual[4]);

            assertTrue(indexZeroisChar);
            assertTrue(indexOneisChar);
            assertEquals(HYPHEN, indexTwo);
            assertTrue(indexThreeisDigit);
            assertTrue(indexFoгrisDigit);
        });
    }

    @Test
    void testGroupsGenerator_thatGroupNamesInListAreDifferent() {
        int actual = groupList.stream().map(s -> s.getName()).distinct().collect(Collectors.toList()).size();
        int expected = GROUPS_QUANTITY;

        assertEquals(expected, actual);
    }

    @Test
    void testGroupsGenerator_thatCourseListNotNull() {
        assertNotNull(groupList);
    }

    @Test
    void testGroupsGenerator_thatCourseListNotEmpty() {
        assertFalse(groupList.isEmpty());
    }

}
