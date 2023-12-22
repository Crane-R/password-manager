package crane.view.module;

import javax.swing.*;
import javax.swing.text.Document;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 队列输出框
 *
 * @author AXing
 * @date 2023/12/22 18:23:18
 */
public class QueueTextArea extends JTextArea {

    private static final Queue<String> messageQueue  = new ArrayDeque<>(2);

    public QueueTextArea() {
        super();
    }

    public QueueTextArea(String text) {
        super(text);
    }

    public QueueTextArea(int rows, int columns) {
        super(rows, columns);
    }

    public QueueTextArea(String text, int rows, int columns) {
        super(text, rows, columns);
    }

    public QueueTextArea(Document doc) {
        super(doc);
    }

    public QueueTextArea(Document doc, String text, int rows, int columns) {
        super(doc, text, rows, columns);
    }

    public void outputMessage(String message) {
        if (messageQueue.size() >= 2) {
            messageQueue.poll();
        }
        messageQueue.offer(message);
        StringBuilder stringBuilder = new StringBuilder();
        messageQueue.forEach(e -> stringBuilder.append(e).append("\n"));
        this.setText(stringBuilder.toString());
    }

    public void clearMessage() {
        messageQueue.clear();
        this.setText(null);
    }

}
