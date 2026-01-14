package net.torosamy.torosamyCore.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.torosamy.torosamyCore.api.TorosamyCoreAPI;
import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyCore.utils.NbtUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunCommands {
    private static final List<String> COMPARE_NUMBER = List.of("[below]", "[above]", "[equal]", "[not_equal]", "[below_equal]", "[above_equal]");

    private static final List<String> JUDGE_String = List.of("[equals]", "[contains]", "[start_with]");

    private final Map<String, String> holders = new HashMap<>();
    
    private final Map<String, List<String>> denyCommands = new HashMap<>();
    
    public RunCommands(Map<String, List<String>> denyCommands) {
        this.denyCommands.putAll(denyCommands);
    }
    
    public RunCommands putHolder(Map<String, String> holders) {
        this.holders.putAll(holders);
        return this;
    }

    public boolean runCommands(Player player, List<String> commands) {
        for (String it : commands) {
            String text = PlaceholderAPI.setPlaceholders(player, it);

            for (String key : holders.keySet()) {
                String replace = holders.get(key);
                text = text.replaceAll(key, replace);
            }

            if (text.startsWith("[holder] ")) {
                String[] split = text.replace("[holder] ", "").split(" ");

                if (split.length == 2) {
                    holders.put(split[0], split[1]);
                }

                continue;
            }

            if (text.startsWith("[has_item]")) {
                if (hasItem(player, text).isEmpty()) {
                    return false;
                }
                continue;
            }


            if (text.startsWith("[take_item]")) {
                if (!takeItem(player, text)) {
                    return false;
                }
                continue;
            }

            if (text.startsWith("[sneak]")) {
                if (!isSneaking(player, text)) {
                    return false;
                }
                continue;
            }
            
            if (text.startsWith("[permission]")) {
                if (!hasPermission(player, text)) {
                    return false;
                }
                continue;
            }

            if (isString(text)) {
                if (!judgeString(JudgeStringType.getByLabel(text), player, text)) {
                    return false;
                }
                continue;
            }


            if (isNumber(text)) {
                if (!compareNumber(CompareNumberType.getByLabel(text), player, text)) {
                    return false;
                }
                continue;
            }

            if(text.startsWith("[send_title] ")) {
                String[] line = text.replace("[send_title] ", "").split("\\|");
                if (line.length == 0) {
                    continue;
                }
                
                String bigLine = MessageUtil.format(player, line[0]);
                
                String smallLine = line.length > 1 ? MessageUtil.format(player, line[1]) : "§r";

                player.sendTitle(bigLine, smallLine);
                continue;
            }

            if(text.startsWith("[all_send_title] ")) {
                String[] line = text.replace("[send_title] ", "").split("\\|");
                if (line.length == 0) {
                    continue;
                }

                String bigLine = MessageUtil.format(player, line[0]);

                String smallLine = line.length > 1 ? MessageUtil.format(player, line[1]) : "§r";
                
                Bukkit.getOnlinePlayers().forEach(onlinePlayer->onlinePlayer.sendTitle(bigLine, smallLine));
                
                continue;
            }

            if(text.startsWith("[targets_send_title] ")) {
                String[] split = text.replace("[targets_send_title] ", "").split("#");

                if (split.length < 2) {
                    continue;
                }

                String[] message = split[split.length - 1].split("\\|");

                String bigLine = MessageUtil.format(player, message[0]);

                String smallLine = message.length > 1 ? MessageUtil.format(player, message[1]) : "§r";
                
                for (int i = 0; i < split.length - 1; i++) {
                    Player target = Bukkit.getPlayer(PlaceholderAPI.setPlaceholders(player, split[i]));

                    if (target == null || !target.isOnline()) {
                        continue;
                    }

                    target.sendTitle(bigLine, smallLine);
                }
                continue;
            }
            

            if(text.startsWith("[send_action_bar] ")) {
                String message = text.replace("[send_action_bar] ", "");
                player.sendActionBar(MessageUtil.component(message));
                continue;
            }

            if(text.startsWith("[all_send_action_bar] ")) {
                String message = text.replace("[all_send_action_bar] ", "");
                Component component = MessageUtil.component(message);
                Bukkit.getOnlinePlayers().forEach(
                        onlinePlayer->onlinePlayer.sendActionBar(component)
                );
                continue;
            }

            if(text.startsWith("[targets_send_action_bar] ")) {
                String[] split = text.replace("[targets_send_action_bar] ", "").split("#");

                if (split.length < 2) {
                    continue;
                }

                Component component = MessageUtil.component(split[split.length - 1]);

                for (int i = 0; i < split.length - 1; i++) {
                    Player target = Bukkit.getPlayer(PlaceholderAPI.setPlaceholders(player, split[i]));

                    if (target == null || !target.isOnline()) {
                        continue;
                    }

                    target.sendActionBar(component);
                }
                continue;
            }

            if(text.startsWith("[close]")) {
                player.closeInventory();
                continue;
            }

            if(text.startsWith("[break]")) {
                break;
            }

            if(text.startsWith("[console] ")) {
                String command = text.replace("[console] ", "");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MessageUtil.format(command));
                continue;
            }

            if(text.startsWith("[log] ")) {
                String command = text.replace("[log] ", "");
                Bukkit.getConsoleSender().sendMessage(MessageUtil.component(player, command));
                continue;
            }
            
            if (text.startsWith("[sound] ")) {
                String soundName = text.replace("[sound] ", "");
                player.playSound(player.getLocation(), soundName, SoundCategory.VOICE, 1, 1);
                continue;
            }



            if(text.startsWith("[player] ")) {
                String command = text.replace("[player] ", "");
                Bukkit.dispatchCommand(player, MessageUtil.format(command));
                continue;
            }

            if(text.startsWith("[message_targets] ")) {
                String[] split = text.replace("[message_targets] ", "").split("#");

                if (split.length < 2) {
                    continue;
                }

                Component component = MessageUtil.component(split[split.length - 1]);

                for (int i = 0; i < split.length - 1; i++) {
                    Player target = Bukkit.getPlayer(PlaceholderAPI.setPlaceholders(player, split[i]));

                    if (target == null || !target.isOnline()) {
                        continue;
                    }

                    target.sendMessage(component);
                }
                continue;
            }
            
            if(text.startsWith("[message] ")) {
                String message = text.replace("[message] ", "");
                player.sendMessage(MessageUtil.component(message));
                continue;
            }

            if(text.startsWith("[allMessage] ")) {
                String message = text.replace("[allMessage] ", "");

                Bukkit.getOnlinePlayers().forEach(onlinePlayer-> {
                    onlinePlayer.sendMessage(MessageUtil.component(message));
                });
                continue;
            }

            if(text.startsWith("[op] ")) {
                String command = text.replace("[op] ", "");
                if (!player.isOp()) {
                    player.setOp(true);
                    player.performCommand(MessageUtil.format(command));
                    player.setOp(false);
                    continue;
                }

                Bukkit.dispatchCommand(player, MessageUtil.format(command));
            }
        }
        return true;
    }
    
    //[has_item]#d# keyword condition amount
    //[has_item] keyword condition amount
    private int getTakeItemAmount(String text) {
        if (text == null || text.isEmpty()) {
            return -1;
        }

        if (text.startsWith("[take_item] ")) {
            String[] split = text.replace("[take_item] ", "").split(" ");
            if (split.length == 2) {
                return TorosamyCoreAPI.parseInt(split[1]);
            }

            if (split.length == 3) {
                return TorosamyCoreAPI.parseInt(split[2]);
            }

            return -1;
        }

        if (text.startsWith("[take_item]#")) {
            String[] split = text.replace("[take_item]#", "").split("# ");
            if (split.length != 2) {
                return -1;
            }

            String[] params = split[1].split(" ");

            if (params.length == 2) {
                return TorosamyCoreAPI.parseInt(params[1]);
            }

            if (params.length == 3) {
                return TorosamyCoreAPI.parseInt(params[2]);
            }

            return -1;
        }
        return -1;
    }

    private boolean takeItem(Player player, String text) {
        List<ItemStack> list = hasItem(player, text.replaceAll("take_item", "has_item"));

        if (list.isEmpty()) {
            return false;
        }

        int amount = getTakeItemAmount(text);

        if (amount == -1) {
            return false;
        }

        for (ItemStack itemStack : list) {
            if (amount == 0) {
                return true;
            }

            if (itemStack.getAmount() <= amount) {
                amount -= itemStack.getAmount();
                itemStack.setAmount(0);
                continue;
            }

            itemStack.setAmount(itemStack.getAmount() - amount);
            amount = 0;
            break;
        }

        return amount == 0;
    }

    private List<ItemStack> hasItem(Player player, String text) {
        if (player == null || !player.isOnline()) {
            return List.of();
        }

        if (text == null || text.isEmpty()) {
            return List.of();
        }

        if (text.startsWith("[has_item] ")) {
            String[] split = text.replace("[has_item] ", "").split(" ");
            if (split.length == 2) {
                return hasItem(player, TorosamyCoreAPI.parseInt(split[1]), split[0]);
            }

            if (split.length == 3) {
                return hasItem(player, TorosamyCoreAPI.parseInt(split[2]), split[0], split[1]);
            }

            return List.of();
        }

        if (text.startsWith("[has_item]#")) {
            String[] split = text.replace("[has_item]#", "").split("# ");
            if (split.length != 2) {
                return List.of();
            }

            String[] params = split[1].split(" ");
            List<String> denyCommand = denyCommands.get(split[0]);

            if (params.length == 2) {
                List<ItemStack> list = hasItem(player, TorosamyCoreAPI.parseInt(params[1]), params[0]);

                if (list.isEmpty() && denyCommand != null) {
                    runCommands(player, denyCommand);
                }

                return list;
            }

            if (params.length != 3) {
                return List.of();
            }

            List<ItemStack> list = hasItem(player, TorosamyCoreAPI.parseInt(params[2]), params[0], params[1]);

            if (list.isEmpty() && denyCommand != null) {
                runCommands(player, denyCommand);
            }

            return list;
        }
        return List.of();

    }


    private static List<ItemStack> hasItem(Player player, int amount, String typeName) {
        if (amount <= 0) {
            return List.of();
        }

        List<ItemStack> itemStacks = new ArrayList<>();
        int sum = 0;

        Material material = Material.getMaterial(typeName.toUpperCase());
        if (material == null) {
            return List.of();
        }


        for (ItemStack content : player.getInventory().getContents()) {
            if (content == null || content.getType() != material || TorosamyCoreAPI.isCustomItem(content)) {
                continue;
            }

            itemStacks.add(content);
            sum += content.getAmount();

            if (sum < amount) {
                continue;
            }

            return itemStacks;
        }

        return List.of();
    }


    private static List<ItemStack> hasItem(Player player, int amount, String keyWord, String condition) {
        if (amount <= 0) {
            return List.of();
        }

        int sum = 0;
        List<ItemStack> itemStacks = new ArrayList<>();

        for (ItemStack content : player.getInventory().getContents()) {
            if (!isSuitCondition(content, keyWord, condition)) {
                continue;
            }

            itemStacks.add(content);
            sum += content.getAmount();

            if (sum < amount) {
                continue;
            }

            return itemStacks;
        }

        return List.of();
    }
    private static boolean isSuitCondition(ItemStack content, String keyWord, String condition) {
        if (keyWord == null || keyWord.isEmpty()) {
            return false;
        }

        if (condition == null || condition.isEmpty()) {
            return false;
        }

        if (content == null || content.getType() == Material.AIR) {
            return false;
        }
        
        String value = NbtUtil.getString(content, keyWord);
        if (value == null) {
            return false;
        }

        return value.equals(condition);
    }

    private static boolean isString(String text) {
        for (String it : JUDGE_String) {
            if (text.startsWith(it)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumber(String text) {
        for (String it : COMPARE_NUMBER) {
            if (text.startsWith(it)) {
                return true;
            }
        }
        return false;
    }

    private static boolean judgeString(JudgeStringType type, String value, String condition) {
        return switch (type) {
            case CONTAINS -> value.contains(condition);
            case START_WITH -> value.startsWith(condition);
            case EQUALS -> value.equals(condition);
            case NOT_CONTAINS -> !value.contains(condition);
            case NOT_START_WITH -> !value.startsWith(condition);
            case NOT_EQUALS -> !value.equals(condition);
        };
    }

    private static boolean compareNumber(CompareNumberType type, int value, int condition) {
        if (value == -1 || condition == -1) {
            return false;
        }

        return switch (type) {
            case ABOVE -> value > condition;
            case BELOW -> value < condition;
            case EQUAL -> value == condition;
            case NOT_EQUAL -> value != condition;
            case ABOVE_EQUAL -> value >= condition;
            case BELOW_EQUAL -> value <= condition;
        };
    }

    public boolean judgeString(JudgeStringType type, Player player, String text) {
        if (type == null || text == null || player == null) {
            return false;
        }

        if (text.startsWith(type.label + " ")) {
            String[] split = text.replace(type.label + " ", "").split(" ");

            if (split.length != 2) {
                return false;
            }

            String value = split[0];
            String condition = split[1];

            return judgeString(type, value, condition);
        }

        if (text.startsWith(type.label + "#")) {
            String[] split = text.replace(type.label + "#", "").split("# ");

            if (split.length != 2) {
                return false;
            }

            String name = split[0];

            String[] params = split[1].split(" ");
            if (params.length != 2) {
                return false;
            }

            String value = params[0];
            String condition = params[1];

            if (judgeString(type, value, condition)) {
                return true;
            }

            List<String> denyCommand = denyCommands.get(name);
            if (denyCommand != null) {
                runCommands(player, denyCommand);
            }
            return false;

        }
        return true;
    }

    public boolean compareNumber(CompareNumberType type, Player player, String text) {
        if (type == null || text == null || player == null) {
            return false;
        }

        if (text.startsWith(type.label + " ")) {
            String[] split = text.replace(type.label + " ", "").split(" ");

            if (split.length != 2) {
                return false;
            }

            int value = TorosamyCoreAPI.parseInt(split[0]);

            int condition = TorosamyCoreAPI.parseInt(split[1]);

            return compareNumber(type, value, condition);
        }

        if (text.startsWith(type.label + "#")) {
            String[] split = text.replace(type.label + "#", "").split("# ");

            if (split.length != 2) {
                return false;
            }

            String name = split[0];

            String[] params = split[1].split(" ");
            if (params.length != 2) {
                return false;
            }

            int value = TorosamyCoreAPI.parseInt(params[0]);
            int condition = TorosamyCoreAPI.parseInt(params[1]);

            if (compareNumber(type, value, condition)) {
                return true;
            }

            List<String> denyCommand = denyCommands.get(name);
            if (denyCommand != null) {
                runCommands(player, denyCommand);
            }
            return false;

        }
        return true;
    }
    
    public boolean isSneaking(Player player, String text) {
        if (text.equals("[sneak]")) {
            return player.isSneaking();
        }
        
        
        if (text.startsWith("[sneak]#")) {
            if (player.isSneaking()) {
                return true;
            }

            String name = text.replace("[sneak]#", "").replaceAll("#", "");

            List<String> denyCommand = denyCommands.get(name);

            if (denyCommand != null) {
                runCommands(player, denyCommand);
            }
            return false;
        }
        
        

        
        return false;
    }

    public boolean hasPermission(Player player, String text) {
        if (text.startsWith("[permission] ")) {
            String permission = text.replace("[permission] ", "");
            return player.hasPermission(permission);
        }

        if (text.startsWith("[permission]#")) {
            String[] split = text.replace("[permission]#", "").split("# ");

            if (split.length != 2) {
                return false;
            }

            if (player.hasPermission(split[1])) {
                return true;
            }

            List<String> denyCommand = denyCommands.get(split[0]);

            if (denyCommand != null) {
                runCommands(player, denyCommand);
            }
            return false;
        }

        return false;
    }


    private enum CompareNumberType {
        BELOW("[below]"),
        ABOVE("[above]"),
        EQUAL("[equal]"),
        BELOW_EQUAL("[below_equal]"),
        ABOVE_EQUAL("[above_equal]"),
        NOT_EQUAL("[not_equal]");

        public final String label;

        public static CompareNumberType getByLabel(String label) {
            if (label.startsWith(BELOW_EQUAL.label)) {
                return BELOW_EQUAL;
            }
            if (label.startsWith(ABOVE_EQUAL.label)) {
                return ABOVE_EQUAL;
            }
            if (label.startsWith(NOT_EQUAL.label)) {
                return NOT_EQUAL;
            }

            if (label.startsWith(BELOW.label)) {
                return BELOW;
            }

            if (label.startsWith(ABOVE.label)) {
                return ABOVE;
            }

            if (label.startsWith(EQUAL.label)) {
                return EQUAL;
            }
            return null;
        }

        CompareNumberType(String label) {
            this.label = label;
        }
    }

    private enum JudgeStringType {
        CONTAINS("[contains]"),
        EQUALS("[equals]"),
        START_WITH("[start_with]"),
        NOT_CONTAINS("[not_contains]"),
        NOT_EQUALS("[not_equals]"),
        NOT_START_WITH("[not_start_with]");

        public final String label;

        public static JudgeStringType getByLabel(String label) {
            if (label.startsWith(CONTAINS.label)) {
                return CONTAINS;
            }

            if (label.startsWith(EQUALS.label)) {
                return EQUALS;
            }

            if (label.startsWith(START_WITH.label)) {
                return START_WITH;
            }

            if (label.startsWith(NOT_CONTAINS.label)) {
                return NOT_CONTAINS;
            }

            if (label.startsWith(NOT_EQUALS.label)) {
                return NOT_EQUALS;
            }

            if (label.startsWith(NOT_START_WITH.label)) {
                return NOT_START_WITH;
            }

            return null;
        }

        JudgeStringType(String label) {
            this.label = label;
        }
    }
}
