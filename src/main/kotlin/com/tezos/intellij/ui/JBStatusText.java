/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tezos.intellij.ui;

import com.intellij.ui.ClickListener;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.UIBundle;
import com.intellij.ui.components.JBViewport;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * This is a copy of https://raw.githubusercontent.com/JetBrains/intellij-community/master/platform/platform-api/src/com/intellij/util/ui/StatusText.java
 * because all of its features are not yet available in 145.x.
 */
public abstract class JBStatusText {
  public static final SimpleTextAttributes DEFAULT_ATTRIBUTES = SimpleTextAttributes.GRAYED_ATTRIBUTES;
  public static final String DEFAULT_EMPTY_TEXT = UIBundle.message("message.nothingToShow");

  private static final int Y_GAP = 2;

  @Nullable private Component myOwner;
  private Component myMouseTarget;
  @NotNull private final MouseMotionListener myMouseMotionListener;
  @NotNull private final ClickListener myClickListener;

  private boolean myIsDefaultText;

  private String myText = "";
  @NotNull protected final SimpleColoredComponent myComponent = new SimpleColoredComponent();
  @NotNull private final SimpleColoredComponent mySecondaryComponent = new SimpleColoredComponent();
  private final List<ActionListener> myClickListeners = ContainerUtil.createLockFreeCopyOnWriteList();
  private final List<ActionListener> mySecondaryListeners = ContainerUtil.createLockFreeCopyOnWriteList();
  private boolean myHasActiveClickListeners; // calculated field for performance optimization
  private boolean myShowAboveCenter = true;
  private boolean myVerticalFlow = true;
  private boolean myFontSet = false;

  protected JBStatusText(JComponent owner) {
    this();
    attachTo(owner);
  }

  public JBStatusText() {
    myClickListener = new ClickListener() {
      @Override
      public boolean onClick(@NotNull MouseEvent e, int clickCount) {
        if (e.getButton() == MouseEvent.BUTTON1 && clickCount == 1) {
          ActionListener actionListener = findActionListenerAt(e.getPoint());
          if (actionListener != null) {
            actionListener.actionPerformed(new ActionEvent(this, 0, ""));
            return true;
          }
        }
        return false;
      }
    };

    myMouseMotionListener = new MouseAdapter() {
      private Cursor myOriginalCursor;

      @Override
      public void mouseMoved(final MouseEvent e) {
        if (isStatusVisible()) {
          if (findActionListenerAt(e.getPoint()) != null) {
            if (myOriginalCursor == null) {
              myOriginalCursor = myMouseTarget.getCursor();
              myMouseTarget.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
          }
          else if (myOriginalCursor != null) {
            myMouseTarget.setCursor(myOriginalCursor);
            myOriginalCursor = null;
          }
        }
      }
    };

    myComponent.setOpaque(false);
    myComponent.setFont(UIUtil.getLabelFont());
    setText(DEFAULT_EMPTY_TEXT, DEFAULT_ATTRIBUTES);
    myIsDefaultText = true;

    mySecondaryComponent.setOpaque(false);
    mySecondaryComponent.setFont(UIUtil.getLabelFont());
  }

  protected boolean isFontSet() {
    return myFontSet;
  }

  public void setFont(@NotNull Font font) {
    myComponent.setFont(font);
    mySecondaryComponent.setFont(font);
    myFontSet = true;
  }

  public void attachTo(@Nullable Component owner) {
    attachTo(owner, owner);
  }

  public void attachTo(@Nullable Component owner, @Nullable Component mouseTarget) {
    if (myMouseTarget != null) {
      myClickListener.uninstall(myMouseTarget);
      myMouseTarget.removeMouseMotionListener(myMouseMotionListener);
    }

    myOwner = owner;
    myMouseTarget = mouseTarget;

    if (myMouseTarget != null) {
      myClickListener.installOn(myMouseTarget);
      myMouseTarget.addMouseMotionListener(myMouseMotionListener);
    }
  }

  protected abstract boolean isStatusVisible();

  @Nullable
  private static ActionListener findListener(@NotNull SimpleColoredComponent component,
                                             @NotNull List<ActionListener> listeners,
                                             int xCoord) {
    int index = component.findFragmentAt(xCoord);
    if (index >= 0 && index < listeners.size()) {
      return listeners.get(index);
    }
    return null;
  }

  @Nullable
  private ActionListener findActionListenerAt(Point point) {
    if (!myHasActiveClickListeners || !isStatusVisible()) return null;

    point = SwingUtilities.convertPoint(myMouseTarget, point, myOwner);

    Rectangle commonBounds = getTextComponentBound();
    if (commonBounds.contains(point)) {
      Rectangle bounds;
      if (myComponent.getPreferredSize().height >= point.y - commonBounds.y) {
        bounds = adjustComponentBounds(myComponent, commonBounds);
        return findListener(myComponent, myClickListeners, point.x - bounds.x);
      }
      bounds = adjustComponentBounds(mySecondaryComponent, commonBounds);
      return findListener(mySecondaryComponent, mySecondaryListeners, point.x - bounds.x);
    }
    return null;
  }

  protected Rectangle getTextComponentBound() {
    Rectangle ownerRec = myOwner == null ? new Rectangle(0, 0, 0, 0) : myOwner.getBounds();

    Dimension size = getPreferredSize();
    int x = (ownerRec.width - size.width) / 2;
    int y = (ownerRec.height - size.height) / (myShowAboveCenter ? 3 : 2);
    return new Rectangle(x, y, size.width, size.height);
  }

  public final boolean isShowAboveCenter() {
    return myShowAboveCenter;
  }

  public final JBStatusText setShowAboveCenter(boolean showAboveCenter) {
    myShowAboveCenter = showAboveCenter;
    return this;
  }

  @NotNull
  public String getText() {
    return myText;
  }

  public JBStatusText setText(String text) {
    return setText(text, DEFAULT_ATTRIBUTES);
  }

  public JBStatusText setText(String text, SimpleTextAttributes attrs) {
    return clear().appendText(text, attrs);
  }

  public JBStatusText clear() {
    myText = "";
    myComponent.clear();
    myClickListeners.clear();
    mySecondaryComponent.clear();
    mySecondaryListeners.clear();
    myHasActiveClickListeners = false;
    repaintOwner();
    return this;
  }

  private void repaintOwner() {
    if (myOwner != null && isStatusVisible()) myOwner.repaint();
  }

  public JBStatusText appendText(String text) {
    return appendText(text, DEFAULT_ATTRIBUTES);
  }

  public JBStatusText appendText(String text, SimpleTextAttributes attrs) {
    return appendText(text, attrs, null);
  }

  public JBStatusText appendText(String text, SimpleTextAttributes attrs, ActionListener listener) {
    if (myIsDefaultText) {
      clear();
      myIsDefaultText = false;
    }

    myText += text;
    myComponent.append(text, attrs);
    myClickListeners.add(listener);
    if (listener != null) {
      myHasActiveClickListeners = true;
    }
    repaintOwner();
    return this;
  }

  public void setIsVerticalFlow(boolean isVerticalFlow) {
    myVerticalFlow = isVerticalFlow;
  }

  @NotNull
  public JBStatusText appendSecondaryText(@NotNull String text, @NotNull SimpleTextAttributes attrs, @Nullable ActionListener listener) {
    mySecondaryComponent.append(text, attrs);
    mySecondaryListeners.add(listener);
    if (listener != null) {
      myHasActiveClickListeners = true;
    }
    repaintOwner();
    return this;
  }

  public void paint(Component owner, Graphics g) {
    if (!isStatusVisible()) return;

    if (owner == myOwner) {
      doPaintStatusText(g, getTextComponentBound());
    }
    else {
      paintOnComponentUnderViewport(owner, g);
    }
  }

  private void paintOnComponentUnderViewport(Component component, Graphics g) {
    JBViewport viewport = ObjectUtils.tryCast(myOwner, JBViewport.class);
    if (viewport == null || viewport.getView() != component || viewport.isPaintingNow()) return;

    // We're painting a component which has a viewport as it's ancestor.
    // As the viewport paints status text, we'll erase it, so we need to schedule a repaint for the viewport with status text's bounds.
    // But it causes flicker, so we paint status text over the component first and then schedule the viewport repaint.

    Rectangle textBoundsInViewport = getTextComponentBound();

    int xInOwner = textBoundsInViewport.x - component.getX();
    int yInOwner = textBoundsInViewport.y - component.getY();
    Rectangle textBoundsInOwner = new Rectangle(xInOwner, yInOwner, textBoundsInViewport.width, textBoundsInViewport.height);
    doPaintStatusText(g, textBoundsInOwner);

    viewport.repaint(textBoundsInViewport);
  }

  private void doPaintStatusText(@NotNull Graphics g, @NotNull Rectangle bounds) {
    if (!hasSecondaryText()) {
      paintComponentInBounds(myComponent, g, bounds);
    }
    else {
      Rectangle primaryBounds = adjustComponentBounds(myComponent, bounds);
      Rectangle secondaryBounds = adjustComponentBounds(mySecondaryComponent, bounds);
      if (myVerticalFlow) {
        secondaryBounds.y += primaryBounds.height + JBUI.scale(Y_GAP);
      }

      paintComponentInBounds(myComponent, g, primaryBounds);
      paintComponentInBounds(mySecondaryComponent, g, secondaryBounds);
    }
  }

  @NotNull
  protected Rectangle adjustComponentBounds(@NotNull JComponent component, @NotNull Rectangle bounds) {
    Dimension size = component.getPreferredSize();

    if (myVerticalFlow) {
      return new Rectangle(bounds.x + (bounds.width - size.width) / 2, bounds.y, size.width, size.height);
    }
    else {
      return component == myComponent
             ? new Rectangle(bounds.x, bounds.y, size.width, size.height)
             : new Rectangle(bounds.x + bounds.width - size.width, bounds.y, size.width, size.height);
    }
  }

  private boolean hasSecondaryText() {
    return mySecondaryComponent.getCharSequence(false).length() > 0;
  }

  private static void paintComponentInBounds(@NotNull SimpleColoredComponent component, @NotNull Graphics g, @NotNull Rectangle bounds) {
    Graphics2D g2 = (Graphics2D)g.create(bounds.x, bounds.y, bounds.width, bounds.height);
    component.setBounds(0, 0, bounds.width, bounds.height);
    component.paint(g2);
    g2.dispose();
  }

  @NotNull
  public SimpleColoredComponent getComponent() {
    return myComponent;
  }

  @NotNull
  public SimpleColoredComponent getSecondaryComponent() {
    return mySecondaryComponent;
  }

  public Dimension getPreferredSize() {
    Dimension componentSize = myComponent.getPreferredSize();
    if (!hasSecondaryText()) return componentSize;
    Dimension secondaryComponentSize = mySecondaryComponent.getPreferredSize();

    if (myVerticalFlow) {
      return new Dimension(Math.max(componentSize.width, secondaryComponentSize.width),
                           componentSize.height + secondaryComponentSize.height + JBUI.scale(Y_GAP));
    }
    else {
      return new Dimension(componentSize.width + secondaryComponentSize.width,
                           Math.max(componentSize.height, secondaryComponentSize.height));
    }
  }

  public boolean isVerticalFlow() {
    return myVerticalFlow;
  }
}