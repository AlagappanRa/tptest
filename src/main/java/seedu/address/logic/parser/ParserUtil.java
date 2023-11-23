package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.Messages.MESSAGE_EMPTY_VALUE;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PATH_FORMAT;
import static seedu.address.logic.parser.CliSyntax.OPTION_HELP;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Category;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.id.GroupId;
import seedu.address.model.id.StudentId;
import seedu.address.model.path.AbsolutePath;
import seedu.address.model.path.RelativePath;
import seedu.address.model.path.exceptions.InvalidPathException;
import seedu.address.model.profbook.Address;
import seedu.address.model.profbook.Email;
import seedu.address.model.profbook.Name;
import seedu.address.model.profbook.Phone;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.ToDo;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {
    public static final DateTimeFormatter DATE_INPUT_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);
    public static final String MESSAGE_INVALID_INDEX = "Index provided is not a non-zero unsigned integer.";
    public static final String MESSAGE_INVALID_OPTION = "Invalid option: %1$s";
    public static final String MESSAGE_STUDENT_ID_NOT_FOUND = "No student id found in the path.";
    public static final String MESSAGE_GROUP_ID_NOT_FOUND = "No group id found in the path.";
    public static final String MESSAGE_INVALID_CATEGORY_FORMAT = "Format is invalid. Should be allStu or allGrp";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String path} into an {@code RelativePath}.
     *
     * @throws ParseException if the given {@code path} is invalid.
     */
    public static RelativePath parseRelativePath(String path) throws ParseException {
        requireNonNull(path);
        String trimmedPath = path.trim();
        RelativePath relativePath = null;
        try {
            relativePath = new RelativePath(trimmedPath);
        } catch (InvalidPathException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_PATH_FORMAT, path));
        }
        return relativePath;
    }

    /**
     * Resolves the {@code target} against given {@code path}
     * and returns the resolved path.
     *
     * @throws ParseException if there is error when resolving.
     */
    public static AbsolutePath resolvePath(AbsolutePath path, String target) throws ParseException {
        requireAllNonNull(path, target);
        RelativePath targetPath = ParserUtil.parseRelativePath(target);
        AbsolutePath fullPath = null;
        try {
            fullPath = path.resolve(targetPath);
        } catch (InvalidPathException e) {
            throw new ParseException(String.format(e.getMessage(), targetPath));
        }
        return fullPath;
    }

    /**
     * Extracts {@code StudentId} from the given {@code AbsolutePath}.
     *
     * @param path The {@code RelativePath} to parse.
     * @return The extracted {@code StudentId}.
     * @throws ParseException If the path is invalid or doesn't contain a valid {@code StudentId}.
     */
    public static StudentId parseStudentId(AbsolutePath path) throws ParseException {
        requireNonNull(path);
        Optional<StudentId> studentIdOptional = path.getStudentId();

        if (!studentIdOptional.isPresent()) {
            throw new ParseException(MESSAGE_STUDENT_ID_NOT_FOUND);
        }

        return studentIdOptional.get();
    }

    /**
     * Parses a {@code String student id} into a {@code StudentId}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code id} is invalid.
     */
    public static StudentId parseStudentId(String id) throws ParseException {
        requireNonNull(id);
        String trimmedId = id.trim();

        if (!StudentId.isValidStudentId(trimmedId)) {
            throw new ParseException(StudentId.MESSAGE_CONSTRAINTS);
        }

        return new StudentId(trimmedId);
    }

    /**
     * Extracts {@code GroupId} from the given {@code AbsolutePath}.
     *
     * @param path The {@code RelativePath} to parse.
     * @return The extracted {@code GroupId}.
     * @throws ParseException If the path is invalid or doesn't contain a valid {@code GroupId}.
     */
    public static GroupId parseGroupId(AbsolutePath path) throws ParseException {
        requireNonNull(path);
        Optional<GroupId> groupIdOptional = path.getGroupId();

        if (!groupIdOptional.isPresent()) {
            throw new ParseException(MESSAGE_GROUP_ID_NOT_FOUND);
        }

        return groupIdOptional.get();
    }

    /**
     * Parses a {@code String group id} into a {@code GroupId}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code id} is invalid.
     */
    public static GroupId parseGroupId(String id) throws ParseException {
        requireNonNull(id);
        String trimmedId = id.trim();

        if (!GroupId.isValidGroupId(trimmedId)) {
            throw new ParseException(GroupId.MESSAGE_CONSTRAINTS);
        }

        return new GroupId(trimmedId);
    }

    /**
     * Parses a {@code String dateTimeStr} into a {@code LocalDateTime}.
     *
     * @param dateTimeStr The dateTime string.
     * @throws ParseException if the given {@code String dateTimeStr} is invalid.
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) throws ParseException {
        requireNonNull(dateTimeStr);
        String trimmedDateTimeStr = dateTimeStr.trim();

        LocalDateTime dateTime = null;
        try {
            dateTime = LocalDateTime.parse(trimmedDateTimeStr, DATE_INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            if (e != null && e.getCause() != null) {
                throw new ParseException(e.getMessage());
            } else {
                throw new ParseException(Messages.MESSAGE_INVALID_DATETIME_FORMAT);
            }
        }

        return dateTime;
    }

    /**
     * Parses a {@code String category} into a {@code enum Category}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code String category} is invalid.
     */
    public static Category parseCategory(String category) throws ParseException {
        requireNonNull(category);
        String trimmedCat = category.trim();

        if (trimmedCat.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_EMPTY_VALUE, "all category"));
        }

        if (trimmedCat.equals("allStu")) {
            return Category.ALLSTU;
        }

        if (trimmedCat.equals("allGrp")) {
            return Category.ALLGRP;
        }

        throw new ParseException(MESSAGE_INVALID_CATEGORY_FORMAT);
    }

    /**
     * Parses a {@code ToDo} with the given {@code desc}.
     *
     * @throws ParseException if the given {@code desc} is empty.
     */
    public static ToDo parseToDo(String desc) throws ParseException {
        requireNonNull(desc);
        String trimmedDesc = desc.trim();

        if (trimmedDesc.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_EMPTY_VALUE, "description"));
        }

        return new ToDo(trimmedDesc);
    }

    /**
     * Parses a {@code Deadline} with the given {@code desc} and {@code by}.
     *
     * @throws ParseException if the given {@code desc} is empty or {@code by} is invalid.
     */
    public static Deadline parseDeadline(String desc, String by) throws ParseException {
        requireAllNonNull(desc, by);
        String trimmedDesc = desc.trim();
        String trimmedBy = by.trim();

        if (trimmedDesc.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_EMPTY_VALUE, "description"));
        }

        if (trimmedBy.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_EMPTY_VALUE, "deadline"));
        }

        LocalDateTime dt = parseDateTime(trimmedBy);
        return new Deadline(desc, dt);
    }

    /**
     * Checks if all options in {@code argString} are valid.
     */
    public static void verifyAllOptionsValid(String args, Option... options) throws ParseException {
        List<String> allOptions = ArgumentTokenizer.extractAllOptionNames(args);
        for (String optionName : allOptions) {
            Stream<Option> optionStream = Arrays.stream(options);
            Predicate<Option> pred = option
                -> optionName.equals(option.getLongName()) || optionName.equals(option.getShortName());
            if (optionStream.noneMatch(pred)) {
                throw new ParseException(String.format(MESSAGE_INVALID_OPTION, optionName));
            }
        }
    }

    /**
     * Returns true if help option included in {@code String args}
     */
    public static boolean hasHelpOption(String args) {
        String argsWithPadding = " " + args + " ";

        if (argsWithPadding.contains(" " + OPTION_HELP.getShortName() + " ")) {
            return true;
        }

        if (argsWithPadding.contains(" " + OPTION_HELP.getLongName() + " ")) {
            return true;
        }

        return false;
    }

    /**
     * Checks no options in the args, else throw ParseException.
     */
    public static void verifyNoOption(String args, String commandWord) throws ParseException {
        if (!ArgumentTokenizer.extractAllOptionNames(args).isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_NO_OPTIONS,
                    commandWord));
        }
    }

    /**
     * Returns true if none of the options contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    public static boolean areOptionsPresent(ArgumentMultimap argumentMultimap, Option... options) {
        return Stream.of(options).allMatch(option -> isOptionPresent(argumentMultimap, option));
    }

    /**
     * Returns true if the specified {@code option} contains non empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    public static boolean isOptionPresent(ArgumentMultimap argumentMultimap, Option option) {
        return argumentMultimap.getValue(option).isPresent();
    }

}
