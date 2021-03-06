package me.glaremasters.guilds.commands;

import java.util.List;
import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.commands.base.CommandBase;
import me.glaremasters.guilds.guild.Guild;
import me.glaremasters.guilds.guild.GuildRole;
import me.glaremasters.guilds.handlers.NameTagEditHandler;
import me.glaremasters.guilds.handlers.TablistHandler;
import me.glaremasters.guilds.handlers.TitleHandler;
import me.glaremasters.guilds.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandPrefix extends CommandBase {


    public CommandPrefix() {
        super("prefix", Guilds.getInstance().getConfig().getString("commands.description.prefix"),
                "guilds.command.prefix", false, null,
                "<new prefix>", 1, 1);
    }

    TitleHandler TitleHandler = new TitleHandler(Guilds.getInstance());
    TablistHandler TablistHandler = new TablistHandler(Guilds.getInstance());
    NameTagEditHandler NTEHandler = new NameTagEditHandler(Guilds.getInstance());

    @Override
    public void execute(Player player, String[] args) {
        Guild guild = Guild.getGuild(player.getUniqueId());
        if (guild == null) {
            Message.sendMessage(player, Message.COMMAND_ERROR_NO_GUILD);
            return;
        }

        GuildRole role = GuildRole.getRole(guild.getMember(player.getUniqueId()).getRole());
        if (!role.canChangePrefix()) {
            Message.sendMessage(player, Message.COMMAND_ERROR_ROLE_NO_PERMISSION);
            return;
        }

        FileConfiguration config = Guilds.getInstance().getConfig();
        if (!args[0].matches(Guilds.getInstance().getConfig().getString("prefix.regex"))) {
            Message.sendMessage(player, Message.COMMAND_PREFIX_REQUIREMENTS);
            return;
        }
        if (Guilds.getInstance().getConfig().getBoolean("enable-blacklist")) {
            List<String> blacklist = Guilds.getInstance().getConfig().getStringList("blacklist");

            for (String censor : blacklist) {
                if (args[0].toLowerCase().contains(censor)) {
                    Message.sendMessage(player, Message.COMMAND_ERROR_BLACKLIST);
                    return;
                }
            }
        }

        Message.sendMessage(player, Message.COMMAND_PREFIX_SUCCESSFUL);
        guild.updatePrefix(ChatColor.translateAlternateColorCodes('&', args[0]));

        TitleHandler.prefixTitles(player);
        TablistHandler.addTablist(player);
        NTEHandler.setTag(player);


    }
}
