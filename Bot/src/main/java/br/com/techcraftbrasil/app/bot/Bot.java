package br.com.techcraftbrasil.app.bot;

import br.com.techcraftbrasil.app.interfaces.CommandsGuild;
import br.com.techcraftbrasil.app.interfaces.CommandsPrivate;
import br.com.techcraftbrasil.app.interfaces.Events;
import br.com.techcraftbrasil.app.interfaces.ReactionsPrivate;
import br.com.techcraftbrasil.restapi.RestAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Bot extends ListenerAdapter {

    private static JDA jda;

    private static List<CommandsGuild> commands = new ArrayList<>();
    private static List<CommandsPrivate> commandsPrivate = new ArrayList<>();
    private static List<Events> events = new ArrayList<>();
    private static String prefix = "=";

    private static  File file = new File("latest.log");
    private static FileOutputStream out;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    public static void print(String message){
        String m = "["+simpleDateFormat.format(new Date())+"]" + message;
        System.out.println(m);
        if(out != null)
            try {
                out.write(("\n" + m).getBytes());
                out.flush();
            } catch (Exception e){
                e.printStackTrace();
            }
    }

    public static boolean start(String token) throws LoginException, InterruptedException {
        if(jda == null) {
            new Bot(token);
            return true;
        } else
            return false;
    }

    private Bot(String token) throws InterruptedException, LoginException {

        print(ConsoleColors.GREEN + "[START] Iniciando o bot do discord");
        if(!file.exists())
            try {
                file.createNewFile();
                out = new FileOutputStream(file);
            } catch (Exception e){
                e.printStackTrace();
            }

        jda = JDABuilder.createDefault(token)
                .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
                .setMemberCachePolicy(MemberCachePolicy.ALL) // ignored if chunking enabled
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(this)
                .build();
        jda.awaitReady();
        jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.streaming("=help =ip =servers", "https://www.twitch.tv/EduardoMGP"), true);
        print(ConsoleColors.GREEN + "[API REST] Iniciando API");

        RestAPI.start();

        print(ConsoleColors.GREEN + "[START] Bot iniciado");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while (true){
                    String entrada = scanner.nextLine();
                    if(entrada.equalsIgnoreCase("stop")) {
                        Bot.print(ConsoleColors.RED + "[START] Bot desligado");

                        System.exit(1);
                    }
                }
            }
        }).start();
    }

    public static JDA getJDA(){
        return jda;
    }
    public static String getPrefix() {return prefix;};

    public static CommandsGuild[] getCommands(){
        CommandsGuild[] cmds = new CommandsGuild[commands.size()];
        int i = 0;
        for(CommandsGuild c : commands){
            cmds[i] = c;
            i++;
        }
        return cmds;
    }

    public static CommandsGuild getCommand(String... name){
        for(CommandsGuild cmd : commands)
            for(String name_ : name)
                if(cmd.getName().equalsIgnoreCase(name_))
                    return cmd;
                else if(cmd.getAlias() != null)
                    for (String alias_ : cmd.getAlias())
                        if (alias_.equalsIgnoreCase(name_))
                            return cmd;
        return null;

    }


    public static CommandsPrivate[] getCommandsPrivate(){
        CommandsPrivate[] cmds = new CommandsPrivate[commands.size()];
        int i = 0;
        for(CommandsPrivate c : commandsPrivate){
            cmds[i] = c;
            i++;
        }
        return cmds;
    }

    private static CommandsPrivate getCommandPrivate(String... name) {
        for(CommandsPrivate cmd : commandsPrivate)
            for(String name_ : name)
                if(cmd.getName().equalsIgnoreCase(name_))
                    return cmd;
                else if(cmd.getAlias() != null)
                    for(String alias_ : cmd.getAlias())
                        if(alias_.equalsIgnoreCase(name_))
                            return cmd;
        return null;
    }

    public static boolean registerCommand(CommandsGuild commandClass){

        String[] alias = commandClass.getAlias();
        int tamanho = 0;
        if(alias != null)
            tamanho = alias.length;

        String[] cmd = new String[tamanho + 1];
        for(int i = 0; i < tamanho; i++)
            cmd[i] = commandClass.getAlias()[0];
        cmd[tamanho] = commandClass.getName();

        if(getCommand(cmd) != null) {
            print(ConsoleColors.RED + "[CommandGuild] Comando ou aliases (" + commandClass.getName().toUpperCase() + ") ja se encontra registrado -> Aliases: " + Arrays.asList(commandClass.getAlias()));
            return false;
        }

        commands.add(commandClass);
        if(tamanho != 0)
            print(ConsoleColors.GREEN + "[CommandGuild] Comando " + commandClass.getName() + " registrado com sucesso -> Aliases: " + Arrays.asList(commandClass.getAlias()));
        else
            print(ConsoleColors.GREEN + "[CommandGuild] Comando " + commandClass.getName() + " registrado com sucesso -> Aliases: ");

        return true;

    }

    public static boolean registerPrivateCommand(CommandsPrivate commandClass){

        String[] alias = commandClass.getAlias();
        int tamanho = 0;
        if(alias != null)
            tamanho = alias.length;

        String[] cmd = new String[tamanho + 1];
        for(int i = 0; i < tamanho; i++)
            cmd[i] = commandClass.getAlias()[0];
        cmd[tamanho] = commandClass.getName();

        if(getCommandPrivate(cmd) != null) {
            print(ConsoleColors.RED + "[CommandPrivate] Comando ou aliases (" + commandClass.getName().toUpperCase() + ") ja se encontra registrado -> Aliases: " + Arrays.asList(commandClass.getAlias()));
            return false;
        }

        commandsPrivate.add(commandClass);
        if(tamanho != 0)
            print(ConsoleColors.GREEN + "[CommandPrivate] Comando " + commandClass.getName() + " registrado com sucesso -> Aliases: " + Arrays.asList(commandClass.getAlias()));
        else
            print(ConsoleColors.GREEN + "[CommandPrivate] Comando " + commandClass.getName() + " registrado com sucesso -> Aliases: ");

        return true;

    }

    public static boolean registerEvent(Events event){
        if(events.contains(event)){
            print(ConsoleColors.RED + "[Events] Este evento já foi registrado");
            return false;
        }
        events.add(event);
        print(ConsoleColors.GREEN + "[Events] Evento registrado Class: " + event.getClass().getName() + " Type: " + event.getClass().getInterfaces()[0].getTypeName());
        return true;
    }


    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String message = event.getMessage().getContentRaw();
        String[] command = message.split(" ");

        if(command[0].startsWith(prefix)) {

            String commandName = command[0].substring(1);
            String[] args = new String[command.length - 1];

            for (int i = 0; i < args.length; i++)
                args[i] = command[i + 1];

            CommandsGuild cmd = getCommand(commandName);
            if (cmd == null) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setAuthor("TechCraftBrasil");
                embedBuilder.setThumbnail("https://i.imgur.com/4i4PZhJ.png");
                embedBuilder.setColor(new Color(255, 50, 10));
                embedBuilder.setTitle("Vish! Não encontrei este comando \uD83E\uDD14");
                embedBuilder.setFooter("Um server feito com amor ❤️", "https://i.imgur.com/4i4PZhJ.png");
                embedBuilder.setDescription(event.getMember().getAsMention()+" Este comando que você digitou não foi encontrado! \uD83D\uDE1F Mas não se preocupe, digite =ajuda ou =help para visualizar a lista de comandos disponíveis \uD83D\uDE04");
                event.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }
            if(!event.isWebhookMessage())
                if(!event.getAuthor().isBot()) {

                    if(cmd.getPermissionChannel() != null)
                        if(!event.getMember().hasPermission(event.getChannel(), cmd.getPermissionChannel())) {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setAuthor("TechCraftBrasil");
                            embedBuilder.setThumbnail("https://i.imgur.com/4i4PZhJ.png");
                            embedBuilder.setColor(new Color(255, 10, 10));
                            embedBuilder.setTitle("Permissão no canal insuficiente \uD83E\uDD14");
                            embedBuilder.setFooter("Um server feito com amor ❤️", "https://i.imgur.com/4i4PZhJ.png");
                            embedBuilder.setDescription("Ops! "+event.getMember().getAsMention()+" parece que você não tem a permissão necessária para usar este comando \uD83D\uDE1F");
                            embedBuilder.addField("Permissão necessária", cmd.getPermissionChannel() + "", true);
                            event.getChannel().sendMessage(embedBuilder.build()).queue();
                            return;
                        }

                    if(cmd.getPermissionGlobal() != null)
                        if(!event.getMember().hasPermission(cmd.getPermissionGlobal())) {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setAuthor("TechCraftBrasil");
                            embedBuilder.setThumbnail("https://i.imgur.com/4i4PZhJ.png");
                            embedBuilder.setColor(new Color(255, 10, 10));
                            embedBuilder.setTitle("Permissão no grupo insuficiente \uD83E\uDD14");
                            embedBuilder.setFooter("Um server feito com amor ❤️", "https://i.imgur.com/4i4PZhJ.png");
                            embedBuilder.setDescription("Ops! "+event.getMember().getAsMention()+" parece que você não tem a permissão necessária para usar este comando \uD83D\uDE1F");
                            embedBuilder.addField("Permissão necessária", cmd.getPermissionGlobal() + "", true);
                            event.getChannel().sendMessage(embedBuilder.build()).queue();
                            return;
                        }

                    cmd.onCommand(
                            args,
                            commandName,
                            event.getChannel(),
                            event.getGuild(),
                            event.getAuthor(),
                            event.getMember(),
                            event.getMessageIdLong()
                    );
                    print(
                            ConsoleColors.GREEN +" [Command LOG] (" + event.getAuthor().getId() + " " + event.getAuthor().getName() +") usou comando " + event.getMessage().getContentRaw());
                }
        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

        if(!event.getAuthor().isBot())
            print(ConsoleColors.CYAN + "[Mensagem privada] "+event.getAuthor().getName()+" ("+event.getAuthor().getId()+") " + event.getMessage().getContentDisplay());
        String message = event.getMessage().getContentRaw();
        String[] command = message.split(" ");

        if(command[0].startsWith(prefix)) {

            String commandName = command[0].substring(1);
            String[] args = new String[command.length - 1];

            for (int i = 0; i < args.length; i++)
                args[i] = command[i];

            CommandsPrivate cmd = getCommandPrivate(commandName);
            if (cmd == null) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setAuthor("TechCraftBrasil");
                embedBuilder.setThumbnail("https://i.imgur.com/4i4PZhJ.png");
                embedBuilder.setColor(new Color(255, 50, 10));
                embedBuilder.setTitle("Vish! Não encontrei este comando \uD83E\uDD14");
                embedBuilder.setFooter("Um server feito com amor ❤️", "https://i.imgur.com/4i4PZhJ.png");
                embedBuilder.setDescription(event.getAuthor().getAsMention()+" Este comando que você digitou não foi encontrado! \uD83D\uDE1F Mas não se preocupe, digite =ajuda ou =help para visualizar a lista de comandos disponíveis \uD83D\uDE04");
                event.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }
            if(!event.getMessage().isWebhookMessage())
                if(!event.getAuthor().isBot()) {
                    cmd.onCommand(
                            args,
                            commandName,
                            event.getChannel(),
                            event.getAuthor(),
                            event.getMessageIdLong()
                    );
                    print(
                            ConsoleColors.GREEN + "[Command Private LOG] (" + event.getAuthor().getId() + " " + event.getAuthor().getName() +") usou comando " + event.getMessage().getContentRaw());
                }
        }
    }


    @Override
    public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event){
        MessageReaction mr = event.getReaction();
        Message message = mr.getPrivateChannel().retrieveMessageById(mr.getMessageId()).complete();
        for(Events e : events)
            if(e instanceof ReactionsPrivate) {
                ((ReactionsPrivate) e).onPrivateReaction(
                        event.getChannel(),
                        event.getReaction(),
                        message,
                        event.getReactionEmote(),
                        event.getUser()
                );
                return;
            }
    }
}
