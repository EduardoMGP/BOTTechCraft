package br.com.techcraftbrasil.app.bot.commands;

import br.com.techcraftbrasil.app.interfaces.CommandsGuild;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

public class CommandPlay implements CommandsGuild {
    @Override
    public void onCommand(String[] args, String command, TextChannel channel, Guild guild, User user, Member member, long message_id) {


//        final Member self = guild.getSelfMember();
//        final GuildVoiceState selfVoiceState = self.getVoiceState();
//
//        if (!selfVoiceState.inVoiceChannel()) {
//            channel.sendMessage("I need to be in a voice channel for this to work").queue();
//            return;
//        }
//
//        final GuildVoiceState memberVoiceState = member.getVoiceState();
//
//        if (!memberVoiceState.inVoiceChannel()) {
//            channel.sendMessage("You need to be in a voice channel for this command to work").queue();
//            return;
//        }
//
//        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
//            channel.sendMessage("You need to be in the same voice channel as me for this to work").queue();
//            return;
//        }
//
//        PlayerManager.getInstance()
//                .loadAndPlay(channel, "https://stream1.thepuremix.net/nexusdance.mp3");

    }

    @Override
    public @NotNull String getName() {
        return "play";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Permission getPermissionChannel() {
        return null;
    }

    @Override
    public Permission getPermissionGlobal() {
        return Permission.ADMINISTRATOR;
    }
}
