package cn.ericmoon.cardGame.draw;

import cn.ericmoon.cardGame.CONSTANT;
import cn.ericmoon.cardGame.GameStart;
import cn.ericmoon.cardGame.cards.AfterCard;
import cn.ericmoon.cardGame.event.AfterStatusEvent;
import cn.ericmoon.cardGame.event.BuffStatusEvent;
import cn.ericmoon.cardGame.event.CardMouseEvent;
import cn.ericmoon.cardGame.cards.Buff;
import cn.ericmoon.cardGame.cards.Card;
import cn.ericmoon.cardGame.cards.DamageCard;
import cn.ericmoon.cardGame.event.RestartEvent;
import cn.ericmoon.cardGame.keys.BuffPlayerKey;
import cn.ericmoon.cardGame.keys.CardPlayerKey;
import cn.ericmoon.cardGame.keys.AfterPlayerKey;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.lang.System.exit;

@SuppressWarnings("all")


public class GameClient extends JFrame {

    Toolkit toolkit = this.getToolkit();
    Container container = this.getContentPane();
    JLayeredPane layeredPane = new JLayeredPane();

    public CardPlayerKey cardPlayerKeySelf;
    public CardPlayerKey cardPlayerKeyEnemy;
    public BuffPlayerKey buffPlayerkeySelf;
    public BuffPlayerKey buffPlayerKeyEnemy;
    public AfterPlayerKey afterPlayerKeySelf;
    public AfterPlayerKey afterPlayerKeyEnemy;

    public String playerDesciption = "";
    public int chosenIndexOfButton = -1;

    private JLabel labelStatus;
    private JLabel labelHPSelf;
    private JLabel labelHPEnemy;
    private JLabel labelLuckSelf;
    private JLabel labelLuckEnemy;
    private JLabel labelBuffSelf;
    private JLabel labelAfterSelf;
    private JLabel labelExit;
    private JLabel labelRestart;
    private JLabel labelSuccess;

    public boolean playing = false;

    ArrayList<JButton> buttons = new ArrayList<>();
    ArrayList<JLabel> buffLabels = new ArrayList<>();
    ArrayList<CardMouseEvent> cardMouseEventsSelf = new ArrayList<>();
    ArrayList<CardMouseEvent> cardMouseEventsEnemy = new ArrayList<>();

    private static BufferedImage damage;
    private static BufferedImage buffc;
    private static BufferedImage after;
    private static BufferedImage buffs;
    private static BufferedImage exit;
    private static BufferedImage restart;


    static {
        try {
             damage = ImageIO.read(GameClient.class.getResource("images/damageCardImage.png"));
             buffc = ImageIO.read(GameClient.class.getResource("images/buffCardImage.png"));
             after = ImageIO.read(GameClient.class.getResource("images/afterCardImage.png"));
             buffs = ImageIO.read(GameClient.class.getResource("images/buff.png"));
             exit = ImageIO.read(GameClient.class.getResource("images/EXIT.png"));
             restart = ImageIO.read(GameClient.class.getResource("images/ReStart.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ImageIcon damageCardImage = new ImageIcon(damage);
    private ImageIcon buffCardImage = new ImageIcon(buffc);
    private ImageIcon afterCardImage = new ImageIcon(after);
    private ImageIcon buffStatusImage = new ImageIcon(buffs);
    private ImageIcon exitImage = new ImageIcon(exit);
    private ImageIcon restartImage = new ImageIcon(restart);


//
//    private ImageIcon damageCardImage = new ImageIcon(this.getClass().getResource("images/damageCardImage.png"));
//    private ImageIcon buffCardImage = new ImageIcon(this.getClass().getResource("images/buffCardImage.png"));
//    private ImageIcon afterCardImage = new ImageIcon(this.getClass().getResource("images/afterCardImage.png)"));
//    private ImageIcon buffStatusImage = new ImageIcon(this.getClass().getResource("images/buff.png"));
//    private ImageIcon exitImage = new ImageIcon(this.getClass().getResource("images/EXIT.png"));
//    private ImageIcon restartImage = new ImageIcon(this.getClass().getResource("images/ReStart.png"));


    /**
     * 双缓冲
     */
/*
    private Image offScreenImage = null;

    public void update(Graphics g){
        if(offScreenImage == null)
            offScreenImage = this.createImage(CONSTANT.frameWidth,CONSTANT.frameHeight);

        Graphics gOff = offScreenImage.getGraphics();
        paint(gOff);
        g.drawImage(offScreenImage,0,0,null);
    }
*/

    public void LaunchFrame() {
        configureFrame();
    }

    public void configureFrame() {
        this.setTitle("Created by EricWong & CarterWang");
        this.setVisible(true);
        this.setSize(CONSTANT.frameWidth, CONSTANT.frameHeight);
        this.setLocation(CONSTANT.frameLocationx, CONSTANT.frmaeLocationy);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //this.setLayeredPane(layeredPane);

        this.container.setLayout(null);
        this.container.setBackground(Color.PINK);
        //layeredPane.add(this.container,JLayeredPane.MODAL_LAYER);

        //setSelfBuffLabel();
        System.out.println("成功设置窗口！");
    }

/*
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        refreshAll();
    }
*/

    public void refreshAll() {
        //System.out.println("refresh被调用！");
        removeAllComponents();
        drawButtons();
        drawInfo();
        repaint();
    }

    public void removeAllComponents() {
        if(cardPlayerKeySelf != null) {
            removeAllLabels();
            removeAllButtons();
            //System.out.println("chosenIndexOfButton被初始化为：" + chosenIndexOfButton);
        }
    }

    private void removeAllLabels() {
        if (this.labelStatus != null)
            container.remove(labelStatus);
        if (this.labelHPSelf != null)
            container.remove(labelHPSelf);
        if(this.labelHPEnemy != null)
            container.remove(labelHPEnemy);
        if(this.labelLuckSelf != null)
            container.remove(labelLuckSelf);
        if(this.labelLuckEnemy != null)
            container.remove(labelLuckEnemy);
        if(this.labelBuffSelf != null)
            container.remove(labelBuffSelf);
        if(this.labelAfterSelf != null)
            container.remove(labelAfterSelf);
        if(this.labelExit != null)
            container.remove(labelExit);
        if(this.labelRestart != null)
            container.remove(labelRestart);
        if(this.labelSuccess != null)
            container.remove(labelSuccess);
    }

    private void removeAllButtons() {
        if(buttons !=null){
            for (int i = 0; i < buttons.size(); i++) {
                JButton button = buttons.get(i);
                container.remove(button);
            }
            buttons.clear();
        }


        this.cardMouseEventsSelf.clear();
        this.cardMouseEventsEnemy.clear();
        //System.out.println("清理鼠标事件！");
        this.chosenIndexOfButton = -1;
    }

    public void drawButtons() {

        //System.out.println("进入函数");
        //System.out.println("清屏");

        drawButtonSelf(cardPlayerKeySelf);
        drawButtonEnemy(cardPlayerKeyEnemy);

    }

    public void drawInfo() {
        drawLabelHP();
        drawLabelLuck();
    }


    public void drawButtonSelf(CardPlayerKey cardPlayerKey) {

        if (cardPlayerKey != null && cardPlayerKey.getCards()!=null &&!cardPlayerKey.getCards().isEmpty()) {
            //System.out.println("自己还剩" + cardPlayerKey.getCards().size() + "张手牌");

            int x = (CONSTANT.frameWidth - cardPlayerKey.getCards().size() * CONSTANT.cardWidth) / 2;
            int y = CONSTANT.selfCardY;
            int labelX = CONSTANT.frameWidth/2 - 30;

            setSelfBuffLabel();

            for (int i = 0; i < cardPlayerKey.getCards().size(); i++) {
                Card card = cardPlayerKeySelf.getCards().get(i);

                String text = card.getCardName();
                String cardDescription = card.getCardDesc();
                boolean beingCovered = card.isBeingCovered();

                CardMouseEvent cardMouseEvent = new CardMouseEvent(i,this,beingCovered);

                JButton button = getButton(true, card, text, cardDescription, x, y, CONSTANT.cardWidth, CONSTANT.cardHeight);
                card.setX(x);
                card.setY(y);

                JLabel cardTypeLabel = getCardTypeLabel(i);
                button.add(cardTypeLabel);

                if(card.getCardType() == 1) {
                    DamageCard damageCard = (DamageCard) card;
                    JLabel damageLabel = getDamageLabel(String.valueOf(damageCard.getDamage()));
                    button.add(damageLabel);
                }

                button.addMouseListener(cardMouseEvent);

                cardMouseEventsSelf.add(cardMouseEvent);
                container.add(button);
                buttons.add(button);
                //System.out.println("Button" + i + " 的Event里的index值:" + cardMouseEvent.getIndex() + "\nchosenIndex: " + cardMouseEvent.getChosenIndex());
                x += CONSTANT.cardWidth + 10;
            }
        }
    }

    public void drawButtonEnemy(CardPlayerKey cardPlayerKey) {
        if (cardPlayerKey != null && cardPlayerKey.getCards()!=null &&!cardPlayerKey.getCards().isEmpty()) {

            int x = (CONSTANT.frameWidth - cardPlayerKey.getCards().size() * CONSTANT.cardWidth) / 2;
            int y = CONSTANT.getCardDistantFromUp;

            for (int i = 0; i < cardPlayerKey.getCards().size(); i++) {
                Card card = cardPlayerKeyEnemy.getCards().get(i);

                String text = card.getCardName();
                String cardDescription = card.getCardDesc();
                boolean beingCovered = card.isBeingCovered();

                JButton button = getButton(false, card, text, cardDescription, x, y, CONSTANT.cardWidth, CONSTANT.cardHeight);
                card.setX(x);
                card.setY(y);
                CardMouseEvent cardMouseEvent = new CardMouseEvent(i,this,beingCovered);

                button.addMouseListener(cardMouseEvent);
                button.getComponent(0).setVisible(false);

                cardMouseEventsEnemy.add(cardMouseEvent);
                container.add(button);
                buttons.add(button);

                //System.out.println("Button" + i + " 的Event里的index值:" + cardMouseEvent.getIndex() + "\nchosenIndex: " + cardMouseEvent.getChosenIndex());
                x += CONSTANT.cardWidth + 10;
            }
            Helper.printLine();
        }
    }

    public void configureIndex() {

        if(cardMouseEventsSelf != null) {
            for(CardMouseEvent cardMouseEvent : cardMouseEventsSelf) {
                if(cardMouseEvent.getChosenIndex() == cardMouseEvent.getIndex()) {
                    //System.out.println("找到了！");
                    int index = cardMouseEventsSelf.indexOf(cardMouseEvent);
                    Card card = cardPlayerKeySelf.getCards().get(index);
                    JButton button = buttons.get(index);
                    int x = card.getX();
                    int y = card.getY() - 30;
                    //updateButton(button,card.getCardName(),card.getCardDesc(),x,y,index);
                    this.chosenIndexOfButton = cardMouseEvent.getChosenIndex();
                    break;
                }
            }
        }
    }


    /**
     * Helper methods
     */

    private JButton getButton(boolean isShowType, Card card, String name, String description, int x, int y, int width,
                              int height) {

        int cardType = card.getCardType();
        ImageIcon imageIcon;

        JButton jButton = new JButton();
        jButton.setBounds(x,y,width,height);
        jButton.setLayout(null);

        JLabel nameLabel = getCardNameLabel(name);
        JLabel desLabel = getCardDesLabel(description);

        jButton.add(nameLabel);
        jButton.add(desLabel);
        if(cardType == 1)
            imageIcon = damageCardImage;
        else if(cardType == 2)
            imageIcon = buffCardImage;
        else
            imageIcon = afterCardImage;

        if (isShowType)
            jButton.setIcon(imageIcon);

        return jButton;
    }

    private JLabel getDamageLabel(String text) {

        text = "攻击值: " + text;

        JLabel label = new JLabel(text);
        label.setVisible(true);
        label.setForeground(Color.black);
        label.setBounds(CONSTANT.cardWidth/2 - 25,CONSTANT.cardHeight - 50,100,20);

        return label;
    }

    private JLabel getCardNameLabel(String text) {

        JLabel label = new JLabel(text);
        label.setForeground(Color.black);
        label.setFont(new Font("楷体", Font.PLAIN,13));
        label.setBounds(CONSTANT.cardWidth/2 - (text.length() * 12)/2,2,100,50);

        return label;
    }

    private JLabel getCardDesLabel(String text) {
        text = "<html>" + text + "</html>";
        JLabel label = new JLabel(text);
        label.setForeground(Color.black);
        label.setFont(new Font("楷体", Font.PLAIN,13));
        label.setBounds(CONSTANT.textAreaSpace,25,CONSTANT.cardWidth - 2*CONSTANT.textAreaSpace,CONSTANT.textAreaHeight);

        label.setVisible(false);
        return label;
    }

    private void setSelfBuffLabel() {

            JLabel label = new JLabel();
            label.setBounds(CONSTANT.selfBuffLabelX, CONSTANT.selfBuffLabelY, CONSTANT.selfBuffLabelWidth, CONSTANT.selfBuffLabelHeight);
            label.setForeground(Color.white);
            label.setIcon(buffStatusImage);
            label.setVisible(true);
            label.addMouseListener(new BuffStatusEvent(this));

            this.labelBuffSelf = label;
            container.add(label);
    }


    public void updateButton(int index) {

        if (cardPlayerKeySelf != null && cardPlayerKeySelf.getCards()!=null && !cardPlayerKeySelf.getCards().isEmpty()) {

            Card card = cardPlayerKeySelf.getCards().get(index);

            JButton button = getButton(true, card, card.getCardName(), card.getCardDesc(), card.getX(), card.getY(),
                    CONSTANT.cardWidth, CONSTANT.cardHeight);
            CardMouseEvent cardMouseEvent = new CardMouseEvent(index,this,card.isBeingCovered());
            button.addMouseListener(cardMouseEvent);

            if(card.getCardType() == 1) {

                DamageCard damageCard = (DamageCard) card;
                JLabel label = getDamageLabel(String.valueOf(damageCard.getDamage()));
                button.add(label);
            }

            JLabel label = getCardTypeLabel(index);
            button.add(label);

            cardMouseEventsSelf.set(index,cardMouseEvent);
            JButton buttonToBeRemoved = buttons.get(index);
            container.remove(buttonToBeRemoved);
            buttons.set(index,button);
            container.add(buttons.get(index));

            //container.getComponent(index+1).setLocation(card.getX(),card.getY());
            repaint();
        }
    }

    public void setBuffStatusVisible() {
        java.util.List<Buff> buffCards = new ArrayList<>();
        java.util.List<AfterCard> afterCards = new ArrayList<>();
        if(buffPlayerkeySelf != null) {
            if(buffPlayerkeySelf.getBuffs() != null)
                buffCards.addAll(buffPlayerkeySelf.getBuffs());
            if(buffPlayerkeySelf.getDeBuffs() != null)
                buffCards.addAll(buffPlayerkeySelf.getDeBuffs());
        }
        if(afterPlayerKeySelf != null) {
           if(afterPlayerKeySelf.getAfterCards() != null) {
              afterCards.addAll(afterPlayerKeySelf.getAfterCards());
           }
       }

        //System.out.println("buffcards number: " + buffCards.size());

        int y = CONSTANT.selfBuffLabelY - 30;

        for (Buff b : buffCards) {
            //System.out.println(b.getStatusDesc());
            JLabel label = new JLabel(b.getStatusDesc());
            label.setVisible(true);
            label.setForeground(Color.white);
            label.setFont(new Font("宋体", Font.BOLD,15));
            label.setBounds(CONSTANT.selfBuffLabelX - 15,y,CONSTANT.selfBuffDesWidth,CONSTANT.selfBuffDesHeight);
            container.add(label);
            buffLabels.add(label);
            y -= CONSTANT.selfBuffDesHeight - 5;
        }

        for(AfterCard b : afterCards) {
            //System.out.println(b.getStatusDesc());
            JLabel label = new JLabel(b.getStatusDesc());
            label.setVisible(true);
            label.setForeground(Color.white);
            label.setFont(new Font("宋体", Font.BOLD,15));
            label.setBounds(CONSTANT.selfBuffLabelX - 15,y,CONSTANT.selfBuffDesWidth,CONSTANT.selfBuffDesHeight);
            container.add(label);
            buffLabels.add(label);
            y -= CONSTANT.selfBuffDesHeight - 5;
        }

        repaint();
    }

    public void setBuffStatusUnvisible() {
        if(buffLabels != null) {
            for(int i=0;i<buffLabels.size();i++) {
                JLabel label = buffLabels.get(i);
                container.remove(label);
            }
            buffLabels.clear();
        }
        repaint();
    }


    public void setDesVisible() {
        if(cardMouseEventsSelf != null) {
            for(CardMouseEvent cardMouseEvent : cardMouseEventsSelf) {
                if(cardMouseEvent.isBeingCovered()) {
                    int index = cardMouseEvent.getIndex();
                    Card card = cardPlayerKeySelf.getCards().get(index);
                    card.setX(card.getX());
                    card.setY(card.getY() - 30);
                    card.setBeingCovered(true);
                    updateButton(index);
                    JButton button = buttons.get(index);
                    JLabel label = (JLabel) button.getComponent(1);
                    label.setVisible(true);
                    break;
                }
            }
        }
    }

    private JLabel getCardTypeLabel(int index) {

        Card card = cardPlayerKeySelf.getCards().get(index);
        JLabel label;
        switch (card.getCardType()) {
            case 1 :
                label = new JLabel("攻击卡");
                break;
            case 2:
                Buff buffCard = (Buff) card;
                String text;
                if(buffCard.isDebuff())
                    text = "Debuff卡";
                else
                    text = "Buff卡";
                label = new JLabel(text);
                break;
            case 3:
                label = new JLabel("反制卡");
                break;
            default:
                label = new JLabel("空");
                break;

        }

        label.setForeground(Color.black);
        label.setVisible(true);
        if(label.getText() == "反制卡"){
            label.setBounds(CONSTANT.cardWidth/2 - 22,CONSTANT.cardHeight - 45,70,20);
        }
        else {
            label.setBounds(CONSTANT.cardWidth / 2 - 22, CONSTANT.cardHeight - 30, 70, 20);
        }

        return label;

    }


    public void setDesUnVisible() {
        if(cardMouseEventsSelf != null){
            for(CardMouseEvent cardMouseEvent : cardMouseEventsSelf) {
                if(cardMouseEvent.isBeingCovered()) {
                    int index = cardMouseEvent.getIndex();
                    Card card = cardPlayerKeySelf.getCards().get(index);
                    card.setX(card.getX());
                    card.setY(card.getY() + 30);
                    card.setBeingCovered(false);
                    updateButton(index);
                    JButton button = buttons.get(index);
                    JLabel label = (JLabel) button.getComponent(1);
                    label.setVisible(false);
                    break;
                }
            }
        }
    }

    public void drawLabelHP() {

        //Self
        int x = CONSTANT.hpLabelSelfX;
        int y = CONSTANT.hpLabelSelfY;

        if (cardPlayerKeySelf != null) {
            //System.out.println("HP函数");
            JLabel labelHP = new JLabel();
            labelHP.setText("HP:" + cardPlayerKeySelf.getPlayer().getHp());
            labelHP.setBounds(x,y,100,100);
            labelHP.setFont(new Font("宋体",1,20));
            labelHP.setForeground(Color.white);
            container.add(labelHP);
            this.labelHPSelf = labelHP;
            //System.out.println(cardPlayerKey.getPlayer().getHp());

        }

        //Enemy
        y = 100;

        if(cardPlayerKeyEnemy != null) {
            //System.out.println("HP函数");
            JLabel labelHP = new JLabel();
            labelHP.setText("HP:" + cardPlayerKeyEnemy.getPlayer().getHp());
            labelHP.setBounds(x,y,100,100);
            labelHP.setFont(new Font("宋体",1,20));
            labelHP.setForeground(Color.white);
            container.add(labelHP);
            this.labelHPEnemy = labelHP;
            //System.out.println(cardPlayerKey.getPlayer().getHp());
        }
    }

    public void drawLabelLuck() {

        //Self
        int x = CONSTANT.luckLabelSelfX;
        int y = CONSTANT.luckLabelSelfY;

        if(cardPlayerKeySelf != null) {
            JLabel labelLuck = new JLabel();
            labelLuck.setText("Luck:" + String.format("%.2f",cardPlayerKeySelf.getPlayer().getLuckNum()));
            labelLuck.setBounds(x,y,100,100);
            labelLuck.setFont(new Font("宋体",1,19));
            labelLuck.setForeground(Color.white);
            container.add(labelLuck);
            this.labelLuckSelf = labelLuck;
        }

        //Eenemy
        y = 50;

        if(cardPlayerKeyEnemy != null) {
            JLabel labelLuck = new JLabel();
            labelLuck.setText("Luck:" + String.format("%.2f",cardPlayerKeyEnemy.getPlayer().getLuckNum()));
            labelLuck.setBounds(x,y,100,100);
            labelLuck.setFont(new Font("宋体",1,19));
            labelLuck.setForeground(Color.white);
            container.add(labelLuck);
            this.labelLuckEnemy = labelLuck;
        }
    }

    public void drawUsedCard(Card card) {
        int x = CONSTANT.frameWidth/2 - CONSTANT.cardWidth/2 + 15;
        int y = CONSTANT.frameHeight/2 - CONSTANT.cardHeight/2 - 50;
        JButton button = getButton(true,card,card.getCardName(),card.getCardDesc(),x,y,CONSTANT.cardWidth,CONSTANT.cardHeight);
        button.setVisible(true);
        button.getComponent(1).setVisible(true);
        container.add(button);
        buttons.add(button);
        repaint();
    }

    public void exitGame(boolean isSuccess) {
        GameStart.continuePlaying = false;
        System.out.println("游戏结束...");
        removeAllComponents();
        repaint();
        setExitLabel();
        setRestartLabel();
        setSuccessLabel(isSuccess);
        repaint();
    }

    public void setExitLabel() {
        JLabel exitLabel = new JLabel();
        exitLabel.setVisible(true);
        exitLabel.setIcon(exitImage);
        exitLabel.setBounds(CONSTANT.frameWidth /2 - exitImage.getIconWidth()/2,CONSTANT.frameHeight/2,exitImage.getIconWidth(),exitImage.getIconHeight());
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                exit(0);
            }
        };
        exitLabel.addMouseListener(mouseAdapter);
        container.add(exitLabel);
        labelExit = exitLabel;
    }

    public void setRestartLabel() {
        JLabel restartLabel = new JLabel();
        restartLabel.setVisible(true);
        restartLabel.setIcon(restartImage);
        restartLabel.setBounds(CONSTANT.frameWidth /2 - restartImage.getIconWidth()/2,CONSTANT.frameHeight/2 - restartImage.getIconHeight() - 50,exitImage.getIconWidth(),exitImage.getIconHeight());
        RestartEvent restartEvent = new RestartEvent(this);
        restartLabel.addMouseListener(restartEvent);
        container.add(restartLabel);
        labelRestart = restartLabel;
    }

    public void setSuccessLabel(boolean isSuccess) {
        JLabel label = new JLabel();
        if(isSuccess){
            label.setText("恭喜您获得胜利!");
            label.setForeground(Color.red);
        }
        else {
            label.setText("很遗憾您输了...");
            label.setForeground(Color.darkGray);
        }
        label.setBounds(CONSTANT.frameWidth /2 - 100,CONSTANT.frameHeight/2 - restartImage.getIconHeight() - 200,300,
                100);
        label.setFont(new Font("宋体",1,30));
        container.add(label);
        this.labelSuccess = label;
    }

}