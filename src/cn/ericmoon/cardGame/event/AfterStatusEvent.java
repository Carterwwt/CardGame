package cn.ericmoon.cardGame.event;

import cn.ericmoon.cardGame.draw.GameClient;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AfterStatusEvent implements MouseListener {

    GameClient f;

    public AfterStatusEvent(GameClient f) {
        this.f = f;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
