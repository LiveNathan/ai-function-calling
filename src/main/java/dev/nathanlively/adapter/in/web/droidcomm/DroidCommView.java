package dev.nathanlively.adapter.in.web.droidcomm;

import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.MessageManager;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Aside;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;
import dev.nathanlively.adapter.in.web.MainLayout;
import jakarta.annotation.security.PermitAll;
import java.util.UUID;

@PageTitle("DroidComm")
@Route(value = "DroidComm", layout = MainLayout.class)
@PermitAll
public class DroidCommView extends VerticalLayout {

    public DroidCommView() {
        addClassNames("droid-comm-view", Width.FULL, Display.FLEX, Flex.AUTO);
        setSpacing(false);

        // UserInfo is used by Collaboration Engine and is used to share details
        // of users to each other to enable collaboration.
        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "Steve Lange");

        // CollaborationMessageList displays messages in a given topic.
        String aiChatTopic = "chat/ai-assistant";
        CollaborationMessageList messageList = new CollaborationMessageList(userInfo, aiChatTopic);
        messageList.setSizeFull();

        // CollaborationMessageInput is a textfield and button to submit new messages.
        CollaborationMessageInput messageInput = new CollaborationMessageInput(messageList);
        messageInput.setWidthFull();

        // Layout setup
        setSizeFull();
        add(messageList, messageInput);
        expand(messageList);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        Page page = attachEvent.getUI().getPage();
        page.retrieveExtendedClientDetails(details -> {
            setMobile(details.getWindowInnerWidth() < 740);
        });
        page.addBrowserWindowResizeListener(e -> {
            setMobile(e.getWidth() < 740);
        });
    }

    private void setMobile(boolean mobile) {
        // Handle any mobile-specific UI changes here if needed
    }
}
