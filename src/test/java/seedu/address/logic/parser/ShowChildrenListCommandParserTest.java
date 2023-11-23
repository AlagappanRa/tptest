package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_PATH_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_PATH_NOT_FOUND;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMPTY_PREAMBLE;
import static seedu.address.logic.commands.CommandTestUtil.HELP_OPTION;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.model.path.AbsolutePath.ROOT_PATH;
import static seedu.address.testutil.TypicalGroups.GROUP_ONE;
import static seedu.address.testutil.TypicalPaths.PATH_TO_GROUP_ONE;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.ShowChildrenListCommand;

public class ShowChildrenListCommandParserTest {
    private ShowChildrenListCommandParser parser = new ShowChildrenListCommandParser();

    @Test
    public void parse_help_returnsHelpMessage() {
        // help option only
        assertParseSuccess(parser, HELP_OPTION, ROOT_PATH, ShowChildrenListCommand.HELP_MESSAGE);

        // valid path with help option
        assertParseSuccess(parser, "grp-001" + HELP_OPTION, ROOT_PATH, ShowChildrenListCommand.HELP_MESSAGE);

        // invalid path with help option
        assertParseSuccess(parser, "0001Y" + HELP_OPTION, ROOT_PATH, ShowChildrenListCommand.HELP_MESSAGE);

        // invalid option with help option
        assertParseSuccess(parser, ADDRESS_DESC_AMY + HELP_OPTION, ROOT_PATH, ShowChildrenListCommand.HELP_MESSAGE);
    }

    @Test
    public void parse_invalidOption_throwsParseException() {
        assertParseFailure(parser, NAME_DESC_AMY, ROOT_PATH,
            String.format(Messages.MESSAGE_NO_OPTIONS, ShowChildrenListCommand.COMMAND_WORD));

        // multiple invalid option -> return first invalid option
        assertParseFailure(parser, ADDRESS_DESC_AMY + NAME_DESC_AMY, ROOT_PATH,
            String.format(Messages.MESSAGE_NO_OPTIONS, ShowChildrenListCommand.COMMAND_WORD));
    }

    @Test
    public void parse_emptyPath_returnsShowChildrenListCommandForCurrentPath() {
        // Show children list command for current path.
        assertParseSuccess(parser, EMPTY_PREAMBLE, ROOT_PATH, new ShowChildrenListCommand());
    }

    @Test
    public void parse_invalidRelativePathFormat_throwsParseException() {
        String invalidRelativePath = "abcde";
        assertParseFailure(parser, invalidRelativePath, ROOT_PATH,
                String.format(MESSAGE_INVALID_PATH_FORMAT, invalidRelativePath));
    }

    @Test
    public void parse_invalidRelativePath_throwsParseException() {
        String invalidRelativePath = "../..";
        assertParseFailure(parser, invalidRelativePath, ROOT_PATH,
                String.format(MESSAGE_PATH_NOT_FOUND, invalidRelativePath));
    }

    @Test
    public void parse_validPath_returnsShowChildrenListCommand() {
        String validPath = GROUP_ONE.getId().toString();
        ShowChildrenListCommand expectedCommand = new ShowChildrenListCommand(PATH_TO_GROUP_ONE);
        assertParseSuccess(parser, validPath, ROOT_PATH, expectedCommand);
    }
}
