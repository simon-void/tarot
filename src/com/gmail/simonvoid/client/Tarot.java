package com.gmail.simonvoid.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Tarot implements EntryPoint {
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    TarotConfiguration config = new TarotConfiguration();
    CelticCrossSpread cardSpread = new CelticCrossSpread(config);

    DecoratorPanel cardSpreadDecorator = new DecoratorPanel();
    cardSpreadDecorator.setWidget(cardSpread);

    RootPanel.get("tarotAppContainer").add(cardSpreadDecorator);
  }
}
