package seedu.address.model.path;

import static seedu.address.logic.Messages.MESSAGE_INVALID_PATH_FORMAT;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import seedu.address.model.path.element.PathElement;
import seedu.address.model.path.element.PathElementType;
import seedu.address.model.path.element.exceptions.InvalidPathElementException;
import seedu.address.model.path.exceptions.InvalidPathException;


/**
 * Represents a path in our application.
 */
public abstract class Path {
    public static final String MESSAGE_EMPTY_PATH_STRING = "Path string cannot be empty";
    public static final String MESSAGE_INVALID_PATH_ELEMENT = "Encountered invalid path element: %1$s";
    public static final String MESSAGE_UNABLE_TO_NAVIGATE_ABOVE_ROOT = "Unable to navigate above root directory";
    public static final String MESSAGE_INVALID_PATH_STRUCTURE = "Invalid path structure.";
    public static final String MESSAGE_EMPTY_PATH_ELEMENT = "Path element cannot be empty.";
    public static final String PATH_DELIMETER = "/";
    protected final List<PathElement> pathElements;

    /**
     * Constructs an empty {@code Path}.
     */
    public Path() {
        this.pathElements = new ArrayList<>();
    }

    /**
     * Constructs a {@code Path} from a list of path elements.
     *
     * @param pathElements The list of path elements.
     */
    public Path(List<PathElement> pathElements) {
        this.pathElements = pathElements;
    }

    /**
     * Common constructor logic for initializing a {@code Path} from a string representation.
     *
     * @param path The string representation of the path.
     * @throws InvalidPathException If an invalid path element is encountered.
     */
    protected void commonConstructor(String path) throws InvalidPathException {
        if (path.startsWith(PATH_DELIMETER)) {
            path = path.substring(1, path.length());
        }

        if (path.endsWith(PATH_DELIMETER)) {
            path = path.substring(0, path.length() - 1);
        }

        if (path.trim().isEmpty()) {
            throw new InvalidPathException(MESSAGE_EMPTY_PATH_STRING);
        }

        String[] elementStrs = path.split("/");

        if (elementStrs.length == 0) {
            throw new InvalidPathException(MESSAGE_EMPTY_PATH_ELEMENT);
        }

        List<PathElement> elements = new ArrayList<>();

        // Parses all string elements into PathElement object.
        for (String elementStr : elementStrs) {
            try {
                PathElement element = PathElement.parsePathElement(elementStr);
                elements.add(element);
            } catch (InvalidPathElementException e) {
                throw new InvalidPathException(String.format(MESSAGE_INVALID_PATH_ELEMENT, elementStr));
            }
        }

        appendPathElements(this.pathElements, elements);
    }

    /**
     * Appends a list of path elements to an existing list of path elements.
     * Handles path element traversal, validation, and structure checks.
     *
     * @param destination The list to which path elements will be appended.
     * @param source The list of path elements to append.
     * @throws InvalidPathException If an invalid path element is encountered or the path structure is invalid.
     */
    protected static void appendPathElements(List<PathElement> destination, List<PathElement> source)
            throws InvalidPathException {

        for (PathElement element : source) {
            if (destination.isEmpty()) {
                destination.add(element);
                continue;
            }

            switch(element.getType()) {
            case CURRENT: // Skip "." element
                continue;
            case PARENT:
                handleParentElement(destination, element);
                break;
            default:
                handleOtherElement(destination, element);
                break;
            }
        }
    }

    private static void handleParentElement(List<PathElement> destination, PathElement element)
            throws InvalidPathException {
        PathElement prevElement = destination.get(destination.size() - 1);

        switch(prevElement.getType()) {
        case ROOT: // Cannot navigate above root
            throw new InvalidPathException(MESSAGE_UNABLE_TO_NAVIGATE_ABOVE_ROOT);
        case PARENT:
            destination.add(element);
            break;
        default:
            destination.remove(destination.size() - 1);
        }
    }

    private static void handleOtherElement(List<PathElement> destination, PathElement element)
            throws InvalidPathException {
        PathElement prevElement = destination.get(destination.size() - 1);

        int priorityDiff = element.getPriorityDiff(prevElement);

        // Make sure current element is 1 level lower than prev element or prev element is ".."
        if (priorityDiff != -1
                && prevElement.getType() != PathElementType.PARENT
                && prevElement.getType() != PathElementType.CURRENT) {
            throw new InvalidPathException(MESSAGE_INVALID_PATH_FORMAT);
        }

        destination.add(element);
    }

    @Override
    public String toString() {
        List<String> pathElementStrings = pathElements.stream()
                .map(PathElement::toString)
                .collect(Collectors.toList());

        return String.join(PATH_DELIMETER, pathElementStrings);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Path)) {
            return false;
        }

        Path other = (Path) obj;

        return this.pathElements.equals(other.pathElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathElements);
    }
}
