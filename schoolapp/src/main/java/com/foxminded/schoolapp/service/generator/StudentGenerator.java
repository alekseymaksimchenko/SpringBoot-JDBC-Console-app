package com.foxminded.schoolapp.service.generator;

import java.util.Map;
import java.util.Set;

public interface StudentGenerator<T> extends Generator<T> {

    Map<T, Set<Integer>> studentToCourseGenerator();

}
