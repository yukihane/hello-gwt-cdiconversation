package com.example.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

    /**
     * @return cid
     */
    String startConversation();

    int continueConversation(int i) throws IllegalArgumentException;

    int endConversation();
}
