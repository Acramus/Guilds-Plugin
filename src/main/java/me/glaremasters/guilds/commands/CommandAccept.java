package me.glaremasters.guilds.commands;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.api.events.GuildJoinEvent;
import me.glaremasters.guilds.commands.base.CommandBase;
import me.glaremasters.guilds.guild.Guild;
import me.glaremasters.guilds.guild.GuildRole;
import me.glaremasters.guilds.handlers.NameTagEditHandler;
import me.glaremasters.guilds.handlers.TablistHandler;
import me.glaremasters.guilds.handlers.TitleHandler;
import me.glaremasters.guilds.handlers.WorldGuardHandler;
import me.glaremasters.guilds.message.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandAccept extends CommandBase {

    public CommandAccept() {
        super("accept", Guilds.getInstance().getConfig().getString("commands.description.accept"),
                "guilds.command.accept", false,
                new String[]{"join"}, "<guild id>", 0, 1);
    }

    WorldGuardHandler WorldGuard = new WorldGuardHandler();
    TitleHandler TitleHandler = new TitleHandler(Guilds.getInstance());
    TablistHandler TablistHandler = new TablistHandler(Guilds.getInstance());
    NameTagEditHandler NTEHandler = new NameTagEditHandler(Guilds.getInstance());


    public void execute(Player player, String[] args) {

        final FileConfiguration config = Guilds.getInstance().getConfig();
        if (Guild.getGuild(player.getUniqueId()) != null) {
            Message.sendMessage(player, Message.COMMAND_ERROR_ALREADY_IN_GUILD);
            return;
        }
        Guild guild = (Guild) Guilds.getInstance().getGuildHandler().getGuilds().values()
                .toArray()[0];
        try {
            if (args.length == 0) {
                int invites = 0;
                int indexes = 0;
                for (int i = 0;
                     i < Guilds.getInstance().getGuildHandler().getGuilds().values().size(); i++) {
                    Guild guildtmp = (Guild) Guilds.getInstance().getGuildHandler().getGuilds()
                            .values().toArray()[i];
                    if (guildtmp.getInvitedMembers().contains(player.getUniqueId())) {
                        invites++;
                        indexes = i;
                    }
                }
                if (invites == 1) {
                    guild = (Guild) Guilds.getInstance().getGuildHandler().getGuilds().values()
                            .toArray()[indexes];
                } else {
                    Message.sendMessage(player, Message.COMMAND_ACCEPT_NOT_INVITED);
                    return;
                }
            } else {
                guild = Guild.getGuild(args[0]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (guild == null) {
            Message.sendMessage(player,
                    Message.COMMAND_ERROR_GUILD_NOT_FOUND.replace("{input}", args[0]));
            return;
        }

        if (guild.getStatus().equalsIgnoreCase("private")) {
            if (!guild.getInvitedMembers().contains(player.getUniqueId())) {
                Message.sendMessage(player, Message.COMMAND_ACCEPT_NOT_INVITED);
                return;
            }
        }

        int maxMembers = guild.getMaxMembers();
        if (guild.getMembers().size() >= maxMembers) {
            Message.sendMessage(player, Message.COMMAND_ACCEPT_GUILD_FULL);
            return;
        }

        GuildJoinEvent event = new GuildJoinEvent(player, guild);
        if (event.isCancelled()) {
            return;
        }

        guild.sendMessage(
                Message.COMMAND_ACCEPT_PLAYER_JOINED.replace("{player}", player.getName()));

        guild.addMember(player.getUniqueId(), GuildRole.getLowestRole());
        guild.removeInvitedPlayer(player.getUniqueId());
        if (Guilds.getInstance().getConfig().getBoolean("hooks.worldguard")) {

            RegionContainer container = WorldGuard.getWorldGuard().getRegionContainer();
            RegionManager regions = container.get(player.getWorld());

            if (regions.getRegion(guild.getName()) != null) {
                regions.getRegion(guild.getName()).getMembers().addPlayer(player.getName());
            }
        }

        for (int i = 1; i <= guild.getTier(); i++) {
            for (String perms : Guilds.getInstance().getConfig().getStringList("tier" + i + ".permissions")) {
                Guilds.getPermissions().playerAdd(null, player, perms);
            }
        }

        TitleHandler.joinTitles(player);
        TablistHandler.addTablist(player);
        NTEHandler.setTag(player);

        Message.sendMessage(player,
                Message.COMMAND_ACCEPT_SUCCESSFUL.replace("{guild}", guild.getName()));
    }
}
