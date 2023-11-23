package seedu.address.logic;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Option;
import seedu.address.model.id.GroupId;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Student;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command:\n\n%1$s";

    public static final String MESSAGE_INVALID_COMMAND_FORMAT =
            "Invalid command format: %1$s\n"
            + "Try \'%2$s --help\' for more information.";

    public static final String MESSAGE_DUPLICATE_FIELDS =
            "Multiple values specified for the following single-valued field(s): ";

    public static final String MESSAGE_NO_OPTIONS = "%1$s: does not take any options.";

    public static final Function<String, String> MESSAGE_MISSING_ARGUMENT =
            x -> String.format(MESSAGE_INVALID_COMMAND_FORMAT, "missing argument(s)", x);

    public static final Function<String, String> MESSAGE_TOO_MANY_ARGUMENTS =
                x -> String.format(MESSAGE_INVALID_COMMAND_FORMAT, "too many arguments", x);

    public static final String MESSAGE_INVALID_DATETIME_FORMAT =
            "Invalid datetime format: Please use the format \'yyyy-MM-dd HH:mm\'"
            + "\n\n\'yyyy\': year, \'MM\': month,"
            + " 'dd\': day, \'HH\': hour (24-hour format),"
            + " 'mm\': minutes.";

    public static final String MESSAGE_INTERNAL_ERROR = "Internal error: %1$s";

    public static final String MESSAGE_INVALID_INDEX = "Invalid index for task list with size %1$s: %2$s";

    public static final String MESSAGE_TASK_LIST_NOT_SHOWN = "No task list is currently displayed.";

    public static final String MESSAGE_PATH_NOT_FOUND = "Path not found in ProfBook: %1$s";

    public static final String MESSAGE_INVALID_PATH_FORMAT = "Invalid relative path: %1$s\n"
            + "The provided path is not in a valid format.";

    public static final String MESSAGE_EMPTY_VALUE = "Value for %1$s cannot be empty.";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicateOptions(Option... duplicateOptions) {
        assert duplicateOptions.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicateOptions).map(Option::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code student} for display to the user.
     */
    public static String format(Student student) {
        final StringBuilder builder = new StringBuilder();
        builder.append("\n Name: ")
                .append(student.getName())
                .append("\n StudentId: ")
                .append(student.getId())
                .append("\n Phone: ")
                .append(student.getPhone())
                .append("\n Email: ")
                .append(student.getEmail())
                .append("\n Address: ")
                .append(student.getAddress());
        return builder.toString();
    }

    /**
     * Formats the {@code group} for display to the user.
     */
    public static String format(Group group) {
        final StringBuilder builder = new StringBuilder();
        builder.append("\n Name: ")
                .append(group.getName())
                .append("\n GroupId: ")
                .append(group.getId());
        return builder.toString();
    }

    /**
     * Formats the {@code groupId} for display to the user.
     */
    public static String format(GroupId groupId) {
        final StringBuilder builder = new StringBuilder();
        builder.append("GroupId: ")
                .append(groupId);
        return builder.toString();
    }
}
