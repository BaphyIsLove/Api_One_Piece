package com.api.onepiece.util;

public class UniqueKeyGenerator {
    
    public static String generateUniqueKey(String prefix, Long lastNum) {
        Long nextNum = lastNum != null ? lastNum + 1 : 1;
        return prefix + "-" + String.format("%03d", nextNum);
    }
    
    public static String generateChapterUniqueKey(String prefix, Long lastNum) {
        Long nextNum = lastNum != null ? lastNum + 1 : 1;
        return prefix + "-" + String.format("%04d", nextNum);
    }

}
