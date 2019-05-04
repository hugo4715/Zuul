package pkg_game.pkg_command;

import pkg_game.GameEngine;
import pkg_game.UserInterface;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CommandWords implements Serializable {
    private Map<String,Command> commands;

    public CommandWords(final GameEngine engine) {
        this.commands = new HashMap<>();

        commands.put("go", new CommandGo(engine));
        commands.put("alea",new CommandAlea(engine));
        commands.put("back", new CommandBack(engine));
        commands.put("drop", new CommandDrop(engine));
        commands.put("eat", new CommandEat(engine));
        commands.put("fire", new CommandFire(engine));
        commands.put("help", new CommandHelp(engine));
        commands.put("items", new CommandItems(engine));
        commands.put("load", new CommandLoad(engine));
        commands.put("look", new CommandLook(engine));
        commands.put("quit", new CommandQuit(engine));
        commands.put("save", new CommandSave(engine));
        commands.put("take", new CommandTake(engine));
        commands.put("test", new CommandTest(engine));
        commands.put("use", new CommandUse(engine));
        commands.put("talk", new CommandTalk(engine));
    }

    public void setGui(final UserInterface userInterface){
        commands.values().forEach(cmd -> cmd.setGui(userInterface));
    }

    /**
     * Get all the available commands
     *
     * @return a String with all the command available
     */
    public String getCommandList() {
        StringBuilder msg = new StringBuilder();
        for (String vCmd : commands.keySet()) {
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
        return commands.containsKey(command);
    }


    public Command getCommand(String vWord1) {
        return commands.get(vWord1);
    }

}
