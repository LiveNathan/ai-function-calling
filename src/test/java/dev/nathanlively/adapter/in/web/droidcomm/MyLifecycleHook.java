package dev.nathanlively.adapter.in.web.droidcomm;

import com.github.mvysny.kaributesting.v10.TestingLifecycleHook;
import com.vaadin.flow.component.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MyLifecycleHook implements TestingLifecycleHook {
    @Override
    public void awaitBeforeLookup() {
        TestingLifecycleHook.getDefault().awaitBeforeLookup();
    }

    @Override
    public void awaitAfterLookup() {
        TestingLifecycleHook.getDefault().awaitAfterLookup();
    }

    @NotNull
    @Override
    public List<Component> getAllChildren(@NotNull Component component) {
//        if (component instanceof BaseView) {
//            return Arrays.asList(((BaseView) component).main);
//        }
        return TestingLifecycleHook.getDefault().getAllChildren(component);
    }

    @Nullable
    @Override
    public String getLabel(@NotNull Component component) {
        return "";
    }
}
