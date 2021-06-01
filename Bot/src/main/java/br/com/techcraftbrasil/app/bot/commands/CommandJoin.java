package br.com.techcraftbrasil.app.bot.commands;

import br.com.techcraftbrasil.app.interfaces.CommandsGuild;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class CommandJoin implements CommandsGuild {
    @Override
    public void onCommand(String[] args, String command, TextChannel channel, Guild guild, User user, Member member, long message_id) {

        final Member self = channel.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (selfVoiceState.inVoiceChannel()) {
            channel.sendMessage("I'm already in a voice channel").queue();
            return;
        }

        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command to work").queue();
            return;
        }

        final AudioManager audioManager = channel.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        audioManager.openAudioConnection(memberChannel);
        channel.sendMessageFormat("Connecting to `\uD83D\uDD0A %s`", memberChannel.getName()).queue();

    }

    @Override
    public @NotNull String getName() {
        return "join";
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
