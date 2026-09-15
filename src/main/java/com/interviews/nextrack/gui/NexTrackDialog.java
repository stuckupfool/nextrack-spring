package com.interviews.nextrack.gui;

import java.util.HashMap;
import java.util.ArrayList;

import org.springframework.shell.jline.tui.component.view.control.ButtonView;
import org.springframework.shell.jline.tui.component.view.control.DialogView;
import org.springframework.shell.jline.tui.component.view.control.GridView;
import org.springframework.shell.jline.tui.component.view.control.View;
import org.springframework.shell.jline.tui.component.view.event.KeyHandler;
import org.springframework.shell.jline.tui.component.view.event.KeyEvent.Key;

public class NexTrackDialog extends DialogView {
    private int focusedField;
    private ArrayList<View> views = new ArrayList<>();
    private HashMap<String, Integer> inputLookup = new HashMap<>();

    public NexTrackDialog(GridView view, ButtonView... buttonViews) {
        super(view, buttonViews);
        initKeyListener();
    }

    private void initKeyListener() {
        Runnable tabAction = () -> {
            focusedField = (focusedField + 1) % views.size();
            for (int i = 0; i < views.size(); i++) {
                View field = views.get(i);
                field.focus(field, i == focusedField);
            }
            getViewService().setFocus(this);
        };
        this.registerHotKeyBinding(Key.Tab, tabAction);
        Runnable shiftTabAction = () -> {
            focusedField = (focusedField - 1 + views.size()) % views.size();
            for (int i = 0; i < views.size(); i++) {
                View field = views.get(i);
                field.focus(field, i == focusedField);
            }
        };
        this.registerHotKeyBinding(Key.Backtab, shiftTabAction);
    }

    public void registerInput(String key, View view) {
        views.add(view);
        if (view instanceof InputView) {
            inputLookup.put(key, views.size() - 1);
        }
    }

    public InputView getInput(String key) {
        return (InputView) views.get(inputLookup.get(key));
    }

    @Override
    public KeyHandler getKeyHandler() {
        if (views.isEmpty()) {
            return null;
        }

        // Return the key handler of the currently focused input view
        View focusedInput = views.get(focusedField);
        return focusedInput.getKeyHandler();
    }
}
