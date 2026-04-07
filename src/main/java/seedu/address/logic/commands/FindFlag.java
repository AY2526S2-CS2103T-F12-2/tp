package seedu.address.logic.commands;

/**
 * Represents whether a find segment is compulsory or optional.
 */
public enum FindFlag {
    COMPULSORY("c", "-c"),
    OPTIONAL("o", "-o");

    private final String regexGroup;
    private final String token;

    FindFlag(String regexGroup, String token) {
        this.regexGroup = regexGroup;
        this.token = token;
    }

    /**
     * Returns the {@code FindFlag} corresponding to the given regex capture-group value.
     *
     * @param group The regex capture-group value.
     * @return The corresponding {@code FindFlag}.
     * @throws IllegalArgumentException If the group does not match any known flag.
     */
    public static FindFlag fromGroup(String group) {
        for (FindFlag flag : values()) {
            if (flag.regexGroup.equals(group)) {
                return flag;
            }
        }
        throw new IllegalArgumentException("Unknown flag group: " + group);
    }

    /**
     * Returns the default flag to use when no explicit flag appears before a segment.
     *
     * @return The default {@code FindFlag}.
     */
    public static FindFlag defaultFlag() {
        return OPTIONAL;
    }
}
