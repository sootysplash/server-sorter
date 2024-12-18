package me.sootysplash.mixin.client;

import me.sootysplash.ConfigSS;
import me.sootysplash.MainSS;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Objects;
import java.util.logging.Level;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {
    @Shadow protected MultiplayerServerListWidget serverListWidget;

    @Shadow private ServerList serverList;

    @Shadow public abstract ServerList getServerList();


    @Shadow @Final private MultiplayerServerListPinger serverListPinger;

    @Shadow public abstract MultiplayerServerListPinger getServerListPinger();

    public MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        ConfigSS config = ConfigSS.getInstance();

        if(config.enabled) {
            tick++;
            if (tick >= config.delay) {

                try {
//                new Thread(()->{
                    ServerList listy;
                    ArrayDeque<ServerInfo> ad = new ArrayDeque<>();
                    listy = new ServerList(MinecraftClient.getInstance());
                    ad.clear();

//                    System.out.println("listy pre size: " + listy.size());

                    while(listy.size() != 0){
                        listy.remove(listy.get(0));
                    }

                    if (serverList.size() == 0 || serverListWidget.children().isEmpty()) {
//                        System.out.println("returned " + serverList.size() + " " + serverListWidget.children().isEmpty());
                        return;
                    }

//                System.out.println("serverlist size: " + serverList.size());
                    int t = 0;
                    while (ad.size() != serverList.size()) {
                        ad.add(serverList.get(t));
                        t++;
                    }

                    ad = arrayDeque(ad);
//                    System.out.println("ad size: "+ ad.size());

                    int g = 0;
                    while(!ad.isEmpty()) {
                        listy.add(ad.getFirst(), false);
                        serverList.set(g, ad.pollFirst());
                        g++;
                    }
//                    System.out.println("g amount: " + g);

                        serverListWidget.setServers(listy);
                        serverList.saveFile();
                        MainSS.LOGGER.log(Level.INFO, "Set servers!");

//                    System.out.println("listy size: " + listy.size());

//                }).start();
                } catch (Exception e) {
//                    throw new RuntimeException(e);

                }

                tick = 0;
            }
        }

    }
    @Unique
    long tick = 0;
    @Unique
    public long ping(ServerInfo o){
        try {

            ConfigSS config = ConfigSS.getInstance();

            if (!(config.str.equals("Address size") || config.str.equals("Name size") || config.str.equals("Random") || config.str.equals("Hash code"))) {
                if (o.ping <= 0) {
                    if (config.invert) {
                        return Long.parseLong("-999999999999");
                    } else {
                        return Long.parseLong("999999999999");
                    }
                }
            }

            long main = switch (config.str) {
                case "Ping" -> o.ping;
                case "Version" -> o.protocolVersion;
                case "Online players" -> Objects.requireNonNull(o.players).online();
                case "Max players" -> Objects.requireNonNull(o.players).max();
                case "Address size" -> o.address.length();
                case "Name size" -> o.name.length();
                case "Hash code" -> o.hashCode();
                case "Random" -> Math.round(getServerList().size() * Math.random());
                default -> 0;
            };
            main *= 100000;

            long second = switch (config.str2) {
                case "Ping" -> o.ping;
                case "Version" -> o.protocolVersion;
                case "Online players" -> Objects.requireNonNull(o.players).online();
                case "Max players" -> Objects.requireNonNull(o.players).max();
                case "Address size" -> o.address.length();
                case "Name size" -> o.name.length();
                case "Hash code" -> o.hashCode();
                case "Random" -> Math.round(getServerList().size() * Math.random());
                default -> 0;
            };
            return main + second;

        }catch(Exception e){
            return 0;
        }
    }

    @Unique
    public ArrayDeque<ServerInfo> arrayDeque(ArrayDeque<ServerInfo> inc){
        try {
            Comparator<ServerInfo> comp = Comparator.comparing(this::ping);
            Object[] def = inc.stream().sorted(comp).toArray();
            ArrayDeque<ServerInfo> best = new ArrayDeque<>();

            for (Object o : def) {
                if (o instanceof ServerInfo) {
                    best.add((ServerInfo) o);
                }
            }
            ConfigSS config = ConfigSS.getInstance();

//            System.out.println("best size: " + best.size());

            if (config.invert) {
                ArrayDeque<ServerInfo> best1 = new ArrayDeque<>();
                while (!best.isEmpty()) {
                    best1.add(Objects.requireNonNull(best.pollLast()));
                }
//                System.out.println("best1 size: " + best1.size());
                return best1;
            }


            return best;
        }catch(Exception e){
//            throw new RuntimeException(e);
            return inc;
        }

    }

}
