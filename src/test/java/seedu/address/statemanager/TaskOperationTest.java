package seedu.address.statemanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskOperation;
import seedu.address.model.UserPrefs;
import seedu.address.model.id.GroupId;
import seedu.address.model.id.Id;
import seedu.address.model.id.StudentId;
import seedu.address.model.path.AbsolutePath;
import seedu.address.model.path.exceptions.InvalidPathException;
import seedu.address.model.profbook.Group;
import seedu.address.model.profbook.Name;
import seedu.address.model.profbook.Root;
import seedu.address.model.profbook.Student;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.ReadOnlyTaskList;
import seedu.address.model.task.Task;
import seedu.address.model.task.exceptions.NoSuchTaskException;
import seedu.address.testutil.StudentBuilder;


public class TaskOperationTest {

    private Root root;
    private Group group;
    private Student student;

    private AbsolutePath rootPath;
    private AbsolutePath grpPath;
    private AbsolutePath stuPath;
    private Model model;
    private final Task task = new Deadline("Assignment 3", LocalDateTime.parse("2023-12-03T23:59"));

    @BeforeEach
    public void init() {
        try {
            rootPath = new AbsolutePath("~/");
            grpPath = new AbsolutePath("~/grp-001");
            stuPath = new AbsolutePath("~/grp-001/0001Y");
        } catch (InvalidPathException e) {
            fail();
            return;
        }
        this.student = new StudentBuilder()
                .withName("zann")
                .withEmail("zannwhatudoing@example.com")
                .withPhone("98765432")
                .withAddress("311, Clementi Ave 2, #02-25")
                .withId("0001Y").build();
        Map<Id, Student> studentMap = new HashMap<>();
        studentMap.put(new StudentId("0001Y"), this.student);
        this.group = new Group(new ReadOnlyTaskList(), studentMap, new Name("gary"), new GroupId("grp-001"));
        Map<Id, Group> groups = new HashMap<>();
        groups.put(new GroupId("grp-001"), this.group);
        this.root = new Root(groups);
        model = new ModelManager(rootPath, root, new UserPrefs());
    }

    @Test
    public void getTaskOperation_noErrorReturn() {
        assertEquals(new TaskOperation(this.group.getTaskListManager()), model.taskOperation(grpPath));
        assertEquals(new TaskOperation(this.student), model.taskOperation(stuPath));
    }

    @Test
    public void taskOperationVerifyDeleteMethod_noErrorReturn() {
        TaskOperation opr;
        opr = model.taskOperation(stuPath);
        assertTrue(this.student.contains(task));
        try {
            for (Task t : this.student.getAllTasks()) {
                System.out.println(t);
            }
            opr.deleteTask(1);
            for (Task t : this.student.getAllTasks()) {
                System.out.println(t);
            }

        } catch (NoSuchTaskException e) {
            fail();
        }
        //assertFalse(this.student.contains(task));
    }

    @Test
    public void taskOperationVerifyAdd_noErrorReturn() {
        TaskOperation opr;
        opr = model.taskOperation(stuPath);
        opr.deleteTask(1);
        assertFalse(opr.hasTask(task));
        opr.addTask(task);
        assertTrue(this.student.contains(task));
    }

    @Test
    public void taskOperationVerifyMarkUnmark_noErrorReturn() {
        TaskOperation opr;
        opr = model.taskOperation(stuPath);
        List<Task> ret = opr.findTask("Assignment");
        assertEquals(ret.get(0), this.task);
        ret = opr.findTask("not here");
        assertEquals(0, ret.size());
    }

    @Test
    public void taskOperationVerifyGetTasks_noErrorReturn() {
        TaskOperation opr;
        opr = model.taskOperation(stuPath);
        assertEquals(opr.getTask(1), this.task);
    }

    @Test
    public void taskOperationVerifyGetAllTasks_noErrorReturn() {
        TaskOperation opr;
        opr = model.taskOperation(stuPath);
        ArrayList<Task> list = new ArrayList<>();
        list.add(this.task);
        assertEquals(opr.getAllTasks(), list);
    }
}
