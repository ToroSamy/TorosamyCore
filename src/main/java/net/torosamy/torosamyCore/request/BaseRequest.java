package net.torosamy.torosamyCore.request;

import org.bukkit.entity.Player;

public abstract class BaseRequest {
    protected final Player sender;
    
    protected final Player receiver;
    
    protected final int timeoutSeconds;
    
    private final long createTime;
    
    public BaseRequest(Player sender, Player receiver, int timeoutSeconds) {
        this.sender = sender;
        this.receiver = receiver;
        this.timeoutSeconds = timeoutSeconds;
        this.createTime = System.currentTimeMillis();
    }
    
    public boolean isMember(String username) {
        return sender.getName().equals(username) || receiver.getName().equals(username);
    }
    
    public boolean isSender(String username) {
        return sender.getName().equals(username);
    }

    public boolean isReceiver(String username) {
        return receiver.getName().equals(username);
    }

    public boolean isExpired() {
        return (System.currentTimeMillis() - createTime) / 1000 >= timeoutSeconds;
    }
    
    public abstract void onTryToSendSame();
    
    public abstract void onSend();
    
    public abstract void onAccept();

    public abstract void onAutoCancel();
    
    public abstract void onManualCancel();
    
    public abstract void onDeny();
    
    public Player getReceiver() {
        return receiver;
    }

    public Player getSender() {
        return sender;
    }
    
    public String getSimpleClassName() {
        return this.getClass().getSimpleName();
    }
    
    public String getFullClassName() {
        return this.getClass().getName();
    }
    
    public String getKey() {
        return buildKey(sender.getName(), this.getClass());
    }
    
    public static String buildKey(String senderName, Class<? extends BaseRequest> clazz) {
        return clazz.getName() + ":" + senderName;
    }
}