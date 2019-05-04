import java.io.Serializable;

public class CommandWords implements Serializable {
    private static final String[] validCommands = {
            "go", "quit", "help", "look", "eat", "back", "test", "take", "drop", "items", "save", "load"
    };

    /**
     * Get all the available commands
     *
     * @return a String with all the command available
     */
    public String getCommandList() {
        StringBuilder msg = new StringBuilder();
        for (String vCmd : validCommands) {
            msg.append(vCmd);
            msg.append(' ');
        }
        msg.append('\n');
        return msg.toString();
    }

    /**
     * Verifie si une String donnee fait partie des commandes valides.
     *
     * @param command la String a tester
     * @return true si pString est une comande valide, false sinon
     */
    public boolean isCommand(final String command) {
        for (int i = 0; i < validCommands.length; i++) {
            if (validCommands[i].equals(command)) {
                return true;
            }
        }
        return false;
    }
}
