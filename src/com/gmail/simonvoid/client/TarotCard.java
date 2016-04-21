package com.gmail.simonvoid.client;

import java.util.LinkedList;
import java.util.List;

import com.gmail.simonvoid.client.event.CardSelectedEvent;
import com.gmail.simonvoid.client.event.CardSelectionListener;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

public class TarotCard
extends Composite
{
  final private Image image;
  final private List<CardSelectionListener> selectionListeners = new LinkedList<CardSelectionListener>();
  private SimplePanel messagePanel;
  final private String explanationLink;
  
  final private int pixelWidth;
  final private int pixelHeight;
  //pixelborder of the decorator panel
  final private int borderPixels = 5;
  
  private boolean covered = true;
  private String imgUrl;
  private String imgRotateUrl;
  
  public void addCardSelectionListener(CardSelectionListener listener)
  {
    selectionListeners.add(listener);
  }
  
  public void setMessage(SimplePanel messagePanel, String cardTitle)
  {
    this.messagePanel = messagePanel;
    image.setTitle(cardTitle);
  }
  
  public TarotCard(final String cardName, final String explanationUrl, final String imgUrl, String imgRotateUrl, final int pixelWidth)
  {
    this.pixelWidth = pixelWidth;
    pixelHeight = (int)Math.ceil(pixelWidth*1.6475);
    this.imgUrl = imgUrl;
    this.imgRotateUrl = imgRotateUrl;
    
    StringBuilder htmlB = new StringBuilder(128);
    htmlB.append("<a target=explaination href=\"");
    htmlB.append(explanationUrl);
    htmlB.append("\" title=\"Click to learn more about the meaning of this card\">");
//    htmlB.append("\">");
    htmlB.append(cardName);
    htmlB.append("</a>");
    explanationLink = htmlB.toString();
    
    //initialize the image
    image = new Image("./images/cardbackside.jpg");
    image.setAltText("couldn't load image for "+cardName);
    Image.prefetch(imgUrl);
    image.setWidth(pixelWidth+"px");
    image.setHeight(pixelHeight+"px");
    image.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event)
      {
        cardSelected();
      }
    });
    
    DecoratorPanel cardDecorator = new DecoratorPanel();
    cardDecorator.setWidget(image);
    cardDecorator.setStyleName("cardDecorator");
    
    initWidget(cardDecorator);
  }
  
  public int getPixelWidth()
  {
    return pixelWidth+2*borderPixels;
  }

  public int getPixelHeight()
  {
    return pixelHeight+2*borderPixels;
  }

  public void rotate()
  {
    image.setUrl("./images/rotated/cardbackside.jpg");
    Image.prefetch(imgRotateUrl);
    imgUrl = imgRotateUrl;
    
    image.setWidth( pixelHeight+"px");
    image.setHeight(pixelWidth +"px");
  }
  
  private void cardSelected()
  {
    boolean uncovered = covered;
    if(covered) {
      //reveal card
      image.setUrl(imgUrl);
      covered = false;
      
      //add caption    
      messagePanel.setWidget(new HTML(explanationLink));
    }
    
    final CardSelectedEvent event = new CardSelectedEvent(this, uncovered);
    for(CardSelectionListener listener: selectionListeners) {
      listener.cardSelected(event);
    }
  }
}
