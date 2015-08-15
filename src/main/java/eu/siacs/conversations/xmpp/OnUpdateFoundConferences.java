package eu.siacs.conversations.xmpp;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by philip on 16.07.15.
 */
public interface OnUpdateFoundConferences {
    @SuppressWarnings("MethodNameSameAsClassName")
    void onUpdateFoundConferences(ArrayList<String> foundConferences);
}
