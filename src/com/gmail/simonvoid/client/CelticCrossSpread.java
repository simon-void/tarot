package com.gmail.simonvoid.client;

import java.util.HashMap;
import java.util.Map;

import com.gmail.simonvoid.client.event.CardSelectedEvent;
import com.gmail.simonvoid.client.event.CardSelectionListener;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

public class CelticCrossSpread extends Composite implements CardSelectionListener {
  private final Map<TarotCard, Position> cardPositions = new HashMap<TarotCard, CelticCrossSpread.Position>(4);
  private final AbsolutePanel absolutePanel;
  private final Grid descriptionGrid;
  private final TarotCardDeck deck;
  private final Button randomButton;
  private final TarotConfiguration config;

  public CelticCrossSpread(TarotConfiguration config) {
    this.config = config;
    deck = new TarotCardDeck(config);

    absolutePanel = new AbsolutePanel();
    descriptionGrid = new Grid(10, 2);
    initDescriptionGrid();

    randomButton = new Button("Give out Cards");
    randomButton.setFocus(true);
    randomButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        reset();
      }
    });

    reset();

    initWidget(absolutePanel);
  }

  private void initDescriptionGrid() {
    descriptionGrid.setBorderWidth(1);
    descriptionGrid.setText(0, 0, "Your condition at present:");
    descriptionGrid.setText(1, 0, "Your obstacle and trouble at present:");
    descriptionGrid.setText(2, 0, "The best possible outcome for you:");
    descriptionGrid.setText(3, 0, "The cause to your present situation:");
    descriptionGrid.setText(4, 0, "Your immediate past:");
    descriptionGrid.setText(5, 0, "Your immediate future:");
    descriptionGrid.setText(6, 0, "You at present:");
    descriptionGrid.setText(7, 0, "Your surroundings at present:");
    descriptionGrid.setText(8, 0, "Your hopes and fears:");
    descriptionGrid.setText(9, 0, "The outcome:");

    for (int i = 0; i < 10; i++) {
      SimplePanel panel = new SimplePanel();
      panel.setWidth("140px");
      descriptionGrid.setWidget(i, 1, panel);
    }
  }

  public void reset() {
    // clear the grid links
    for (int i = 0; i < 10; i++) {
      Panel panel = (Panel) descriptionGrid.getWidget(i, 1);
      panel.clear();
    }

    // clear the cards
    absolutePanel.clear();

    TarotCard[] cards = deck.pickRandomCards(10);
    final int width = cards[0].getPixelWidth();
    final int heigth = cards[0].getPixelHeight();
    final int space = 10;
    final int cluster_separation_space = 50;

    final int[] left = { 0, width + space, space + (int) Math.rint((width + heigth) * 0.5), width + heigth + 2 * space,
        2 * width + 2 * space + cluster_separation_space + heigth,
        3 * width + 2 * space + cluster_separation_space + heigth };

    final int[] top = { 0, 0, heigth + space, space + (int) Math.rint(1.5 * heigth - 0.5 * width), 0,
        2 * (heigth + space), (int) Math.rint(2.5 * (heigth + space)), 3 * (heigth + space), 4 * heigth + 3 * space + 5 };

    // place cards
    addCard(cards[0], left[2], top[2], true, 0);
    cards[1].rotate();
    addCard(cards[1], left[1], top[3], true, 1);
    addCard(cards[2], left[2], top[0], false, 2);
    addCard(cards[3], left[2], top[5], false, 3);
    addCard(cards[4], left[3], top[2], false, 4);
    addCard(cards[5], left[0], top[2], false, 5);
    addCard(cards[6], left[4], top[7], false, 6);
    addCard(cards[7], left[4], top[5], false, 7);
    addCard(cards[8], left[4], top[2], false, 8);
    addCard(cards[9], left[4], top[0], false, 9);

    // place button
    absolutePanel.add(randomButton, left[0], top[0]);

    // place grid
    final int gridBorder = width / 5;
    absolutePanel.add(descriptionGrid, gridBorder + 20, top[7] + gridBorder);

    setSize(left[5], top[8] + config.getPxHeightOffset() + gridBorder);
  }

  public void setSize(int pixelWidth, int pixelHeight) {
    absolutePanel.setHeight(pixelHeight + "px");
    absolutePanel.setWidth(pixelWidth + "px");
  }

  private void addCard(TarotCard card, int left, int top, boolean popToTop, int gridIndex) {
    if (popToTop) {
      cardPositions.put(card, new Position(left, top));
    }
    card.addCardSelectionListener(this);

    SimplePanel msgPanel = (SimplePanel) descriptionGrid.getWidget(gridIndex, 1);
    String title = descriptionGrid.getText(gridIndex, 0).replace(':', '.');

    card.setMessage((SimplePanel) msgPanel, title);
    absolutePanel.add(card, left, top);
  }

  private void topCard(TarotCard card) {
    Position position = cardPositions.get(card);
    if (position != null) {
      absolutePanel.add(card, position.left, position.top);
    }
  }

  public void cardSelected(CardSelectedEvent event) {
    TarotCard selectedCard = event.getSelectedCard();
    topCard(selectedCard);
  }

  private class Position {
    final int left;
    final int top;

    public Position(int left, int top) {
      this.left = left;
      this.top = top;
    }
  }
}
