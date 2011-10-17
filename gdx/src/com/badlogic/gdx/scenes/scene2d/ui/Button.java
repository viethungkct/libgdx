
package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Button extends Table {
	public ButtonStyle style;
	public boolean isChecked;

	protected ClickListener listener;

	public Button (TextureRegion region) {
		ButtonStyle style = new ButtonStyle();
		style.up = new NinePatch(region);
		setStyle(style);
		initialize();
	}

	public Button (Skin skin) {
		this(skin.getStyle(ButtonStyle.class), null);
	}

	public Button (ButtonStyle style) {
		this(style, null);
	}

	public Button (Actor child, Skin skin) {
		this(child, skin.getStyle(ButtonStyle.class));
	}

	public Button (Actor child, ButtonStyle style) {
		this(style, null);
		add(child);
		pack();
	}

	public Button (String text, Skin skin) {
		this(skin.getStyle(ButtonStyle.class), null);
		setText(text);
	}

	public Button (String text, ButtonStyle style) {
		this(style, null);
		setText(text);
	}

	public Button (String text, ButtonStyle style, String name) {
		this(style, name);
		setText(text);
	}

	public Button (ButtonStyle style, String name) {
		super(name);
		setStyle(style);
		initialize();
	}

	private void initialize () {
		super.setClickListener(new ClickListener() {
			public void click (Actor actor) {
				isChecked = !isChecked;
				if (listener != null) listener.click(actor);
			}
		});
	}

	public void setStyle (ButtonStyle style) {
		this.style = style;
		for (int i = 0; i < children.size(); i++) {
			Actor child = children.get(i);
			if (child instanceof Label) {
				((Label)child).setStyle(style);
				break;
			}
		}
		setBackground(isPressed ? style.down : style.up);
		invalidateHierarchy();
	}

	public void setClickListener (ClickListener listener) {
		this.listener = listener;
	}

	public Label getLabel () {
		for (int i = 0; i < children.size(); i++) {
			Actor child = children.get(i);
			if (child instanceof Label) return (Label)child;
		}
		throw new GdxRuntimeException("No child label was found.");
	}

	public void setText (String text) {
		if (children.isEmpty()) {
			Label label = new Label(text, style);
			label.setAlignment(Align.CENTER);
			add(label);
			invalidateHierarchy();
			return;
		}
		getLabel().setText(text);
	}

	public String getText () {
		return getLabel().getText();
	}

	public void draw (SpriteBatch batch, float parentAlpha) {
		float offsetX = 0, offsetY = 0;
		if (isPressed) {
			setBackground(style.down == null ? style.up : style.down);
			offsetX = style.pressedOffsetX;
			offsetY = style.pressedOffsetY;
		} else {
			if (style.checked == null)
				setBackground(style.up);
			else
				setBackground(isChecked ? style.checked : style.up);
			offsetX = style.unpressedOffsetX;
			offsetY = style.unpressedOffsetY;
		}
		for (int i = 0; i < children.size(); i++) {
			Actor child = children.get(i);
			child.x += offsetX;
			child.y += offsetY;
		}
		super.draw(batch, parentAlpha);
		for (int i = 0; i < children.size(); i++) {
			Actor child = children.get(i);
			child.x -= offsetX;
			child.y -= offsetY;
		}
	}

	public float getMinWidth () {
		return getPrefWidth();
	}

	public float getMinHeight () {
		return getPrefHeight();
	}

	/** Defines a button style, see {@link Button}
	 * @author mzechner */
	static public class ButtonStyle extends LabelStyle {
		public NinePatch down;
		public NinePatch up;
		public NinePatch checked;
		public float pressedOffsetX, pressedOffsetY;
		public float unpressedOffsetX, unpressedOffsetY;

		public ButtonStyle () {
		}

		public ButtonStyle (NinePatch down, NinePatch up, NinePatch checked, float pressedOffsetX, float pressedOffsetY,
			float unpressedOffsetX, float unpressedOffsetY, BitmapFont font, Color fontColor) {
			super(font, fontColor);
			this.down = down;
			this.up = up;
			this.checked = checked;
			this.pressedOffsetX = pressedOffsetX;
			this.pressedOffsetY = pressedOffsetY;
			this.unpressedOffsetX = unpressedOffsetX;
			this.unpressedOffsetY = unpressedOffsetY;
		}
	}
}
