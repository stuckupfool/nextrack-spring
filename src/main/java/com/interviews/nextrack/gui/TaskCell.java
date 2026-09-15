package com.interviews.nextrack.gui;

import org.springframework.shell.jline.tui.component.view.control.cell.AbstractListCell;
import org.springframework.shell.jline.tui.component.view.screen.Screen;
import org.springframework.shell.jline.tui.component.view.screen.Screen.Writer;
import org.springframework.shell.jline.tui.geom.Rectangle;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import com.interviews.nextrack.todo.Task;


public class TaskCell extends AbstractListCell<Task> {

	public TaskCell(Task item) {
		super(item);
	}

	@Override
	public void drawContent(Screen screen) {
		boolean isSelected = getItem().getCompleted();
		String indicator = isSelected ? "[*]" : "[ ]";
		AttributedString attributedText = new AttributedStringBuilder()
				.style(AttributedStyle.DEFAULT.foreground(isSelected ? AttributedStyle.GREEN : AttributedStyle.BLACK))
				.append(indicator + " ")
				.append(getItem().getId() + " ")
				.style(AttributedStyle.BOLD.foreground(isSelected ? AttributedStyle.GREEN : AttributedStyle.WHITE))
				.append(getItem().getTitle() + "    ")
				.style(AttributedStyle.DEFAULT.foreground(isSelected ? AttributedStyle.GREEN : AttributedStyle.WHITE))
				.append(getItem().getDescription())
				.toAttributedString();
		Rectangle rect = getRect();
		Writer writer = screen.writerBuilder().style(getStyle()).build();
		writer.text(attributedText, rect.x(), rect.y());
		writer.background(rect, getBackgroundColor());
	}

}