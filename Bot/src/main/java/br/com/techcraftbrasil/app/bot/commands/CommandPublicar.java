package br.com.techcraftbrasil.app.bot.commands;

import br.com.techcraftbrasil.app.bot.Bot;
import br.com.techcraftbrasil.app.bot.SendMessage;
import br.com.techcraftbrasil.app.interfaces.CommandsGuild;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

public class CommandPublicar implements CommandsGuild {

    @Override
    public void onCommand(String[] args, String command, TextChannel channel, Guild guild, User user, Member member, long message_id) {
        if(args.length < 2) {
            channel.sendMessage("Uso correto: " + Bot.getPrefix() + "publicar id_mensagem titulo").queue();
            return;
        }

        Message message = null;
        try {
            long id = Long.parseLong(args[0]);
            message = channel.retrieveMessageById(id).complete();
        } catch (Exception e){
            e.printStackTrace();
            channel.sendMessage("Mensagem não encontrada").queue();
            return;
        }
        if(message == null) {
            channel.sendMessage("Mensagem não encontrada").queue();
            return;
        }

        String title = "";
        for (int i = 1; i < args.length; i++){
            if(title.equals(""))
                title = args[i];
            else
                title += " " + args[i];
        }
        if(SendMessage.sendMessage(channel, member, guild.getMembers(), message.getContentDisplay(), title))
            channel.sendMessage("Aguarde, as mensagens estão sendo enviadas").queue();
        else
            channel.sendMessage("Já possui uma mensagem sendo enviada no momento, aguarde").queue();

    }

    @Override
    public @NotNull String getName() {
        return "publicar";
    }

    @Override
    public String[] getAlias() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Enviar mensagens privadas a todos os usuários do grupo";
    }

    @Override
    public Permission getPermissionChannel() {
        return null;
    }
    public Permission getPermissionGlobal() {
        return  Permission.ADMINISTRATOR;
    }
}
