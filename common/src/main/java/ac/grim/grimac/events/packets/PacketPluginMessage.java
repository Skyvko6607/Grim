package ac.grim.grimac.events.packets;

import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.common.arguments.CommonGrimArguments;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientPluginMessage;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import org.jetbrains.annotations.NotNull;

public class PacketPluginMessage extends PacketListenerAbstract {

    public void onPacketReceive(@NotNull PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE) {
            WrapperPlayClientPluginMessage packet = new WrapperPlayClientPluginMessage(event);
            checkChannel(event.getUser(), packet.getChannelName());
        } else if (event.getPacketType() == PacketType.Configuration.Client.PLUGIN_MESSAGE) {
            WrapperConfigClientPluginMessage packet = new WrapperConfigClientPluginMessage(event);
            checkChannel(event.getUser(), packet.getChannelName());
        }
    }

    private void checkChannel(User user, String channelName) {
        if (!"vv:proxy_details".equals(channelName)) return;
        // Players connecting through a proxy running ViaVersion are always allowed in.
        // Only log a warning if a proxy is configured and the warning hasn't been disabled.
        if (ProxyAlertMessenger.isUsingProxy() && CommonGrimArguments.WARN_ON_VIA_PROXY.value()) {
            LogUtil.warn(
                    user.getName() + " seems to have connected through a proxy running ViaVersion. "
                            + "Having ViaVersion installed on the proxy is incompatible with GrimAC and causes many issues. "
                            + "Please remove ViaVersion from your proxy server and install it on your backend servers instead."
            );
        }
    }

}
