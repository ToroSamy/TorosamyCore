package net.torosamy.torosamyCore.request;

import org.bukkit.scheduler.BukkitRunnable;
import java.util.*;

public class RequestManager extends BukkitRunnable {
    private static final RequestManager instance = new RequestManager();
    // ClassName:senderName
    private final Map<String, BaseRequest> requests = new HashMap<>();

    public <T extends BaseRequest> T getRequestBySenderName(String senderName, Class<T> clazz) {
        BaseRequest request = requests.get(BaseRequest.buildKey(senderName, clazz));

        if (clazz.isInstance(request)) {
            return clazz.cast(request);
        }
        return null;
    }

    public <T extends BaseRequest> T getRequestByReceiverName(String receiverName, Class<T> clazz) {
        for (BaseRequest request : requests.values()) {
            if (request.isReceiver(receiverName) && clazz.isInstance(request)) {
                return clazz.cast(request);
            }
        }
        return null;
    }

    private boolean clearByRequest(BaseRequest request) {
        return requests.remove(request.getKey()) != null;
    }

    public boolean clearByName(String username) {
        return requests.entrySet().removeIf(it-> it.getValue().isMember(username));
    }
    
    public boolean clearByName(String username, Class<? extends BaseRequest> clazz) {
        return requests.entrySet().removeIf(it-> 
                it.getValue().isMember(username) && it.getValue().getFullClassName().equals(clazz.getName())
        );
    }

    public boolean clearBySenderName(String senderName, Class<? extends BaseRequest> clazz) {
        return requests.remove(BaseRequest.buildKey(senderName, clazz)) != null;
    }

    public boolean clearByReceiverName(String receiverName, Class<? extends BaseRequest> clazz) {
        return requests.entrySet().removeIf(it -> 
            it.getValue().isReceiver(receiverName) && it.getValue().getFullClassName().equals(clazz.getName())
        );
    }
    
    private RequestManager() {}
    
    public static RequestManager getInstance() {
        return instance;
    }

    public boolean sendRequest(BaseRequest newRequest) {
        return sendRequest(newRequest, false);
    }
    
    public boolean sendRequest(BaseRequest newRequest, boolean forceSend) {
        String newKey = newRequest.getKey();
        BaseRequest oldRequest = requests.get(newKey);
        
        if (oldRequest == null) {
            requests.put(newKey, newRequest);
            newRequest.onSend();            
            return true;
        }

        if (!oldRequest.isReceiver(newRequest.receiver.getName())) {
            requests.put(newKey, newRequest);
            return true;
        }

        newRequest.onTryToSendSame();
        
        if (!forceSend) {
            return false;
        }
        
        requests.put(newKey, newRequest);
        return false;
    }


    public boolean acceptRequest(BaseRequest request) {
        String key = request.getKey();

        if (requests.remove(key) == null) {
            return false;
        }
        
        request.onAccept();
        return true;
    }

    public boolean denyRequest(BaseRequest request) {
        String key = request.getKey();

        if (requests.remove(key) == null) {
            return false;
        }

        request.onDeny();
        return true;
    }
    
    public boolean cancelRequest(BaseRequest request) {
        String key = request.getKey();

        if (requests.remove(key) == null) {
            return false;
        }

        request.onManualCancel();
        return true;
    }
    
    @Override
    public void run() {
        requests.entrySet().removeIf(it-> {
            if (!it.getValue().isExpired()) {
                return false;
            }
            it.getValue().onAutoCancel();
            return true;
        });
    }
}