package me.seetch.restarter;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import me.hteppl.tools.string.StringTools;
import me.seetch.format.Format;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Restarter extends PluginBase {

    private int hours = 120;

    @Override
    public void onEnable() {
        this.getServer().getScheduler().scheduleRepeatingTask(this, () -> {
            if (this.hours <= 0) {
                shutdown();
            } else {
                int[] hours = {10, 5, 1};
                if (contains(hours, this.hours)) {
                    this.getServer().broadcastMessage(Format.BLUE.colorize("\uE110", "Сервер будет перезагружен через %0.", StringTools.getFullPluralForm(this.hours, "минуту", "минуты", "минут")));
                }
            }
            this.hours--;
        }, 20 * 60);
    }

    private void shutdown() {
        AtomicInteger seconds = new AtomicInteger(10);
        this.getServer().getScheduler().scheduleRepeatingTask(this, () -> {
            if (seconds.get() <= 0) {
                for (Player player : this.getServer().getOnlinePlayers().values()) {
                    player.kick(Format.LIGHT_PURPLE.colorize("►", "Сервер перезагружается, перезайдите через %0.", "10 секунд"));
                }
                this.getServer().forceShutdown();
            } else {
                String message = Format.RED.colorize("\uE112", "Сервер будет перезагружен через %0.", StringTools.getFullPluralForm(this.hours, "секунду", "секунды", "секунд"));
                this.getServer().broadcastMessage(message);
                for (Player player : this.getServer().getOnlinePlayers().values()) {
                    player.sendTip(message);
                }
            }
            seconds.getAndDecrement();
        }, 20);
    }

    public static boolean contains(final int[] arr, final int key) {
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }
}
