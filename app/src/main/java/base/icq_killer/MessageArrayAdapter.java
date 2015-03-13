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

    private TextView messageView;
    private List<Message> messages = new ArrayList<>();
    private LinearLayout wrapper;

    @Override
    public void add(Message object) {
        messages.add(object);
        super.add(object);
    }

    public MessageArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
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
        wrapper = (LinearLayout) row.findViewById(R.id.wrapper);
        Message message = getItem(position);
        messageView = (TextView) row.findViewById(R.id.message);
        messageView.setText(message.text);
        messageView.setBackgroundResource(message.isMine ? R.drawable.bubble_yellow : R.drawable.bubble_green);
        wrapper.setGravity(message.isMine ? Gravity.START : Gravity.END);
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
