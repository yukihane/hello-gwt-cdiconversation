package com.example.client;

import static com.example.shared.Constants.CID_HEADER;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MyModule implements EntryPoint {
    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    private static final String SERVER_ERROR = "An error occurred while "
        + "attempting to contact the server. Please check your network "
        + "connection and try again.";

    /**
     * Create a remote service proxy to talk to the server-side Greeting
     * service.
     */
    private final GreetingServiceAsync greetingService;

    private final Messages messages = GWT.create(Messages.class);

    private String cid;

    private DialogBox dialogBox;
    private Button closeButton;
    private HTML serverResponseLabel;

    public MyModule() {
        RpcRequestBuilder builder = new RpcRequestBuilder() {
            @Override
            protected void doFinish(RequestBuilder rb) {
                super.doFinish(rb);
                if (cid != null && !cid.isEmpty()) {
                    rb.setHeader(CID_HEADER, cid);
                }
            }
        };

        greetingService = GWT.create(GreetingService.class);
        ((ServiceDefTarget) greetingService).setRpcRequestBuilder(builder);
    }

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        final Button startButton = new Button("start");
        final Button contButton = new Button("continue");
        final Button endButton = new Button("end");

        final TextBox numberField = new TextBox();
        numberField.setText(messages.nameField());
        final Label errorLabel = new Label();

        // We can add style names to widgets
        startButton.addStyleName("sendButton");

        // Add the nameField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        RootPanel.get("startButtonContainer").add(startButton);
        RootPanel.get("nameFieldContainer").add(numberField);
        RootPanel.get("contButtonContainer").add(contButton);
        RootPanel.get("endButtonContainer").add(endButton);
        RootPanel.get("errorLabelContainer").add(errorLabel);

        // Focus the cursor on the name field when the app loads
        numberField.setFocus(true);
        numberField.selectAll();

        // Create the popup dialog box
        dialogBox = new DialogBox();
        dialogBox.setText("Remote Procedure Call");
        dialogBox.setAnimationEnabled(true);
        closeButton = new Button("Close");
        // We can set the id of a widget by accessing its Element
        closeButton.getElement().setId("closeButton");
        final Label textToServerLabel = new Label();
        serverResponseLabel = new HTML();
        VerticalPanel dialogVPanel = new VerticalPanel();
        dialogVPanel.addStyleName("dialogVPanel");
        dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
        dialogVPanel.add(textToServerLabel);
        dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
        dialogVPanel.add(serverResponseLabel);
        dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        dialogVPanel.add(closeButton);
        dialogBox.setWidget(dialogVPanel);

        // Add a handler to close the DialogBox
        closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                dialogBox.hide();
                startButton.setEnabled(true);
                startButton.setFocus(true);
            }
        });

        startButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                cid = null;
                greetingService.startConversation(new AsyncCallback <String>() {

                    @Override
                    public void onSuccess(String result) {
                        cid = result;
                        display("recieved CID: " + cid);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        handleError(caught);
                    }
                });
            }
        });

        contButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String text = numberField.getText();
                int num = Integer.valueOf(text).intValue();
                greetingService.continueConversation(num, new AsyncCallback <Integer>() {
                    @Override
                    public void onSuccess(Integer result) {
                        display("total(cont): " + result);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        handleError(caught);
                    }
                });
            }
        });

        endButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                greetingService.endConversation(new AsyncCallback <Integer>() {
                    @Override
                    public void onSuccess(Integer result) {
                        display("final result: " + result);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        handleError(caught);
                    }
                });
            }
        });
    }

    private void display(String html) {
        dialogBox.setText("Remote Procedure Call");
        serverResponseLabel.removeStyleName("serverResponseLabelError");
        serverResponseLabel.setHTML(html);
        dialogBox.center();
        closeButton.setFocus(true);
    }

    private void handleError(Throwable caught) {
        // Show the RPC error message to the user
        dialogBox.setText("Remote Procedure Call - Failure");
        serverResponseLabel.addStyleName("serverResponseLabelError");
        serverResponseLabel.setHTML(SERVER_ERROR);
        dialogBox.center();
        closeButton.setFocus(true);
    }
}
