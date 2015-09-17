package com.example.server;

import javax.inject.Inject;

import com.example.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
    GreetingService {

    @Inject
    private Storage storage;

    @Override
    public String startConversation() {
        storage.begin();
        return storage.getCid();
    }

    @Override
    public int continueConversation(int i) throws IllegalArgumentException {
        return storage.add(i);
    }

    @Override
    public int endConversation() {
        storage.end();
        return storage.getNumber();
    }
}
