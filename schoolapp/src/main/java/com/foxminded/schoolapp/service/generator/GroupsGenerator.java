package com.foxminded.schoolapp.service.generator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foxminded.schoolapp.dao.entity.GroupEntity;
import com.foxminded.schoolapp.exception.ServiceException;

@Service
public class GroupsGenerator implements Generator<GroupEntity> {

    private static final char HYPHEN = '-';
    private static final String NO_GROUP_OPTION = "No Group";
    private static final String PROP_KEY = "groupsQuantity";
    private static final String GET_MESSAGE = "Property file missing";
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupsGenerator.class);
    private final int groupQuantity;
    private Random random = new Random();

    @Autowired
    public GroupsGenerator(@Value("${generator.file}") String file) throws ServiceException {
        LOGGER.trace("Load group quantity from config file = {}", file);
        Properties properties = new Properties();
        try (InputStream fileInputStream = new FileInputStream(file)) {
            properties.load(fileInputStream);
            this.groupQuantity = Integer.parseInt(properties.getProperty(PROP_KEY));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ServiceException(GET_MESSAGE, e);
        }
    }

    @Override
    public List<GroupEntity> generate() {
        LOGGER.debug("GroupsGenerator generate() - starts");
        List<GroupEntity> groupList = Stream.generate(GroupEntity::new).limit(groupQuantity)
                .collect(Collectors.toCollection(ArrayList::new));
        groupList.forEach(s -> s.setName(getRandomName()));
        GroupEntity noGroup = new GroupEntity();
        noGroup.setName(NO_GROUP_OPTION);
        groupList.add(noGroup);
        LOGGER.trace("Returning of GroupEntities List that contains ({}) elements", groupList.size());
        return groupList;
    }

    private String getRandomName() {
        char firstChar = (char) ('a' + random.nextInt(26));
        char secondChar = (char) ('a' + random.nextInt(26));
        int firstNumber = random.nextInt(9);
        int secondNumber = random.nextInt(9);

        return String.format("%c%c%c%d%d", firstChar, secondChar, HYPHEN, firstNumber, secondNumber);
    }
}
