package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.model.id.StudentId;
import seedu.address.model.profbook.Address;
import seedu.address.model.profbook.Email;
import seedu.address.model.profbook.Name;
import seedu.address.model.profbook.Phone;
import seedu.address.model.profbook.Student;
import seedu.address.model.tag.Tag;
import seedu.address.model.taskmanager.Task;
import seedu.address.model.taskmanager.TaskList;
import seedu.address.commons.exceptions.IllegalValueException;


/**
 * Jackson-friendly version of {@link Student}.
 */
public class JsonAdaptedStudent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Student's %s field is missing!";

    private final String name;
    private final String email;
    private final String phone;

    // Data fields
    private final String address;
    private final Set<JsonAdaptedTag> tags = new HashSet<>();
    private final String id;

    private final Set<JsonAdaptedTasks> tasks = new HashSet<>();

    /**
     * Constructs a {@code JsonAdaptedStudent} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedStudent(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email, @JsonProperty("address") String address,
                              @JsonProperty("id") String id, @JsonProperty("tags") List<JsonAdaptedTag> tags,
                              @JsonProperty("tasks") List<JsonAdaptedTasks> tasks) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.id = id;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (tasks != null) {
            this.tasks.addAll(tasks);
        }
    }

    public JsonAdaptedStudent(Student source) {

    }




    public Student toModelType() throws IllegalValueException {
        final List<Tag> studentTags = new ArrayList<>();

        final List<Task> taskList = new ArrayList<>();

        for (JsonAdaptedTag tag : tags) {
            studentTags.add(tag.toModelType());
        }

        for (JsonAdaptedTasks task : tasks) {

            taskList.add(task.toModelType());
        }
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    seedu.address.model.profbook.Name.class.getSimpleName()));
        }
        if (!seedu.address.model.profbook.Name.isValidName(name)) {
            throw new IllegalValueException(seedu.address.model.profbook.Name.MESSAGE_CONSTRAINTS);
        }
        final seedu.address.model.profbook.Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    seedu.address.model.profbook.Phone.class.getSimpleName()));
        }
        if (!seedu.address.model.profbook.Phone.isValidPhone(phone)) {
            throw new IllegalValueException(seedu.address.model.profbook.Phone.MESSAGE_CONSTRAINTS);
        }
        final seedu.address.model.profbook.Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    seedu.address.model.profbook.Email.class.getSimpleName()));
        }
        if (!seedu.address.model.profbook.Email.isValidEmail(email)) {
            throw new IllegalValueException(seedu.address.model.profbook.Email.MESSAGE_CONSTRAINTS);
        }
        final seedu.address.model.profbook.Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    seedu.address.model.profbook.Address.class.getSimpleName()));
        }
        if (!seedu.address.model.profbook.Address.isValidAddress(address)) {
            throw new IllegalValueException(seedu.address.model.profbook.Address.MESSAGE_CONSTRAINTS);
        }
        final seedu.address.model.profbook.Address modelAddress = new Address(address);

        if (id == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    seedu.address.model.id.StudentId.class.getSimpleName()));
        }
        if (!seedu.address.model.id.StudentId.isValidStudentId(id)) {
            throw new IllegalValueException(seedu.address.model.id.StudentId.MESSAGE_CONSTRAINTS);
        }
        final seedu.address.model.id.Id studId = new StudentId(id);

        final seedu.address.model.taskmanager.TaskList modelTList = new TaskList(taskList);

        //USE WHEN MING IMPLEMENTS INTO STUDENT CONSTRUCTOR
        final Set<Tag> modelTags = new HashSet<>(studentTags);

        return new Student(modelTList, modelName, modelEmail, modelPhone, modelAddress, studId);
    }

}
