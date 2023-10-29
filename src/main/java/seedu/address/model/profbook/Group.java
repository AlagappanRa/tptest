package seedu.address.model.profbook;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Map;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.id.GroupId;
import seedu.address.model.id.Id;
import seedu.address.model.task.ReadOnlyTaskList;
import seedu.address.ui.GroupCard;

/**
 * Encapsulates logic for a group within a tutorial group
 */
public class Group extends ChildrenAndTaskListManager<Group, Student> {

    /**
     * Name of the group
     */
    private final Name name;

    /**
     * Unique identifier of the group
     */
    private final GroupId id;

    /**
     * Constructs a Group instance with all fields.
     *
     * @param taskList - The task list associated with this group
     * @param students - The list of students in this group
     * @param name     - The group name
     * @param id       - Unique identifier of the group
     */
    public Group(ReadOnlyTaskList taskList, Map<Id, Student> students, Name name, GroupId id) {
        super(students, taskList);
        requireAllNonNull(name, id);
        this.name = name;
        this.id = id;
    }

    /**
     * Constructs a new {@code Group} without task list and student map.
     */
    public Group(Name name, GroupId id) {
        super();
        this.name = name;
        this.id = id;
    }

    /**
     * Create a {@code Group} with the data in {@code toBeCopied}.
     */
    public Group(Group toBeCopied) {
        super(toBeCopied);
        this.name = toBeCopied.name;
        this.id = toBeCopied.id;
    }

    public GroupId getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    @Override
    public Group deepCopy() {
        return new Group(this);
    }

    @Override
    public GroupCard getDisplayCard(int displayedIndex) {
        return new GroupCard(this, displayedIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("Group Id", id)
                .add("name", name)
                .add("Task List and Student List", super.toString())
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Group)) {
            return false;
        }

        Group otherGroup = (Group) other;
        return super.equals(otherGroup)
                && this.name.equals(otherGroup.name)
                && this.id.equals(otherGroup.id);
    }

}

