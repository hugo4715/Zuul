

/**
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 * <p>
 * This class holds an enumeration table of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * @author Michael Kolling and David J. Barnes + D.Bureau
 * @version 2008.03.30 + 2013.09.15
 */
public class CommandWords {
    // tableau constant qui contient tous les mots de commande valides
    private static final String[] validCommands = {
            "go", "quit", "help", "look", "eat", "back"
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
        // pour chacune des commandes valides (du tableau constant)
        for (int i = 0; i < validCommands.length; i++) {
            // si elle est egale a pString
            if (validCommands[i].equals(command))
                return true;
        } // for
        // si nous arrivons la, c'est que la commande
        // n'a pas ete trouvee dans le tableau
        return false;
    }
}
