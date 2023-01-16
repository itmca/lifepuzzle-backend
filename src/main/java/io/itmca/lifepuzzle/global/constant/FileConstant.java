package io.itmca.lifepuzzle.global.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileConstant {
    static final public String TEMP_FOLDER_PATH = String.format("%s%stmp", System.getProperty("user.dir"), File.separator);
}
