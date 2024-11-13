package com.sublimeS0.radonchat.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Environment(EnvType.CLIENT)
@Mixin(ChatHud.class)
public abstract class ChatHudMixin extends DrawableHelper {

    @ModifyVariable(
            method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private Text cps$modifyMessage(Text message, Text m, MessageSignatureData sig, int ticks, MessageIndicator messageIndicator, boolean refreshing) {

        final Style initStyle = message.getStyle();
        final Style nameStyle = message.getStyle().withColor(TextColor.parse("#2030FE"));

        StringBuilder builder = new StringBuilder();

        String senderName = message.getString();
        senderName = senderName.substring(senderName.indexOf("<") + 1);
        String contents = senderName.substring(senderName.indexOf(">"));
        senderName = senderName.substring(0, senderName.indexOf(">"));


        Text start = Text.empty().setStyle(initStyle).append("<");
        Text name = Text.empty().setStyle(nameStyle).append(senderName);
        Text end = Text.empty().setStyle(initStyle).append("> ").append(contents);

        return Text.empty().append(start).append(name).append(contents);
    }

    private void getStyledName() {

    }

}
