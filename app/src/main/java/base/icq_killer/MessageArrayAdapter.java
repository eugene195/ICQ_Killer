package base.icq_killer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import entities.Message;

public class MessageArrayAdapter extends ArrayAdapter<Message> {

    private List<Message> messages = new ArrayList<>();

    private static String myName = "";

    @Override
    public void add(Message object) {
        messages.add(object);
        super.add(object);
    }

    public MessageArrayAdapter(Context context, int textViewResourceId, ArrayList<Message> msglist, String my) {
        super(context, textViewResourceId);
        messages = msglist;
        myName = my;
    }

    public int getCount() {
        return this.messages.size();
    }

    public Message getItem(int index) {
        return this.messages.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.listitem_chat, parent, false);
        }
        LinearLayout wrapper = (LinearLayout) row.findViewById(R.id.wrapper);
        Message message = getItem(position);
        TextView messageView = (TextView) row.findViewById(R.id.message);
        messageView.setText(message.getText());
        boolean isMine = message.getFrom().equals(myName);
        messageView.setBackgroundResource(isMine ? R.drawable.bubble_yellow : R.drawable.bubble_green);
        wrapper.setGravity(isMine ? Gravity.START : Gravity.END);
        return row;
    }
}
