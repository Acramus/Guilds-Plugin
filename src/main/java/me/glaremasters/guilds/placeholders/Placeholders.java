package me.glaremasters.guilds.placeholders;

import me.glaremasters.guilds.guild.Guild;
import me.glaremasters.guilds.guild.GuildMember;
import me.glaremasters.guilds.guild.GuildRole;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Created by GlareMasters on 1/7/2018.
 */
public class Placeholders {

    public static String getGuild(Player player) {
        Guild guild = Guild.getGuild(player.getUniqueId());
        if (guild == null) {
            return "";
        }

        return guild.getName();
    }

    public static String getGuildMaster(Player player) {
        Guild guild = Guild.getGuild(player.getUniqueId());
        if (guild == null) {
            return "";
        }

        return Bukkit.getOfflinePlayer(guild.getGuildMaster().getUniqueId()).getName();
    }

    public static String getGuildMemberCount(Player player) {
        Guild guild = Guild.getGuild(player.getUniqueId());
        if (guild == null) {
            return "";
        }

        return String.valueOf(guild.getMembers().size());
    }

    public static String getGuildMembersOnline(Player player) {
        Guild guild = Guild.getGuild(player.getUniqueId());
        if (guild == null) {
            return "";
        }

        return String.valueOf(
                guild.getMembers().stream()
                        .map(member -> Bukkit.getOfflinePlayer(member.getUniqueId()))
                        .filter(OfflinePlayer::isOnline).count());
    }

    public static String getGuildStatus(Player player) {
        Guild guild = Guild.getGuild(player.getUniqueId());
        if (guild == null) {
            return "";
        }

        return guild.getStatus();
    }

    public static String getGuildPrefix(Player player) {
        Guild guild = Guild.getGuild(player.getUniqueId());
        if (guild == null) {
            return "";
        }

        return guild.getPrefix();
    }

    public static String getGuildRole(Player player) {
        Guild guild = Guild.getGuild(player.getUniqueId());

        if (guild == null) {
            return "";
        }
        GuildMember roleCheck = guild.getMember(player.getUniqueId());
        return GuildRole.getRole(roleCheck.getRole()).getName();
    }

    public static int getGuildTier(Player player) {
        Guild guild = Guild.getGuild(player.getUniqueId());
        if (guild == null) {
            return 0;
        }

        return guild.getTier();
    }

    public static double getBankBalance(Player player) {
        Guild guild = Guild.getGuild(player.getUniqueId());
        if (guild == null) {
            return 0;
        }

        return guild.getBankBalance();
    }

    public static double getUpgradeCost(Player player) {
        Guild guild = Guild.getGuild(player.getUniqueId());
        if (guild == null) {
            return 0;
        }

        return guild.getTierCost();
    }

    public static String getTierName(Player player) {
        Guild guild = Guild.getGuild(player.getUniqueId());
        if (guild == null) {
            return "";
        }

        return guild.getTierName();
    }

}
