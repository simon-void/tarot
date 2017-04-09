package com.gmail.simonvoid.client;

import com.google.gwt.user.client.ui.RootPanel;

public class TarotConfiguration {
  final private String minorArcanaPath;
  final private String majorArcanaPath;
  final private String imgPath;
  final private int pxHeightOffset;
  final private int cardPixelWidth;

  public TarotConfiguration() {
    final String webpageUrl = readHtmlConfig("webpageUrl");
    minorArcanaPath = webpageUrl + readHtmlConfig("relativeMinorArcanaFolderPath");
    majorArcanaPath = webpageUrl + readHtmlConfig("relativeMajorArcanaFolderPath");
    imgPath = webpageUrl + readHtmlConfig("relativeImageFolderPath");

    cardPixelWidth = 100;

    int pxHeightOffsetValue = 80; // default value
    try {
      String pxHeightOffsetStr = readHtmlConfig("pxHeightOffset");
      int configPxHeight = Integer.parseInt(pxHeightOffsetStr);
      if (configPxHeight >= 0) {
        pxHeightOffsetValue = configPxHeight;
      }
    } catch (NumberFormatException | IllegalStateException e) {
    }
    pxHeightOffset = pxHeightOffsetValue;
  }

  private String readHtmlConfig(String elemId) {
    RootPanel elemPanel = RootPanel.get(elemId);
    if (elemPanel == null) {
      throw new IllegalStateException("Check your Tarot.html. Couldn't find configuration html element with id: "
          + elemId);
    }
    String configValue = elemPanel.getElement().getInnerText();
    if (configValue.equals("")) {
      throw new IllegalStateException("Check your Tarot.html. Configuration html element with id: " + elemId
          + " has no inner text (configuration value)");
    }
    return configValue;
  }

  public String getMinorArcanaPath() {
    return minorArcanaPath;
  }

  public String getMajorArcanaPath() {
    return majorArcanaPath;
  }

  public String getImagePath() {
    return imgPath;
  }

  public int getPxHeightOffset() {
    return pxHeightOffset;
  }

  public int getCardPixelWidth() {
    return cardPixelWidth;
  }
}
