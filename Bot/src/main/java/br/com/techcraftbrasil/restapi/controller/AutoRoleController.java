package br.com.techcraftbrasil.restapi.controller;

import br.com.techcraftbrasil.app.Main;
import br.com.techcraftbrasil.app.bot.Bot;
import br.com.techcraftbrasil.restapi.model.Result;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class AutoRoleController {

    @PostMapping("/add")
    public Result addRole(
            @RequestHeader("x-api-key") String key,
            @RequestBody Map<String, Object> args,
            HttpServletRequest request ){
        try {
            String guild_id = Main.getStorage().checkCredentials(key, request.getRemoteAddr());

            if(guild_id != null) {
                if(args.containsKey("username") && args.containsKey("role_id")) {

                    Guild guild = Bot.getJDA().getGuildById(guild_id);
                    Role role = null;
                    if(guild != null) role = guild.getRoleById((long) args.get("role_id"));

                    if(role != null) {

                        String member_id = Main.getStorage().getDiscordFromUsername((String) args.get("username"), guild_id);
                        if(member_id != null){

                            Member member = guild.getMemberById(member_id);
                            if(member != null) {

                                guild.addRoleToMember(member.getIdLong(), role).queue();
                                return new Result(202, "success", "Role adicionada ao usuário");

                            } else return new Result(304 , "error", "Usuário não esta mais neste grupo do discord");
                        } else return new Result(304 , "error", "Usuário não possui o username sincronizado com o discord");
                    } else return new Result(404, "error", "Essa role não existe");
                } else return new Result(400, "error", "Parâmetros necessários");
            } else return new Result(401, "error", "Acesso não autorizado");

        } catch (Exception e){
            return new Result(500 , "error", "Um erro ocorreu ao processar sua solicitação");
        }
    }
}
