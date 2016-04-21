package com.gmail.simonvoid.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.RootPanel;

public class TarotCardDeck
{
  final int pixelWidth;
  final List<TarotCardInfo> deck;
  
  public TarotCardDeck(int cardPixelWidth)
  {
    pixelWidth = cardPixelWidth;
    deck = new LinkedList<TarotCardInfo>();
    
    TarotConfiguration config = new TarotConfiguration();
    DeckBuilder majorArcanaBuilder = new DeckBuilder(config.getMajorArcanaPath(), config.getImagePath(), deck);
    majorArcanaBuilder.addCard("The Fool", "tarot-fool.htm", "00.jpg");
    majorArcanaBuilder.addCard("The Magician", "tarot-magician.htm", "01.jpg");
    majorArcanaBuilder.addCard("The High Priestess", "tarot-highpriestess.htm", "02.jpg");
    majorArcanaBuilder.addCard("The Empress", "tarot-empress.htm", "03.jpg");
    majorArcanaBuilder.addCard("The Emperor", "tarot-emperor.htm", "04.jpg");
    majorArcanaBuilder.addCard("The Hierophant", "tarot-hierophant.htm", "05.jpg");
    majorArcanaBuilder.addCard("The Lovers", "tarot-lovers.htm", "06.jpg");
    majorArcanaBuilder.addCard("The Chariot", "tarot-chariot.htm", "07.jpg");
    majorArcanaBuilder.addCard("Strength", "tarot-strength.htm", "08.jpg");
    majorArcanaBuilder.addCard("The Hermit", "tarot-hermit.htm", "09.jpg");
    majorArcanaBuilder.addCard("The Wheel of Fortune", "tarot-wheeloffortune.htm", "10.jpg");
    majorArcanaBuilder.addCard("Justice", "tarot-justice.htm", "11.jpg");
    majorArcanaBuilder.addCard("The Hanged Man", "tarot-hangedman.htm", "12.jpg");
    majorArcanaBuilder.addCard("Death", "tarot-death.htm", "13.jpg");
    majorArcanaBuilder.addCard("Temperance", "tarot-temperance.htm", "14.jpg");
    majorArcanaBuilder.addCard("The Devil", "tarot-devil.htm", "15.jpg");
    majorArcanaBuilder.addCard("The Tower", "tarot-tower.htm", "16.jpg");
    majorArcanaBuilder.addCard("The Star", "tarot-star.htm", "17.jpg");
    majorArcanaBuilder.addCard("The Moon", "tarot-moon.htm", "18.jpg");
    majorArcanaBuilder.addCard("The Sun", "tarot-sun.htm", "19.jpg");
    majorArcanaBuilder.addCard("Judgement", "tarot-judgement.htm", "20.jpg");
    majorArcanaBuilder.addCard("The World", "tarot-world.htm", "21.jpg");
    
    addMinorArcana("Wands", config, deck);
    addMinorArcana("Pentacles", config, deck);
    addMinorArcana("Cups", config, deck);
    addMinorArcana("Swords", config, deck);
  }
  
  private void addMinorArcana( String arcanaName, TarotConfiguration config, List<TarotCardInfo> deckToAddCardsToo)
  {
    String[] cardNames = new String[15];
    
    //generate all card names
    cardNames[1] = "Ace of "+arcanaName;
    StringBuilder cardName = new StringBuilder(arcanaName.length()+6);
    for(int i=2; i<11; i++) {
      cardName.append(i).append(" of ").append(arcanaName);
      cardNames[i] = cardName.toString();
      //clear stringbuilder
      cardName.delete(0, cardName.length());
    }
    cardNames[11] = "Page of "+arcanaName;
    cardNames[12] = "Knight of "+arcanaName;
    cardNames[13] = "Queen of "+arcanaName;
    cardNames[14] = "King of "+arcanaName;
    
    //add all the cards of this minor arcana
    StringBuilder minorArcanaPathB = new StringBuilder(64);
    minorArcanaPathB.append(config.getMinorArcanaPath()).append("tarot-").append(arcanaName.toLowerCase()).append(".htm");
    DeckBuilder deckBuilder = new DeckBuilder(minorArcanaPathB.toString(), config.getImagePath(), deck);
    final String imgBase = "tarot-"+arcanaName.toLowerCase()+"-";
    for(int i=1; i<cardNames.length; i++) {
      deckBuilder.addCard(cardNames[i], "#"+i, imgBase+intToTwoLetters(i)+".jpg");
    }
  }
    
  private String intToTwoLetters(int i)
  {
    if(i<10) return "0"+i;
    else return Integer.toString(i);
  }
  
  public int getNumberOfCards()
  {
    return deck.size();
  }
  
  public TarotCard[] pickRandomCards(final int howMany)
  {
    if(howMany>deck.size()) {
      throw new IllegalArgumentException(
          "there are only "+getNumberOfCards()+
          " in the deck. so you can't pick "+howMany
      );
    }
    if(howMany<0) {
      throw new IllegalArgumentException(
          "you have to pick a positive amount of cards, not "+howMany
      );
    }
    
    List<TarotCardInfo> cardInfos = new LinkedList<TarotCardInfo>(deck);
    ArrayList<TarotCard> cards = new  ArrayList<TarotCard>(howMany);
    
    for(int i=0; i<howMany; i++) {
      int randomIndex = Random.nextInt(cardInfos.size());
      TarotCardInfo cardInfo = cardInfos.remove(randomIndex);
      cards.add(getCardFromInfo(cardInfo));
    }
    
    return cards.toArray(new TarotCard[howMany]);
  }
  
  private TarotCard getCardFromInfo(TarotCardInfo info)
  {
    TarotCard card = new TarotCard(
        info.name,
        info.explanationUrl,
        info.imageUrl,
        info.imageRotateUrl,
        pixelWidth
    );
    
    return card;
  }
  
  class TarotCardInfo
  {
    final private String name;
    final private String explanationUrl;
    final private String imageUrl;
    final private String imageRotateUrl;
    
    public TarotCardInfo(String name, String explanationUrl, String imageUrl, String imageRotateUrl)
    {
      this.name = name;
      this.explanationUrl = explanationUrl;
      this.imageUrl = imageUrl;
      this.imageRotateUrl = imageRotateUrl;
    }
  }
  
  class DeckBuilder
  {
    final List<TarotCardInfo> deckToAddCardsToo;
    final private String explainFolderPath;
    final private String imgFolderPath;
    final private String imgRotatedFolderPath;
    
    public DeckBuilder(final String explainFolderPath, final String imgFolderPath, final List<TarotCardInfo> deckToAddCardsToo)
    {
      this.deckToAddCardsToo = deckToAddCardsToo;
      
      this.explainFolderPath = explainFolderPath;
      this.imgFolderPath     = imgFolderPath;
      imgRotatedFolderPath = "./images/rotated/";
    }
    
    public void addCard( String name, String explanationFileName, String imageFileName)
    {
      TarotCardInfo info = new TarotCardInfo(
          name,
          explainFolderPath+explanationFileName,
          imgFolderPath+imageFileName,
          imgRotatedFolderPath+imageFileName
      );
      
      deckToAddCardsToo.add(info);
    }
  }
}

class TarotConfiguration {
  final private String minorArcanaPath;
  final private String majorArcanaPath;
  final private String imgPath;
  
  public TarotConfiguration()
  {
    final String webpageUrl = readHtmlConfig("webpageUrl");
    minorArcanaPath = webpageUrl + readHtmlConfig("relativeMinorArcanaFolderPath");
    majorArcanaPath = webpageUrl + readHtmlConfig("relativeMajorArcanaFolderPath");
    imgPath = webpageUrl + readHtmlConfig("relativeImageFolderPath");
  }
  
  private String readHtmlConfig(String elemId)
  {
    RootPanel elemPanel = RootPanel.get(elemId);
    if(elemPanel==null) {
      throw new IllegalStateException("Check your Tarot.html. Couldn't find configuration html element with id: "+elemId);
    }
    String configValue = elemPanel.getElement().getInnerText();
    if(configValue.equals("")) {
      throw new IllegalStateException("Check your Tarot.html. Configuration html element with id: "+elemId+" has no inner text (configuration value)");
    }
    return configValue;
  }
  
  public String getMinorArcanaPath()
  {
    return minorArcanaPath;
  }
  
  public String getMajorArcanaPath()
  {
    return majorArcanaPath;
  }
  
  public String getImagePath()
  {
    return imgPath;
  }
}