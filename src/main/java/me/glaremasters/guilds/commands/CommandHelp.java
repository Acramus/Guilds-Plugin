package me.glaremasters.guilds.commands;

import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.commands.base.CommandBase;
import me.glaremasters.guilds.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandHelp extends CommandBase {

    private final int MAX_PAGE_SIZE = 6;

    public CommandHelp() {
        super("help", Guilds.getInstance().getConfig().getString("commands.description.help"),
                "guilds.command.help", true, null, "[page]", 0, 1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        boolean nextPage = true;

        int page = 1;

        if (args.length > 0) {
            try {
                page = Integer.valueOf(args[0]);
            } catch (NumberFormatException ex) {
                Message.sendMessage(sender,
                        Message.COMMAND_ERROR_INVALID_NUMBER.replace("{input}", args[0]));
            }
        }
        sender.sendMessage(ChatColor.GREEN + "=====================================================");
        for (int i = 0; i < MAX_PAGE_SIZE; i++) {

            int index = ((page - 1) * 6) + i;
            if (index > Guilds.getInstance().getCommandHandler().getCommands().size() - 1) {
                nextPage = false;
                if (i == 0) {
                    Message.sendMessage(sender, Message.COMMAND_HELP_INVALID_PAGE);
                }

                break;
            }

            CommandBase command =
                    Guilds.getInstance().getCommandHandler().getCommands().get(index);

            Message.sendMessage(sender, Message.COMMAND_HELP_MESSAGE
                    .replace("{command}", command.getName(), "{arguments}", command.getArguments(),
                            "{description}", command.getDescription()));

        }

        if (nextPage) {
            Message.sendMessage(sender, Message.COMMAND_HELP_NEXT_PAGE
                    .replace("{next-page}", String.valueOf((page + 1))));
        }
        sender.sendMessage(ChatColor.GREEN + "=====================================================");
    }
}
